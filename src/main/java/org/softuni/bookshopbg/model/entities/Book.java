package org.softuni.bookshopbg.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "books")
public class Book extends BaseEntity {


	@NotBlank(message = "Title cannot be null")
	private String title;

	@ManyToOne
	@NotNull(message = "Author cannot be null")
	private Author author;

	@Column(length = 50)
	private String publisher;

	@Column(name = "release_date")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@PastOrPresent(message = "Release date cannot be in the future")
	@NotNull(message = "Release date cannot be null")
	private Date releaseDate;


	private String language;

	@ManyToOne
	@NotNull(message = "Category cannot be null")
	private Category category;

	@Column(name = "number_of_pages")
	@NotNull(message = "Number of pages cannot be null")
	@Positive(message = "Number of pages must be positive")
	private int numberOfPages;


	private String format;

	@Column(nullable = false)
	@NotNull(message = "ISBN cannot be null")
	@Positive(message = "ISBN must be positive")
	private int isbn;

	@Column(name = "shipping_weight")
	@NotNull(message = "Shipping weight cannot be null")
	@Positive(message = "Shipping weight must be positive")
	private double shippingWeight;

	@Column(name = "list_price")
	@NotNull
	private BigDecimal listPrice;

	@Column(name = "our_price")
	@NotNull(message = "Our price cannot be null")
	@Positive(message = "Our price must be positive")
	private BigDecimal ourPrice;

	private boolean active;

	@Column(name = "in_stock_number")
	@Positive(message = "In stock number must be positive")
	private int inStockNumber;

	@Column
	@NotBlank(message = "Description cannot be null")
	private String description;

	@Column(name = "image_url")
	private String imageUrl;

	@ManyToMany
	private List<Order> orders;


	@OneToMany
	@JsonIgnore
	private List<BookToCartItem> bookToCartItemList;


}