package hu.ponte.hr.exceptions;

import java.io.IOException;

/**
 * @author tattila93
 * Thrown if the file not found.
 */
public class IncorrectFileException extends IOException {

    public IncorrectFileException(String str){
        super(str);
    }
}
