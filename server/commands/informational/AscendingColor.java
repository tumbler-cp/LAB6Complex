package commands.informational;

import collection.CollectionManager;
import commands.Command;
import common.Signal;
import dragon.Dragon;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Class of <b>print_field_ascending_color</b> command
 *
 * @author Abdujalol Khodjaev
 * @see PrintAscending
 */
public class AscendingColor extends Command implements Serializable {
    /**
     * Collection manager command works with.
     */
    CollectionManager collection;

    public AscendingColor(CollectionManager collectionManager) {
        super("print_field_ascending_color", "вывести значения поля color всех элементов в порядке возрастания");
        this.collection = collectionManager;
    }

    @Override
    public boolean execute() {
        collection.get_list().stream()
                .sorted(Comparator.comparing(Dragon::getColor))
                .toList()
                .forEach(System.out::println);
        network.request(Signal.TEXT, response());
        return true;
    }
}
