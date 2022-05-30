package com.faguy.genius.entity;

import java.io.Serializable;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class PaiementPays implements Serializable {
	@Id @GeneratedValue
		private long id;
		@ManyToOne
		@JoinColumn(name="mode_id")
		private ModePaiement mode;
		@ManyToOne
		@JoinColumn(name="pays_id")
		private Pays pays;
		public PaiementPays() {
			super();
			// TODO Auto-generated constructor stub
		}
		public PaiementPays(ModePaiement mode, Pays pays) {
			super();
			this.mode = mode;
			this.pays = pays;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public ModePaiement getMode() {
			return mode;
		}
		public void setMode(ModePaiement mode) {
			this.mode = mode;
		}
		public Pays getPays() {
			return pays;
		}
		public void setPays(Pays pays) {
			this.pays = pays;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		private String code;
}
