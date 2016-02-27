package edu.pk.poc.orm.core.helper;

import edu.pk.poc.orm.core.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class SessionFactoy {
    String url, userName, password;
    Connection connection;
    List<Class> classes;
    MappingHelper helper = new MappingHelper();
    public SessionFactoy(List<Class> classList) {
        classes = classList;
        helper.introspect(classes);
    }

    public Session openSession() {
        try {
            connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            System.out.println(e);
        }
        Session session = new Session();
        session.setConnection(connection);
        session.setMappingHelper(helper);
        return session;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
