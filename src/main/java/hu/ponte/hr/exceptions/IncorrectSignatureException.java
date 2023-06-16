package hu.ponte.hr.exceptions;

import java.security.SignatureException;

/**
 * @author tattila93
 * Thrown if the signature is not correct.
 */
public class IncorrectSignatureException extends SignatureException {

    public IncorrectSignatureException(String str){
        super(str);
    }
}
