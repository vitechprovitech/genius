package com.faguy.genius.entity;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "typeAchat",
	    discriminatorType =DiscriminatorType.STRING
	)
public abstract class Achat implements Serializable {
	@Id @GeneratedValue
	protected int id;
	protected String MACs="";
	protected String MAC="";
	protected int totalMAC;
	protected int amount=0;
	
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}

	protected int periode=0;
	public int getPeriode() {
		return periode;
	}
	public void setPeriode(int periode) {
		this.periode = periode;
	}
	public int getTotalMAC() {
		return totalMAC;
	}
	public void setTotalMAC(int totalMAC) {
		this.totalMAC = totalMAC;
	}
	public String getMAC() {
		return MAC;
	}
	public void setMAC(String mAC) {
		MAC = mAC;
	}
	public String getMACs() {
		return MACs;
	}
	public void setMACs(String mACs) {
		MACs = mACs;
	}

	@ManyToOne
	@JoinColumn(name="client")
	protected User client;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getClient() {
		return client;
	}
	public void setClient(User client) {
		this.client = client;
	}

	public long getDateExpiration() {
		return dateExpiration;
	}
	public void setDateExpiration(long dateExpiration) {
		this.dateExpiration = dateExpiration;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	
	protected int productKey;
	public int getProductKey() {
		return productKey;
	}
	public void setProductKey(int productKey) {
		this.productKey = productKey;
	}

	protected long dateExpiration;
	
	private String commentaire;   // Ceci va contenir un indicateur de cloture d'achat
	protected long editeLe=0;
	public long getEditeLe() {
		return editeLe;
	}
	public void setEditeLe(long editeLe) {
		this.editeLe = editeLe;
	}
}
