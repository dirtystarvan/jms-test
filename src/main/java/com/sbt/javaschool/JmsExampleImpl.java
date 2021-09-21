package com.sbt.javaschool;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.NamingException;
import java.util.Map;

public class JmsExampleImpl implements JmsExample {
    private Connection conn = JmsContext.getConnection();
    private Context context = JmsContext.getContext();

    /**
     * Отправка сообщения в тестовую очередь
     *
     * @param body тело сообщения
     */
    @Override
    public void sendMessage(String body) {
//        Connection conn = JmsContext.getConnection();
//        Context context = JmsContext.getContext();
        try {
            Session session = conn.createSession();
            MessageProducer prod = session.createProducer((Destination)context.lookup("TestQueue"));

            Message msg = session.createTextMessage(body);
            prod.send(msg);
        } catch (JMSException | NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Отправка сообщения в тестовую очередь
     *
     * @param body    тело сообщения
     * @param headers заголовки сообщения
     */
    @Override
    public void sendMessage(String body, Map<String, String> headers) {
        try {
            Session session = conn.createSession();
            MessageProducer prod = session.createProducer((Destination)context.lookup("TestQueue"));

            Message msg = session.createTextMessage(body);
            for (Map.Entry<String, String> pair: headers.entrySet()) {
                msg.setStringProperty(pair.getKey(), pair.getValue());
            }

            prod.send(msg);
        } catch (JMSException | NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получение сообщения из тестовой очереди
     *
     * @param messageId JMSMessageID сообщения для построения селектора
     * @return JMSCorrelationID вычитанного сообщения
     */
    @Override
    public String receiveMessage(String messageId) {
        String result = "";
        try {
            Session session = conn.createSession();
            MessageConsumer mc = session.createConsumer((Destination)context.lookup("TestQueue"), String.format("JMSMessageID = '%s'", messageId));
            Message msg = mc.receive(10000L);
            result = msg.getJMSCorrelationID();

        } catch (JMSException | NamingException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Получение информации о сообщении из топика. Подписаться на топик нужно заранее
     *
     * @return JMSCorrelationID сообщения, полученного с помощью подписки
     */
    @Override
    public String subscribeMessage() {
        try {
            Session session = conn.createSession();
            TopicSubscriber subscr = session.createDurableSubscriber((Topic)context.lookup("TestTopic"), "Ac1d");
            

        } catch (JMSException | NamingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
