package client.commands;

import common.CommandClient;
import common.Signal;

public class UpdateIdClient implements CommandClient {
    String[] args = new String[0];
    @Override
    public String type() {
        return "update";
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
        return args.length == 3 || args.length == 1;
    }

    @Override
    public Signal signal() {
        return Signal.COMMAND;
    }
}
