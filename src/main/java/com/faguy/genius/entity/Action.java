package com.faguy.genius.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Action {
	@Id @GeneratedValue
	private int id;
	private String objet;
	private String action;    // DOWNLOADED, CONNECTED, VIEWED, PACKED   

	public void setObjet(String objet) {
		this.objet = objet;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	public int getId() {
		return id;
	}
	public Action( User user) {
		super();
		this.user = user;
		lastUpdate=new Date().getTime();
	}
	public Action(String objet, String action, User user ) {
		super();
		this.objet = objet;
		this.action = action;
		this.user = user;
		lastUpdate=new Date().getTime();
	}
	public Action() {
		super();
		// TODO Auto-generated constructor stub
		lastUpdate=new Date().getTime();
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Assignation getArticle() {
		return article;
	}
	public void setArticle(Assignation article) {
		this.article = article;
	}
	public long getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getObjet() {
		return objet;
	}

	@ManyToOne
	@JoinColumn(name = "article_id")
	private Assignation article;
	private long lastUpdate;

}
