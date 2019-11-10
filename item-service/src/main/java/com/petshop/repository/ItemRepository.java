package com.petshop.repository;

import com.petshop.model.Item;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {

	Optional<Item> findById(Integer id);

	List<Item> findByType(String type);

}
