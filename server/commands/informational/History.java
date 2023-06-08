package commands.informational;

import commands.Command;
import common.Signal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class of <b>history</b> command
 *
 * @author Abdujalol Khodjaev
 */
public class History extends Command implements Serializable {
    /**
     * History list
     */
    List<String> list = new ArrayList<>();

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
        this.list.add(c);
        if (list.size() > 14) list.remove(0);
    }

    @Override
    public boolean execute() {
        int i = 1;
        for (String s : list) {
            addToResponse(i + " " + s);
            i++;
        }
        network.request(Signal.TEXT, response());
        return true;
    }
}
