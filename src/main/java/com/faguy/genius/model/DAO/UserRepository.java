package com.faguy.genius.model.DAO;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.faguy.genius.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {
	@Query(value = "select u from User u where u.email=:email")
	User findByEmail(@Param("email") String email);
	@Query(value = "select u from User u where u.login=:email")
	User findByLogin(@Param("email") String email);
	@Query(value="select u from User u where u.email=:email and u.password=:password")
	User findbyEmailAndPassword(@Param("email") String email, @Param("password")String password);
	@Query(value="select u from User u where u.login=:login and u.password=:password")
	User findbyLoginAndPassword(@Param("login") String email, @Param("password")String password);
	@Query(value="select u from User u where   u.phone=:phone")
	User findByPhone(@Param("phone") String phone);
	@Query(value="select u from User u where u.email=:email and u.MAC=:mac")
	User findbyEmailAndMAC(@Param("email") String email, @Param("mac")String mac);
	
}
