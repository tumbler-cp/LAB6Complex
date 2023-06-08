package dragon;

import exceptions.NoSuchOptionException;
import server.Server;

/**
 * Dragon character
 *
 * @author Abdujalol Khodjaev
 */
public enum DragonCharacter {
    WISE,
    GOOD,
    CHAOTIC_EVIL;

    /**
     * Parses String to DragonCharacter
     *
     * @param string String to parse
     * @return WISE, GOOD or CHAOTIC_EVIL
     */
    public static DragonCharacter parse(String string) throws NoSuchOptionException {
        return switch (string) {
            case "WISE", "1" -> WISE;
            case "GOOD", "2" -> GOOD;
            case "CHAOTIC_EVIL", "3" -> CHAOTIC_EVIL;
            default -> {
                Server.logger.error("Такой характер отсутствует: " + string);
                throw new NoSuchOptionException();
            }
        };
    }
}
