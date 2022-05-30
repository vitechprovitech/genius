package com.faguy.genius.model.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import com.faguy.genius.entity.Categorie;
import com.faguy.genius.entity.Dossier;

public interface DossierRepository extends  JpaRepository<Dossier, Long>  {
	
}
