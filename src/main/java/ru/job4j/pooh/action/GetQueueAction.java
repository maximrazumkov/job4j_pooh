package ru.job4j.pooh.action;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class GetQueueAction implements Action {

    private final Map<String, BlockingQueue<String>> queueList;

    public GetQueueAction(Map<String, BlockingQueue<String>> queueList) {
        this.queueList = queueList;
    }

    @Override
    public String exec(String header, String body) {
        String result = "404 Bad Request";
        try {
            String queueName = header.substring(header.lastIndexOf("/") + 1);
            BlockingQueue<String> queue = queueList.get(queueName);
            if (queue != null) {
                result = queue.poll(10, TimeUnit.SECONDS);
                result = result == null ? "408 Request Timeout" : String.format("200 %s", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
