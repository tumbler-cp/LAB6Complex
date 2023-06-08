package commands.informational;

import collection.CollectionManager;
import commands.Command;
import common.Signal;

import java.io.Serializable;

/**
 * Class of <b>print_ascending</b> command
 *
 * @author Abdujalol Khodjaev
 */
public class PrintAscending extends Command implements Serializable {
    /**
     * Collection manager this command works with
     */
    CollectionManager collection;

    /**
     * Default constructor
     *
     * @param collectionManager collection to work with
     */
    public PrintAscending(CollectionManager collectionManager) {
        super("print_ascending", "вывести элементы коллекции в порядке возрастания");
        collection = collectionManager;
    }

    @Override
    public boolean execute() {
        collection.get_list().stream()
                .sorted()
                .toList()
                .forEach(this::addToResponse);
        network.request(Signal.TEXT, response());
        return true;
    }
}
