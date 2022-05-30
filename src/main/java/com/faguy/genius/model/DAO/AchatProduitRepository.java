package com.faguy.genius.model.DAO;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.faguy.genius.entity.Achat;
import com.faguy.genius.entity.AchatProduit;
import com.faguy.genius.entity.Assignation;

public interface AchatProduitRepository extends JpaRepository<AchatProduit, Integer> {
	@Query("select a from AchatProduit a where a.client.email=:email and a.produit.id=:id and a.productKey=:code" )
	public AchatProduit findAchatbyUserAndProduct(@Param("email")String email, @Param("id")int idProduct, @Param("code")int code);
	@Query("select a from AchatProduit a where a.client.email=:email and a.produit.id=:id and a.productKey=:code" )
	public AchatProduit findAchatProductbyUser(@Param("email")String email, @Param("id")int idProduct);
	@Query("select a from AchatProduit a where a.client.email=:email and a.produit.id=:id" )
	public AchatProduit findAchatbyUserAndProduct(@Param("email")String email, @Param("id")int idProduct);
}
