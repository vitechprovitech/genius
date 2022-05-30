package com.faguy.genius.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.faguy.genius.model.DAO.TransfertAction;

@Entity
public class User implements Serializable {
	@Id 
	private String name;
	private String login;
	private String surname;
	@ManyToOne
	@JoinColumn(name="profil_id")
	private Profil profil;
	@ManyToOne()
	@JoinColumn(name="ville_id")
	private Ville city;
	
	private String email;
	private String phone;
	@Lob
	private String photo;
	private String address;
	private String fonction;
	
	public String getFonction() {
		return fonction;
	}
	public void setFonction(String fonction) {
		this.fonction = fonction;
	}

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Collection<Dossier> dossiers;
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Collection<TransfertAction> transferts;
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Collection<Attribution> roles;
	public Collection<Attribution> getRoles() {
		return roles;
	}
	public void setRoles(Collection<Attribution> roles) {
		this.roles = roles;
	}

	@OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
	private Collection<Achat> achats;
	@OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
	private Collection<Categorie> categories;

	@OneToMany(mappedBy = "actor", fetch = FetchType.LAZY)
	private Collection<Assignation> assignations;
	public long getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	private long lastUpdate;
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Collection<Token> tokens;

	@OneToMany(mappedBy = "editedBy", fetch = FetchType.LAZY)
	private Collection<Article> editedArticles;
	@OneToMany(mappedBy = "validatedBy", fetch = FetchType.LAZY)
	private Collection<Article> validatedArticles;
	@OneToMany(mappedBy = "user",  fetch = FetchType.LAZY)
	private Collection<Action> actions;
	@Override
	public String toString() {
		return "User []";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Profil getProfil() {
		return profil;
	}
	public void setProfil(Profil profil) {
		this.profil = profil;
	}

	public Ville getCity() {
		return city;
	}
	public void setCity(Ville city) {
		this.city = city;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public User() {
		super();
		lastUpdate=new Date().getTime();
	}

	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	private Date birthDate;
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMAC() {
		return MAC;
	}
	public void setMAC(String mAC) {
		MAC = mAC;
	}
	public int getStatut() {
		return statut;
	}

	public User(String name, Profil profil, Ville city, String email, String phone,
			Date birthDate, String password, String mAC, int statut) {
		super();
		this.name = name;
		this.profil = profil;
		this.city = city;
		this.email = email;
		this.phone = phone;
		this.birthDate = birthDate;
		this.password = password;
		MAC = mAC;
		this.statut = statut;
	}
	public void setStatut(int statut) {
		this.statut = statut;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}



	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}

	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}

	private String password;
	private String MAC;
	
    private  int statut;   // 0=EN ATTENTE, 1=ACTIF, -1=BLOQUE

}

