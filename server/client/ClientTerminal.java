package client;

import common.*;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientTerminal extends Terminal {
    private final UDPInterface datagram;
    private final Scanner userIn;
    private final List<CommandClient> commands;
    private final Handler handler;
    public ClientTerminal(UDPInterface udp, List<CommandClient> commandManager, Scanner scanner, Handler handler){
        userIn = scanner;
        datagram = udp;
        commands = commandManager;
        this.handler = handler;
    }
    @Override
    public void run() throws IOException {
        while (true) {
            String[] line;
            try {
                line = userIn.nextLine().split(" ");
            } catch (NoSuchElementException n) {
                System.out.println("Выключение...");
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
                continue;
            }
            command.setArgs(commArgs);
            Request request = new Request(command.signal(), command);
            if (request.validate()) {
                datagram.send(request.bytes());
                Request response;
                do {
                    response = SerializationUtils.deserialize(datagram.receive());
                } while (handler.handle(response));
            }
            /*
            if (command.validate())
            {
                datagram.request(command.signal(), SerializationUtils.serialize(command));
                var signal = (Signal) SerializationUtils.deserialize(datagram.receive());
                while (signal != Signal.CLOSING) {
                        if (signal == Signal.ASK) {
                            System.out.print(new String(datagram.receive()));
                            try {
                                datagram.send(userIn.nextLine().getBytes());
                            }
                            catch (NoSuchElementException n) {
                                System.out.println(ServerTerminal.RED + "Выключение..." + ServerTerminal.RESET);
                            }
                        } else if (signal == Signal.FILE_COMMAND) {
                            String filename = new String(datagram.receive()).trim();
                            File toSend = new File(filename);
                            datagram.send(SerializationUtils.serialize(toSend));
                        }
                        else if (signal == Signal.WAIT) {
                            System.out.println("Сервер отправил сигнал ожидания. Повторите запрос через некоторое время!");
                            break;
                        }
                        else System.out.print(new String(datagram.receive()));
                    signal = SerializationUtils.deserialize(datagram.receive());
                }
             */
        }
    }
}
