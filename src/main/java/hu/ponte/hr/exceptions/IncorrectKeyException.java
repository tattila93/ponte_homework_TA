package hu.ponte.hr.exceptions;

import java.security.spec.InvalidKeySpecException;

/**
 * @author tattila93
 * Thrown if the given key is invalid.
 */
public class IncorrectKeyException extends InvalidKeySpecException {

    public IncorrectKeyException(String str){
        super(str);
    }
}
