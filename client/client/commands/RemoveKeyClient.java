package client.commands;

import common.CommandClient;
import common.Signal;

public class RemoveKeyClient implements CommandClient {
    String[] args = new String[0];
    @Override
    public String type() {
        return "remove_key";
    }

    @Override
    public String[] argLine() {
        return args;
    }

    @Override
    public void setArgs(String[] args) {
        this.args = args;
    }

    @Override
    public boolean validate() {
        return args.length == 1;
    }

    @Override
    public Signal signal() {
        return Signal.COMMAND;
    }
}
