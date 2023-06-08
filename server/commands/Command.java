package commands;

import common.UDPInterface;
import exceptions.NoSuchOptionException;

import java.io.IOException;
import java.io.Serializable;


/**
 * Abstract class for all commands
 *
 * @author Abdujalol Khodjaev
 */
public abstract class Command implements Serializable {
    /**
     * Name and arguments of command
     */
    private String descriptionLeft;
    protected static UDPInterface network;

    public static void setNetwork(UDPInterface network) {
        Command.network = network;
    }

    /**
     * Command description
     */
    private String descriptionRight;
    public boolean isRunning = false;
    private String response = "";
    /**
     * Arguments of command
     */
    private String[] args = {};

    /**
     * Default constructor.
     * Exists, because class heirs have their own constructor with different params
     *
     * @see Command#Command(String, String)
     */
    public Command() {
    }

    /**
     * Constructor for name and description assignment
     *
     * @param Description1 name and arguments
     * @param Description2 description
     * @see Command#Command()
     */
    public Command(String Description1, String Description2) {
        this.descriptionLeft = Description1;
        this.descriptionRight = Description2;
    }

    /**
     * Arguments assignment
     *
     * @param args arguments
     */
    public void setArgs(String[] args) {
        this.args = args;
    }

    /**
     * Arguments
     *
     * @return Command current arguments
     */
    public String[] getArgs() {
        return args;
    }

    public void manual() {
        addToResponse(this.descriptionRight);
    }
    public boolean execute() throws NoSuchOptionException, IOException {
        return true;
    }
    /**
     * Command name and args getter
     *
     * @return name and args of command
     */
    public String getName() {
        return descriptionLeft;
    }

    /**
     * Command description getter
     *
     * @return description of command
     */
    public String getDescription() {
        return descriptionRight;
    }

    @Override
    public String toString() {
        return descriptionLeft + " - " + descriptionRight;
    }
    protected void addToResponse(String s)
    {
        response += s + "\n";
    }
    protected void addToResponse(Object s)
    {
        response += s.toString() + "\n";
    }
    public String response() {
        String data = response;
        response = "";
        return data;
    }
}
