package com.faguy.genius.model.DAO;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.faguy.genius.entity.Token;

import antlr.collections.List;


public interface TokenRepository extends JpaRepository<Token, Long> {
	@Query("select t from Token t where  t.user.phone=:login or t.user.email=:login and t.token=:token")
	Token findByLoginOrPhone(@Param("login") String login, int token);
	@Query("select t from Token t where  t.user.email=:login and t.token=:token")
	Token findByLogin(@Param("login") String login, int token);
	@Query("select t from Token t where  t.user.phone=:login or t.user.email=:login")
	ArrayList<Token> findTokensByloginOrPhone(@Param("login") String login);
}
