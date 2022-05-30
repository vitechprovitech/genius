package com.faguy.genius.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.hibernate.transform.ToListResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.faguy.genius.entity.Achat;
import com.faguy.genius.entity.AchatCategorie;
import com.faguy.genius.entity.AchatProduit;
import com.faguy.genius.entity.Action;
import com.faguy.genius.entity.Article;
import com.faguy.genius.entity.Assignation;
import com.faguy.genius.entity.Categorie;
import com.faguy.genius.entity.Fichier;
import com.faguy.genius.entity.IProduct;
import com.faguy.genius.entity.PaiementsChoisis;
import com.faguy.genius.entity.User;
import com.faguy.genius.model.Reponse;
import com.faguy.genius.model.DAO.AchatRepository;
import com.faguy.genius.model.DAO.ActionRepository;
import com.faguy.genius.model.DAO.ArticleRepository;
import com.faguy.genius.model.DAO.AssignationRepository;
import com.faguy.genius.model.DAO.CategorieRepository;
import com.faguy.genius.model.DAO.FichierRepository;
import com.faguy.genius.model.DAO.PaiementsChoisisRepository;
import com.faguy.genius.model.DAO.UserRepository;
import com.faguy.genius.model.DTO.ArticleDTO;
import com.faguy.genius.model.DTO.AssignationDTO;
import com.faguy.genius.model.DTO.CategorieDTO;
import com.google.gson.Gson;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import antlr.collections.List;

@Service
public class MediasService {
	@Autowired
	private ArticleRepository articleDAO;
	@Autowired
	private PaiementsChoisisRepository paiementschoisisDAO;
	@Autowired
	private CategorieRepository categorieDAO;
	@Autowired private FichierRepository fichierDAO;
	@Autowired private UserRepository userDAO;
	@Autowired private AssignationRepository assignationDAO;
	@Autowired
	ServletContext context;
	@Autowired 
	private ActionRepository actionDAO;
	@Autowired AchatRepository achatDAO;
	
	public ArrayList<Categorie> lesCategories(){
		return (ArrayList<Categorie>) categorieDAO.findRootCategories();
	}
	public Categorie saveFolder(Categorie categorie) {
		Categorie c=categorie.getParent();
		if(c!=null) {
			c=categorieDAO.findByCode(c.getCode());
			categorie.setParent(c);
			return categorieDAO.save(c);
		}else {
			return null;
		}
	}
	
	public ArrayList<Categorie> findRootCategoriesByType(String type){
		ArrayList<Integer> ids=assignationDAO.findRootCategoriesByType(type); 
		ArrayList<Categorie> categories=new ArrayList<Categorie>();
		for(int i:ids) {
			Categorie c=categorieDAO.findById(i);
			categories.add(c);
		}
		return categories;
	}
	
