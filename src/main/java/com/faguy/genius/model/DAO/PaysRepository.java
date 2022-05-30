package com.faguy.genius.model.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.faguy.genius.entity.Pays;


public interface PaysRepository extends JpaRepository<Pays, Long> {
	@Query(value = "select p from Pays p where p.nom=:nom")
	Pays findByName(@Param("nom") String nom);
	
	
}
