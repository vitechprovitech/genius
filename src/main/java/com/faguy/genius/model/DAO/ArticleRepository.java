package com.faguy.genius.model.DAO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.faguy.genius.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	@Query("select id from Article a ORDER BY a.id asc")
	ArrayList<Long> lesIds();
	
	@Query("select a from Article a where a.id=:id")
	Article findArticleById(@Param("id") long id);
	@Query("select a from Article a where a.typeMedia=:typeMedia")
	ArrayList<Article> findArticlesByType(@Param("typeMedia") String typeMedia);
	@Query("select a from Article a where a.id NOT IN (:ids)")
	ArrayList<Article> lesArticles(@Param("ids") List<Long> ids);
}
