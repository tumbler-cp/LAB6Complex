package commands.informational;

import commands.Command;
import commands.CommandManager;
import common.Signal;
import common.Terminal;
import exceptions.EmptyCollectionException;

import java.io.IOException;
import java.io.Serializable;

/**
 * Class of <b>help</b> command
 *
 * @author Abdujalol Khodjaev
 */
public class Help extends Command implements Serializable {
    /**
     * Command collection manager
     */
    CommandManager commands;

    /**
     * Default constructor
     *
     * @param commandManager Manager where from help gets information about commands
     */
    public Help(CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.commands = commandManager;
    }

    @Override
    public boolean execute() throws IOException {
        try {
            this.commands.get_map().values().forEach(command ->
                    addToResponse(Terminal.RED + command.getName() + " : " + Terminal.RESET + command.getDescription()));
        } catch (EmptyCollectionException e) {
            addToResponse("Команд нет!");
        }
        network.request(Signal.TEXT, response());
        return true;
    }
}
