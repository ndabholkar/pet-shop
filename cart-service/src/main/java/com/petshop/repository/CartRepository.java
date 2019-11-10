package com.petshop.repository;

import com.petshop.model.Cart;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends CrudRepository<Cart, Integer> {

	Cart save(Cart s);

	List<Cart> findByUserId(Integer id);

	Optional<Cart> findById(Integer id);

}
