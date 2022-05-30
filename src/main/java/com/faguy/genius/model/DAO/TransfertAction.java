package com.faguy.genius.model.DAO;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.faguy.genius.entity.User;


@Entity
public class TransfertAction {
	@Id @GeneratedValue
	private int id;
	private String MAC1;
	private long lastUpdate;
	private long status; // 0 signifie que si l'ancien device est en ligne, alors supprimer tout son contenu. 1--> signifie que la procédure est géré
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	public int getId() {
		return id;
	}
	public TransfertAction() {
		super();
		lastUpdate=new Date().getTime();
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMAC1() {
		return MAC1;
	}
	public void setMAC1(String mAC1) {
		MAC1 = mAC1;
	}
	public long getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public long getStatus() {
		return status;
	}
	public void setStatus(long status) {
		this.status = status;
	}
	

}
