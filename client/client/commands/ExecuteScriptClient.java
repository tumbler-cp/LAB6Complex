package client.commands;

import common.CommandClient;
import common.Signal;

import java.io.File;
/**
 * @author Abdujalol Khodjaev
 * Object to make 'execute_script' request
 * Implements CommandClient
 * @see CommandClient
 */
public class ExecuteScriptClient implements CommandClient {
    String[] args = new String[0];
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public String type() {
        return "execute_script";
    }
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public String[] argLine() {
        return args;
    }
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public void setArgs(String[] args) {
        this.args = args;
    }
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public boolean validate() {
        return new File(args[0]).exists() && args.length == 1;
    }
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public Signal signal() {
        return Signal.FILE_COMMAND;
    }
}
