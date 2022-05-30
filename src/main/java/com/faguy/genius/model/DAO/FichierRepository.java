package com.faguy.genius.model.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.faguy.genius.entity.Fichier;


public interface FichierRepository extends JpaRepository<Fichier, Long> {
	
	@Query("select f from Fichier f where f.id=:id")
	Fichier findFichierByid(@Param("id")long id);
	@Query("select f from Fichier f where f.article.id=:id")
	Fichier findFichierByArticle(@Param("id")long id);
}
