package com.petshop.repository;

import com.petshop.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	User save(User s);

	User findByLogin(String login);

}
