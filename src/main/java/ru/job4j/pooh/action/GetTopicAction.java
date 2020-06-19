package ru.job4j.pooh.action;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class GetTopicAction implements Action {

    private final Map<String, Map<String, BlockingQueue<String>>> topicList;

    public GetTopicAction(Map<String, Map<String, BlockingQueue<String>>> topicList) {
        this.topicList = topicList;
    }

    @Override
    public String exec(String header, String body) {
        String result = "404 Not found";
        try {
            String[] headerArr = header.split("/");
            Map<String, BlockingQueue<String>> subscriberList = topicList.get(headerArr[2]);
            if (subscriberList != null) {
                Map<String, Object> bodyMap = new ObjectMapper().readValue(body, HashMap.class);
                String code = (String) bodyMap.get("code");
                BlockingQueue<String> subcsriber = subscriberList.get(code);
                if (subcsriber != null) {
                    result = subcsriber.poll(10, TimeUnit.SECONDS);
                    result = result == null ? "408 Request Timeout" : String.format("200 %s", result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
