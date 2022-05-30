package com.faguy.genius.entity;

import java.nio.file.Path;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
@Entity
public class Fichier {
	@Id @GeneratedValue
	private long id;
	
	private String nom;
	private long taille;
	private String format;
	private String contentType;
	private String chemin;
	@Lob
	private String thumbnail;
	private long thumbnail_size;
	private String thumbnail_format;
	public Fichier(String nom, int taille, String format, String chemin, Article article) {
		super();
		this.nom = nom;
		this.taille = taille;
		this.format = format;
		this.chemin = chemin;
		this.article = article;
	}
	@ManyToOne
	@JoinColumn(name = "article_id")
	private Article article;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public long getTaille() {
		return taille;
	}
	public void setTaille(long taille) {
		this.taille = taille;
	}
	public String getFormat() {
		return format;
	}
	public Fichier() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getChemin() {
		return chemin;
	}
	public void setChemin(String chemin) {
		this.chemin = chemin;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public long getThumbnail_size() {
		return thumbnail_size;
	}
	public void setThumbnail_size(long thumbnail_size) {
		this.thumbnail_size = thumbnail_size;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getThumbnail_format() {
		return thumbnail_format;
	}
	public void setThumbnail_format(String thumbnail_format) {
		this.thumbnail_format = thumbnail_format;
	}
}