	public ArrayList<IProduct> findPurchasesByUser(String email, String typeMedia) {
		ArrayList<IProduct> purchasedProducts=new ArrayList<IProduct>();
		
		ArrayList<Achat> achats=achatDAO.findSubscribeds(email);
		for(Achat a:achats) {
			if(a instanceof AchatCategorie) {
				Categorie c=((AchatCategorie) a).getBundle();
				ArrayList<Assignation> ass=assignationDAO.findArticleByTypeAndCategorie(typeMedia, c.getId());
				c.setArticles(ass);
				CategorieDTO cto=CategorieDTO.toDTO(c);
				if(ass.size()>0)
				purchasedProducts.add(cto);
			}
			if(a instanceof AchatProduit) {
				Assignation ass=((AchatProduit) a).getProduit();
				AssignationDTO asdto=AssignationDTO.toDTO(ass);
				String typeTemp=asdto.getArticle().getTypeMedia();
				if(typeTemp.equalsIgnoreCase(typeMedia))
				purchasedProducts.add(asdto);
			}
		}
		return purchasedProducts;
	}
	public Categorie findPurchasesByUserAndCategorie(String email, String typeMedia, int idCategorie) {
		Categorie pa=categorieDAO.findById(idCategorie);
		ArrayList<Assignation> assdto=new ArrayList<Assignation>();
		ArrayList<Categorie> sCategories=new ArrayList<Categorie>();
		ArrayList<Achat> achats=achatDAO.findSubscribeds(email);    // Liste de tous les achats.
		ArrayList<AchatCategorie> tabB=new ArrayList<AchatCategorie>();   // Listes des achats de bundles d'articles
		ArrayList<AchatProduit> tabA=new ArrayList<AchatProduit>();    // Liste des achats d'articles
		ArrayList<AchatCategorie> tabBfin=new ArrayList<AchatCategorie>();   // Listes des achats de bundles d'articles
		ArrayList<AchatProduit> tabAfin=new ArrayList<AchatProduit>();    // Liste des achats d'articles
		for(Achat a:achats) {	  	
			if(a instanceof AchatCategorie) {
				tabB.add((AchatCategorie) a);  // Remplissage du tableau des achats de bundle	
			}else {
				if(a instanceof AchatProduit) {
					tabA.add((AchatProduit) a);   // Remplissage du tableau des achats de produits
				}				
			}
		}
		ArrayList<Integer> idsArticle = (ArrayList<Integer>) tabA.stream().map(s->s.getProduit().getId()).distinct().collect(Collectors.toList()); //Listes des identifiants d'articles achetés 
		ArrayList<Long> idsBundle = (ArrayList<Long>) tabB.stream().map(s->s.getBundle().getId()).distinct().collect(Collectors.toList()); //Listes des identifiants des bundles achetés 
		for(int i:idsArticle) {
			ArrayList<AchatProduit> ais=(ArrayList<AchatProduit>) tabA.stream().filter(z->z.getProduit().getId()==i).collect(Collectors.toList());
			AchatProduit ai=ais.stream().sorted(Comparator.comparingInt(AchatProduit::getId).reversed()).findFirst().get();
			tabAfin.add(ai);
		}
		for(long i:idsBundle) {
			ArrayList<AchatCategorie> ais=(ArrayList<AchatCategorie>) tabB.stream().filter(z->z.getBundle().getId()==i).collect(Collectors.toList());
			AchatCategorie ai=ais.stream().sorted(Comparator.comparingInt(AchatCategorie::getId).reversed()).findFirst().get();
			tabBfin.add(ai);
		}		
		for(AchatCategorie a:tabBfin) { 
			if(a instanceof AchatCategorie) {
				Categorie c=((AchatCategorie) a).getBundle();
				if(c!=null && c.getParent()!=null && c.getParent().getId()==pa.getId()) {
					ArrayList<Assignation> ass=assignationDAO.findArticleByTypeAndCategorie(typeMedia, c.getId());
					c.setArticles(ass);
					sCategories.add(c);					
				}

			}
		}		
		for(AchatProduit a:tabAfin) {
			ArrayList<Achat> a2=(ArrayList<Achat>) achats.stream().filter(s->s.getId()==a.getId()).sorted(Comparator.comparingInt(Achat::getId)).collect(Collectors.toList());

			if(a instanceof AchatProduit) {
				Assignation ass=((AchatProduit) a).getProduit();
				if(ass.getCategorie().getId()==pa.getId())
				assdto.add(ass);
			}
			
		}
		pa.setArticles(assdto);
		pa.setSousCategories(sCategories);
		return pa;
	}
	
	
	public ArrayList<CategorieDTO> findSubscribedRootCategorieByUser(String email, String typeMedia){
		ArrayList<IProduct> purchasedProducts=findPurchasesByUser(email, typeMedia);   // Recupère la liste des achats
		ArrayList<CategorieDTO> hset = new ArrayList<CategorieDTO>();
		for(IProduct ip:purchasedProducts) {   // Pour chaque achat
			if(ip instanceof CategorieDTO) {   // Si c'est un package, alors ajouter dans la liste des catégories
				int ida=((CategorieDTO) ip).getAncestor().getId();
				Categorie c=categorieDAO.findById(ida);
				/*    */
				int doublon=0;
				for(CategorieDTO it:hset) {
					if(it.getId()==c.getId()) {
						doublon=1;
						break;
					}
				}
				if(doublon<1) 
				hset.add(CategorieDTO.toDTO(c));
				/*     */

				
			}
			if(ip instanceof AssignationDTO) {   // 
				CategorieDTO p=((AssignationDTO) ip).getCategorie();
				if(p.getParent()!=null) {   // si c'est un package
					int ida=p.getAncestor().getId();
					Categorie c=categorieDAO.findById(ida);
					int doublon=0;
					for(CategorieDTO it:hset) {
						if(it.getId()==c.getId()) {
							doublon=1;
							break;
						}
					}
					if(doublon<1) 
					hset.add(CategorieDTO.toDTO(c));					
				}else {
					int doublon=0;
					for(CategorieDTO it:hset) {
						if(it.getId()==p.getId()) {
							doublon=1;
							break;
						}
					}
					if(doublon<1) {
						hset.add(p);	
					}
										
				}
				
			}
		}
		return hset;
	}
	public ArrayList<Categorie> findRootCategoriesByUser(String email){
		ArrayList<Integer> ids=assignationDAO.findRootCategoriesByUser(email); 
		ArrayList<Categorie> categories=new ArrayList<Categorie>();
		for(int i:ids) {
			Categorie c=categorieDAO.findById(i);
			categories.add(c);
		}
		return categories;
	}
	public Categorie recommandes(){
		Categorie categorie=new Categorie();
		categorie.setId(-100);
		categorie.setAncestor(0);
		categorie.setCategorie("HOME");
		ArrayList<Assignation> assignation=assignationDAO.findLastArticles();
		ArrayList<Categorie> sCategories=new ArrayList<Categorie>();
		categorie.setArticles(assignation);
		return categorie;
	}
	
