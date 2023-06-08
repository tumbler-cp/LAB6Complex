package commands.informational;

import collection.CollectionManager;
import commands.Command;
import common.Signal;
import exceptions.EmptyCollectionException;
import tableMaker.Table;

import java.io.IOException;
import java.io.Serializable;

/**
 * Class of <b>show</b> command
 *
 * @author Abdujalol Khodjaev
 */
public class Show extends Command implements Serializable {
    /**
     * Collection manager this command works with
     */
    CollectionManager collection;

    /**
     * Default constructor
     *
     * @param collection Collection to work with
     */
    public Show(CollectionManager collection) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collection = collection;
    }

    @Override
    public boolean execute() throws IOException {
        try {
            addToResponse(new Table(collection.get_collection()));
        } catch (EmptyCollectionException e) {
            addToResponse("Коллекция пуста!");
        }
        network.request(Signal.TEXT, response());
        return true;
    }
}
