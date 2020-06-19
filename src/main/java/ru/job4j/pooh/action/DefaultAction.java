package ru.job4j.pooh.action;

public class DefaultAction implements Action {
    @Override
    public String exec(String header, String body) {
        return String.format("404 Not found %s", header);
    }
}
