package commands;

import commands.informational.Help;
import exceptions.EmptyCollectionException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class for managing command objects
 *
 * @author Abdujalol Khodjaev
 * @see collection.CollectionManager
 * @see Command
 */
public class CommandManager implements Serializable {
    /**
     * HashMap list. Contains Command objects
     *
     * @see Command
     */
    HashMap<String, Command> list = new LinkedHashMap<>();

    /**
     * Method for adding new commands. You can add only still non-existing objects.
     *
     * @param CommandName - command name. Only name. No arguments
     * @param command     - command
     */
    public void add(String CommandName, Command command) {
        this.list.put(CommandName, command);
    }
    public Command find(String key){
        return list.get(key);
    }
    /**
     * Get map of command objects. Using example: help command
     *
     * @return Map list
     * @see Help
     */
    public Map<String, Command> get_map() throws EmptyCollectionException {
        if (this.list.isEmpty()) throw new EmptyCollectionException();
        return this.list;
    }
}
