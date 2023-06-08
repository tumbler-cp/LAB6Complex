package dragon;

import exceptions.IncorrectFieldException;
import exceptions.NoSuchOptionException;
import server.ServerTerminal;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Class Dragon
 *
 * @author Abdujalol Khodjaev
 * @see collection.CollectionManager
 * @see Color
 * @see Coordinates
 * @see Dragon
 * @see DragonCave
 * @see DragonType
 */
public class Dragon implements Comparable<Dragon> {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int age; //Значение поля должно быть больше 0
    private Color color; //Поле не может быть null
    private DragonType type; //Поле может быть null
    private DragonCharacter character; //Поле может быть null
    private DragonCave cave; //Поле может быть null
    //id, name, coordinates, creationDate, age, color, type, character, cave

    /**
     * Default constructor
     *
     * @param name        Name of dragon
     * @param coordinates Dragon coordinates
     * @param age         Dragon age
     * @param color       Dragon color 1-5
     * @param type        Dragon type 1-3
     * @param character   Dragon character 1-3
     * @param cave        Dragon cave
     */
    public Dragon(String name, Coordinates coordinates, Integer age, Color color, DragonType type, DragonCharacter character, DragonCave cave) {
        this.id = maxId;
        Dragon.updateId();
        this.name = name;
        this.coordinates = coordinates;
        this.age = age;
        this.color = color;
        this.type = type;
        this.character = character;
        this.cave = cave;
        this.creationDate = LocalDate.now();
    }

    /**
     * Max fixed id. Used for applying id for new Dragon objects
     */
    private static int maxId = 1;

    /**
     * Increments maxId
     */
    public static void updateId() {
        maxId += 1;
    }

    /**
     * Checks correctness of Dragon field.
     *
     * @return Correctness
     */
    public boolean check() {
        if (this.name == null || this.name.isEmpty()) return false;
        if (this.coordinates == null) return false;
        if (this.age <= 0) return false;
        if (this.color == null) return false;
        if (this.type == null) return false;
        if (this.character == null) return false;
        if (this.cave == null) return false;
        return cave.check();
    }

    /**
     * Age setter
     *
     * @param age Age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Age getter
     *
     * @return Dragon age
     */
    public int getAge() {
        return age;
    }

    /**
     * Cave setter
     *
     * @param cave cave to set
     */
    public void setCave(DragonCave cave) {
        this.cave = cave;
    }

    /**
     * Cave getter
     *
     * @return Dragon cave
     */

    public DragonCave getCave() {
        return cave;
    }

    /**
     * Dragon character setter
     *
     * @param character character to set
     */
    public void setCharacter(DragonCharacter character) {
        this.character = character;
    }

    /**
     * Dragon character getter
     *
     * @return Dragon character
     */
    public DragonCharacter getCharacter() {
        return character;
    }

    /**
     * Dragon color setter
     *
     * @param color color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Color getter
     *
     * @return dragon color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Coordinates setter
     *
     * @param coordinates coordinates to set
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Coordinates getter
     *
     * @return Dragon coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Creation date setter
     *
     * @param s LocalDate to set
     */
    public void setCreationDate(String s) {
        LocalDate creationDate;
        String[] c = s.split("-");
        creationDate = LocalDate.of(Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]));
        this.creationDate = creationDate;
    }

    /**
     * Creation date getter
     *
     * @return Dragon creation date
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     * Id setter
     *
     * @param id id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Id getter
     *
     * @return Dragon id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Name setter
     *
     * @param name Name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Name getter
     *
     * @return Dragon name
     */
    public String getName() {
        return name;
    }

    /**
     * Type setter
     *
     * @param type Type to set
     */
    public void setType(DragonType type) {
        this.type = type;
    }

    /**
     * Type getter
     *
     * @return Dragon type
     */
    public DragonType getType() {
        return type;
    }


    /**
     * toString override
     *
     * @return Information about Dragon
     */
    @Override
    public String toString() {
        return ServerTerminal.RED + "id: " + ServerTerminal.RESET + getId() + "\n" +
                ServerTerminal.RED + "name: " + ServerTerminal.RESET + getName() + "\n" +
                ServerTerminal.RED + "age: " + ServerTerminal.RESET + getAge() + "\n" +
                ServerTerminal.RED + "coordinates: " + ServerTerminal.RESET + getCoordinates() + "\n" +
                ServerTerminal.RED + "color: " + ServerTerminal.RESET + getColor() + "\n" +
                ServerTerminal.RED + "type: " + ServerTerminal.RESET + getType() + "\n" +
                ServerTerminal.RED + "character: " + ServerTerminal.RESET + getCharacter() + "\n" +
                ServerTerminal.RED + "cave: " + ServerTerminal.RESET + getCave() + "\n";
    }

    /**
     * compareTo override
     *
     * @param o Dragon to compare with
     * @return Result of comparing ids of this Dragon and Dragon o
     */
    @Override
    public int compareTo(Dragon o) {
        return this.getName().compareTo(o.getName());
    }

    /**
     * Makes dragon from String[]
     *
     * @param args   String[] param with necessary data
     * @param fileIn Set it true if parsing from csv file
     * @return Dragon
     * @throws NoSuchOptionException If there are problems with enums of Dragon
     */
    public static Dragon parseDrag(String[] args, boolean fileIn) throws NoSuchOptionException, IOException {
        //12,"12","in","1.0 2.0","2023-03-18","12","GREEN","UNDERGROUND","WISE","12"
        //12, "in","1.0 2.0","12","GREEN","UNDERGROUND","WISE","12"
        int offset = fileIn ? 1 : 0;
        return new Dragon(null, null, 0, null, null, null, null) {{
            if (fileIn) setId(Integer.parseInt(args[1]));
            setName(args[1 + offset]);
            setCoordinates(Coordinates.toCoordinates(args[2 + offset]));
            if (fileIn) setCreationDate(args[4]);
            setAge(Integer.parseInt(args[3 + 2 * offset]));
            setColor(Color.toColor(args[4 + 2 * offset]));
            setType(DragonType.toDragonType(args[5 + 2 * offset]));
            setCharacter(DragonCharacter.parse(args[6 + 2 * offset]));
            try {
                setCave(new DragonCave(Integer.parseInt(args[7 + 2 * offset])));
            } catch (IncorrectFieldException e) {
                System.out.println("Переданное значение для DragonCave неправильное");
            }
        }};
    }
}

