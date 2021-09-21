package com.sbt.javaschool;

import java.util.Map;

public class JmsExampleImpl implements JmsExample {

    /**
     * Отправка сообщения в тестовую очередь
     *
     * @param body тело сообщения
     */
    @Override
    public void sendMessage(String body) {

    }

    /**
     * Отправка сообщения в тестовую очередь
     *
     * @param body    тело сообщения
     * @param headers заголовки сообщения
     */
    @Override
    public void sendMessage(String body, Map<String, String> headers) {

    }

    /**
     * Получение сообщения из тестовой очереди
     *
     * @param messageId JMSMessageID сообщения для построения селектора
     * @return JMSCorrelationID вычитанного сообщения
     */
    @Override
    public String receiveMessage(String messageId) {
        return null;
    }

    /**
     * Получение информации о сообщении из топика. Подписаться на топик нужно заранее
     *
     * @return JMSCorrelationID сообщения, полученного с помощью подписки
     */
    @Override
    public String subscribeMessage() {
        return null;
    }
}
