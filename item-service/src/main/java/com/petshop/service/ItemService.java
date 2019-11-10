package com.petshop.service;

import com.petshop.model.Item;
import com.petshop.repository.ItemRepository;
import java.util.List;
import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ItemService {

	private final ItemRepository itemRepository;

	@Autowired
	public ItemService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}


	public Item getItemById(int id) throws ServletException {

		Item item = itemRepository.findById(id).orElse(null);

		if (item == null) {
			throw new ServletException("Page Not Found!");
		}

		return item;
	}

	public List<Item> getAllProducts() {

		return (List<Item>) itemRepository.findAll();
	}

	public List<Item> getAllPets() {

		return itemRepository.findByType("pet");
	}

	public List<Item> getAllStuff() {

		return itemRepository.findByType("stuff");
	}


}
