package org.softuni.bookshopbg.service;

import org.softuni.bookshopbg.model.entities.ShippingAddress;
import org.softuni.bookshopbg.model.entities.UserShipping;
public interface ShippingAddressService {
	ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);
}
