package com.petshop.service;

import com.petshop.model.Cart;
import com.petshop.model.Item;
import com.petshop.repository.CartRepository;
import java.util.List;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service
public class CartService {

	private final CartRepository cartRepository;

	@Autowired
	public CartService(CartRepository cartRepository) {
		this.cartRepository = cartRepository;
	}


	public List<Cart> getCartItems(int id) {

		return cartRepository.findByUserId(id);
	}


	public String addItem(int itemId, int userId) {

		Cart cartEntity = new Cart();
		cartEntity.setUserId(userId);
		cartEntity.setItemId(itemId);

		cartRepository.save(cartEntity);
		return "Added.";
	}


	public String deleteItem(int id, int userId) throws ServletException {

		Cart cart = cartRepository.findById(id).orElse(null);
		if (cart == null) {
			throw new ServletException("Item not found in cart.");
		}

		if (cart.getUserId() == userId) {
			cartRepository.deleteById(id);
		} else {
			throw new ServletException("Item not found in cart.");
		}

		return "Deleted.";
	}

	@Transactional
	public String pay(int userId, float balance, String token) throws ServletException {

		List<Cart> cart = cartRepository.findByUserId(userId);

		// Verification
		Float sum = 0f;
		for (Cart cartEntity : cart) {

			int itemId = cartEntity.getItemId();

			RestTemplate restTemplate = new RestTemplate();
			Item item = restTemplate.getForObject("http://localhost:8081/item/" + itemId, Item.class);

			if (item == null) {
				throw new ServletException("Item " + itemId + ": not found.");
			} else {
				sum += item.getPrice();
			}
		}

		// Purchase
		if (sum <= balance) {

			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", token);
			headers.add("purchase", sum.toString());
			HttpEntity<String> httpEntity = new HttpEntity<>("parameters", headers);

			restTemplate.exchange("http://localhost:8090/balance/update", HttpMethod.POST, httpEntity, String.class);
			cartRepository.deleteAll(cart);
		}

		return "Purchase Сompleted.";
	}


}