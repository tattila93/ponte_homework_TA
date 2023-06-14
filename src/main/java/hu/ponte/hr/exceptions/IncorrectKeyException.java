package hu.ponte.hr.exceptions;

import java.security.spec.InvalidKeySpecException;

public class IncorrectKeyException extends InvalidKeySpecException {

    public IncorrectKeyException(String str){
        super(str);
    }
}
