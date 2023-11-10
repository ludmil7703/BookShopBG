package org.softuni.bookshopbg.model.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_order")
public class Order extends BaseEntity{

	private Date orderDate;
	private Date shippingDate;
	private String shippingMethod;
	private String orderStatus;
	private BigDecimal orderTotal;
	
	@OneToMany(mappedBy = "order", cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	private List<CartItem> cartItemList;
	
	@OneToOne(mappedBy = "order", cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	private ShippingAddress shippingAddress;
	
	@OneToOne(mappedBy = "order", cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	private BillingAddress billingAddress;
	
	@OneToOne(mappedBy = "order", cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	private Payment payment;
	
	@ManyToOne
	private UserEntity user;

	
}
