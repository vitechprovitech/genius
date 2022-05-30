package com.faguy.genius.model.DTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.faguy.genius.entity.Article;
import com.faguy.genius.entity.Assignation;
import com.faguy.genius.entity.Categorie;
import com.faguy.genius.entity.IProduct;
public class CategorieDTO  implements IProduct {
	private int id;
	private String code;
	private String categorie;
	private String contenu;
	private int children=0;
	private String titre;
	private int prix=0;
	public String getContenu() {
		return contenu;
	}
	public void setContenu(String contenu) {
		this.contenu = contenu;
	}
	public int getChildren() {
		return children;
	}
	public void setChildren(int children) {
		this.children = children;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public int getPrix() {
		return prix;
	}
	public void setPrix(int prix) {
		this.prix = prix;
	}
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
	private int periode=-1;
	private int totalMAC=-1;
	private CategorieDTO ancestor;
	private CategorieDTO parent;
	private ArrayList<CategorieDTO> subCategories=new ArrayList<>();
	private ArrayList<AssignationDTO> assignations=new ArrayList<AssignationDTO>();
	public ArrayList<CategorieDTO> getSubCategories() {
		return subCategories;
	}
	public void setSubCategories(ArrayList<CategorieDTO> subCategories) {
		this.subCategories = subCategories;
	}
	public CategorieDTO getParent() {
		return parent;
	}
	public void setParent(CategorieDTO parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "CategorieDTO [code=" + code + ", categorie=" + categorie + ", parent=" + parent + ", subCategories="
				+ subCategories + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public CategorieDTO(int id, String code, String categorie) {
		super();
		this.id = id;
		this.code = code;
		this.categorie = categorie;
	}
	public CategorieDTO(int id, String code, String categorie, CategorieDTO ancestor) {
		super();
		this.id = id;
		this.code = code;
		this.categorie = categorie;
		this.ancestor=ancestor;
	}
	public CategorieDTO(int id) {
		super();
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCategorie() {
		return categorie;
	}
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
	public static CategorieDTO toDTO(Categorie cd) {
		CategorieDTO cda=new CategorieDTO((int) cd.getId(), cd.getCode(), cd.getCategorie());
		cda.setPrix(cd.getPrix());
		cda.setContenu(cd.getDescription());
		cda.setTotalMAC(cd.getTotalMAC());
		cda.setPeriode(cd.getPeriode());
		cda.setTitre(cd.getTitre());
		int ancestor=cd.getAncestor();
		cda.setAncestor(new CategorieDTO(cd.getAncestor()));
		Categorie parent=cd.getParent();
		if(parent!=null) {
					cda.setParent(new CategorieDTO((int) cd.getParent().getId(), cd.getParent().getCode(), cd.getParent().getCategorie()));
		}
		Collection<Assignation> articles=cd.getArticles();
		ArrayList<Assignation> assignations=new ArrayList<Assignation>();
		if(articles!=null)
			assignations=(ArrayList<Assignation>) articles.stream().collect(Collectors.toList());
		
		ArrayList<AssignationDTO> asdto=new ArrayList<AssignationDTO>();
		for(Assignation as:assignations) {
			asdto.add(AssignationDTO.toDTO(as));
		}
		Collection<Categorie> sousCategories=cd.getSousCategories();
		ArrayList<Categorie> sCategories=new ArrayList<Categorie>();
		System.err.println("KIIIII === >  "+sousCategories);
		if(sousCategories!=null)
			sCategories=(ArrayList<Categorie>) sousCategories.stream().collect(Collectors.toList());
		ArrayList<CategorieDTO> scdtos=new ArrayList<CategorieDTO>();
		for(Categorie sc:sCategories) {
			CategorieDTO xxx=CategorieDTO.toDTO(sc);
			scdtos.add(xxx);
		}
		cda.setAssignations(asdto);
		cda.setSubCategories(scdtos);
		return cda;
	}
	public ArrayList<AssignationDTO> getAssignations() {
		return assignations;
	}
	public void setAssignations(ArrayList<AssignationDTO> assignations) {
		this.assignations = assignations;
	}
	public CategorieDTO getAncestor() {
		return ancestor;
	}
	public void setAncestor(CategorieDTO ancestor) {
		this.ancestor = ancestor;
	}
}
