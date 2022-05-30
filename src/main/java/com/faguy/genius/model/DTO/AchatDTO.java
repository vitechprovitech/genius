package com.faguy.genius.model.DTO;

import com.faguy.genius.entity.Achat;
import com.faguy.genius.entity.AchatCategorie;
import com.faguy.genius.entity.AchatProduit;

public  class AchatDTO {

	protected int id;
	protected String MACs="";
	private CategorieDTO bundle;
	private AssignationDTO produit;
	private String MAC;
	public AssignationDTO getProduit() {
		return produit;
	}




	public void setProduit(AssignationDTO produit) {
		this.produit = produit;
	}

	public CategorieDTO getBundle() {
		return bundle;
	}
	public void setBundle(CategorieDTO bundle) {
		this.bundle = bundle;
	}
	public String getMACs() {
		return MACs;
	}
	public void setMACs(String mACs) {
		MACs = mACs;
	}

	protected UserDTO client;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UserDTO getClient() {
		return client;
	}
	public void setClient(UserDTO client) {
		this.client = client;
	}

	public long getDateExpiration() {
		return dateExpiration;
	}
	public void setDateExpiration(long dateExpiration) {
		this.dateExpiration = dateExpiration;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	
	protected int productKey;
	public int getProductKey() {
		return productKey;
	}
	public void setProductKey(int productKey) {
		this.productKey = productKey;
	}

	protected long dateExpiration;
	
	private String commentaire;
	protected long editeLe=0;
	public long getEditeLe() {
		return editeLe;
	}
	public void setEditeLe(long editeLe) {
		this.editeLe = editeLe;
	}
	public static AchatDTO toDTO(Achat achat) {
		AchatDTO a=new AchatDTO();
		a.setClient(new UserDTO(achat.getClient().getName(), achat.getClient().getSurname(), achat.getClient().getEmail(), achat.getClient().getEmail()));
		a.setProductKey(achat.getProductKey());
		a.setDateExpiration(achat.getDateExpiration());
		a.setMACs(achat.getMACs());
		a.setMAC(achat.getMAC());
		a.setId(achat.getId());
		a.setEditeLe(achat.getEditeLe());	
		if( achat instanceof AchatCategorie) {
			a.setBundle(new CategorieDTO((int) ((AchatCategorie) achat).getBundle().getId(), ((AchatCategorie) achat).getBundle().getCode(), ((AchatCategorie) achat).getBundle().getCategorie(), new CategorieDTO(((AchatCategorie) achat).getBundle().getAncestor())));
		}else {
			if(achat instanceof AchatProduit){
				a.setProduit(AssignationDTO.toDTO(((AchatProduit) achat).getProduit()));
			
			}
		}
		return a;
	}




	public String getMAC() {
		return MAC;
	}




	public void setMAC(String mAC) {
		MAC = mAC;
	}
}
