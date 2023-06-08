package server;

import collection.CollectionManager;
import commands.Command;
import commands.CommandManager;
import commands.ExecuteScript;
import commands.collManaging.*;
import commands.informational.*;
import file.FileManager;

import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Server {
    public static final Logger logger = LogManager.getLogger();
    public static UDPServer serverUDP;
    public static void main(String[] args) throws IOException {
        logger.info("Начало выполнения программы");
        String HOST = "localhost";
        int PORT = 666;
        CollectionManager collection = new CollectionManager();
        FileManager collectionFile;
        try {
            collectionFile = new FileManager(args[0], collection);
        } catch (ArrayIndexOutOfBoundsException a) {
            System.out.println("Напишите путь к файлу в аргументах программы!");
            logger.error("Не был указан путь к .csv - файлу с коллекцией");
            return;
        }
        serverUDP = new UDPServer(HOST, PORT);
        Command.setNetwork(serverUDP);
        CommandManager commands = new CommandManager(){{
            add("help", new Help(this));
            add("insert", new Insert(collection, serverUDP));
            add("clear", new Clear(collection));
            add("info", new Info(collection));
            add("show", new Show(collection));
            add("print_ascending", new PrintAscending(collection));
            add("remove_key", new RemoveKey(collection));
            add("update", new UpdateId(collection));
            add("remove_lower_key", new RemoveLowerKey(collection));
            add("filter_contains_name", new FilterName(collection));
            add("print_field_ascending_color", new AscendingColor(collection));
            add("replace_if_greater", new ReplaceGreater(collection, serverUDP));
        }};
        var save = new Save(collectionFile);
        var terminal = new ServerTerminal(serverUDP, commands, collectionFile);
        commands.add("execute_script", new ExecuteScript(terminal, commands, serverUDP));
        terminal.run();
        save.execute();
        serverUDP.disconnect();
        logger.info("Закрытие канала...");
        serverUDP.close();
        logger.info("Канал был закрыт!");
    }
}
