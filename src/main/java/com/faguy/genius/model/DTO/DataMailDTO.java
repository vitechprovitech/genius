package com.faguy.genius.model.DTO;

import com.faguy.genius.entity.Achat;
import com.faguy.genius.entity.Assignation;
import com.faguy.genius.entity.User;

public class DataMailDTO {

	private User user;
	private Achat achat;
	private int remainingPeriod;
	private int remainingMaxDevices;
	private String dateSubscription;
	private int activationCode;
	private String productName;
	private String type;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	private int prix;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Achat getAchat() {
		return achat;
	}
	public void setAchat(Achat achat) {
		this.achat = achat;
	}
	public int getRemainingPeriod() {
		return remainingPeriod;
	}
	public void setRemainingPeriod(int remainingPeriod) {
		this.remainingPeriod = remainingPeriod;
	}
	public int getRemainingMaxDevices() {
		return remainingMaxDevices;
	}
	public void setRemainingMaxDevices(int remainingMaxDevices) {
		this.remainingMaxDevices = remainingMaxDevices;
	}
	public String getDateSubscription() {
		return dateSubscription;
	}
	public void setDateSubscription(String dateSubscription) {
		this.dateSubscription = dateSubscription;
	}
	public int getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(int activationCode) {
		this.activationCode = activationCode;
	}
	public int getPrix() {
		return prix;
	}
	public void setPrix(int prix) {
		this.prix = prix;
	}	
}
