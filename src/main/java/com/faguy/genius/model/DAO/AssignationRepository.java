package com.faguy.genius.model.DAO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.faguy.genius.entity.Assignation;
import com.faguy.genius.entity.Categorie;




public interface AssignationRepository extends JpaRepository<Assignation, Integer> {
	@Query("select DISTINCT(c.categorie.id) from Assignation c where  c.categorie.parent is null and c.article.state=1 order by c.id desc")
	ArrayList<Integer> findRootCategoriesClient();
	@Query("select DISTINCT(c.categorie.id) from Assignation c where  c.categorie.parent is null and c.article.state=1 order by c.id desc")
	ArrayList<Integer> lesRootCategoriesRecentes();
	@Query("select DISTINCT(c.categorie.id) from Assignation c where  c.categorie.parent is null and c.article.typeMedia=:typeMedia and c.article.state=1")
	ArrayList<Integer> findRootCategoriesByType(@Param("typeMedia") String typeMedia);
	@Query("select c from Assignation c where  c.categorie.id=:idCategorie and c.article.typeMedia=:typeMedia and c.article.state=1")
	ArrayList<Assignation> findArticleByTypeAndCategorie(@Param("typeMedia") String typeMedia, @Param("idCategorie")  long idCategorie);
	@Query("select c from Assignation c where  c.categorie.id=:idCategorie")
	ArrayList<Assignation> findArticleByCategorie(@Param("idCategorie")  long idCategorie);
	@Query("select c from Assignation c where  c.categorie.id=:idCategorie and c.article.state=1")
	ArrayList<Assignation> findArticleClientByCategorie(@Param("idCategorie")  long idCategorie);
	@Query("select DISTINCT(c.categorie.id) from Assignation c where  c.categorie.parent is null and c.article.editedBy.email=:email")
	ArrayList<Integer> findRootCategoriesByUser(@Param("email") String email);	
	@Query("select c from Assignation c where  c.categorie.parent is null and c.article.editedBy.email=:email")
	ArrayList<Assignation> findArticlesByUser(@Param("email")  String email);
	@Query("select c from Assignation c where  c.categorie.parent is null")
	ArrayList<Assignation> findAllArticles();
	
	@Query("select c from Assignation c where  c.categorie.id=:idCategorie and c.article.id=:idArticle")
	Assignation findAssignationByArticleAndCategorie(@Param("idCategorie")  long idCategorie, @Param("idArticle") long idArticle);
	@Query("select c from Assignation c where c.article.state=1 order by c.id DESC")
	ArrayList<Assignation> findLastArticles();

}
