package com.faguy.genius.model.DAO;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.faguy.genius.entity.Achat;
import com.faguy.genius.entity.Assignation;
import com.faguy.genius.entity.Categorie;



public interface AchatRepository extends JpaRepository<Achat, Integer> {
	@Query("select a from Achat a where a.client.email=:email")
	ArrayList<Achat> findSubscribeds(@Param("email")String email);
	
	/*@Query("select Categorie c  from AchatCategorie a")
	ArrayList<Categorie> findSubscribedPacks();*/
}
