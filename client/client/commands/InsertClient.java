package client.commands;

import common.CommandClient;
import common.Signal;
/**
 * @author Abdujalol Khodjaev
 * Object to make 'insert' request
 * Implements CommandClient
 * @see CommandClient
 */
public class InsertClient implements CommandClient {
    String[] argLine = new String[0];
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public String type() {
        return "insert";
    }
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public String[] argLine() {
        return argLine;
    }
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public void setArgs(String[] args) {
        this.argLine = args;
    }
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public boolean validate() {
        return ((argLine.length == 9) ||
                ((argLine.length == 1) &&
                        (argLine[0].matches("^\\d+$") ||
                                argLine[0].equals("man"))));
    }
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public Signal signal() {
        if (argLine.length == 9 || argLine[0].equals("man")) return Signal.COMMAND;
        else return Signal.BIG_COMMAND;
    }
}
