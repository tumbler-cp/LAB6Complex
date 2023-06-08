package client.commands;

import common.CommandClient;
import common.Signal;

public class InsertClient implements CommandClient {
    String[] argLine = new String[0];
    @Override
    public String type() {
        return "insert";
    }

    @Override
    public String[] argLine() {
        return argLine;
    }

    @Override
    public void setArgs(String[] args) {
        this.argLine = args;
    }

    @Override
    public boolean validate() {
        return ((argLine.length == 9) ||
                ((argLine.length == 1) &&
                        (argLine[0].matches("^\\d+$") ||
                                argLine[0].equals("man"))));
    }

    @Override
    public Signal signal() {
        if (argLine.length == 9 || argLine[0].equals("man")) return Signal.COMMAND;
        else return Signal.BIG_COMMAND;
    }
}
