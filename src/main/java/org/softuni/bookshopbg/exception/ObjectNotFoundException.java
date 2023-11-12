package org.softuni.bookshopbg.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends RuntimeException {
    private final String id;
    public ObjectNotFoundException( String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
