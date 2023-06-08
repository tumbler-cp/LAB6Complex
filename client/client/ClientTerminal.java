package client;

import common.*;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Terminal for interaction with user.
 * @author Abdujalol Khodjaev
 * @see ClientHandler
 * @see UDPClient
 */
public class ClientTerminal {
    private final UDPInterface datagram;
    private final Scanner userIn;
    private final List<CommandClient> commands;
    private final Handler handler;

    /**
     * Default constructor
     * @param udp Clients UDP network
     * @param commandManager List of Client Command Objects
     * @param scanner Scanner to read user in from
     * @param handler Handler for server responses
     */
    public ClientTerminal(UDPInterface udp, List<CommandClient> commandManager, Scanner scanner, Handler handler){
        userIn = scanner;
        datagram = udp;
        commands = commandManager;
        this.handler = handler;
        Client.logger.info("Создан терминал");
    }

    /**
     * Inherited method. Programs' main loop.
     * @throws IOException May be thrown while receiving data
     */
    public void run() throws IOException {
        while (true) {
            String[] line;
            try {
                line = userIn.nextLine().split(" ");
            } catch (NoSuchElementException n) {
                Client.logger.info("Выключение...");
                return;
            }
            String commType = line[0];
            if(commType.equals("exit")) break;
            String[] commArgs = new String[line.length - 1];
            System.arraycopy(line, 1, commArgs, 0, line.length - 1);
            CommandClient command = null;
            for (CommandClient cc : commands) {
                if (cc.type().equals(commType)) {
                    command = cc;
                    break;
                }
            }
            if (command == null){
                System.out.println("Команда " + commType + " не найдена.");
                Client.logger.info("Команда " + commType + " не найдена.");
                continue;
            }
            command.setArgs(commArgs);
            Request request = new Request(command.signal(), command);
            Client.logger.info("Создан запрос " + request.getSignal() + request.getObj());
            if (request.validate()) {
                datagram.send(request.bytes());
                Client.logger.info("Запрос отправлен");
                Request response;
                do {
                    response = SerializationUtils.deserialize(datagram.receive());
                    Client.logger.info("Получен ответ с сигналом " + response.getSignal());
                } while (handler.handle(response));
            }
        }
    }
}
