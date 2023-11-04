package org.softuni.bookshopbg.service;

import org.softuni.bookshopbg.model.dto.BookBindingModel;

import java.io.IOException;

public interface ImageDataService {

    void upload(BookBindingModel bookBindingModel) throws IOException;

    void delete(Long id);

    byte[] getImage(String imageName);
}
