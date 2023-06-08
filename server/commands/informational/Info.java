package commands.informational;

import collection.CollectionManager;
import commands.Command;
import common.Signal;

import java.io.Serializable;

/**
 * Class of <b>info</b> command
 *
 * @author Abdujalol Khodjaev
 */
public class Info extends Command implements Serializable {
    /**
     * Collection manager this command works with
     */
    CollectionManager collectionManager;

    /**
     * Default constructor
     *
     * @param collectionManager Collection to work with
     */
    public Info(CollectionManager collectionManager) {
        super("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute() {
        addToResponse(this.collectionManager.toString());
        network.request(Signal.TEXT, response());
        return true;
    }
}
