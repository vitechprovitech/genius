package com.faguy.genius.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Token implements Serializable {
	@Id @GeneratedValue
	private long id;
	private int token;
	private Date delai;
	@ManyToOne()
	@JoinColumn(name="user_id")
	private User user;
	
	public Date getDelai() {
		return delai;
	}
	public Token() {
		super();
	}
	
	public void setDelai(Date delai) {
		this.delai = delai;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getToken() {
		return token;
	}
	public void setToken(int token) {
		this.token = token;
	}
	
}
