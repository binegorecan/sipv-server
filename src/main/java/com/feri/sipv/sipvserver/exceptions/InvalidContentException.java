package com.feri.sipv.sipvserver.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class InvalidContentException extends Exception {
    public InvalidContentException(String message) {
        super(message);
    }
}
