package org.softuni.bookshopbg.service;


import org.softuni.bookshopbg.model.entities.BillingAddress;
import org.softuni.bookshopbg.model.entities.UserBilling;

public interface BillingAddressService {
	BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);
}
