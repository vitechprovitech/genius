package com.faguy.genius.model.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.faguy.genius.entity.Profil;


public interface ProfilRepository extends JpaRepository<Profil, Long>{
	
	@Query("select p from Profil p where p.profil=:profil")
	Profil findByName(@Param("profil") String profil);
}
