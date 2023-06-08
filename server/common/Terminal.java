package common;

import java.io.IOException;

public abstract class Terminal {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public void run () throws IOException{};
    public void executor (String[] line) {};
}
