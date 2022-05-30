package com.faguy.genius.model.DAO;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.faguy.genius.entity.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Integer> {
	@Query("select a from Action a where  a.article!=null")	
	ArrayList<Action> findActionbyArticleNotNull(); 
/*	@Query("select a from Action a where  a.user.email=:email and a.article.id=:id and a.type=:type")	
	Action findActionByEmailAndArticle(@Param("email")String email, @Param("id")long idArticle, @Param("type") String type);*/ 
}
