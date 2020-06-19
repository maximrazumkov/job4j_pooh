package ru.job4j.pooh;

import ru.job4j.pooh.action.*;
import ru.job4j.pooh.server.MqServer;
import ru.job4j.pooh.server.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Pooh {
    private static final int DEFAULT_PORT = 4004;
    private static final int DEFAULT_CONNECTIONS = 10;

    public static void main(String[] args) {
        new Pooh().run(args);
    }

    private final void run(String[] args) {
        Args arguments = parseArgs(args);
        int port = arguments.getPort();
        Map<String, Action> actions = new HashMap<>();
        Map<String, BlockingQueue<String>> queueList = new ConcurrentHashMap<>();
        queueList.put("weather", new LinkedBlockingQueue<>());
        Map<String, Map<String, BlockingQueue<String>>> topicList = new ConcurrentHashMap<>();
        topicList.put("weather", new ConcurrentHashMap<>());
        actions.put("POST /topic/subscriber", new PostSubscriberAction(topicList));
        actions.put("GET /queue/weather", new GetQueueAction(queueList));
        actions.put("GET /topic/weather", new GetTopicAction(topicList));
        actions.put("POST /queue", new PostQueueAction(queueList));
        actions.put("POST /topic", new PostTopicAction(topicList));
        Server server = new MqServer(port, actions);
        server.start();
    }

    private Args parseArgs(String[] args) {
        Args result = new Args(DEFAULT_CONNECTIONS, DEFAULT_PORT);
        for (int i = 0; i < args.length; ++i) {
            String[] arg = args[i].split("=");
            switch (arg[0]) {
                case "port":
                    result.setPort(Integer.valueOf(arg[1]));
                    break;
                case "connections":
                    result.setCountConnection(Integer.valueOf(arg[1]));
                    break;
                default:
                    break;
            }
        }
        return result;
    }
}
