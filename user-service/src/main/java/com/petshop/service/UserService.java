package com.petshop.service;

import com.petshop.model.Login;
import com.petshop.model.User;
import com.petshop.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.util.Date;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

	private UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	public String registration(Login data) throws ServletException {

		if (userRepository.findByLogin(data.getLogin()) != null) {
			throw new ServletException("This user exists");
		}

		User user = new User();
		user.setLogin(data.getLogin());
		user.setPassword(data.getPassword());
		user.setKeyword("null");
		user.setBalance(5000.0f);

		userRepository.save(user);

		return "OK!";

	}


	public String login(Login login) throws ServletException {

		if (login.getLogin() == null || login.getPassword() == null) {
			throw new ServletException("Please fill in username and password");
		}

		// Data check
		User user = userRepository.findByLogin(login.getLogin());

		if (user == null) {
			throw new ServletException("User not found. Check your login or sign up");
		}

		if (!user.getPassword().equals(login.getPassword())) {
			throw new ServletException("Invalid login. Please check your login and password.");
		}

		return tokenBuilding(user);
	}


	public String refreshToken(String refreshToken) throws ServletException {

		User user = tokenCheck(refreshToken, "refreshkey");
		return tokenBuilding(user);
	}


	public float getBalance(String token) throws ServletException {

		User user = tokenCheck(token, "secretkey");
		return user.getBalance();
	}


	public User tokenCheck(String bearerToken, String key) throws ServletException {

		if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
			throw new ServletException("Missing or invalid Authorization header");
		}

		String token = bearerToken.substring(7);

		try {

			// Token validating
			Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
			String username = claims.getSubject();
			String keyword = (String) claims.get("keyword");

			User user = userRepository.findByLogin(username);

			if (user == null) {
				throw new ServletException("Invalid token");
			}

			if (!user.getKeyword().equals(keyword)) {
				throw new ServletException("Invalid token");
			}

			return user;

		} catch (SignatureException e) {

			throw new ServletException("Invalid token");

		} catch (ExpiredJwtException e) {

			throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "JWToken expired. Please refresh your token");
		}

	}


	public void balanceUpdate(String token, float purchase) throws ServletException {

		User user = tokenCheck(token, "secretkey");

		float balance = user.getBalance() - purchase;
		user.setBalance(balance);

		userRepository.save(user);
	}


	private String tokenBuilding(User user) {

		final long accessTokenExp = 1800000; // 30 min

		// Time expiration formatting
		long nowMillis = System.currentTimeMillis();
		long expMillis = nowMillis + accessTokenExp;
		Date expTime = new Date(expMillis);

		// User interaction
		user.updateKeyWord();
		String username = user.getLogin();
		String keyword = user.getKeyword();
		userRepository.save(user);

		String jwtToken = Jwts.builder().setSubject(username)
			.claim("keyword", keyword)
			.setExpiration(expTime)
			.signWith(SignatureAlgorithm.HS256, "secretkey").compact();

		String jwtRefreshToken = Jwts.builder().setSubject(username)
			.claim("keyword", keyword)
			.signWith(SignatureAlgorithm.HS256, "refreshkey").compact();

		return "token: " + jwtToken + "\nrefreshToken: " + jwtRefreshToken;
	}


}
