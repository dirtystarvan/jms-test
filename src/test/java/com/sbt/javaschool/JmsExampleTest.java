package com.sbt.javaschool;

import org.junit.*;

import javax.jms.*;
import javax.naming.NamingException;
import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

public class JmsExampleTest {
    private static final String TEST_BODY = "TEST";

    private static Connection connection;
    private static JmsExample jmsExample;

    private static Queue queue;
    private static Topic topic;
    private Session session;
    private MessageConsumer consumer;
    private MessageProducer producer;
    private MessageProducer topicPublisher;

    @BeforeClass
    public static void initJmsContext() throws NamingException, JMSException {
        jmsExample = new JmsExampleImpl();
        connection = JmsContext.getConnection();
        connection.start();
        queue = (Queue) JmsContext.getContext().lookup("TestQueue");
        topic = (Topic) JmsContext.getContext().lookup("TestTopic");
    }

    @AfterClass
    public static void destroyJmsContext() throws JMSException {
        connection.close();
    }

    @Before
    public void init() throws JMSException {
        session = connection.createSession();
        consumer = session.createConsumer(queue);
        producer = session.createProducer(queue);
        topicPublisher = session.createProducer(topic);
    }

    @After
    public void destroy() throws JMSException {
        closeJmsObjects();
    }

    private void closeJmsObjects() throws JMSException {
        consumer.close();
        producer.close();
        topicPublisher.close();
        session.close();
    }

    @Test
    public void sendMessageTest() throws JMSException {
        jmsExample.sendMessage(TEST_BODY);
        Message receive = consumer.receive(Duration.ofSeconds(5).toMillis());
        Assert.assertNotNull("Не удалось получить сообщение из очереди", receive);
    }

    @Test
    public void sendMessageWithHeadersTest() throws JMSException {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("ServiceName", "TestService");
        jmsExample.sendMessage(TEST_BODY, headers);
        Message receive = consumer.receive(Duration.ofSeconds(5).toMillis());
        Assert.assertNotNull("Не удалось получить сообщение из очереди", receive);
    }

    @Test
    public void receiveMessageTest() throws JMSException {
        TextMessage message = session.createTextMessage(TEST_BODY);
        message.setJMSCorrelationID(UUID.randomUUID().toString());
        producer.send(message);
        closeJmsObjects();
        String correlationId = jmsExample.receiveMessage(message.getJMSMessageID());
        Assert.assertEquals("Вычитано нужное сообщение", message.getJMSCorrelationID(), correlationId);
    }

    @Test
    public void subscribeTopicTest() throws JMSException {
        TextMessage message = session.createTextMessage(TEST_BODY);
        message.setJMSCorrelationID(UUID.randomUUID().toString());
        topicPublisher.send(message);
        String messageId = jmsExample.subscribeMessage();
        Assert.assertEquals("Вычитано нужное сообщение", message.getJMSCorrelationID(), messageId);
    }
}
