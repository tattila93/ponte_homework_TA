package hu.ponte.hr.exceptions;

import java.io.IOException;

/**
 * Thrown if the file not found.
 * @author tattila93
 */
public class IncorrectFileException extends IOException {

    public IncorrectFileException(String str){
        super(str);
    }
}
