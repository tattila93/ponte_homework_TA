package hu.ponte.hr.exceptions;

import java.security.spec.InvalidKeySpecException;

/**
 * Thrown if the given key is invalid.
 * @author tattila93
 */
public class IncorrectKeyException extends InvalidKeySpecException {

    public IncorrectKeyException(String str){
        super(str);
    }
}
