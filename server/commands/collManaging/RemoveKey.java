package commands.collManaging;

import collection.CollectionManager;
import commands.Command;
import common.Signal;
import exceptions.EmptyCollectionException;

import java.io.IOException;
import java.io.Serializable;

/**
 * Class of <b>remove_key</b> command
 *
 * @author Abdujalol Khodjaev
 */
public class RemoveKey extends Command implements Serializable {
    /**
     * Collection manager this command works with
     */
    CollectionManager collection;

    /**
     * Default constructor
     *
     * @param collectionManager collection this command works with
     */
    public RemoveKey(CollectionManager collectionManager) {
        super("remove_key null", "удалить элемент из коллекции по его ключу");
        collection = collectionManager;
    }

    @Override
    public boolean execute() throws IOException {
        int key = Integer.parseInt(this.getArgs()[0]);
        try {
            collection.get_collection().remove(key);
        } catch (EmptyCollectionException e) {
            addToResponse("Коллекция пуста!");
        }
        network.request(Signal.TEXT, response());
        return true;
    }
}