	public Categorie showArticleByCategorie(int idCategorie){
		Categorie categorie=categorieDAO.findById(idCategorie);
		ArrayList<Assignation> assignation=assignationDAO.findArticleClientByCategorie(idCategorie);
		ArrayList<Categorie> sCategoriesTemp=categorieDAO.findSubCategories(idCategorie);
		ArrayList<Categorie> sCategories=new ArrayList<Categorie>();
		for(Categorie c:sCategoriesTemp) {
			String childIds=c.getChildIds();
			if(childIds!=null && childIds.contains(";")) {
				String[] ids=childIds.split(";");
				ArrayList<Article> as=new ArrayList<Article>();
				if(ids.length>0) {
					for(String i:ids) {
						Article a=articleDAO.findArticleById(Integer.parseInt(i));
						as.add(a);
					}
				}
				if(as.size()>0) {
					c.setChildren(as.size());
					c.setParent(new Categorie(c.getId(), c.getCode()));
					
					sCategories.add(c);
				}
			}
		}
		categorie.setArticles(assignation);
		categorie.setSousCategories(sCategories);
		return categorie;
	}
	public Categorie showArticleByCategorieAndTypeMedia1(int idCategorie, String typeMedia){
		Categorie categorie=categorieDAO.findById(idCategorie);
		ArrayList<Assignation> assignation=assignationDAO.findArticleByTypeAndCategorie(typeMedia, idCategorie);
		ArrayList<Categorie> sCategoriesTemp=categorieDAO.findSubCategories(idCategorie);
		ArrayList<Categorie> sCategories=new ArrayList<Categorie>();
		for(Categorie c:sCategoriesTemp) {
			String childIds=c.getChildIds();
			if(childIds!=null && childIds.contains(";")) {
				String[] ids=childIds.split(";");
				ArrayList<Article> as=new ArrayList<Article>();
				if(ids.length>0) {
					for(String i:ids) {
						Article a=articleDAO.findArticleById(Integer.parseInt(i));		
						if(a!=null && a.getTypeMedia().equalsIgnoreCase(typeMedia)) {
							as.add(a);
						}
					}
				}
				if(as.size()>0) {
					c.setChildren(as.size());
					c.setParent(new Categorie(c.getId(), c.getCode()));
					sCategories.add(c);
				}		
			}	
		}
		categorie.setArticles(assignation);
		categorie.setSousCategories(sCategories);
		return categorie;
	}
	public Categorie showArticleByCategorieAndTypeMedia(int idCategorie, String typeMedia){
		Categorie categorie=categorieDAO.findById(idCategorie);
		ArrayList<Assignation> assignation=assignationDAO.findArticleByTypeAndCategorie(typeMedia, idCategorie);
		ArrayList<Categorie> sCategoriesTemp=categorieDAO.findSubCategories(idCategorie);
		ArrayList<Categorie> sCategories=new ArrayList<Categorie>();
		for(Categorie c:sCategoriesTemp) {
			String childIds=c.getChildIds();
			if(childIds!=null && childIds.contains(";")) {
				String[] ids=childIds.split(";");
				ArrayList<Article> as=new ArrayList<Article>();
				if(ids.length>0) {
					for(String i:ids) {
						Article a=articleDAO.findArticleById(Integer.parseInt(i));		
						if(a!=null && a.getTypeMedia().equalsIgnoreCase(typeMedia)) {
							as.add(a);
						}
					}
				}
				if(as.size()>0) {
					c.setChildren(as.size());
					c.setParent(new Categorie(c.getId(), c.getCode()));
					sCategories.add(c);
				}		
			}	
		}
		categorie.setArticles(assignation);
		categorie.setSousCategories(sCategories);
		return categorie;
	}

