package client;

import common.Handler;
import common.Request;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class ClientHandler implements Handler {
    private final UDPClient network;
    public ClientHandler(UDPClient udp){
        this.network = udp;
    }
    public boolean handle(Request request) {
        switch (request.getSignal()){
            case CLOSING -> {
                return false;
            }
            case TEXT -> {
                System.out.println(request.getObj());
                return true;
            }
            case ASK -> {
                System.out.println(request.getObj());
                network.send((new Scanner(System.in).nextLine()).getBytes());
                return true;
            }
            case FILE_COMMAND -> {
                var filename = (String) request.getObj();
                System.out.println("Запрошен файл: " + request.getObj());
                File file = new File(filename);
                FileInputStream fin;
                try {
                    fin = new FileInputStream(file);
                } catch (FileNotFoundException fnf) {
                    System.out.println("Сервер запросил файл. Запрошенный файл не найден: ");
                    return true;
                }
                try {
                    network.sendFile(fin);
                    fin.close();
                } catch (IOException e) {
                    System.out.println("Ошибка при чтении файла!");
                    return true;
                }
                System.out.println("Файл успешно отправлен");
                return true;
            }
            case WAIT -> {
                System.out.println("Сервер отправил сигнал ожидания");
                return false;
            }
            default -> {
                System.out.println("Ошибка на сервере!");
                return false;
            }
        }
    }
}
