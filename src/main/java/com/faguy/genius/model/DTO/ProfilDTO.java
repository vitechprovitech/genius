package com.faguy.genius.model.DTO;



public class ProfilDTO {
	private int id;
	private String profil;
	private String details;
	public int getId() {
		return id;
	}
	public ProfilDTO() {
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
	public ProfilDTO(String profil, String details) {
		super();
		this.profil = profil;
		this.details = details;
	}
	public ProfilDTO(int id, String profil, String details) {
		super();
		this.profil = profil;
		this.details = details;
		this.id=id;
	}
}
