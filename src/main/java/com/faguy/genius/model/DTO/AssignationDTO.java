package com.faguy.genius.model.DTO;

import com.faguy.genius.entity.Assignation;
import com.faguy.genius.entity.IProduct;

public class AssignationDTO  implements IProduct {
	private int id;
	private CategorieDTO  categorie;
	private ArticleDTO article;
	private String etiquette;
	private String description;
	private long creeLe;
	private int periode=-1;
	private int prix=0;
	private int totalMac;
	private UserDTO actor;    // le createur de l'assignation
	public int getId() {
		return id;
	}
	public int getPeriode() {
		return periode;
	}
	public void setPeriode(int periode) {
		this.periode = periode;
	}
	public int getPrix() {
		return prix;
	}
	public void setPrix(int prix) {
		this.prix = prix;
	}
	public int getTotalMac() {
		return totalMac;
	}
	public void setTotalMac(int totalMac) {
		this.totalMac = totalMac;
	}
	public AssignationDTO(int id, CategorieDTO categorie, ArticleDTO article, String etiquette, String description,
			long creeLe, UserDTO actor) {
		super();
		this.id = id;
		this.categorie = categorie;
		this.article = article;
		this.etiquette = etiquette;
		this.description = description;
		this.creeLe = creeLe;
		this.actor = actor;
	}
	public void setId(int id) {
		this.id = id;
	}
	public CategorieDTO getCategorie() {
		return categorie;
	}
	public void setCategorie(CategorieDTO categorie) {
		this.categorie = categorie;
	}
	public ArticleDTO getArticle() {
		return article;
	}
	public void setArticle(ArticleDTO article) {
		this.article = article;
	}
	public String getEtiquette() {
		return etiquette;
	}
	public void setEtiquette(String etiquette) {
		this.etiquette = etiquette;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getCreeLe() {
		return creeLe;
	}
	public void setCreeLe(long creeLe) {
		this.creeLe = creeLe;
	}
	public UserDTO getActor() {
		return actor;
	}
	public void setActor(UserDTO actor) {
		this.actor = actor;
	}
	public static AssignationDTO toDTO(Assignation as) {
		AssignationDTO ass=new AssignationDTO(as.getId(), new CategorieDTO((int) as.getCategorie().getId(), as.getCategorie().getCode(), as.getCategorie().getCategorie(), new CategorieDTO(as.getCategorie().getAncestor())), ArticleDTO.toArticleDTO(as.getArticle()), as.getEtiquette(), as.getDescription(), as.getCreeLe(), new UserDTO(as.getActor().getEmail(), as.getActor().getName()));
		ass.setPrix(as.getPrix());
		ass.setPeriode(as.getPeriode());
		ass.setTotalMac(as.getTotalMac());
		return ass;
	}
	
}
