package org.softuni.bookshopbg.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.softuni.bookshopbg.utils.ImageUtil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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

	@Column(nullable = false, length = 50)
	private String title;

	@ManyToOne
	private Author author;

	private String publisher;

	@Column(name = "release_date")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date publicationDate;

	private String language;

	@ManyToOne
	private Category category;

	private int numberOfPages;

	private String format;

	private int isbn;

	private double shippingWeight;

	private BigDecimal listPrice;

	private BigDecimal ourPrice;

	private boolean active=true;

	private int inStockNumber;

	@Column(length = 100)
	private String description;




	@ManyToMany
	private List<Order> orders;

	@OneToOne
	private ImageData bookImage;



	@OneToMany(mappedBy = "book")
	@JsonIgnore
	private List<BookToCartItem> bookToCartItemList;

	public Book(){
		setActive(this.inStockNumber);
	}
	public void setActive(int inStockNumber) {

		if(inStockNumber <= 0) {
			active = false;
		}
		this.active = active;
	}

	public void readImage() throws IOException {

		ByteArrayInputStream bis = new ByteArrayInputStream(bookImage.getImageData());
		BufferedImage bImage2 = ImageIO.read(bis);
		ImageIO.write(bImage2, "jpg", new File("output.jpg") );

	}

}