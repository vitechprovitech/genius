package com.faguy.genius.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;


@Entity
public class Pays implements Serializable {
	@Id @GeneratedValue
	private long id;
	public Pays(String nom, String code, String codeIndicatif, String drapeau) {
		super();
		this.nom = nom;
		this.code = code;
		this.codeIndicatif = codeIndicatif;
		this.drapeau=drapeau;
	}
	@OneToMany(mappedBy = "pays", fetch = FetchType.LAZY)
	private Collection<PaiementPays> modePaiements;
	
	private String nom;

	private String code;
	@Lob
	private String drapeau;   // drapeau en base64
	private String codeIndicatif;

	@OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
	private Collection<Ville> cities;

		
		public long getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}

		public String getNom() {
			return nom;
		}

		public void setNom(String nom) {
			this.nom = nom;
		}

		public void setId(long id) {
			this.id = id;
		}
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public Collection<Ville> getCities() {
			return cities;
		}
		public void setCities(Collection<Ville> cities) {
			this.cities = cities;
		}
		public String getCodeIndicatif() {
			return codeIndicatif;
		}

		public void setCodeIndicatif(String codeIndicatif) {
			this.codeIndicatif = codeIndicatif;
		}

		public void add(Ville ville) {
			
			if(cities==null) {
				cities=new ArrayList();		
			}
			cities.add(ville);
		}
		public void addAll(ArrayList<Ville> villes) {
			this.cities=villes;
		}

	public Pays() {
		// TODO Auto-generated constructor stub

	}
	public String getDrapeau() {
		return drapeau;
	}
	public void setDrapeau(String drapeau) {
		this.drapeau = drapeau;
	}
}