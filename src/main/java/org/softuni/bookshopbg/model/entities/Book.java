package org.softuni.bookshopbg.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "books")
public class Book extends BaseEntity {

	@Column(length = 50)
	@NotBlank
	private String title;

	@ManyToOne
	@NotNull
	private Author author;

	@Column(length = 50)
	private String publisher;

	@Column(name = "release_date")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@PastOrPresent
	@NotNull
	private Date releaseDate;

	@Column(length = 50)
	private String language;

	@ManyToOne
	@NotNull
	private Category category;

	@Column(name = "number_of_pages")
	@NotNull
	private int numberOfPages;


	private String format;

	@Column(nullable = false, unique = true)
	@NotNull
	private int isbn;

	@Column(name = "shipping_weight")
	@NotNull
	private double shippingWeight;

	@Column(name = "list_price")
	@NotNull
	private BigDecimal listPrice;

	@Column(name = "our_price")
	@NotNull
	private BigDecimal ourPrice;

	private boolean active;

	private int inStockNumber;

	@Column
	@NotBlank
	private String description;

	@Column(name = "image_url")
	private String imageUrl;

	@ManyToMany
	private List<Order> orders;


	@OneToMany(mappedBy = "book")
	@JsonIgnore
	private List<BookToCartItem> bookToCartItemList;

	public Book(){
	}




}