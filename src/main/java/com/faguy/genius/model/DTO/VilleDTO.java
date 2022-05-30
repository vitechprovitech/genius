package com.faguy.genius.model.DTO;

import java.util.Collection;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.faguy.genius.entity.User;


public class VilleDTO {
	@Id @GeneratedValue
    private long id;
	private PaysDTO country;
	public PaysDTO getCountry() {
		return country;
	}
	public void setCountry(PaysDTO country) {
		this.country = country;
	}

	public VilleDTO(long id, String nom) {
		super();
		this.id = id;
		this.nom = nom;
	}
	public VilleDTO(PaysDTO country, String nom, int id) {
		super();
		this.country = country;
		this.nom = nom;
		this.id=id;
	}
	private Collection<User> users;
    private String nom;


    public VilleDTO(String nom)
    {
        this.setNom(nom);
    }
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public VilleDTO() {
		// TODO Auto-generated constructor stub
	}

	public VilleDTO(long id, PaysDTO country, Collection<User> users, String nom) {
		super();
		this.id = id;
		this.country = country;
		this.users = users;
		this.nom = nom;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

}
