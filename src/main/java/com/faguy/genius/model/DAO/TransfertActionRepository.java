package com.faguy.genius.model.DAO;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TransfertActionRepository extends JpaRepository<TransfertAction, Long> {

	@Query("select t from TransfertAction t where t.user.id=:id")
	ArrayList<TransfertAction> getTransfertById(@Param("id") int id);
	@Query("select t from TransfertAction t where t.user.email=:email and t.MAC1=:mac and t.user.password=:password and status=0")
	TransfertAction findTransfertByMACAndEmailAndPassword(@Param("email") String email,@Param("password") String password , @Param("mac") String mac);	
	@Query("select t from TransfertAction t where t.user.email=:email and t.MAC1=:mac and t.user.password=:password and status=0")
	TransfertAction findTransfertByMACAndEmail(@Param("email") String email, @Param("mac") String mac);
}
