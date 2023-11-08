package org.softuni.bookshopbg.controlller;


import org.softuni.bookshopbg.model.dto.BookBindingModel;
import org.softuni.bookshopbg.model.entities.Book;
import org.softuni.bookshopbg.model.entities.CartItem;
import org.softuni.bookshopbg.model.entities.ShoppingCart;
import org.softuni.bookshopbg.model.entities.UserEntity;
import org.softuni.bookshopbg.repositories.BookRepository;
import org.softuni.bookshopbg.service.BookService;
import org.softuni.bookshopbg.service.CartItemService;
import org.softuni.bookshopbg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private BookService bookService;

	@Autowired
	private BookRepository bookRepository;

    public ShoppingCartController() {
    }

//	@Autowired
//	private ShoppingCartService shoppingCartService;

//	@RequestMapping("/cart")
//	public String shoppingCart(Model model, Principal principal) {
////		UserEntity user = userService.findByUsername(principal.getName());
////		ShoppingCart shoppingCart = user.getShoppingCart();
////
////		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
////
////		shoppingCartService.updateShoppingCart(shoppingCart);
////
////		model.addAttribute("cartItemList", cartItemList);
////		model.addAttribute("shoppingCart", shoppingCart);
//
//		return "shoppingCart";
//	}
//
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
//
////	@RequestMapping("/updateCartItem")
////	public String updateShoppingCart(
////			@ModelAttribute("id") Long cartItemId,
////			@ModelAttribute("qty") int qty
////			) {
////		CartItem cartItem = cartItemService.findById(cartItemId);
////		cartItem.setQty(qty);
////		cartItemService.updateCartItem(cartItem);
////
////		return "forward:/shoppingCart/cart";
////	}
//
//	@RequestMapping("/removeItem")
//	public String removeItem(@RequestParam("id") Long id) {
//		cartItemService.removeCartItem(cartItemService.findById(id));
//
//		return "forward:/shoppingCart/cart";
//	}
//
}
