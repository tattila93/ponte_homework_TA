package hu.ponte.hr.exceptions;

import java.security.SignatureException;

/**
 * Thrown if the signature is not correct.
 * @author tattila93
 */
public class IncorrectSignatureException extends SignatureException {

    public IncorrectSignatureException(String str){
        super(str);
    }
}
