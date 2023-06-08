package commands.informational;

import collection.CollectionManager;
import commands.Command;
import common.Signal;
import dragon.Dragon;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class of <b>filter_contains_name</b> command
 *
 * @author Abdujalol Khodjaev
 */
public class FilterName extends Command implements Serializable {
    /**
     * Collection manager this command works with
     */
    CollectionManager collection;

    /**
     * Default constructor
     *
     * @param collectionManager - collection object
     */
    public FilterName(CollectionManager collectionManager) {
        super("filter_contains_name name", "вывести элементы, значение поля name которых содержит заданную подстроку");
        this.collection = collectionManager;
    }

    @Override
    public boolean execute() {
        String reg = ".*" + this.getArgs()[0] + ".*";
        Pattern pattern = Pattern.compile(reg);
        List<Dragon> list = collection.get_list();
        for (Dragon d : list) {
            Matcher matcher = pattern.matcher(d.getName());
            if (matcher.matches()) addToResponse(d.toString());
        }
        network.request(Signal.TEXT, response());
        return true;
    }
}
