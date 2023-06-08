package dragon;

import exceptions.NoSuchOptionException;
import server.Server;

/**
 * Color of Dragon
 *
 * @author Abdujalol Khodjaev
 */
public enum Color {
    GREEN,
    RED,
    BLUE,
    YELLOW,
    BROWN;

    /**
     * Parse String to Color
     *
     * @param string Color. Uppercase name of color or special number
     * @return Color
     */
    public static Color toColor(String string) throws NoSuchOptionException {
        return switch (string) {
            case "GREEN", "1" -> GREEN;
            case "RED", "2" -> RED;
            case "BLUE", "3" -> BLUE;
            case "YELLOW", "4" -> YELLOW;
            case "BROWN", "5" -> BROWN;
            default -> {
                Server.logger.error("Такой цвет отсутствует: " + string);
                throw new NoSuchOptionException();
            }
        };
    }
}
