package hu.ponte.hr.exceptions;

import java.security.NoSuchAlgorithmException;

public class IncorrectAlgorithmException extends NoSuchAlgorithmException {
    public IncorrectAlgorithmException(String str){
        super(str);
    }
}
