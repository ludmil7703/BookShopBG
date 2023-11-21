package org.softuni.bookshopbg.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class UserPayment extends BaseEntity{

	private Long id;
	private String type;
	private String cardName;
	private String cardNumber;
	private int expiryMonth;
	private int expiryYear;
	private int cvc;
	private String holderName;
	private boolean defaultPayment;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private UserEntity user;
	
	@OneToOne(cascade = CascadeType.ALL,mappedBy = "userPayment",orphanRemoval = true)
	private UserBilling userBilling;

}
