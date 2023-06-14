package hu.ponte.hr.exceptions;

import java.io.IOException;

public class IncorrectFileException extends IOException {

    public IncorrectFileException(String str){
        super(str);
    }
}
