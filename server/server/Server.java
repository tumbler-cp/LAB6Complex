package server;

import collection.CollectionManager;
import commands.Command;
import commands.CommandManager;
import commands.ExecuteScript;
import commands.collManaging.*;
import commands.informational.*;
import file.FileManager;

import java.io.IOException;

public class Server {
    public static void main(String[] args) throws IOException {
        CollectionManager collection = new CollectionManager();
        FileManager collectionFile;
        try {
            collectionFile = new FileManager(args[0], collection);
        } catch (ArrayIndexOutOfBoundsException a) {
            System.out.println("Напишите путь к файлу в аргументах программы!");
            return;
        }

        UDPServer serverUDP = new UDPServer("localhost", 4567);
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


        var terminal = new ServerTerminal(serverUDP, commands);
        commands.add("execute_script", new ExecuteScript(terminal, commands, serverUDP));
        terminal.run();
        save.execute();
        serverUDP.disconnect();
        serverUDP.close();
    }
}
