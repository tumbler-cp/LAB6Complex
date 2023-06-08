package client.commands;

import common.CommandClient;
import common.Signal;
import common.UDPInterface;

public class ReplaceGreaterClient implements CommandClient {
    String[] args = new String[0];
    UDPInterface network;
    @Override
    public String type() {
        return "replace_if_greater";
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
    public Signal signal(){
        return Signal.BIG_COMMAND;
    }
}
