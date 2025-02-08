package com.nofear.nac.exception;

//Class nay chay run time de xem flow chay
public class JwtValidationException extends RuntimeException{
    public JwtValidationException(String message) {
        super(message);
    }
}
