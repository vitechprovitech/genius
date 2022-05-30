package com.faguy.genius.model.DTO;

import java.util.ArrayList;
import java.util.Collection;

public class PaysDTO {
	private long id;
	private String nom;
	private String code;
	private String drapeau;   // drapeau en base64
	private String codeIndicatif;
	private Collection<VilleDTO> cities;		
	public long getId() {
		return id;
	}
	public PaysDTO(String code) {
		super();
		this.code = code;
	}
	public void setId(int id) {
		this.id = id;
	}

	public PaysDTO(long id, String nom) {
		super();
		this.id = id;
		this.nom = nom;
	}
	public PaysDTO(long id, String nom, String code, String drapeau, String codeIndicatif,
			Collection<VilleDTO> cities) {
		super();
		this.id = id;
		this.nom = nom;
		this.code = code;
		this.drapeau = drapeau;
		this.codeIndicatif = codeIndicatif;
		this.cities = cities;
	}
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeIndicatif() {
		return codeIndicatif;
	}

	public void setCodeIndicatif(String codeIndicatif) {
		this.codeIndicatif = codeIndicatif;
	}

	public void add(VilleDTO ville) {
		
		if(cities==null) {
			cities=new ArrayList();		
		}
		cities.add(ville);
	}
	public void addAll(ArrayList<VilleDTO> villes) {
		this.cities=villes;
	}

	public PaysDTO() {
		// TODO Auto-generated constructor stub

	}
	public String getDrapeau() {
		return drapeau;
	}
	public void setDrapeau(String drapeau) {
		this.drapeau = drapeau;
	}

}
