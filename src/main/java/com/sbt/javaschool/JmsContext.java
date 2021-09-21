package com.sbt.javaschool;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JmsContext {
    private static final Context context;
    private static final ConnectionFactory connectionFactory;
    private final static Connection connection;


    static {
        try {
            context = new InitialContext();
            connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            connection.setClientID("Ac1dTest");
        } catch (NamingException | JMSException e) {
            System.err.println("Ошибка при создании JMS-контекста");
            throw new RuntimeException(e);
        }
    }

    public static Context getContext() {
        return context;
    }

    public static Connection getConnection() {
        return connection;
    }
}
