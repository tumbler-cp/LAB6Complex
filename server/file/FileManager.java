package file;

import collection.CollectionManager;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import dragon.Dragon;
import exceptions.EmptyCollectionException;
import exceptions.IncorrectValueException;
import exceptions.NoSuchOptionException;
import server.Server;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for managing file, where collection is saved in csv format
 *
 * @author Abdujalol Khodjaev
 */
public class FileManager {
    private final String filename;
    private final CollectionManager collection;

    /**
     * Default constructor
     *
     * @param filename          Name of .csv file
     * @param collectionManager Collection, which objects can be saved, added
     */
    public FileManager(String filename, CollectionManager collectionManager) throws IOException {
        this.filename = filename;
        this.collection = collectionManager;
        try {
            this.update();
        } catch (FileNotFoundException f) {
            System.out.println("Указанный файл не найден: " + filename);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println("Ошибка потока ввода/вывода.");
        }
        new File(filename) {{
            if(!canWrite()) if (setWritable(true)) System.out.println("Были изменены права к записи в файл.");
            if(!canRead()) if (setReadable(true)) System.out.println("Были изменены права к чтению файла.");
        }};
    }

    /**
     * Updates Collection adding objects from given file
     *
     * @throws IOException            In case if there will be any problems with IO stream
     * @throws CsvValidationException In case if .csv file has problems
     */
    public void update() throws IOException, CsvValidationException {
        CSVReader in = new CSVReader(new FileReader(this.filename));
        String[] currentLine;

        while ((currentLine = in.readNext()) != null) {
            if (currentLine.length == 0) return;
            try {
                try {
                    if (!collection.insertWithKey(Integer.parseInt(currentLine[0]), Dragon.parseDrag(currentLine, true))) {
                        System.out.println("Дракон с id " + Dragon.parseDrag(currentLine, true).getId() + " неправильный.");
                    }
                } catch (IncorrectValueException e) {
                    System.out.println("Неправильные данные в файле. Дракон не подошел по условиям.");
                }
            } catch (NoSuchOptionException n) {
                System.out.println("Неправильный формат данных в файле.");
            }

        }
        in.close();
    }

    /**
     * Write objects in file
     *
     * @throws IOException In case if there will be any problems with IO stream
     */
    public void write() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(this.filename));
        CSVWriter out = new CSVWriter(bw);
        out.writeAll(get_dragon(collection));
        out.flush();
        out.close();
    }

    /**
     * Creates List<String[]> for CSVWriter
     *
     * @param c Collection
     * @return List<String [ ]>
     */
    private static List<String[]> get_dragon(CollectionManager c) {
        return new ArrayList<>() {{
            /**/
            try {
                for (Dragon d : c.get_collection().values()) {
                    add(new String[]{
                                    c.getObjKey(d).toString(),
                                    d.getId().toString(),
                                    d.getName(),
                                    d.getCoordinates().toString(),
                                    d.getCreationDate().toString(),
                                    String.valueOf(d.getAge()),
                                    d.getColor().toString(),
                                    d.getType().toString(),
                                    d.getCharacter().toString(),
                                    d.getCave().toString()
                            }
                    );
                }
            } catch (EmptyCollectionException e) {
                System.out.println("Коллекция пуста");
            }
        }};
    }
}
