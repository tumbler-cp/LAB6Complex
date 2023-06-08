package client;

import client.commands.*;
import common.CommandClient;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client implements Serializable {
    static UDPClient datagram;

    public static void main(String[] args) throws IOException {

        try {
            datagram = new UDPClient("localhost", 4567);
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода");
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

        Scanner userIn = new Scanner(System.in);
        ClientTerminal terminal = new ClientTerminal(datagram, commands, userIn, new ClientHandler(datagram));
        terminal.run();
    }
}
