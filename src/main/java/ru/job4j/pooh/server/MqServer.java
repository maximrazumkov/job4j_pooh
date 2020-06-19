package ru.job4j.pooh.server;

import ru.job4j.pooh.action.Action;
import ru.job4j.pooh.action.DefaultAction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class MqServer implements Server {

    private final int port;
    private final Map<String, Action> actions;

    public MqServer(int port, Map<String, Action> actions) {
        this.port = port;
        this.actions = actions;
    }

    @Override
    public void start() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (!server.isClosed()) {
                try (
                        final Socket socket = server.accept();
                        final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        final PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                ) {
                    String header = in.readLine().trim();
                    String body = in.readLine().trim();
                    Action action = actions.getOrDefault(header, new DefaultAction());
                    String result = action.exec(header, body);
                    out.println(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
