package com.faguy.genius.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.faguy.genius.model.IRessource;


@Entity
public class Categorie implements IProduct{
	@Id @GeneratedValue
	private long id;
	private String categorie;
	private String description;
	private int children=0;
	private String childIds="";
	private String titre;
	private int prix=0;
	private int periode=-1;
	private int totalMAC=-1;
	public void setPickine(long idArticle) {
		this.childIds=this.childIds+idArticle+";";
		if(parent!=null) {
			this.parent.setPickine(idArticle);
		}
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public int getPrix() {
		return prix;
	}
	public void setPrix(int prix) {
		this.prix = prix;
	}
	public int getPeriode() {
		return periode;
	}
	public void setPeriode(int periode) {
		this.periode = periode;
	}
	public int getTotalMAC() {
		return totalMAC;
	}
	public void setTotalMAC(int totalMAC) {
		this.totalMAC = totalMAC;
	}
	@ManyToOne
	@JoinColumn(name = "categorie_id")
	private Categorie parent;
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY,  cascade = CascadeType.PERSIST)
	private Collection<Categorie> sousCategories;
	private int ancestor=0;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User createdBy;
	public User getCreatedBy() {
		return createdBy;
	}
	public int getAncestor() {
		return ancestor;
	}
	public void setAncestor(int ancestor) {
		this.ancestor = ancestor;
	}
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	public Collection<Categorie> getSousCategories() {
		return sousCategories;
	}
	public void setSousCategories(Collection<Categorie> sousCategories) {
		this.sousCategories = sousCategories;
	}
	public Collection<Assignation> getArticles() {
		return articles;
	}
	public void setArticles(Collection<Assignation> articles) {
		this.articles = articles;
	}
	public Categorie getParent() {
		return parent;
	}
	public void setParent(Categorie parent) {
		this.parent = parent;
	}
	private String code;
	@OneToMany(mappedBy = "categorie", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Collection<Assignation> articles;
	private long createdAt;
	public Categorie() {
		super();
		// TODO Auto-generated constructor stub
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCategorie() {
		return categorie;
	}
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
	public Categorie(String categorie, String code) {
		super();
		this.categorie = categorie;
		this.code = code;
	}
	public Categorie(long id, String code) {
		super();
		this.id=id;
		this.code = code;
	}
	public Categorie(String categorie, String code, long createdAt) {
		super();
		this.categorie = categorie;
		this.code = code;
		this.createdAt = createdAt;
		
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public long getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getChildIds() {
		return childIds;
	}
	public void setChildIds(String childIds) {
		this.childIds = childIds;
	}
	public int getChildren() {
		return children;
	}
	public void setChildren(int children) {
		this.children = children;
	}
}
