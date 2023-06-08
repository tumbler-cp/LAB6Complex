package commands.collManaging;

import commands.Command;
import file.FileManager;

import java.io.IOException;
import java.io.Serializable;

/**
 * Class of <b>save</b> command
 *
 * @author Abdujalol Khodjaev
 */
public class Save extends Command implements Serializable {
    /**
     * File manager command works with
     */
    FileManager fm;

    /**
     * Default constructor
     *
     * @param fileManager File manager which write all data to file
     */
    public Save(FileManager fileManager) {
        super("save", "сохранить коллекцию в файл");
        this.fm = fileManager;
    }

    @Override
    public boolean execute() throws IOException {
        try {
            this.fm.write();
        } catch (IOException io) {
            addToResponse("Ошибка записи в файл.");
        }
        return true;
    }
}
