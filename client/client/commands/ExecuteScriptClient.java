package client.commands;

import common.CommandClient;
import common.Signal;

import java.io.File;

public class ExecuteScriptClient implements CommandClient {
    String[] args = new String[0];
    @Override
    public String type() {
        return "execute_script";
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
        return new File(args[0]).exists() && args.length == 1;
    }

    @Override
    public Signal signal() {
        return Signal.FILE_COMMAND;
    }
}
