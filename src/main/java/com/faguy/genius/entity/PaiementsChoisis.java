package com.faguy.genius.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class PaiementsChoisis {
	@Id @GeneratedValue
	private long id;
	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;
	@ManyToOne
	@JoinColumn(name = "mode_id")
	private ModePaiement mode;
	public PaiementsChoisis() {
		super();
		// TODO Auto-generated constructor stub
	}
	public long getId() {
		return id;
	}
	public PaiementsChoisis(long id, Article article, ModePaiement mode) {
		super();
		this.id = id;
		this.article = article;
		this.mode = mode;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public ModePaiement getMode() {
		return mode;
	}
	public void setMode(ModePaiement mode) {
		this.mode = mode;
	}
}
