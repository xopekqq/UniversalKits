package ru.xopek.universalkits.database;

import ru.xopek.universalkits.UniversalKits;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.xopek.universalkits.api.StringAPI;

public class HibernateUtils {
    private static SessionFactory sessionFactory;

    public static void init (ConfigurationSection yaml) {
        Bukkit.getScheduler().runTaskAsynchronously(UniversalKits.getInst(), () -> {
            System.out.println(StringAPI.asColor("&6UniversalKits | &fCreating Hibernate Instance Asynchronously"));

            Configuration config = new Configuration();

            config.setProperty("hibernate.connection.driver_class", yaml.getString("driver"));
            config.setProperty("hibernate.connection.url", yaml.getString("jdbcurl"));
            config.setProperty("hibernate.connection.username", yaml.getString("username"));
            config.setProperty("hibernate.connection.password", yaml.getString("password"));
            config.setProperty("hibernate.dialect", yaml.getString("dialect"));
            config.setProperty("hibernate.dialect.storage_engine", yaml.getString("engine"));
            config.setProperty("hibernate.show_sql", "false");
            config.setProperty("hibernate.hbm2ddl.auto", "update");

            config.addAnnotatedClass(UserEntity.class);

            sessionFactory = config.buildSessionFactory();
        });
    }
    public static void close() {
        if(sessionFactory != null) sessionFactory.close();
    }
    public static Session getSession() {
        return sessionFactory.openSession();
    }
}
