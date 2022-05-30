package com.faguy.genius.model.DAO;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.faguy.genius.entity.Achat;
import com.faguy.genius.entity.AchatCategorie;
import com.faguy.genius.entity.Categorie;

public interface AchatCategorieRepository extends JpaRepository<AchatCategorie, Integer> {
	@Query("select p from AchatCategorie p where p.client.email=:email and p.bundle.id=:idBundle and p.productKey=:code")
	public AchatCategorie findAchatbyUserAndProduct(@Param("email")String email, @Param("idBundle")long idProduct, @Param("code")int code);
	@Query("select p from AchatCategorie p where p.client.email=:email and p.bundle.id=:id")
	public AchatCategorie findAchatByUserAndProduct(@Param("email")String email, @Param("id")int idProduct);

}
