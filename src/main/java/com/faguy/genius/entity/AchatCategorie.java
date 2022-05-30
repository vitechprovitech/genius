package com.faguy.genius.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("BUNDLE")
public class AchatCategorie extends Achat {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name="categorie_id")
	private Categorie bundle;

	public Categorie getBundle() {
		return bundle;
	}

	public void setBundle(Categorie bundle) {
		this.bundle = bundle;
	}
}
