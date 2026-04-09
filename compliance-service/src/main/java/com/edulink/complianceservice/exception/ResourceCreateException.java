package com.edulink.complianceservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceCreateException extends RuntimeException{
    public ResourceCreateException(String message){
        super(message);
    }
}
