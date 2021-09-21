package com.sbt.javaschool;

import java.util.Map;

public interface JmsExample {
    void sendMessage(String body);

    void sendMessage(String body, Map<String, String> headers);

    String receiveMessage(String messageId);

    String subscribeMessage();
}
