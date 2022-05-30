package com.faguy.genius.entity;

import java.io.Serializable;
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
public class Article implements Serializable, IRessource {
	@Id @GeneratedValue
	private long id;
	private String title;
	private String content;
	private long valideLe=0;
	private long editeLe;
	public int getTotalMAC() {
		return totalMAC;
	}
	public void setTotalMAC(int totalMAC) {
		this.totalMAC = totalMAC;
	}
	private double prix=0;
	private long periode=-1;
	private String thumbnail;
	private String fichier;
	private long tailleFichier;
	private int totalMAC=-1;
	public long getTailleFichier() {
		return tailleFichier;
	}
	public void setTailleFichier(long tailleFichier) {
		this.tailleFichier = tailleFichier;
	}
	public Collection<Assignation> getAssignations() {
		return assignations;
	}
	public void setAssignations(Collection<Assignation> assignations) {
		this.assignations = assignations;
	}
	public long getPeriode() {
		return periode;
	}
	public void setPeriode(long periode) {
		this.periode = periode;
	}
	private String typeMedia;
	private String devise;
	private String message;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}


	public String getDevise() {
		return devise;
	}
	public void setDevise(String devise) {
		this.devise = devise;
	}
	private int state=0;      // 0=ATTENTE  1=validé   -1 desactivé -4=rejeté
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Article() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public User getEditedBy() {
		return editedBy;
	}
	public void setEditedBy(User editedBy) {
		this.editedBy = editedBy;
	}
	public User getValidatedBy() {
		return validatedBy;
	}
	public void setValidatedBy(User validatedBy) {
		this.validatedBy = validatedBy;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getValideLe() {
		return valideLe;
	}
	public void setValideLe(long valideLe) {
		this.valideLe = valideLe;
	}
	public long getEditeLe() {
		return editeLe;
	}
	public void setEditeLe(long editeLe) {
		this.editeLe = editeLe;
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}
	public String getEtatService() {
		return etatService;
	}
	public void setEtatService(String etatService) {
		this.etatService = etatService;
	}
	
	public Collection<PaiementsChoisis> getModesPaiements() {
		return modesPaiements;
	}
	public void setModesPaiement(Collection<PaiementsChoisis> modesPaiements) {
		this.modesPaiements = modesPaiements;
	}
	public Collection<Fichier> getFichiers() {
		return fichiers;
	}
	public void setFichiers(Collection<Fichier> fichiers) {
		this.fichiers = fichiers;
	}
	public String getTypeMedia() {
		return typeMedia;
	}
	public void setTypeMedia(String typeMedia) {
		this.typeMedia = typeMedia;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getFichier() {
		return fichier;
	}
	public void setFichier(String fichier) {
		this.fichier = fichier;
	}

	private String etatService;     // free ou pay
	@OneToMany(mappedBy = "article",  fetch = FetchType.LAZY)
	private Collection<PaiementsChoisis> modesPaiements;
	@OneToMany(mappedBy = "article",  fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Collection<Assignation> assignations;
	@OneToMany(mappedBy = "article",  fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Collection<Fichier> fichiers;


	@ManyToOne()
	@JoinColumn(name = "editor_id")
	private User editedBy;
	@ManyToOne()
	@JoinColumn(name="validator_id")
	private User validatedBy;
	
}
