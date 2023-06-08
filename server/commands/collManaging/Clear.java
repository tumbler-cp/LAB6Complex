package commands.collManaging;

import collection.CollectionManager;
import commands.Command;
import common.Signal;
import exceptions.EmptyCollectionException;

import java.io.IOException;
import java.io.Serializable;

/**
 * Class of <b>clear</b> command.
 *
 * @author Abdujalol Khodjaev
 * @see RemoveKey
 * @see RemoveLowerKey
 */
public class Clear extends Command implements Serializable {
    /**
     * Collection manager this command works with
     */
    CollectionManager collection;

    /**
     * Default constructor
     *
     * @param collectionManager - <b>collection</b> manager
     */
    public Clear(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.collection = collectionManager;
    }

    @Override
    public boolean execute() throws IOException {
        isRunning = true;
        try {
            collection.get_collection().clear();
        } catch (EmptyCollectionException e) {
            addToResponse("Коллекция пуста!");
        }
        isRunning = false;
        network.request(Signal.TEXT, response());
        return true;
    }
}
