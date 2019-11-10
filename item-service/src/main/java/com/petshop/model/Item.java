package com.petshop.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "item")
public class Item {

	@Id
	private Integer id;

	private String type;
	private String name;
	private Float price;


	public Item() {
	}

	public Item(int id, float price, String name, String type) {
		this.id = id;
		this.price = price;
		this.name = name;
		this.type = type;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
