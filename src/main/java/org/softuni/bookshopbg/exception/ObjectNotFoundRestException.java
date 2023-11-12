package org.softuni.bookshopbg.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ObjectNotFoundRestException extends RuntimeException{
    private final String id;
    public ObjectNotFoundRestException( String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
