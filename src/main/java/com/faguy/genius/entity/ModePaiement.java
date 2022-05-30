package com.faguy.genius.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
public class ModePaiement implements Serializable {
	@Id @GeneratedValue
	private long id;
	private String mode;
	private String code;
	@Lob
	private String logos;
	public ModePaiement(String mode, String code, String logos) {
		super();
		this.mode = mode;
		this.code = code;
		this.logos = logos;
	}
	public String getLogos() {
		return logos;
	}
	public void setLogos(String logos) {
		this.logos = logos;
	}
	public Collection<PaiementPays> getPays() {
		return pays;
	}
	public void setPays(Collection<PaiementPays> pays) {
		this.pays = pays;
	}
	@OneToMany(mappedBy = "mode", fetch = FetchType.LAZY)
	private Collection<PaiementPays> pays;
	@OneToMany(mappedBy = "mode", fetch = FetchType.LAZY)
	private Collection<PaiementsChoisis> articles;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Collection<PaiementsChoisis> getArticles() {
		return articles;
	}
	public ModePaiement() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ModePaiement(String mode, String code) {
		super();
		this.mode = mode;
		this.code = code;
	}
	public void setArticles(Collection<PaiementsChoisis> articles) {
		this.articles = articles;
	}

	
}
