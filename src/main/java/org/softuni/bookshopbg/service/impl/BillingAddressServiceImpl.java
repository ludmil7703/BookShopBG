package org.softuni.bookshopbg.service.impl;

import org.softuni.bookshopbg.model.entities.BillingAddress;
import org.softuni.bookshopbg.model.entities.UserBilling;
import org.softuni.bookshopbg.service.BillingAddressService;
import org.springframework.stereotype.Service;

@Service
public class BillingAddressServiceImpl implements BillingAddressService {

	public BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress) {

		billingAddress.setBillingAddressName(userBilling.getUserBillingName());
		billingAddress.setBillingAddressStreet1(userBilling.getUserBillingStreet1());
		billingAddress.setBillingAddressStreet2(userBilling.getUserBillingStreet2());
		billingAddress.setBillingAddressCity(userBilling.getUserBillingCity());
		billingAddress.setBillingAddressState(userBilling.getUserBillingState());
		billingAddress.setBillingAddressCountry(userBilling.getUserBillingCountry());
		billingAddress.setBillingAddressZipcode(userBilling.getUserBillingZipcode());

		return billingAddress;
	}

}
