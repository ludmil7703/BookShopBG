package org.softuni.bookshopbg.controlller;


import org.softuni.bookshopbg.model.entities.*;
import org.softuni.bookshopbg.service.*;
import org.softuni.bookshopbg.utils.BGConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


@Controller
public class CheckoutController {

	private ShippingAddress shippingAddress = new ShippingAddress();
	private BillingAddress billingAddress = new BillingAddress();
	private Payment payment = new Payment();

	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private ShippingAddressService shippingAddressService;

	@Autowired
	private BillingAddressService billingAddressService;

	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private UserShippingService userShippingService;

	@Autowired
	private UserPaymentService userPaymentService;
	
	@Autowired
	private OrderService orderService;

	@RequestMapping("/checkout")
	public String checkout(
			@RequestParam("id") Long cartId,
			@RequestParam(value="missingRequiredField", required=false) boolean missingRequiredField,
			Model model, Principal principal
			){
		UserEntity user = userService.findUserByUsername(principal.getName()).orElse(null);

        assert user != null;
        if(cartId != user.getShoppingCart().getId()) {
			return "badRequestPage";
		}
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
		
		if(cartItemList.isEmpty()) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppintCart/cart";
		}
		
		for (CartItem cartItem : cartItemList) {
			if(cartItem.getBook().getInStockNumber() < cartItem.getQty()) {
				model.addAttribute("notEnoughStock", true);
				return "forward:/shoppingCart/cart";
			}
		}

		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();
		
		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);
		
		if (userPaymentList.isEmpty()) {
			model.addAttribute("emptyPaymentList", true);
		} else {
			model.addAttribute("emptyPaymentList", false);
		}
		
		if (userShippingList.isEmpty()) {
			model.addAttribute("emptyShippingList", true);
		} else {
			model.addAttribute("emptyShippingList", false);
		}
		
		ShoppingCart shoppingCart = user.getShoppingCart();
		
		for(UserShipping userShipping : userShippingList) {
			if(userShipping.isUserShippingDefault()) {
				shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			}
		}
		
		for (UserPayment userPayment : userPaymentList) {
			if(userPayment.isDefaultPayment()) {
				paymentService.setByUserPayment(userPayment, payment);
				billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
			}
		}
		
		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("payment", payment);
		model.addAttribute("billingAddress", billingAddress);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", user.getShoppingCart());
		
		List<String> stateList = BGConstants.listOfBGStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		
		model.addAttribute("classActiveShipping", true);
		
		if(missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}
		
		return "checkout";
		
	}
	
	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkoutPost(@ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
			@ModelAttribute("billingAddress") BillingAddress billingAddress, @ModelAttribute("payment") Payment payment,
			@ModelAttribute("billingSameAsShipping") String billingSameAsShipping,
			@ModelAttribute("shippingMethod") String shippingMethod, Principal principal, Model model) {
		ShoppingCart shoppingCart = userService.findUserByUsername(principal.getName()).get().getShoppingCart();

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		model.addAttribute("cartItemList", cartItemList);

		if (billingSameAsShipping.equals("true")) {
			billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName());
			billingAddress.setBillingAddressStreet1(shippingAddress.getShippingAddressStreet1());
			billingAddress.setBillingAddressStreet2(shippingAddress.getShippingAddressStreet2());
			billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity());
			billingAddress.setBillingAddressState(shippingAddress.getShippingAddressState());
			billingAddress.setBillingAddressCountry(shippingAddress.getShippingAddressCountry());
			billingAddress.setBillingAddressZipcode(shippingAddress.getShippingAddressZipcode());
		}

		if (shippingAddress.getShippingAddressStreet1().isEmpty() || shippingAddress.getShippingAddressCity().isEmpty()
				|| shippingAddress.getShippingAddressState().isEmpty()
				|| shippingAddress.getShippingAddressName().isEmpty()
				|| shippingAddress.getShippingAddressZipcode().isEmpty() || payment.getCardNumber().isEmpty()
				|| payment.getCvc() == 0 || billingAddress.getBillingAddressStreet1().isEmpty()
				|| billingAddress.getBillingAddressCity().isEmpty() || billingAddress.getBillingAddressState().isEmpty()
				|| billingAddress.getBillingAddressName().isEmpty()
				|| billingAddress.getBillingAddressZipcode().isEmpty())
			return "redirect:/checkout?id=" + shoppingCart.getId() + "&missingRequiredField=true";
		
		UserEntity user = userService.findUserByUsername(principal.getName()).orElse(null);
		
		Order order = orderService.createOrder(shoppingCart, shippingAddress, billingAddress, payment, shippingMethod, user);

		
		shoppingCartService.clearShoppingCart(shoppingCart, principal);
		
		LocalDate today = LocalDate.now();
		LocalDate estimatedDeliveryDate;
		
		if (shippingMethod.equals("groundShipping")) {
			estimatedDeliveryDate = today.plusDays(5);
		} else {
			estimatedDeliveryDate = today.plusDays(3);
		}
		
		model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);
		
		return "orderSubmittedPage";
	}
	
	@RequestMapping("/setShippingAddress")
	public String setShippingAddress(
			@RequestParam("userShippingId") Long userShippingId,
			Principal principal, Model model
			) {
		UserEntity user = userService.findUserByUsername(principal.getName()).orElse(null);
		UserShipping userShipping = userShippingService.findById(userShippingId).get();

        assert user != null;
        if(!Objects.equals(userShipping.getUser().getId(), user.getId())) {
			return "badRequestPage";
		} else {
			shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			
			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
		
			
			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());
			
			List<String> stateList = BGConstants.listOfBGStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			
			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();
			
			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);
			
			model.addAttribute("shippingAddress", shippingAddress);
			
			model.addAttribute("classActiveShipping", true);
			
			if (userPaymentList.size() == 0) {
				model.addAttribute("emptyPaymentList", true);
			} else {
				model.addAttribute("emptyPaymentList", false);
			}
			
			
			model.addAttribute("emptyShippingList", false);
			
			
			return "checkout";
		}
	}
	
	@RequestMapping("/setPaymentMethod")
	public String setPaymentMethod(
			@RequestParam("userPaymentId") Long userPaymentId,
			Principal principal, Model model
			) {
		UserEntity user = userService.findUserByUsername(principal.getName()).orElse(null);
		UserPayment userPayment = userPaymentService.findById(userPaymentId);
		UserBilling userBilling = userPayment.getUserBilling();

        assert user != null;
        if(!Objects.equals(userPayment.getUser().getId(), user.getId())){
			return "badRequestPage";
		} else {
			paymentService.setByUserPayment(userPayment, payment);
			
			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
			
			billingAddressService.setByUserBilling(userBilling, billingAddress);
			
			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("billingAddress", billingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());
			
			List<String> stateList = BGConstants.listOfBGStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			
			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();
			
			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);
			
			model.addAttribute("shippingAddress", shippingAddress);
			
			model.addAttribute("classActivePayment", true);
			
			
			model.addAttribute("emptyPaymentList", false);
			
			
			if (userShippingList.size() == 0) {
				model.addAttribute("emptyShippingList", true);
			} else {
				model.addAttribute("emptyShippingList", false);
			}
			
			return "checkout";
		}
	}
}
