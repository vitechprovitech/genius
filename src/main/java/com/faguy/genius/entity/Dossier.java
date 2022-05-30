package com.faguy.genius.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ManyToAny;

@Entity
public class Dossier {
	@Id @GeneratedValue
	private int id;
	@ManyToOne()
	@JoinColumn(name = "user_id")
	private User user;
	private String comment;
	private long updateAt;
	private int isValidated;    // 0=WAITING   -1=REJECTED    1=ACCEPTED
	private long validatedAt=0;  // 0000000000 
	public int getIsValidated() {
		return isValidated;
	}
	public Dossier() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setIsValidated(int isValidated) {
		this.isValidated = isValidated;
	}
	public Dossier(int id, User user, String comment, long updateAt, int isValidated, long validatedAt) {
		super();
		this.id = id;
		this.user = user;
		this.comment = comment;
		this.updateAt = updateAt;
		this.isValidated = isValidated;
		this.validatedAt = validatedAt;
	}
	public long getValidatedAt() {
		return validatedAt;
	}
	public void setValidatedAt(long validatedAt) {
		this.validatedAt = validatedAt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public long getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(long updateAt) {
		this.updateAt = updateAt;
	}
}
