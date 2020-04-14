package org.example;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.*;

public class App
{
    public static void main( String[] args ) throws SQLException {

        Thread.currentThread().setName("Bob");

        System.setProperty("java.util.logging.config.file", "logging.properties");
//        Logger rootLogger = LogManager.getLogManager().getLogger("");
//        rootLogger.setLevel(Level.ALL);
//        Arrays.stream(rootLogger.getHandlers()).forEach(h -> h.setLevel(Level.ALL));
//
        Properties properties = new Properties();
//        properties.put("hibernate.connection.datasource", createDataSource());
        properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/high_performance_java_persistence");
        properties.put("hibernate.connection.username", "postgres");
        properties.put("hibernate.connection.password", "admin");


//        final BootstrapServiceRegistryBuilder bsrb = new BootstrapServiceRegistryBuilder();
//        final BootstrapServiceRegistry bsr = bsrb.build();

        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder(/*bsr*/);
        ssrb.applySettings(properties);
        final StandardServiceRegistry serviceRegistry = ssrb.build();

        final MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        final MetadataBuilder metadataBuilder = metadataSources.getMetadataBuilder();
        MetadataImplementor metadata = (MetadataImplementor) metadataBuilder.build();
        final SessionFactoryBuilder sfb = metadata.getSessionFactoryBuilder();
        SessionFactory sessionFactory = sfb.build();

        System.out.println("About to open a session...");
        sessionFactory.openSession().doWork(connection -> {
            Statement statement = connection.createStatement();
            boolean execute = statement.execute("SELECT * FROM POST;");
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                System.out.println(rs.getObject(2));
            }
        });

    }

    private static DataSource createDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName("high_performance_java_persistence");
        dataSource.setServerName("localhost");
        dataSource.setUser("postgres");
        dataSource.setPassword("admin");
        return dataSource;
    }
}
