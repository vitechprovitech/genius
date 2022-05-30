package com.faguy.genius.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("article")
public class AchatProduit extends Achat {
	/**
	 * 
	 */
	private static final long serialVersionUID = 552405050214054781L;
	@ManyToOne
	@JoinColumn(name="produit_id")
	private Assignation produit;




	public Assignation getProduit() {
		return produit;
	}




	public void setProduit(Assignation produit) {
		this.produit = produit;
	}




	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