	public Reponse saveCategorie(int id, String nom, String description, User user) {
		String code="";
		Categorie exist=categorieDAO.findByCode(code);
		Categorie categorie=new Categorie();
		
		
		categorie.setCategorie(nom);
		categorie.setCode(code);
		categorie.setDescription(description);
		
		categorie.setCreatedBy(user);
		categorie.setCreatedAt(new Date().getTime());
		if(exist!=null) {
			return new Reponse(-1, "EXIST", null);
		}
		Categorie parent=categorieDAO.findById(id);
		categorie.setParent(parent);
		if(parent==null) {
			code="CT_"+new Date().getTime();
			
		}else {
			code="ST_"+new Date().getTime();
			if(categorie.getParent().getAncestor()==0)
			categorie.setAncestor((int) categorie.getParent().getId());
			else
				categorie.setAncestor(categorie.getParent().getAncestor());
		}
		categorie.setCode(code);
		Categorie result=categorieDAO.save(categorie);
		return new Reponse(1, "SUCCESS", result);
	}
	
	public ArrayList<CategorieDTO> lesCategoriesDTO(){
		ArrayList<Categorie> categories=lesCategories();
		ArrayList<CategorieDTO> cdto=new ArrayList<CategorieDTO>();
		for(Categorie c:categories) {
			cdto.add(new CategorieDTO((int) c.getId(), c.getCode(), c.getCategorie()));
		}
		//return (ArrayList<Categorie>) categorieDAO.findAll();
		return cdto;
	}
	private  long copyFileUsingChannel(FileChannel sourceChannel, File dest) throws IOException {
	    FileChannel destChannel = null;
	    long v=0;
	    try {
	        destChannel = new FileOutputStream(dest).getChannel();
	        v=destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
	       }finally{
	           sourceChannel.close();
	           destChannel.close();
	   }
	    return v;
	}
	public Reponse creerBundle(ArrayList<Article> articles, Categorie categorie, User user, String description, String label) {
		ArrayList<Assignation> saved=new ArrayList<Assignation>();
		for(Article article:articles) {
			Assignation a=new Assignation();
			a.setActor(user);
			a.setArticle(article);
			a.setCategorie(categorie);
			a.setCreeLe(new Date().getTime());
			a.setDescription(description);
			a.setEtiquette(label);

			a.getCategorie().setPickine(article.getId());
			
			Assignation as=assignationDAO.save(a);
			if(as.getId()>0) {
				saved.add(as);
				
			}
		}
		if(saved.size()>0) {
			return new Reponse(1, "OK", saved);
		}
		return new Reponse(-1, "", null);
	}
	public Reponse creerBundle(ArrayList<Article> articles, Categorie categorie, User user, String description, String label, int prix, int totalMac, int periode) {
		ArrayList<Assignation> saved=new ArrayList<Assignation>();
		for(Article article:articles) {
			Assignation a=new Assignation();
			a.setActor(user);
			a.setArticle(article);
			a.setCategorie(categorie);
			a.setCreeLe(new Date().getTime());
			a.setDescription(description);
			a.setEtiquette(label);
			a.setPrix(prix);
			a.setPeriode(periode);
			a.setTotalMac(totalMac);
			a.getCategorie().setPickine(article.getId());
			Assignation as=assignationDAO.save(a);
			if(as.getId()>0) {
				saved.add(as);
				
			}
		}
		if(saved.size()>0) {
			return new Reponse(1, "OK", saved);
		}
		return new Reponse(-1, "", null);
	}
	public Reponse creerBundle(Article article, Categorie categorie, User user, String description, String label) {
	
			Assignation a=new Assignation();
			a.setActor(user);
			a.setArticle(article);
			a.setCategorie(categorie);
			a.setCreeLe(new Date().getTime());
			a.setDescription(description);
			a.setEtiquette(label);
			a.getCategorie().setPickine(article.getId());
			assignationDAO.save(a);
		return new Reponse(-1, "", null);
	}

