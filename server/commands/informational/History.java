package commands.informational;

import commands.Command;
import common.Signal;
import server.UDPServer;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Class of <b>history</b> command
 *
 * @author Abdujalol Khodjaev
 */
public class History extends Command implements Serializable {
    /**
     * History list
     */
    Map<SocketAddress, String[]> list = new HashMap<>();

    /**
     * Default constructor
     */
    public History() {
        super("history", "вывести последние 14 команд (без их аргументов)");
    }

    /**
     * Adding command name to list. Deletes first added if list size is bigger than 14;
     *
     * @param c - command name
     */
    public void add(String c) {
        String[] currentHistory = list.get(((UDPServer) network).getClientAddress());
        if (currentHistory == null) {
            currentHistory = new String[14];
            Arrays.fill(currentHistory, "");
            currentHistory[0] = c;
            list.put(((UDPServer) network).getClientAddress(), currentHistory);
            return;
        }
        String[] buff = new String[14];
        System.arraycopy(currentHistory, 1, buff, 0, 13);
        buff[13] = c;
        list.put(((UDPServer) network).getClientAddress(), buff);
    }

    @Override
    public boolean execute() {
        int i = 1;
        String[] history = list.get(((UDPServer) network).getClientAddress());
        for (String s : history) {
            addToResponse(i + " " + s);
            i++;
        }
        network.request(Signal.TEXT, response());
        return true;
    }
}
