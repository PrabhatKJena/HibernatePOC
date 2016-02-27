package edu.pk.poc.orm.core;

import edu.pk.poc.orm.core.helper.SessionFactoy;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Configuration {

    Properties dataSource;
    List<Class> classList = new ArrayList<>();

    public Configuration configure() throws IOException {
        dataSource = new Properties();
        dataSource.load(getClass().getClassLoader().getResourceAsStream("db-config.properties"));
//        System.out.println(dataSource);
        try {
            Class.forName(dataSource.getProperty("driverClass"));
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        return this;
    }

    public SessionFactoy buildSessionFactory() throws ClassNotFoundException, SQLException {
        SessionFactoy sessionFactoy = new SessionFactoy(classList);
        sessionFactoy.setUrl(dataSource.getProperty("url"));
        sessionFactoy.setUserName(dataSource.getProperty("userName"));
        sessionFactoy.setPassword(dataSource.getProperty("password"));
        return sessionFactoy;
    }

    public Configuration addAnnotatedClass(Class klass) {
        classList.add(klass);
        return this;
    }
}
