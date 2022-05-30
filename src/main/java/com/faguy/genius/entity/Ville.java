package com.faguy.genius.entity;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Ville {
	@Id @GeneratedValue
    private long id;
	@ManyToOne()
	@JoinColumn(name="pays_id")
	private Pays country;
	public Pays getCountry() {
		return country;
	}
	public void setCountry(Pays country) {
		this.country = country;
	}
	public Collection<User> getUsers() {
		return users;
	}
	public void setUsers(Collection<User> users) {
		this.users = users;
	}
	public Ville(Pays country, String nom, int id) {
		super();
		this.country = country;
		this.nom = nom;
		this.id=id;
	}
	@OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
	private Collection<User> users;
    private String nom;


    public Ville(String nom)
    {
        this.setNom(nom);
    }
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Ville() {
		// TODO Auto-generated constructor stub
	}

	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
}
