package org.softuni.bookshopbg.controlller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.softuni.bookshopbg.model.entities.*;
import org.softuni.bookshopbg.service.*;
import org.softuni.bookshopbg.utils.BGConstants;
import org.softuni.bookshopbg.utils.MailConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class CheckoutController {

	private final ShippingAddress shippingAddress = new ShippingAddress();
	private final BillingAddress billingAddress = new BillingAddress();
	private final Payment payment = new Payment();

	private final CategoryService categoryService;

	private final JavaMailSender mailSender;

	private final MailConstructor mailConstructor;

	private final UserService userService;

	private final CartItemService cartItemService;

	private final ShoppingCartService shoppingCartService;

	private final ShippingAddressService shippingAddressService;

	private final BillingAddressService billingAddressService;

	private final PaymentService paymentService;

	private final UserShippingService userShippingService;

	private final UserPaymentService userPaymentService;

	private final OrderService orderService;

	public CheckoutController(CategoryService categoryService,
							  JavaMailSender mailSender,
							  MailConstructor mailConstructor,
							  UserService userService,
							  CartItemService cartItemService,
							  ShoppingCartService shoppingCartService,
							  ShippingAddressService shippingAddressService,
							  BillingAddressService billingAddressService,
							  PaymentService paymentService,
							  UserShippingService userShippingService,
							  UserPaymentService userPaymentService,
							  OrderService orderService) {
		this.categoryService = categoryService;
		this.mailSender = mailSender;
		this.mailConstructor = mailConstructor;
		this.userService = userService;
		this.cartItemService = cartItemService;
		this.shoppingCartService = shoppingCartService;
		this.shippingAddressService = shippingAddressService;
		this.billingAddressService = billingAddressService;
		this.paymentService = paymentService;
		this.userShippingService = userShippingService;
		this.userPaymentService = userPaymentService;
		this.orderService = orderService;
	}


	@RequestMapping("/checkout")
	public String checkout(
			@RequestParam("id") Long cartId,
			@RequestParam(value="missingRequiredField", required=false) boolean missingRequiredField,
			Model model, Principal principal
	){
		UserEntity user = userService.findUserByUsername(principal.getName());

		List<Category> categoryList = categoryService.getAllCategories();
		model.addAttribute("categoryList", categoryList);

		if(cartId != user.getShoppingCart().getId()) {
			return "error";
		}

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());

		if(cartItemList.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppingCart/cart";
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
		return "checkoutPage";
	}
	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkoutPost(@ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
							   @ModelAttribute("billingAddress") BillingAddress billingAddress, @ModelAttribute("payment") Payment payment,
							   @ModelAttribute("billingSameAsShipping") String billingSameAsShipping,
							   @ModelAttribute("shippingMethod") String shippingMethod, Principal principal, Model model) {
		UserEntity user = userService.findUserByUsername(principal.getName());

		ShoppingCart shoppingCart = user.getShoppingCart();

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		model.addAttribute("cartItemList", cartItemList);

		List<Category> categoryList = categoryService.getAllCategories();
		model.addAttribute("categoryList", categoryList);

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

		Order order = orderService.createOrder(shoppingCart, shippingAddress, billingAddress, payment, shippingMethod, user);

		mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, Locale.ENGLISH));

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
		UserEntity user = userService.findUserByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId).get();

		List<Category> categoryList = categoryService.getAllCategories();
		model.addAttribute("categoryList", categoryList);

		if(userShipping.getUser().getId() != user.getId()) {
			return "error";
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


			return "checkoutPage";
		}
	}

	@RequestMapping("/setPaymentMethod")
	public String setPaymentMethod(
			@RequestParam("userPaymentId") Long userPaymentId,
			Principal principal, Model model
	) {
		UserEntity user = userService.findUserByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(userPaymentId);
		UserBilling userBilling = userPayment.getUserBilling();

		List<Category> categoryList = categoryService.getAllCategories();
		model.addAttribute("categoryList", categoryList);

		if(userPayment.getUser().getId() != user.getId()){
			return "error";
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

			return "checkoutPage";
		}
	}
}