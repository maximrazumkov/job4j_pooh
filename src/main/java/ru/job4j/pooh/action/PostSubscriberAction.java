package ru.job4j.pooh.action;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PostSubscriberAction implements Action {

    private final Map<String, Map<String, BlockingQueue<String>>> topicList;

    public PostSubscriberAction(Map<String, Map<String, BlockingQueue<String>>> topicList) {
        this.topicList = topicList;
    }

    @Override
    public String exec(String header, String body) {
        String result = "404 Bad Request";
        try {
            Map<String, Object> bodyMap = new ObjectMapper().readValue(body, HashMap.class);
            String topicName = (String) bodyMap.get("topic");
            Map<String, BlockingQueue<String>> topic = topicList.get(topicName);
            if (topic != null) {
                String codeSubscriber = String.valueOf(System.currentTimeMillis());
                topic.put(codeSubscriber, new LinkedBlockingQueue<>());
                result = String.format("200 { \"code\": \"%s\" }", codeSubscriber);
            }
        } catch (Exception e) {
            result = "400 Bad Request";
            e.printStackTrace();
        }
        return result;
    }
}
