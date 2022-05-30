package com.faguy.genius.model.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.faguy.genius.entity.ModePaiement;


public interface ModePaiementtRepository extends JpaRepository<ModePaiement, Long> {

	@Query("select m from ModePaiement m where m.code=:code")
	ModePaiement findByCode(@Param("code")String code);
}
