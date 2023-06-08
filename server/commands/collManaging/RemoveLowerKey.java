package commands.collManaging;

import collection.CollectionManager;
import commands.Command;
import common.Signal;
import exceptions.EmptyCollectionException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class <b>remove_lower_key</b> command
 *
 * @author Abdujalol Khodjaev
 */
public class RemoveLowerKey extends Command implements Serializable {
    /**
     * Collection manager this command works with
     */
    CollectionManager collection;

    /**
     * Default constructor
     *
     * @param collectionManager collection to work with
     */
    public RemoveLowerKey(CollectionManager collectionManager) {
        super("remove_lower_key null", "удалить из коллекции все элементы, ключ которых меньше, чем заданный");
        collection = collectionManager;
    }

    @Override
    public boolean execute() throws IOException {
        int key = Integer.parseInt(this.getArgs()[0]);
        ArrayList<Integer> toDel = new ArrayList<>();
        try {
            for (Integer currentKey : collection.get_collection().keySet()) {
                if (key > currentKey) toDel.add(currentKey);
            }
            for (Integer i : toDel) {
                collection.get_collection().remove(i);
            }
        } catch (EmptyCollectionException e) {
            addToResponse("Коллекция пуста!");
        }
        network.request(Signal.TEXT, response());
        return true;
    }
}
