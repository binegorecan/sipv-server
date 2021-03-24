package com.feri.sipv.sipvserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class RejectedException extends Exception{
    public RejectedException(String message) {
        super(message);
    }
}
