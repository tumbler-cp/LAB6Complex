package server;

import commands.Command;
import commands.CommandManager;
import commands.ExecuteScript;
import commands.informational.History;
import common.CommandClient;
import common.Request;
import common.Signal;
import common.Terminal;
import exceptions.NoSuchOptionException;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.util.Scanner;

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

    public ServerTerminal(UDPServer connection, CommandManager commandManager) {
        udp = connection;
        commands = commandManager;
        commands.add("history", this.history);
    }

    public void handle(Request req) throws NoSuchOptionException, IOException {
        udp.inProcess = true;
        var signal = req.getSignal();
        switch (signal){
            case COMMAND, BIG_COMMAND, FILE_COMMAND -> {
                var command = mkComm(req.getObj());
                if (command.execute()) history.add(command.getName());
                udp.request(Signal.CLOSING, "");
            }
            case TEXT -> {
                System.out.println(req.getObj());
                udp.request(Signal.CLOSING, "");
            }
        }
        udp.inProcess = false;
    }
    private Command mkComm(Object obj) {
        CommandClient commReq = (CommandClient) obj;
        var command = commands.find((commReq.type()));
        command.setArgs(commReq.argLine());
        return command;
    }
    @Override
    public void executor(String[] line) {
        String comm = line[0];
        String[] args = new String[line.length - 1];
        System.arraycopy(line, 1, args, 0, line.length - 1);
        var com = commands.find(comm);
        com.setArgs(args);
        try {
            System.out.println("Выполнение...");
            com.execute();
        } catch (NoSuchOptionException | IOException e) {
            throw new RuntimeException(e);
        }
        udp.request(Signal.TEXT, com.response());
        /*
        try {
            if(com.execute()) history.add(com.getName());
            String toOut = com.response();
            System.out.println(toOut);
            udp.request(Signal.TEXT, toOut);
        } catch (NoSuchOptionException e) {
            udp.request(Signal.TEXT, "Нет такой опции");
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода");
        }
        */
    }
    public void run() throws IOException {
        Scanner in = new Scanner(System.in);
        while (true) {
            if (in.hasNextLine()) {
                udp.inProcess = true;

                udp.inProcess = false;
            }
            Request request = SerializationUtils.deserialize(udp.receive());
            try {
                handle(request);
            } catch (NoSuchOptionException e) {
                udp.request(Signal.TEXT, e.getMessage());
                System.out.println(e.getMessage());
            }
        }
    }

}
