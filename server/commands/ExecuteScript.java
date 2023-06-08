package commands;

import common.Request;
import common.Signal;
import common.UDPInterface;
import exceptions.EmptyCollectionException;
import server.ServerTerminal;
import server.UDPServer;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Class of execute_script command
 *
 * @author Abdujalol Khodjaev
 * @see file.FileManager
 */
//TODO:Переделать под сервер
public class ExecuteScript extends Command implements Serializable {
    private final HashSet<String> buffer = new HashSet<>();
    private int buff = 10;
    public int cycleDepth = 50;
    UDPInterface network;
    CommandManager CM;
    /**
     * Terminal for executing commands
     */
    ServerTerminal console;

    /**
     * Default constructor
     *
     * @param terminal - console origin
     */
    public ExecuteScript(ServerTerminal terminal, CommandManager collectionManager, UDPInterface network) {
        super("execute_script file_name", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
        console = terminal;
        this.CM = collectionManager;
        this.network = network;
    }

    /**
     * Reads lines from file and execute commands written in the file
     */

    @Override
    public boolean execute() {
        network.request(Signal.FILE_COMMAND, getArgs()[0]);
        try {
            ((UDPServer)network).receiveFile(getArgs()[0]);
        } catch (IOException e) {
            System.out.println("Ошибка получения файла!");
            return false;
        }
        System.out.println("Здесь 1");
        String scriptFile = this.getArgs()[0];
        buffer.add(scriptFile);
        if (buffer.contains(scriptFile)) buff++;
        Scanner fIN;
        try {
            if (new File(scriptFile).exists()) System.out.println("Файл существует.");
            fIN = new Scanner(new FileReader(scriptFile));
        } catch (FileNotFoundException f) {
            network.request(Signal.TEXT, ("Файл " + scriptFile + " не найден").getBytes());
            return false;
        }
        try {
            miniEx(fIN);
        } catch (EmptyCollectionException e) {
            network.request(Signal.TEXT, "Это ошибка возникает если вы программу неправильно написали".getBytes());
            return false;
        }
        System.out.println("Здесь 2");
        return true;
    }

    /**
     * Second part of executing
     *
     * @param in        Scanner of File
     * @throws EmptyCollectionException I don't think this exception will be thrown
     */

    private void miniEx(Scanner in) throws EmptyCollectionException {
        StringBuilder buffer;
        String b;
        System.out.println("OOOOOOOOOOOOOOOOOOOOOOOO");
        System.out.println(1);
        if (in.hasNext()) {
            b = in.next();
            System.out.println(b);
        } else return;
        System.out.println(2);
        while (in.hasNextLine()) {
            System.out.println(3);
            System.out.println("_______________________________________________________");
            if (buff > cycleDepth + 1) {
                System.out.println("Идет отмена МММММММММММММММММММММ  " + buff);
                buff = 1;
                return;
            }
            System.out.println(4);
            buffer = new StringBuilder();
            do {
                buffer.append(b).append(" ");
                b = in.nextLine();
            } while (!CM.get_map().containsKey(b.split(" ")[0]));
            System.out.println(5);
            System.out.println(buffer);
            System.out.println(Arrays.toString(buffer.toString().split(" ")));

            console.executor(buffer.toString().split(" "));
            System.out.println(6);
        }
        buffer = new StringBuilder();
        buffer.append(b);
        System.out.println(Arrays.toString(buffer.toString().split(" ")));
        console.executor(buffer.toString().split(" "));
    }
}
