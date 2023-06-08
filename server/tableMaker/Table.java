package tableMaker;

import dragon.Dragon;
import server.ServerTerminal;

import java.util.Map;


/**
 * Table maker for command 'Show'
 */
public class Table {
    private final Map<Integer, Dragon> collection;
    private int max_id = 2,
            max_key = 3,
            max_name = 4,
            max_coor = 11,
            max_color = 5,
            max_character = 9,
            max_type = 4,
            max_cave = 4,
            max_date = 14,
            max_age = 3;

    /**
     * Default constructor
     *
     * @param map HashMap of collectionManager
     */
    public Table(Map map) {
        collection = map;
        reg_ints();
    }

    /**
     * Sets max lengths of every field of all Dragons
     */
    private void reg_ints() {
        for (int k : collection.keySet()) {
            Dragon d = collection.get(k);
            if (d.getId().toString().length() > max_id) max_id = d.getId().toString().length();
            if (d.getName().length() > max_name) max_name = d.getName().length();
            if (d.getCoordinates().toString().length() > max_coor) max_coor = d.getCoordinates().toString().length();
            if (d.getColor().toString().length() > max_color) max_color = d.getColor().toString().length();
            if (d.getCharacter().toString().length() > max_character)
                max_character = d.getCharacter().toString().length();
            if (d.getType().toString().length() > max_type) max_type = d.getType().toString().length();
            if (d.getCave().toString().length() > max_cave) max_cave = d.getCave().toString().length();
            if (String.valueOf(d.getAge()).length() > max_age) max_age = String.valueOf(d.getAge()).length();
            if (String.valueOf(k).length() > max_key) max_key = String.valueOf(k).length();
        }
    }

    /**
     * Returns collection in table form
     *
     * @return Tabled (Not array table) collection
     */
    public String toString() {
        StringBuilder table = new StringBuilder();
        String spc = " ";
        table.append(ServerTerminal.BLUE);
        table.append("key").append(spc.repeat(max_key - 2)).append('|');
        table.append("id").append(spc.repeat(max_id - 1)).append('|');
        table.append("name").append(spc.repeat(max_name - 3)).append('|');
        table.append("age").append(spc.repeat(max_age - 2)).append('|');
        table.append("creation date" + ' ' + '|');
        table.append("coordinates").append(spc.repeat(max_coor - 10)).append('|');
        table.append("color").append(spc.repeat(max_color - 4)).append('|');
        table.append("character").append(spc.repeat(max_character - 8)).append('|');
        table.append("type").append(spc.repeat(max_type - 3)).append('|');
        table.append("cave").append(spc.repeat(max_cave - 3)).append('|').append('\n');
        table.append(ServerTerminal.RESET);
        for (int k : collection.keySet()) {
            Dragon d = collection.get(k);

            table.append(k);
            table.append(spc.repeat(max_key + 1 - String.valueOf(k).length()));
            table.append("|");
            table.append(d.getId());
            table.append(spc.repeat(max_id + 1 - d.getId().toString().length()));
            table.append("|");
            table.append(d.getName());
            table.append(spc.repeat(max_name + 1 - d.getName().length()));
            table.append("|");
            table.append(d.getAge());
            table.append(spc.repeat(max_age + 1 - String.valueOf(d.getAge()).length()));
            table.append("|");
            table.append(d.getCreationDate());
            table.append(spc.repeat(4));
            table.append("|");
            table.append(d.getCoordinates());
            table.append(spc.repeat(max_coor + 1 - d.getCoordinates().toString().length()));
            table.append("|");
            table.append(d.getColor());
            table.append(spc.repeat(max_color + 1 - d.getColor().toString().length()));
            table.append("|");
            table.append(d.getCharacter());
            table.append(spc.repeat(max_character + 1 - d.getCharacter().toString().length()));
            table.append("|");
            table.append(d.getType());
            table.append(spc.repeat(max_type + 1 - d.getType().toString().length()));
            table.append("|");
            table.append(d.getCave());
            table.append(spc.repeat(max_cave + 1 - d.getCave().toString().length()));
            table.append("|");
            table.append("\n");
        }
        return table.toString();
    }

}