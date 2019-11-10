package com.petshop.controller;

import com.petshop.model.Login;
import com.petshop.model.User;
import com.petshop.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}


	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@RequestBody Login data) throws ServletException {

		return userService.registration(data);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestBody Login data) throws ServletException {

		return userService.login(data);
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.PUT)
	public String refresh(@RequestHeader(value = "Authorization") String token) throws ServletException {

		return userService.refreshToken(token);
	}

	@RequestMapping(value = "/balance", method = RequestMethod.GET)
	public float showBalance(@RequestHeader(value = "Authorization") String token) throws ServletException {

		return userService.getBalance(token);
	}


	@RequestMapping(value = "/check", method = RequestMethod.POST)
	public Map<String, String> tokenCheck(@RequestBody Map<String, String> request) {

		String token = request.get("token");
		String keyword = request.get("keyword");
		Map<String, String> map = new HashMap<>();

		try {
			User user = userService.tokenCheck(token, keyword);

			map.put("response", "200");
			map.put("user_id", user.getId().toString());
			map.put("balance", user.getBalance().toString());

		} catch (ServletException e) {
			map.put("response", "401");

		} catch (ExpiredJwtException e) {
			map.put("response", "510");
		}

		return map;
	}

	@RequestMapping(value = "/balance/update", method = RequestMethod.POST)
	public void update(@RequestHeader(value = "Authorization") String token,
		@RequestHeader(value = "purchase") String value) throws ServletException {

		float purchase = Float.parseFloat(value);
		userService.balanceUpdate(token, purchase);
	}


}
