package com.faguy.genius.model.DAO;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.faguy.genius.entity.PaiementsChoisis;


public interface PaiementsChoisisRepository extends JpaRepository<PaiementsChoisis, Long> {
		@Query("select p from PaiementsChoisis p where p.article.id=:idArticle")
		ArrayList<PaiementsChoisis> findPaiementsByArticle(@Param("idArticle") long id);
		@Query("select p from PaiementsChoisis p where p.article.id=:idArticle and p.mode.code=:code")
		ArrayList<PaiementsChoisis> findbyArticleAndMode(@Param("idArticle") long idArticle, @Param("code")String  code );
}
