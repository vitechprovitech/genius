package com.faguy.genius.model.DAO;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.faguy.genius.entity.Categorie;


public interface CategorieRepository extends JpaRepository<Categorie, Long> {
	@Query("select c from Categorie c where c.categorie=:name")
	Categorie findbyName(@Param("name")String name);
	@Query("select c from Categorie c where c.code=:code")
	Categorie findByCode(@Param("code")String code);
	@Query("select c from Categorie c where c.id=:id")
	Categorie findById(@Param("id")long id);
	@Query("select c from Categorie c where c.parent is not null and c.parent.id=:id")
	ArrayList<Categorie> findSubCategories(@Param("id") long id);
	@Query("select c from Categorie c where  c.parent is null")
	ArrayList<Categorie> findRootCategories();
}
