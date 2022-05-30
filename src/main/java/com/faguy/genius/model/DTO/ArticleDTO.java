package com.faguy.genius.model.DTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.faguy.genius.entity.Article;
import com.faguy.genius.entity.Fichier;
import com.faguy.genius.entity.PaiementsChoisis;


public class ArticleDTO {
		private int id;
		private CategorieDTO categorie;
		private String contenu;
		private String titre;
		private FichierDTO fichier;
		private String typeMedia;
		private int totalMAC;
		private int periode;
		private String thumbnail;
		private String filepath;
		private String urlVideo;
		
		public int getPeriode() {
			return periode;
		}

		public void setPeriode(int periode) {
			this.periode = periode;
		}

		public String getTypeMedia() {
			return typeMedia;
		}

		public void setTypeMedia(String typeMedia) {
			this.typeMedia = typeMedia;
		}

		public int getTotalMAC() {
			return totalMAC;
		}

		public void setTotalMAC(int totalMAC) {
			this.totalMAC = totalMAC;
		}
		private UserDTO editedby;
		public ArticleDTO(int id) {
			super();
			this.id = id;
		}
		
		public ArticleDTO(int id, String contenu, String titre, FichierDTO fichier, UserDTO editedby,
				ArrayList<ModePaiementDTO> modes, double price, long editeLe) {
			super();
			this.id = id;
			this.contenu = contenu;
			this.titre = titre;
			this.fichier = fichier;
			this.editedby = editedby;
			this.modes = modes;
			this.price = price;
			this.editeLe = editeLe;
		}

		public ArticleDTO() {
			super();
			// TODO Auto-generated constructor stub
		}
		private ArrayList<ModePaiementDTO> modes;
		private double price;
		private long editeLe;
		public static ArticleDTO toArticleDTO(Article article) {
			ArticleDTO a=new ArticleDTO();
			a.setEditeLe(article.getEditeLe());
			a.setId((int) article.getId());
			a.setPrice(article.getPrix());
			a.setTotalMAC(article.getTotalMAC());
			a.setPeriode((int) article.getPeriode());
			a.setThumbnail(article.getThumbnail());
			Collection<PaiementsChoisis> cpms=	article.getModesPaiements();
			long p=cpms.size();
			ArrayList<PaiementsChoisis> pms=(ArrayList<PaiementsChoisis>) cpms.stream().collect(Collectors.toList());
			ArrayList<ModePaiementDTO> pdto=new ArrayList<ModePaiementDTO>();
			for(int i=0;i<pms.size();i++) {
					pdto.add(new ModePaiementDTO((int) pms.get(i).getMode().getId(), pms.get(i).getMode().getCode()));
			}
			//CategorieDTO cdto=new CategorieDTO((int) article.getCategorie().getId(), article.getCategorie().getCode(), article.getCategorie().getCategorie());
		
			//a.setCategorie(cdto);
			Collection<Fichier> fichiers= article.getFichiers();
			//ArrayList<Fichier> fichier=(ArrayList<Fichier>)fichiers;
			ArrayList<Fichier> fichier=new ArrayList<Fichier>();
			for(Fichier f:fichiers){
				fichier.add(f);
			}
			
			if(fichier!=null && fichier.size()>0) {
				a.setFichier(new FichierDTO((int) fichier.get(0).getId(), fichier.get(0).getThumbnail(), fichier.get(0).getFormat(), fichier.get(0).getNom()));
			}
			a.setEditedby(new UserDTO(article.getEditedBy().getEmail(), article.getEditedBy().getName()));
			
			a.setTitre(article.getTitle());
			a.setContenu(article.getContent());
			a.setTypeMedia(article.getTypeMedia());
		
			return a;
		}
		public String getThumbnail() {
			return thumbnail;
		}

		public void setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
		}

		public int getId() {
			return id;
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

		public String getContenu() {
			return contenu;
		}
		public void setContenu(String contenu) {
			this.contenu = contenu;
		}
		public String getTitre() {
			return titre;
		}
		public void setTitre(String titre) {
			this.titre = titre;
		}
		public FichierDTO getFichier() {
			return fichier;
		}
		public void setFichier(FichierDTO fichier) {
			this.fichier = fichier;
		}
		public UserDTO getEditedby() {
			return editedby;
		}
		public void setEditedby(UserDTO editedby) {
			this.editedby = editedby;
		}
		public ArrayList<ModePaiementDTO> getModes() {
			return modes;
		}
		public void setModes(ArrayList<ModePaiementDTO> modes) {
			this.modes = modes;
		}
		public long getEditeLe() {
			return editeLe;
		}
		public void setEditeLe(long editeLe) {
			this.editeLe = editeLe;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
}
