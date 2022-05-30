package com.faguy.genius.model.DAO;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.faguy.genius.entity.Ville;


public interface VilleRepository extends JpaRepository<Ville, Long>{
	
	@Query(value = "select v from Ville v where v.nom=:nom")
	Ville findByName(@Param("nom") String nom);
	@Query(value="select v from Ville v where v.country.id=:id")
	ArrayList<Ville> findByIdPays(@Param("id") long id); 
	
}