	public Reponse uploadPdf(MultipartFile fichier, String titre, String contenu, String login, String categorie, String typeMedia) {
		//String prefix = fichier.getName().substring(fichier.getName().lastIndexOf("."));
	    File file = null;
	    String absolutePath = context.getRealPath("resources"+File.separator+"partenaires");
	    Categorie categ=categorieDAO.findbyName(categorie);
	    User editedBy=userDAO.findByLogin(login);
	    if(editedBy==null) {
	    	return new Reponse(-1, "user not exist", null);
	    }
	    if(categ==null) {
	    	return new Reponse(-1, "Category not exist", null);
	    }
	    String typeContent=fichier.getContentType();
	    String nom=fichier.getName();
	    long taille=fichier.getSize();
	    String nomTotal=fichier.getOriginalFilename();
	    String path=absolutePath+File.separator+login;
	    boolean created=false;
	    if(typeMedia.equalsIgnoreCase("AUDIO")){
	    	path+=File.separator+"AUDIOS";	
		    FileInputStream fis=null;
		    FileChannel fc=null;
		    FileChannel destChannel =null;
		    try {
				fis=(FileInputStream) fichier.getInputStream();
				fc=fis.getChannel();
				long size = fc.size();
				
			      File destination=new File("");
			      Path currentWorkingDir = Paths.get("").toAbsolutePath();
			      
			      new File(path).mkdirs();			      
			      long c=copyFileUsingChannel(fc, new File(path+File.separator+fichier.getOriginalFilename()));
		    }
		    catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    finally {
    				try {

    					fis.close();
    					
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
			}
		    /////////////////////////////////////// jusqu'ici ///////////////////////////////
	    	
	    }else {
	    	if(typeMedia.equalsIgnoreCase("BOOK")){
		    		    //////////////////////// enregistrement BOOK //////////////////
		    	    	path+=File.separator+"BOOKS";
		    		    FileInputStream fis=null;
		    		    FileChannel fc=null;
		    		    FileChannel destChannel =null;
		    		    try {
		    				fis=(FileInputStream) fichier.getInputStream();
		    				fc=fis.getChannel();
		    				long size = fc.size();
		    				
		    			      File destination=new File("");
		    			      new File(path).mkdirs();			      
		    			      long c=copyFileUsingChannel(fc, new File(path+File.separator+fichier.getOriginalFilename()));
		    		    }
		    		    catch (IOException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		    		    finally {
			    				try {

			    					fis.close();
			    					
			    				} catch (IOException e) {
			    					// TODO Auto-generated catch block
			    					e.printStackTrace();
			    				}
		    			}
		    		    /////////////////////////////////////// jusqu'ici ///////////////////////////////
	    	}
	    }
	    
	    ////////////////////***********enregistrement en base de données ////////////*****

	    Article article=new Article();
	    article.setContent(contenu);
	    article.setTitle(titre);
	    article.setPrix(0);
	    article.setTypeMedia(typeMedia);
	    article.setFichier(fichier.getOriginalFilename());
	    article.setTailleFichier(fichier.getSize());
	    article.setDevise("XAF");
	    article.setEditeLe(new Date().getTime());
	    article.setEditedBy(editedBy);
	    
	    	
	    	article.setEditedBy(editedBy);
	    	articleDAO.save(article);
	    	Fichier fichier_db=new Fichier();
	    	fichier_db.setArticle(article);
	    	fichier_db.setChemin(path);
	    	fichier_db.setContentType(typeContent);
	    	fichier_db.setThumbnail("");
	    	fichier_db.setThumbnail_format("");
	    	fichier_db.setThumbnail_size(0);
	    	fichier_db.setNom(fichier.getOriginalFilename());
	    	fichier_db.setTaille(fichier.getSize());
	    	fichierDAO.save(fichier_db);
	    	creerBundle(article, categ, editedBy, "", "");
	    /**************************************************/
		return new Reponse(1, "OK", null);
   }

	public Reponse uploadMedias(MultipartFile fichier, MultipartFile thumbnail,  String titre, String contenu, User editedBy, String categorie, String typeMedia) {
	    Categorie categ=categorieDAO.findbyName(categorie);
	    if(categ!=null && categ.getParent()!=null) {
	    	return new Reponse(-1, "ERROR", null);
	    }
		//String prefix = fichier.getName().substring(fichier.getName().lastIndexOf("."));
	    File file = null;
	    String vignette=thumbnail.getOriginalFilename();
	    long tailleVignette=thumbnail.getSize();
	    String nomVignette=thumbnail.getOriginalFilename();
	    //JsonArray json=new JsonParser().parse(optionsPaiement).getAsJsonArray();
	    //typeMedia="VIDEO";
	    String typeContent=fichier.getContentType();
	    String nom=fichier.getName();
	    long taille=fichier.getSize();
	    String nomTotal=fichier.getOriginalFilename();
	    String absolutePath = context.getRealPath("resources"+File.separator+"partenaires");
	    String path=absolutePath+File.separator+editedBy.getEmail();
	    boolean created=false;
	    if(typeMedia.equalsIgnoreCase("AUDIO")){
	    	path+=File.separator+"AUDIOS";	
	    }else {
	    	if(typeMedia.equalsIgnoreCase("BOOK")){
		    		    //////////////////////// enregistrement BOOK //////////////////
		    	    	path+=File.separator+"BOOKS";
		    		    FileInputStream fis=null;
		    		    FileChannel fc=null;
		    		    FileChannel destChannel =null;
		    		    try {
		    				fis=(FileInputStream) fichier.getInputStream();
		    				fc=fis.getChannel();
		    				long size = fc.size();
		    				
		    			      File destination=new File("");
		    			      new File(path).mkdirs();			      
		    			      long c=copyFileUsingChannel(fc, new File(path+File.separator+fichier.getOriginalFilename()));
		    		    }
		    		    catch (IOException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		    		    finally {
			    				try {
			    					destChannel.close();
			    					fc.close();
			    					fis.close();
			    					
			    				} catch (IOException e) {
			    					// TODO Auto-generated catch block
			    					e.printStackTrace();
			    				}
		    			}
		    		    /////////////////////////////////////// jusqu'ici ///////////////////////////////
	    	}else {
	    		System.err.println(typeMedia.toString().trim().equalsIgnoreCase("VIDEO"));
	    		if(typeMedia.equalsIgnoreCase("VIDEO")) {
	    		    ////////////////////////vignette ///////////////////
	    	    	path+=File.separator+"VIDEOS";
	  			      File f1=new File(path);
	  			      if(!f1.exists()) {
	  			    	  created=new File(path).mkdirs();
	  			      }
	    		    String[] tabs=nomTotal.split("\\.");
	    		    nomVignette=tabs[0]+".jpg";
	    		    FileInputStream fisThumb=null;
	    		    
	    		    try {
	    		    	//ByteArrayInputStream f=(ByteArrayInputStream) thumbnail.getInputStream();
	    		    	fisThumb=(FileInputStream) thumbnail.getInputStream();
	    		    	
	    				copyFileUsingChannel(fisThumb.getChannel(), new File(path+File.separator+nomVignette));
	    			} catch (IOException e1) {
	    				// TODO Auto-generated catch block
	    				e1.printStackTrace();
	    			}
	    		    /////////////////////////jusqu'ici //////////////////
	    		    //////////////////////// enregiistrement video //////////////////
	    		    FileInputStream fis=null;
	    		    FileChannel fc=null;
	    		    FileChannel destChannel =null;
	    		    try {
	    				fis=(FileInputStream) fichier.getInputStream();
	    				fc=fis.getChannel();
	    				long size = fc.size();
	    			      File destination=new File("");

	    			      System.err.println("=====<>======="+path);
	    			      destChannel= new FileOutputStream(new File(path+File.separator+fichier.getOriginalFilename())).getChannel();
	    			      long c=destChannel.transferFrom(fc, 0, size);
	    			      System.err.println("Nombre d'octets lus "+c);
	    			} catch (IOException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    		    finally {
	    				try {
	    					destChannel.close();
	    					fc.close();
	    					fis.close();
	    					
	    				} catch (IOException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}
	    			}
	    		    /////////////////////////////////////// jusqu'ici ///////////////////////////////

	    		}
	    	}
	    }
	    
	    ////////////////////***********enregistrement en base de données ////////////*****
	    Article article=null; 		
	    article=new Article();
	    article.setContent(contenu);
	    article.setTitle(titre);
	    article.setPrix(0);
	    article.setThumbnail(nomVignette);
	    article.setFichier(fichier.getOriginalFilename());
	    article.setDevise("XAF");
	    article.setEditeLe(new Date().getTime());
	    article.setEditedBy(editedBy);

	    article.setTypeMedia(typeMedia);
	    	article.setEditedBy(editedBy);
	    	articleDAO.save(article);
	    	
	    	Fichier fichier_db=new Fichier();
	    	fichier_db.setArticle(article);
	    	fichier_db.setChemin(path);
	    	fichier_db.setContentType(typeContent);
	    	fichier_db.setThumbnail(thumbnail.getOriginalFilename());
	    	fichier_db.setThumbnail_format("image/jpeg");
	    	fichier_db.setThumbnail_size(tailleVignette);
	    	fichier_db.setNom(fichier.getOriginalFilename());
	    	fichier_db.setTaille(fichier.getSize());
	    	
	    	fichierDAO.save(fichier_db);
		    if(categ!=null) {
		    	creerBundle(article, categ, editedBy, "", "");
		    }
	    /**************************************************/
		return new Reponse(1, "OK", article.getEditeLe());
   }	
	public CategorieDTO findCategorieById(long id){
		Categorie c=categorieDAO.findById(id);
		CategorieDTO cdto=null;
		if(c!=null) {
			ArrayList<Categorie> subCategories=categorieDAO.findSubCategories((int) c.getId());
			cdto=new CategorieDTO((int) c.getId(), c.getCode(), c.getCategorie());
			if(c.getParent()!=null)
			cdto.setParent(new CategorieDTO((int) c.getParent().getId(), c.getParent().getCode(), c.getParent().getCategorie()));
			ArrayList<CategorieDTO> categoriesDTO=new ArrayList<CategorieDTO>(); 
			for(Categorie ca:subCategories) {
				categoriesDTO.add(new CategorieDTO((int) ca.getId(), ca.getCode(), ca.getCategorie()));
			}
			cdto.setSubCategories(categoriesDTO);
			
			
		}
		return cdto;
	}

	
	public ArrayList<ArticleDTO> lesArticles(ArrayList<Long> ids) {
		ArrayList<Article> articles=(ArrayList<Article>) articleDAO.lesArticles(ids);
		 ArrayList<ArticleDTO> articlesDTO=new ArrayList<ArticleDTO>();
		for(Article a:articles) {
			ArrayList<PaiementsChoisis> pcs=paiementschoisisDAO.findPaiementsByArticle(a.getId());
		    String vignetteBase64 ="";
		    String typeMedia=a.getTypeMedia();
			  if(typeMedia.equalsIgnoreCase("VIDEO")) {
				      try {
				    	  Fichier fichier= fichierDAO.findFichierByArticle(a.getId());
				    	  if(fichier!=null) {
				    		  String nomVideo=fichier.getNom().split("\\.")[0];
				    		  File file=new File(fichier.getChemin()+File.separator+nomVideo+".jpg");   			  
				    		  vignetteBase64=Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));
				    		  fichier.setFormat(fichier.getThumbnail());
				    		 // fichier.setThumbnail(vignetteBase64);
				    		  fichier.setThumbnail(nomVideo+".jpg");
				    	  }
				    	  ArrayList<Fichier> fichiers=new ArrayList<Fichier>();  
				    	  fichiers.add(fichier);
				    	 a.setFichiers(fichiers);
						
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}					  
			  }		    
		
			if(pcs!=null && pcs.size()>0) {
				a.setModesPaiement(pcs);
			}	
			articlesDTO.add(ArticleDTO.toArticleDTO(a));
		}
		   Gson gson = new Gson();  
		   String m=gson.toJson(articlesDTO);
		   System.err.println(m);
		return articlesDTO;
	}
	public Article unArticle(long id) {
		Article a=articleDAO.findArticleById(id);
		ArrayList<PaiementsChoisis> pcs=paiementschoisisDAO.findPaiementsByArticle(a.getId());
	    String vignetteBase64 ="";
	    String typeMedia=a.getTypeMedia();
		  if(typeMedia.equalsIgnoreCase("VIDEO")) {
			      try {
			    	  Fichier fichier= fichierDAO.findFichierByArticle(a.getId());
			    	  if(fichier!=null) {
			    		  String nomVideo=fichier.getNom().split("\\.")[0];
			    		  File file=new File(fichier.getChemin()+File.separator+nomVideo+".jpg");   			  
			    		  vignetteBase64=Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));
			    		  fichier.setFormat(fichier.getThumbnail());
			    		  //fichier.setThumbnail(vignetteBase64);
			    		  fichier.setThumbnail(nomVideo+".jpg");
			    	  }
			    	  ArrayList<Fichier> fichiers=new ArrayList<Fichier>();  
			    	  fichiers.add(fichier);
			    	 a.setFichiers(fichiers);
					
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}					  
		  }		    
	
		if(pcs!=null && pcs.size()>0) {
			a.setModesPaiement(pcs);
		}	
		return a;
	}


	public ArrayList<Article> listArticles() {
		ArrayList<Article> articles=(ArrayList<Article>) articleDAO.findAll();
		for(Article a:articles) {
			ArrayList<PaiementsChoisis> pcs=paiementschoisisDAO.findPaiementsByArticle(a.getId());
		    String vignetteBase64 ="";
		    
		      try {
		    	  Fichier fichier= fichierDAO.findFichierByArticle(a.getId());
		    	  if(fichier!=null) {
		    		  File file=new File(fichier.getChemin()+File.separator+fichier.getThumbnail());
		    		  vignetteBase64=Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));
		    		  fichier.setFormat(fichier.getThumbnail());
		    		  fichier.setThumbnail(vignetteBase64);
		    	  }
		    	  ArrayList<Fichier> fichiers=new ArrayList<Fichier>();
		    	  
		    	  fichiers.add(fichier);
		    	 a.setFichiers(fichiers);
				
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}			
			if(pcs!=null && pcs.size()>0) {
				a.setModesPaiement(pcs);
			}
		
		}
		return articles;		
	}

}
