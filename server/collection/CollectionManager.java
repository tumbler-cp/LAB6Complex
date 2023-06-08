package collection;

import common.Terminal;
import dragon.Dragon;
import exceptions.*;
import server.Server;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class for managing collection
 *
 * @author Abdujalol Khodjaev
 * @see commands.CommandManager
 */

public class CollectionManager {
    /**
     * Field dragons. Contains Dragon objects
     */
    private final HashMap<Integer, Dragon> dragons = new HashMap<>();
    /**
     * Field initDate. Date of initialization of class
     */
    private final LocalDate initDate;

    /**
     * Default constructor
     */
    public CollectionManager() {
        initDate = LocalDate.now();
    }

    public boolean key_check(int key) throws ValueAlreadyExist {
        if (this.dragons.containsKey(key)) {
            Server.logger.error("Ключ уже существует");
            throw new ValueAlreadyExist();
        }
        return false;
    }

    public boolean insertWithKey(int key, Dragon dragon) throws IncorrectValueException {
        try {
            key_check(key);
        } catch (ValueAlreadyExist e) {
            return false;
        }
        if (dragon.check()) {
            this.dragons.put(key, dragon);
            return true;
        }
        Server.logger.error("Неподходящий дракон");
        throw new IncorrectValueException();
    }

    /**
     * Method for getting dragons
     *
     * @return HashMap dragons
     */
    public HashMap<Integer, Dragon> get_collection() throws EmptyCollectionException {
        if (this.dragons.isEmpty()){
            Server.logger.error("Пустая коллекция");
            throw new EmptyCollectionException();}
        return this.dragons;
    }

    /**
     * Method for getting dragons values in List
     *
     * @return List<Dragon></>
     */
    public List<Dragon> get_list() {
        return new ArrayList<>(this.dragons.values());
    }

    public Dragon getByKey(int k) throws KeyNotFoundException {
        if (!dragons.containsKey(k)){
            Server.logger.error("Ключ не найден");
            throw new KeyNotFoundException();
        }
        return this.dragons.get(k);
    }

    public Dragon getById(int id) throws IdNotFoundException {
        for (Dragon d : dragons.values()) {
            if (id == d.getId()) return d;
        }
        Server.logger.error("id не найдено");
        throw new IdNotFoundException();
    }

    /**
     * toString Override
     *
     * @return Information about collection
     */
    @Override
    public String toString() {
        return Terminal.YELLOW + "Collection type: " + Terminal.RESET + this.dragons.getClass().toString().split("class java.util.")[1] + "\n" +
                Terminal.YELLOW + "Init. Date: " + Terminal.RESET + this.initDate.toString() + "\n" +
                Terminal.YELLOW + "Object count: " + Terminal.RESET + this.dragons.size();
    }

    public Integer getObjKey(Dragon d) {
        for (Integer k : dragons.keySet()) {
            if (dragons.get(k) == d) {
                return k;
            }
        }
        return null;
    }
}
