package ru.job4j.pooh;

public class Args {
    private int countConnection;
    private int port;

    public Args(int countConnection, int port) {
        this.countConnection = countConnection;
        this.port = port;
    }

    public int getCountConnection() {
        return countConnection;
    }

    public void setCountConnection(int countConnection) {
        this.countConnection = countConnection;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
