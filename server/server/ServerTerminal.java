package server;

import commands.Command;
import commands.CommandManager;
import commands.ExecuteScript;
import commands.collManaging.Save;
import commands.informational.History;
import common.CommandClient;
import common.Request;
import common.Signal;
import common.Terminal;
import exceptions.NoSuchOptionException;
import exceptions.TimeOutException;
import file.FileManager;
import org.apache.commons.lang3.SerializationUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

/**
 * Terminal class. Has main loop for command enter and in line filter
 * @author Abdujalol Khodjaev
 */
public class ServerTerminal extends Terminal {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    private final UDPServer udp;
    private final CommandManager commands;
    private final History history = new History();
    private final FileManager file;

    /**
     * Default constructor
     * @param connection Server network connection
     * @param commandManager Manager with available commands
     * @param file File to save collection
     */
    public ServerTerminal(UDPServer connection, CommandManager commandManager, FileManager file) {
        udp = connection;
        commands = commandManager;
        commands.add("history", this.history);
        this.file = file;
        Server.logger.info("Терминал был создан!");
    }

    /**
     * Request handler. Interacts with request object depend on request signal
     * @param req Request to handle
     * @throws NoSuchOptionException May be thrown while executing command
     * @throws IOException May be thrown while sending data
     */
    public void handle(Request req) throws NoSuchOptionException, IOException {
        Server.logger.info("Начало обработки запроса");
        udp.inProcess = true;
        var signal = req.getSignal();
        Server.logger.info("Сигнал " + signal);
        switch (signal){
            case COMMAND, BIG_COMMAND, FILE_COMMAND -> {
                var command = mkComm((CommandClient) req.getObj());
                if (command.execute()) {
                    history.add(command.getName());
                    Server.logger.info(command.getName() + " выполнено успешно!");
                }
                udp.request(Signal.CLOSING, "");
                Server.logger.info("Ответ отправлен!");
            }
            case TEXT -> {
                System.out.println(req.getObj());
                Server.logger.info("Запрос успешно выполнен!");
                udp.request(Signal.CLOSING, "");
                Server.logger.info("Ответ отправлен!");
            }
        }
        udp.inProcess = false;
        Server.logger.info("Конец обработки запроса");
    }

    /**
     * Finds command depend on command type
     * @param obj CommandClient object
     * @return Command object
     */
    private Command mkComm(CommandClient obj) {
        var command = commands.find((obj.type()));
        command.setArgs(obj.argLine());
        return command;
    }

    /**
     * Interacts with line and executes command from. Need for 'execute_script' command.
     * @param line line for execution
     */
    @Override
    public void executor(String[] line) {
        String comm = line[0];
        String[] args = new String[line.length - 1];
        System.arraycopy(line, 1, args, 0, line.length - 1);
        var com = commands.find(comm);
        com.setArgs(args);
        try {
            Server.logger.info("Выполнение команды от execute_script: " + com.getName());
            com.execute();
        } catch (NoSuchOptionException | IOException e) {
            throw new RuntimeException(e);
        }
        udp.request(Signal.TEXT, com.response());
    }

    /**
     * Main server loop
     * @throws IOException May be thrown while receiving data
     */
    public void run() throws IOException {
        Scanner reader = new Scanner(System.in);
        while (true) {
            if (System.in.available() != 0) {
                String str = reader.nextLine();
                if (str.equals("exit")) break;
                else if (str.equals("save")){
                    new Save(file).execute();
                }
                continue;
            }
            Request request;
            try {
                request = SerializationUtils.deserialize(udp.receive());
            } catch (TimeOutException e) {
                continue;
            }
            try {
                handle(request);
            } catch (NoSuchOptionException e) {
                udp.request(Signal.TEXT, e.getMessage());
                System.out.println(e.getMessage());
            }
        }
    }
}
