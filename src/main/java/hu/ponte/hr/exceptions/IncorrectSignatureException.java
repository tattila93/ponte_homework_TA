package hu.ponte.hr.exceptions;

import java.security.SignatureException;

public class IncorrectSignatureException extends SignatureException {

    public IncorrectSignatureException(String str){
        super(str);
    }
}
