package common;

import java.io.Serializable;

public interface CommandClient extends Serializable {
    String type();
    String[] argLine();
    void setArgs(String[] args);
    boolean validate();
    Signal signal();
}
