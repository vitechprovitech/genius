package com.faguy.genius.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
public class Assignation implements Serializable, IProduct {

	@Id @GeneratedValue
	private int id;
	@ManyToOne
	@JoinColumn(name = "categorie_id")
	private Categorie  categorie;
	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;
	@OneToMany(mappedBy = "produit",  fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Collection<AchatProduit> achats;
	private String etiquette;
	private String description;
	private int periode=-1;
	private int prix=0;
	@OneToMany(mappedBy = "article",  fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)	
	private Collection<Action> actions;
	
	public Collection<AchatProduit> getAchats() {
		return achats;
	}
	public void setAchats(Collection<AchatProduit> achats) {
		this.achats = achats;
	}
	public int getPeriode() {
		return periode;
	}
	public void setPeriode(int periode) {
		this.periode = periode;
	}
	public int getPrix() {
		return prix;
	}
	public void setPrix(int prix) {
		this.prix = prix;
	}
	public int getTotalMac() {
		return totalMac;
	}
	public void setTotalMac(int totalMac) {
		this.totalMac = totalMac;
	}
	private int totalMac=-1;
	public String getEtiquette() {
		return etiquette;
	}
	public void setEtiquette(String etiquette) {
		this.etiquette = etiquette;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	private long creeLe;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User actor;    // le createur de l'assignation  
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Categorie getCategorie() {
		return categorie;
	}
	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}

	public long getCreeLe() {
		return creeLe;
	}
	public void setCreeLe(long creeLe) {
		this.creeLe = creeLe;
	}
	public User getActor() {
		return actor;
	}
	public void setActor(User actor) {
		this.actor = actor;
	}


}
