package commands.collManaging;

import collection.CollectionManager;
import commands.Command;
import common.Signal;
import common.UDPInterface;
import dragon.*;
import exceptions.IncorrectFieldException;
import exceptions.NoSuchOptionException;

import java.io.IOException;
import java.io.Serializable;

/**
 * Class of <b>insert</b> command
 *
 * @author Abdujalol Khodjaev
 */
public class Insert extends Command implements Serializable {

    /**
     * Collection manager this command works with
     */
    CollectionManager collectionManager;
    private Dragon dragArg = null;
    private final UDPInterface network;

    /**
     * Default constructor
     *
     * @param collectionManager collection to work with
     */
    public Insert(CollectionManager collectionManager, UDPInterface network) {
        super("insert null {element}", "добавить новый элемент с заданным ключом. Введите insert man для просмотра инструкции");
        this.collectionManager = collectionManager;
        this.network = network;
    }

    /**
     * Insert user in loop.
     */

    private void inLoop() throws IOException {
        int key = Integer.parseInt(getArgs()[0]);
        String[] mans = new String[]
                {
                        "Введите имя: ",
                        "Введите координаты(x y): ",
                        "Введите возраст: ",
                        "1.Зеленый\n2.Красный\n3.Синий\n4.Жёлтый\n5.Коричневый\nВыберите цвет: ",
                        "1.Подземный\n2.Воздушный\n3.Огненный\nВыберите тип: ",
                        "1.Мудрый\n2.Хороший\n3.Злой хаотичный\nВыберите характер: ",
                        "Введите число сокровищ в пещере: "
                };
        Dragon d = new Dragon(null, null, 0, null, null, null, null);
        String buff;
        for (int i = 0; i < 7; ++i) {
            System.out.println("Test 2 passed!");
            network.request(Signal.ASK , mans[i]);
            buff = new String(network.receive()).trim();
            System.out.println(buff);
            try {
                switch (i) {
                    case 0 -> d.setName(buff);
                    case 1 -> d.setCoordinates(Coordinates.toCoordinates(buff));
                    case 2 -> d.setAge(Integer.parseInt(buff));
                    case 3 -> d.setColor(Color.toColor(buff));
                    case 4 -> d.setType(DragonType.toDragonType(buff));
                    case 5 -> d.setCharacter(DragonCharacter.parse(buff));
                    case 6 -> d.setCave(new DragonCave(Integer.parseInt(buff)));
                }
            } catch (NoSuchOptionException n) {
                network.request(Signal.TEXT, "Такой выбор отсутствует!");
                i--;
            } catch (NumberFormatException | IncorrectFieldException n) {
                network.request(Signal.TEXT ,"Неправильное значение для данного поля");
                i--;
            }
        }
        if (collectionManager.insertWithKey(key, d)) network.request(Signal.TEXT, "Дракон успешно добавлен!");
        else network.request(Signal.TEXT,"Ошибка при добавлении!");
    }
    public void manual() {
        addToResponse("""
                       Syntax: insert <int key> <values[]>\s
                       Vars:
                               1 - name                           | 1 - GREEN       | 1 - \u001B[33mUNDERGROUND\u001B[0m  | 1 - WISE
                               2 - Coordinates. <int x>/<int y>   | 2 - RED         | 2 - \u001B[31mFIRE\u001B[0m         | 2 - GOOD
                               3 - age                            | 3 - BLUE        | 3 - \u001B[32mAIR\u001B[0m          | 3 - CHAOTIC_EVIL
                               4 - color: column 1                | 4 - YELLOW      |                  |
                               5 - type: column 2                 | 5 - BROWN       |                  |
                               6 - character: column 3            |                 |                  |
                               7 - cave. <int Num. of treasure>   |                 |                  |
                       Чтобы обновить поля сразу в нескольких элементах необходимо вводить элементы/аргументы/значения через запятую без пробелов.
                """);
    }
    @Override
    public void setArgs(String[] args) {
        super.setArgs(args);
    }
    @Override
    public boolean execute() throws NoSuchOptionException, IOException {
        isRunning = true;
        if (getArgs().length == 0) {
            network.request(Signal.TEXT, "Введите ключ нового дракона.");
            isRunning = false;
            return true;
        }
        if(getArgs()[0].equals("man")) {
            manual();
            isRunning = false;
            return true;
        }
        System.out.println("Здесь");
        int key = Integer.parseInt(getArgs()[0]);
        if (collectionManager.key_check(key)) {
            isRunning = false;
            return true;
        }
        if (dragArg != null) {
            collectionManager.insertWithKey(key, dragArg);
            dragArg = null;
            isRunning = false;
            return true;
        }
        if (getArgs().length > 1) {
            String[] args = getArgs();
            String[] finalArgs = new String[8];
            for (int i = 0; i < args.length; i++) {
                if (i < 2) {
                    finalArgs[i] = args[i];
                } else if (i == 2) {
                    finalArgs[i] = args[i] + " " + args[i + 1];
                    i++;
                } else {
                    finalArgs[i - 1] = args[i];
                }
            }
            if (collectionManager.insertWithKey(Integer.parseInt(finalArgs[0]), Dragon.parseDrag(finalArgs, false))) {
                network.request(Signal.TEXT, "Дракон успешно добавлен");
            } else {
                network.request(Signal.TEXT, "Неизвестная ошибка");
                isRunning = false;
                return true;
            }
            isRunning = false;
            return true;
        }
        System.out.println("Test 1 passed!");
        inLoop();
        return true;
    }
}
