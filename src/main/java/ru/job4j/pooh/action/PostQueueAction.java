package ru.job4j.pooh.action;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class PostQueueAction implements Action {

    private final Map<String, BlockingQueue<String>> queueList;

    public PostQueueAction(Map<String, BlockingQueue<String>> queueList) {
        this.queueList = queueList;
    }

    @Override
    public String exec(String header, String body) {
        String result = "404 not found";
        try {
            Map<String, Object> bodyMap = new ObjectMapper().readValue(body, HashMap.class);
            String queueName = (String) bodyMap.get("queue");
            BlockingQueue<String> queue = queueList.get(queueName);
            if (queue != null) {
                queue.put(body);
                result = "201";
            }
        } catch (Exception e) {
            result = "400 Bad Request";
            e.printStackTrace();
        }
        return result;
    }
}
