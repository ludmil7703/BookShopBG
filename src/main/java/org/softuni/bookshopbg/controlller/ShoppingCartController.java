package org.softuni.bookshopbg.controlller;


import org.modelmapper.ModelMapper;
import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.CartItem;
import org.softuni.bookshopbg.model.entities.ShoppingCart;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.service.BookService;
import org.softuni.bookshopbg.service.CartItemService;
import org.softuni.bookshopbg.exception.ObjectNotFoundException;
import org.softuni.bookshopbg.service.ShoppingCartService;
import org.softuni.bookshopbg.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
	
	private final UserService userService;

	private final CartItemService cartItemService;

	private final BookService bookService;

	private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(UserService userService,
								  CartItemService cartItemService,
								  BookService bookService, ShoppingCartService shoppingCartService) {
		this.userService = userService;
		this.cartItemService = cartItemService;
		this.bookService = bookService;
		this.shoppingCartService = shoppingCartService;
	}



	@RequestMapping("/cart")
	public String shoppingCart(Model model, Principal principal) {
		UserEntity user = userService.findUserByUsername(principal.getName());
        assert user != null;
        ShoppingCart shoppingCart = user.getShoppingCart();
		shoppingCart.setUser(user);

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		if (cartItemList.isEmpty()){
			return "forward:/";
		}

		shoppingCartService.updateShoppingCart(shoppingCart);

		model.addAttribute("user", user);

		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);

		return "shoppingCart";
	}



	@PostMapping("/addItem")
	public String addItem(
			@ModelAttribute("book") BookBindingModel bookBindingModel,
			@ModelAttribute("qty") String qty,
			Model model, Principal principal
			) {
		UserEntity user = userService.findUserByUsername(principal.getName());
		Book bookInStock = bookService.findBookById(bookBindingModel.getId());
		if (Integer.parseInt(qty) > bookInStock.getInStockNumber()) {
			model.addAttribute("notEnoughStock", true);
			return "forward:/bookDetail/"+bookInStock.getId();
		}

		ModelMapper modelMapper = new ModelMapper();
		BookBindingModel book = modelMapper.map(bookInStock, BookBindingModel.class);

		cartItemService.addBookToCartItem(book, user, Integer.parseInt(qty));

		model.addAttribute("addBookSuccess", true);
		return "forward:/bookDetail/"+book.getId();
	}

	@RequestMapping("/updateCartItem")
	public String updateShoppingCart(
			@ModelAttribute("id") Long cartItemId,
			@ModelAttribute("qty") int qty
			) {
		CartItem cartItem = cartItemService.findById(cartItemId);
		cartItem.setQty(qty);
		cartItemService.updateCartItem(cartItem);

		return "forward:/shoppingCart/cart";
	}

	@RequestMapping("/removeItem")
	public String removeItem(@ModelAttribute("id") Long id) {
		cartItemService.deleteCartItemById(id);

		return "forward:/shoppingCart/cart";
	}

}
