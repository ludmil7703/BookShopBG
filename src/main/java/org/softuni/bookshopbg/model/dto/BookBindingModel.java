package org.softuni.bookshopbg.model.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;


@Getter
@Setter
public class BookBindingModel {
    private Long id;

    @NotBlank(message = "Title cannot be null")
    private String title;

    @NotBlank(message = "Author cannot be null")
    private String author;

    private String publisher;

    @NotNull(message = "Publication date cannot be null")
    @PastOrPresent(message = "Publication date cannot be in the future")
    private String publicationDate;

    @NotNull(message = "Language cannot be null")
    private String language;

    @NotNull(message = "Category cannot be null")
    private CategoryName category;

    @NotNull(message = "Number of pages cannot be null")
    @Positive(message = "Number of pages must be positive")
    private int numberOfPages;

    @NotNull(message = "Format cannot be null")
    private String format;

    @NotNull(message = "ISBN cannot be null")
    @Positive(message = "ISBN must be positive")
    @UniqueElements(message = "ISBN must be unique")
    private int isbn;

    @NotNull(message = "Shipping weight cannot be null")
    @Positive(message = "Shipping weight must be positive")
    private double shippingWeight;

    @NotNull(message = "List price cannot be null")
    @Positive(message = "List price must be positive")
    private BigDecimal listPrice;

    @NotNull(message = "Our price cannot be null")
    @Positive(message = "Our price must be positive")
    private BigDecimal ourPrice;

    @NotNull(message = "Description cannot be null")
    @Size(min = 10, message = "Description must be at least 10 characters long")
    private String description;

    @NotNull(message = "In stock number cannot be null")
    @Positive(message = "In stock number must be positive")
    private int inStockNumber;

    private boolean isActive;


    private MultipartFile bookImage;


    public BookBindingModel() {

    }

}
