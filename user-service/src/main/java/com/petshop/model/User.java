package com.petshop.model;

import java.util.Random;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String login;
	private String password;
	private String keyword;
	private Float balance;

	public User() {
	}


	public void updateKeyWord() {

		Random random = new Random();
		StringBuilder keyword = new StringBuilder();

		for (int i = 0; i < 5; i++) {

			int num = random.nextInt(52);
			char base = (num < 26) ? 'A' : 'a';
			char letter = (char) (base + num % 26);

			keyword.append(letter);
		}

		this.keyword = keyword.toString();
	}


	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

}
