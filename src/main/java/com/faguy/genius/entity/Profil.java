package com.faguy.genius.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Profil implements Serializable{
	@Id @GeneratedValue
	private int id;
	private String profil;
	@OneToMany(mappedBy = "profil", fetch = FetchType.LAZY)
	private Collection<User> users;
	private String details;
	public int getId() {
		return id;
	}
	public Profil() {
		super();
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProfil() {
		return profil;
	}
	public void setProfil(String profil) {
		this.profil = profil;
	}
	public String getDetails() {
		return details;
	}
	
	public void setDetails(String details) {
		this.details = details;
	}
	public Profil(int id, String profil, String details) {
		super();
		this.profil = profil;
		this.details = details;
		this.id=id;
	}
	public Profil(String profil, String details) {
		super();
		this.profil = profil;
		this.details = details;
	}

	

}
