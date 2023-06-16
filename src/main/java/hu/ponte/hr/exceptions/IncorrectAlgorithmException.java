package hu.ponte.hr.exceptions;

import java.security.NoSuchAlgorithmException;

/**
 * Thrown if the given algoritm not exists.
 * @author tattila93
 */
public class IncorrectAlgorithmException extends NoSuchAlgorithmException {
    public IncorrectAlgorithmException(String str){
        super(str);
    }
}
