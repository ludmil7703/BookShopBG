package org.softuni.bookshopbg.controlller;


import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.CartItem;
import org.softuni.bookshopbg.model.entities.ShoppingCart;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.service.BookService;
import org.softuni.bookshopbg.service.CartItemService;
import org.softuni.bookshopbg.service.ShoppingCartService;
import org.softuni.bookshopbg.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
		UserEntity user = userService.findUserByUsername(principal.getName()).orElse(null);
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
			@ModelAttribute("book") Book book,
			@ModelAttribute("qty") String qty,
			Model model, Principal principal
			) {
		UserEntity user = userService.findUserByUsername(principal.getName()).get();
		book = bookService.findById(book.getId());

		if (Integer.parseInt(qty) > book.getInStockNumber()) {
			model.addAttribute("notEnoughStock", true);
			return "forward:/bookDetail/"+book.getId();
		}

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
