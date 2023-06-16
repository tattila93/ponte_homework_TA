package hu.ponte.hr.exceptions;

import java.security.NoSuchAlgorithmException;

/**
 * @author tattila93
 * Thrown if the given algoritm not exists.
 */
public class IncorrectAlgorithmException extends NoSuchAlgorithmException {
    public IncorrectAlgorithmException(String str){
        super(str);
    }
}
