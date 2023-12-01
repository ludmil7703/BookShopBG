package org.softuni.bookshopbg.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends RuntimeException {
    private final String id;
    public ObjectNotFoundException( String id) {
        this.id = id;
    }

}
