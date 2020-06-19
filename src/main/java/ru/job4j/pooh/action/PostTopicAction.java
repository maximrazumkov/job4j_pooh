package ru.job4j.pooh.action;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class PostTopicAction implements Action {

    private final Map<String, Map<String, BlockingQueue<String>>> topicList;

    public PostTopicAction(Map<String, Map<String, BlockingQueue<String>>> topicList) {
        this.topicList = topicList;
    }

    @Override
    public String exec(String header, String body) {
        String result = "404 not found";
        try {
            Map<String, Object> bodyMap = new ObjectMapper().readValue(body, HashMap.class);
            String topicName = (String) bodyMap.get("topic");
            Map<String, BlockingQueue<String>> topic = topicList.get(topicName);
            if (topic != null && !topic.isEmpty()) {
                for (Map.Entry<String, BlockingQueue<String>> subscriber : topic.entrySet()) {
                        subscriber.getValue().put(body);
                }
                result = "200";
            }
        } catch (Exception e) {
            result = "400 Bad Request";
            e.printStackTrace();
        }
        return result;
    }
}
