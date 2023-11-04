package org.softuni.bookshopbg.service.impl;

import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.ImageData;
import org.softuni.bookshopbg.repositories.ImageDataRepository;
import org.softuni.bookshopbg.service.ImageDataService;
import org.softuni.bookshopbg.utils.ImageUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageDataServiceImpl implements ImageDataService {

    private ImageDataRepository imageDataRepository;

    public ImageDataServiceImpl(ImageDataRepository imageDataRepository) {
        this.imageDataRepository = imageDataRepository;
    }
    @Override
    public void upload(BookBindingModel bookBindingModel) throws IOException {
        imageDataRepository.save(ImageData.builder()
                .name(bookBindingModel.getBookImage().getName())
                .type(bookBindingModel.getBookImage().getContentType())
                        .imageData(ImageUtil.compressImage(bookBindingModel.getBookImage().getBytes()))
                .build());

    }

    @Override
    public void delete(Long id) {

        Optional<ImageData> imageData = imageDataRepository.findById(id);
        imageData.ifPresent(data -> imageDataRepository.delete(data));
    }

    @Override
    public byte[] getImage(String imageName) {
        Optional<ImageData> imageData = imageDataRepository.findByName(imageName);
        return imageData.map(data -> ImageUtil.decompressImage(data.getImageData())).orElseGet(() -> new byte[0]);
    }
}
