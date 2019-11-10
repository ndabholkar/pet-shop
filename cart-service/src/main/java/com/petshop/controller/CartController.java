package com.petshop.controller;

import com.petshop.model.Cart;
import com.petshop.service.CartService;
import java.util.List;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CartController {

	private final CartService cartService;

	@Autowired
	public CartController(CartService cartService) {
		this.cartService = cartService;
	}


	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public List<Cart> showCart(@RequestHeader(value = "user_id") String value) {

		int userId = Integer.parseInt(value);
		return cartService.getCartItems(userId);
	}

	@RequestMapping(value = "/cart/add/{id}", method = RequestMethod.PUT)
	public String addToCart(@RequestHeader(value = "user_id") String value, @PathVariable int id) {

		int userId = Integer.parseInt(value);
		return cartService.addItem(id, userId);
	}

	@RequestMapping(value = "/cart/delete/{id}", method = RequestMethod.DELETE)
	public String deleteFromCart(@RequestHeader(value = "user_id") String value, @PathVariable int id) throws ServletException {

		int userId = Integer.parseInt(value);
		return cartService.deleteItem(id, userId);
	}

	@RequestMapping(value = "/cart/pay", method = RequestMethod.POST)
	public String payPurchases(@RequestHeader(value = "user_id") String user_id, @RequestHeader(value = "balance") String value,
		@RequestHeader(value = "Authorization") String token) throws ServletException {

		int userId = Integer.parseInt(user_id);
		float balance = Float.parseFloat(value);
		return cartService.pay(userId, balance, token);
	}


}
