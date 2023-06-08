package commands;

import java.io.Serializable;

/**
 * Class of <b>exit</b> command
 *
 * @author Abdujalol Khodjaev
 */
public class Exit extends Command implements Serializable {
    /**
     * Default constructor
     */
    public Exit() {
        super("exit", "завершить программу (без сохранения в файл)");
    }

    @Override
    public boolean execute() {
        return false;
    }
}
