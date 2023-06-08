package commands.collManaging;

import collection.CollectionManager;
import commands.Command;
import common.Signal;
import common.UDPInterface;
import dragon.*;
import exceptions.IncorrectFieldException;
import exceptions.KeyNotFoundException;
import exceptions.NoSuchOptionException;

import java.io.IOException;
import java.io.Serializable;

/**
 * Class of <b>replace_if_greater</b> command
 *
 * @author Abdujalol Khodjaev
 */
public class ReplaceGreater extends Command implements Serializable {
    /**
     * Collection manager this command works with
     */
    CollectionManager collection;
    private UDPInterface network;

    /**
     * Default constructor
     *
     * @param collectionManager Collection to work with
     */
    public ReplaceGreater(CollectionManager collectionManager, UDPInterface network) {
        super("replace_if_greater null {element}", "заменить значение по ключу, если новое значение больше старого");
        this.collection = collectionManager;
        this.network = network;
    }

    /**
     * Manual
     */
    public void manual() {
        network.request(Signal.TEXT,
                """
                        Syntax: replace_if_greater <int key> <int var> <String arg>\s
                        Vars:
                        1 - name
                        2 - Coordinates. <int x>/<int y>
                        3 - age
                        4 - color:
                            1)Green
                            2)Red
                            3)Blue
                            4)Yellow
                            5)Brown
                        5 - type:
                            1)Underground
                            2)Fire
                            3)Air
                        6 - character:
                            1)Wise
                            2)Good
                            3)Chaotic_Evil
                        7 - cave. <int Num. of treasure>
                        """.getBytes()
        );
    }

    @Override
    public boolean execute() throws NoSuchOptionException, IOException {
        isRunning = true;
        Dragon dragon;
        try {
            dragon = collection.getByKey(Integer.parseInt(getArgs()[0]));
        } catch (KeyNotFoundException k) {
            network.request(Signal.TEXT, "Элемента с таким ключом не существует".getBytes());
            isRunning = false;
            return true;
        }
        network.request(Signal.TEXT, dragon.toString().getBytes());
        if (getArgs().length > 1) {
            miniExecute1(dragon);
            isRunning = false;
            return true;
        }
        manual();
        miniExecute2(dragon);
        return true;
    }


    /**
     * 2nd part of executing
     * @throws NoSuchOptionException Will be thrown if there are any problems with enums
     */
    private void miniExecute1(Dragon dragon) throws NoSuchOptionException {
        String[] args = this.getArgs();
        byte[] message = "Если вы видите это сообщение замена выполнена.".getBytes();
        switch (args[1]) {
            case "1" -> {
                if (dragon.getName().compareTo(args[2]) > 0) return;
                dragon.setName(args[2]);
                network.request(Signal.TEXT, message);
            }
            case "2" -> {
                if (dragon.getCoordinates().compareTo(Coordinates.toCoordinates(args[2])) > 0) return;
                dragon.setCoordinates(Coordinates.toCoordinates(args[2]));
                network.request(Signal.TEXT, message);
            }
            case "3" -> {
                if (dragon.getAge() > Integer.parseInt(args[2])) return;
                dragon.setAge(Integer.parseInt(args[2]));
                addToResponse(message);
            }
            case "4" -> {
                if (dragon.getColor().compareTo(Color.toColor(args[2])) > 0) return;
                dragon.setColor(Color.toColor(args[2]));
                network.request(Signal.TEXT, message);
            }
            case "5" -> {
                if (dragon.getType().compareTo(DragonType.toDragonType(args[2])) > 0) return;
                dragon.setType(DragonType.toDragonType(args[2]));
                network.request(Signal.TEXT, message);
            }
            case "6" -> {
                if (dragon.getCharacter().compareTo(DragonCharacter.parse(args[2])) > 0) return;
                dragon.setCharacter(DragonCharacter.parse(args[2]));
                network.request(Signal.TEXT, message);
            }
            case "7" -> {
                try {
                    if (dragon.getCave().compareTo(new DragonCave(Integer.parseInt(args[2]))) > 0) return;
                    dragon.setCave(new DragonCave(Integer.parseInt(args[2])));
                    network.request(Signal.TEXT, message);
                } catch (IncorrectFieldException io) {
                    network.request(Signal.TEXT, "Это значение для DragonCave неверное".getBytes());
                }
            }
        }
    }

    private void miniExecute2(Dragon dragon) throws IOException {
        String buffer;
        while (true) {
            network.request(Signal.ASK, "Выберите поле которое хотите изменить: ".getBytes());
            buffer = new String(network.receive());
            buffer = buffer.trim();
            if (Integer.parseInt(buffer) > 7 || Integer.parseInt(buffer) < 1) {
                network.request(Signal.ASK, "Такой выбор отсутствует!".getBytes());
                continue;
            }
            break;
        }
        network.request(Signal.ASK, "Введите/Выберите значение: ".getBytes());
        String valBuff = new String(network.receive());
        setArgs(new String[]{getArgs()[0], buffer, valBuff.trim()});
        try {
            miniExecute1(dragon);
        } catch (NoSuchOptionException e) {
            network.request(Signal.TEXT, "Ошибка".getBytes());
        }
    }
}

