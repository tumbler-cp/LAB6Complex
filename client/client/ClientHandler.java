package client;

import common.Handler;
import common.Request;
import common.Signal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Abdujalol Khodjaev
 * Module to handle incoming server requests and responses.
 * @see Handler
 * @see ClientTerminal
 * @see UDPClient
 */
public class ClientHandler implements Handler {
    private final UDPClient network;

    /**
     * Default constructor
     * @param udp Clients' udp network
     */
    public ClientHandler(UDPClient udp){
        this.network = udp;
    }

    /**
     * Executes necessary code depend on the type of signal it got from request object.
     * @param request Request object to handle
     * @return boolean to handle requests while it returns true. When it returns false, client can start to type a new command.
     * @see Request
     */
    public boolean handle(Request request) {
        switch (request.getSignal()){
            case CLOSING -> {
                return false;
            }
            case TEXT -> {
                System.out.print(request.getObj());
                return true;
            }
            case ASK -> {
                System.out.print(request.getObj());
                network.send((new Scanner(System.in).nextLine()).getBytes());
                return true;
            }
            case FILE_COMMAND -> {
                var filename = (String) request.getObj();
                Client.logger.info("Запрошен файл: " + request.getObj());
                File file = new File(filename);
                FileInputStream fin;
                try {
                    fin = new FileInputStream(file);
                } catch (FileNotFoundException fnf) {
                    Client.logger.info("Запрошенный файл не найден.");
                    network.send(Signal.CLOSING.bytes());
                    return true;
                }
                try {
                    network.sendFile(fin);
                    fin.close();
                } catch (IOException e) {
                    Client.logger.error("Ошибка при чтении файла!");
                    return true;
                }
                Client.logger.info("Файл успешно отправлен");
                return true;
            }
            case WAIT -> {
                Client.logger.warn("Сервер отправил сигнал ожидания");
                return false;
            }
            default -> {
                Client.logger.error("Ошибка на сервере!");
                return false;
            }
        }
    }
}
