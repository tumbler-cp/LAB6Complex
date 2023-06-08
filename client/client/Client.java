package client;

import client.commands.*;
import common.CommandClient;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Client implements Serializable {
    public static final Logger logger = LogManager.getLogger();
    public static void main(String[] args) throws IOException {
        UDPClient datagram;
        Scanner userIn = new Scanner(System.in);
        String HOST = "localhost";
        int PORT = 666;
        System.out.print("Сервер находится на другом адресе(y n)?(Сейчас: " + HOST + "):");
        while (true) {
            String s = userIn.nextLine().trim();
            if (!(s.equals("y") || s.equals("n"))){
                System.out.print("Введите y или n: ");
                continue;
            }
            if (s.equals("y")){
                System.out.print("Введите хост: ");
                s = userIn.nextLine().trim();
                HOST = s;
                logger.info("Хост был изменен на " + HOST);
            }
            break;
        }
        System.out.print("Нужно изменить порт?(y n). Сейчас: " + PORT + " :");
        while (true) {
            String s = userIn.nextLine().trim();
            if (!(s.equals("y") || s.equals("n"))){
                System.out.print("Введите y или n: ");
                continue;
            }
            if (s.equals("y")) {
                while (true) {
                    System.out.print("Введите порт: ");
                    s = userIn.nextLine().trim();
                    if (!s.matches("[0-9]{0,5}")) {
                        System.out.println("Порт должен принимать значения от 0 до 65535");
                        continue;
                    }
                    PORT = Integer.parseInt(s);
                    logger.info("Порт был изменен на " + PORT);
                    break;
                }
            }
            break;
        }
        try {
            datagram = new UDPClient(HOST, PORT);
        } catch (IOException e) {
            logger.error("Ошибка ввода/вывода");
            return;
        }

        List<CommandClient> commands = new ArrayList<>(){{
            add(new HelpClient());
            add(new InsertClient());
            add(new ClearClient());
            add(new InfoClient());
            add(new ShowClient());
            add(new PrintAscendingClient());
            add(new RemoveKeyClient());
            add(new UpdateIdClient());
            add(new RemoveLowerKeyClient());
            add(new FilterNameClient());
            add(new AscendingColorClient());
            add(new ReplaceGreaterClient());
            add(new ExecuteScriptClient());
            add(new HistoryClient());
        }};


        ClientTerminal terminal = new ClientTerminal(datagram, commands, userIn, new ClientHandler(datagram));
        terminal.run();
        datagram.close();
    }
}
