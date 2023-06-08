package client.commands;

import common.CommandClient;
import common.Signal;

public class HistoryClient implements CommandClient {

    @Override
    public String type() {
        return "history";
    }

    @Override
    public String[] argLine() {
        return new String[0];
    }

    @Override
    public void setArgs(String[] args) {

    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public Signal signal() {
        return Signal.COMMAND;
    }
}