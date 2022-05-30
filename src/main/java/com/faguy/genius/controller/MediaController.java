package com.faguy.genius.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.naming.factory.SendMailFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.faguy.genius.entity.Achat;
import com.faguy.genius.entity.AchatCategorie;
import com.faguy.genius.entity.AchatProduit;
import com.faguy.genius.entity.Action;
import com.faguy.genius.entity.Article;
import com.faguy.genius.entity.Assignation;
import com.faguy.genius.entity.Categorie;
import com.faguy.genius.entity.Fichier;
import com.faguy.genius.entity.User;
import com.faguy.genius.model.Input;
import com.faguy.genius.model.Reponse;
import com.faguy.genius.model.DAO.AchatCategorieRepository;
import com.faguy.genius.model.DAO.AchatProduitRepository;
import com.faguy.genius.model.DAO.AchatRepository;
import com.faguy.genius.model.DAO.ActionRepository;
import com.faguy.genius.model.DAO.ArticleRepository;
import com.faguy.genius.model.DAO.AssignationRepository;
import com.faguy.genius.model.DAO.CategorieRepository;
import com.faguy.genius.model.DAO.FichierRepository;
import com.faguy.genius.model.DAO.ModePaiementtRepository;
import com.faguy.genius.model.DAO.PaiementsChoisisRepository;
import com.faguy.genius.model.DAO.TransfertAction;
import com.faguy.genius.model.DAO.TransfertActionRepository;
import com.faguy.genius.model.DAO.UserRepository;
import com.faguy.genius.model.DTO.AchatDTO;
import com.faguy.genius.model.DTO.ArticleDTO;
import com.faguy.genius.model.DTO.AssignationDTO;
import com.faguy.genius.model.DTO.CategorieDTO;
import com.faguy.genius.model.DTO.DataMailDTO;
import com.faguy.genius.model.DTO.UserDTO;
import com.faguy.genius.service.AccountService;
import com.faguy.genius.service.EmailService;
import com.faguy.genius.service.MailContentBuilder;
import com.faguy.genius.service.MediasService;
import com.faguy.genius.util.Util;
import com.google.gson.Gson;

import antlr.collections.List;

@RestController
public class MediaController {

	@Autowired
	private UserRepository userDAO;
	@Autowired
	private ArticleRepository articleDAO;
	@Autowired
	private CategorieRepository categorieDAO;
	@Autowired
	private ModePaiementtRepository modePaiementDAO;
	@Autowired
	private PaiementsChoisisRepository paiementschoisisDAO;
    @Autowired
    private JavaMailSender mailSender;
	@Autowired
	ServletContext context;
	@Autowired MediasService mediaService;
	@Autowired
	private FichierRepository fichierDAO;
	@Autowired
	private ActionRepository actionDAO;
	@Autowired private AchatRepository achatDAO;
	@Autowired private AchatProduitRepository achatProduitDAO;
	@Autowired private AchatCategorieRepository achatCategorieDAO;
	@Autowired private TransfertActionRepository transfertDAO;
	@Autowired private AssignationRepository assignationDAO;
	
	@RequestMapping(value = "/downloadMedia", method = RequestMethod.POST)
	   @ResponseBody 
		public ResponseEntity  downloadMedia(@RequestParam("login") String login,@RequestParam("password") String password, @RequestParam("mac") String mac, @RequestParam("nomFichier") String fichier, @RequestParam("id")long id, HttpServletResponse response) {
			User client=userDAO.findbyEmailAndPassword(login, password);
			Resource resource = null;
			if(client==null) {
					Fichier file=fichierDAO.findFichierByid(id);
					File f = new File(file.getChemin()+ fichier);
					if(f.exists()) {
						Path path = Paths.get(""+file.getChemin()+ fichier);
						
						try {
							resource = new UrlResource(path.toUri());
							
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
						
					}
					return ResponseEntity.ok()
							.contentType(MediaType.parseMediaType(file.getContentType()))
							.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
							.body(resource);
			}else {
				
				return ResponseEntity.ok().body("EMPTY");
			}
		
			
		}
	
	
	@RequestMapping(value = "/android/uploadPdfAudio", method = RequestMethod.POST)
	   @ResponseBody 
		public Reponse uploadPdf(@RequestParam("fichier") MultipartFile fichier,  @RequestParam("titre") String titre, @RequestParam("contenu")String contenu, @RequestParam("login")String login, @RequestParam("categorie")String categorie, @RequestParam("typeMedia")String typeMedia) {
			Reponse reponse=mediaService.uploadPdf(fichier, titre, contenu, login, categorie, typeMedia);
			return reponse;
	   }
	@RequestMapping(value = "/android/uploadMedias", method = RequestMethod.POST)
   @ResponseBody 
	public Reponse uploadMedias(@RequestParam("fichier") MultipartFile fichier, @RequestParam("thumbnail") MultipartFile thumbnail, @RequestParam("titre") String titre, @RequestParam("contenu")String contenu, @RequestParam("login")String login, @RequestParam("categorie")String categorie, @RequestParam("typeMedia")String typeMedia) {
		User user=null;
		user=userDAO.findByLogin(login);
		Reponse reponse=null;
		if(user==null) {
			return new Reponse(-1, "User not exist", null);
		}
		   TransfertAction transfert=transfertDAO.findTransfertByMACAndEmailAndPassword(login, user.getPassword(), user.getMAC());
		   if(transfert!=null && transfert.getStatus()==0) {  
			   return new Reponse(101, "DEVICE", null);   // Le compte est transferé donc on doit supprimer ce compte
		   }
		Categorie categ=categorieDAO.findbyName(categorie);
		switch(typeMedia) {
			case "VIDEO":
				reponse=mediaService.uploadMedias(fichier, thumbnail, titre, contenu, user, categ.getCategorie(), typeMedia);
				break; 
			case "AUDIO":
				reponse=mediaService.uploadPdf(fichier, titre, contenu, user.getEmail(), categ.getCategorie(), typeMedia);
				break;
			case "BOOK":
				reponse=mediaService.uploadPdf(fichier, titre, contenu, user.getEmail(), categ.getCategorie(), typeMedia);
				break;
		}
		return reponse;
	}
	@RequestMapping(value = "/calculer", method = RequestMethod.GET)
	   @ResponseBody 
		public int calcuuler(@RequestParam("a")  int a, @RequestParam("b") int b) {
		   return a+b;
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

	   
	   @PostMapping(value="/addAction")
	   @ResponseBody
	   public Reponse addAction(@RequestParam("email") String email, @RequestParam("idArticle") int idArticle, @RequestParam("lastUpdate") long lastUpdate,  @RequestParam("action") String action ) {
		   Action a=new Action();
		   try {
			   a.setAction(action);
			   a.setLastUpdate(new Date().getTime());
			   Assignation ass=null;
			   if(idArticle>0) {
				   ass=assignationDAO.findById(idArticle).get();
				   a.setArticle(ass);
			   }
			   User user=userDAO.findByEmail(email);
			   if(user!=null) {
				   a.setUser(user);
			   }
			   Action res=actionDAO.save(a);
			   if(res!=null)
			   return new Reponse(1, "OK", null);	
			   else
				   return new Reponse(-1, "ERROR", null);
	   		}catch(Exception e) {
	   			return new Reponse(-1, "ERROR", null);
	   		}

	   }
		@RequestMapping(value="/android/subscribedArticlesByCategorieAndType", method = RequestMethod.POST)
		private Reponse subscribedContentsByCategorie(@RequestParam("email")String email, @RequestParam("password")String password, @RequestParam("MAC")String MAC, @RequestParam("typeMedia")String typeMedia, @RequestParam("id")int id) {
			User user=null;
			   TransfertAction transfert=transfertDAO.findTransfertByMACAndEmailAndPassword(email, password, MAC);
			   if(transfert!=null && transfert.getStatus()==0) {  
				   return new Reponse(101, "DEVICE", null);   
			   }
			   Categorie categorie=null;
			//ArrayList<Article> articles=articleDAO.findArticlesByType(typeMedias);
			 categorie=mediaService.findPurchasesByUserAndCategorie(email, typeMedia, id);
				Gson json=new Gson();
				 String m=json.toJson( CategorieDTO.toDTO(categorie));
				 System.err.println(">>>>>>>> >>>> >>>> ="+m);
				return new Reponse(1, "OK", m);
		}
		@RequestMapping(value="/android/infosClientCategorieInfos", method = RequestMethod.POST)
		private Reponse showClientCategorieInfos(String email, String password, String MAC, String typeMedia, int id) {
			   TransfertAction transfert=transfertDAO.findTransfertByMACAndEmailAndPassword(email, password, MAC);
			   if(transfert!=null && transfert.getStatus()==0) {  
				   return new Reponse(101, "DEVICE", null);   // Le compte est transferé donc on doit supprimer ce compte
			   }
			//ArrayList<Article> articles=articleDAO.findArticlesByType(typeMedias);
			   Categorie categorie=null;
			   if(typeMedia!=null && !typeMedia.equalsIgnoreCase("")) {
				   categorie=mediaService.showArticleByCategorieAndTypeMedia(id, typeMedia);
			}else {
					categorie=mediaService.showArticleByCategorie(id);
				//ArrayList<Assignation> assignations=assignationDAO.findArticleClientByCategorie((int)id);
				//ArrayList<AssignationDTO> asdto=new ArrayList<AssignationDTO>();
				//System.err.println("-----> ^^^^^===> ..."+assignations.size());
				/*for(Assignation a:assignations) {
					asdto.add(AssignationDTO.toDTO(a));
				}*/
				Gson json=new Gson();
				 String m=json.toJson( CategorieDTO.toDTO(categorie));
				 System.err.println(">>>>>>>> >>>> >>>> ="+m);
				return new Reponse(1, "OK", m);
			}
			   
			return new Reponse(1, "OK", categorie);
		}
		@RequestMapping(value="/android/lesDerniers", method = RequestMethod.POST)
		private Reponse lesDerniers(String email, String password, String MAC) {
			TransfertAction transfert = transfertDAO.findTransfertByMACAndEmailAndPassword(email, password, MAC);
			if (transfert != null && transfert.getStatus() == 0) {
				return new Reponse(101, "DEVICE", null);
			}
			Categorie categorie = null;

			Gson json = new Gson();
			String m = json.toJson(CategorieDTO.toDTO(categorie));
			System.err.println(">>>>>>>> >>>> >>>> =" + m);
			return new Reponse(1, "OK", m);
		}		
		@RequestMapping(value="/android/subscribedRootCategories", method = RequestMethod.POST)
		private Reponse showSubscribedRootCategorieByTypeAnUser(String email, String password, String MAC, String typeMedias) {
			   TransfertAction transfert=transfertDAO.findTransfertByMACAndEmailAndPassword(email, password, MAC);
			   if(transfert!=null && transfert.getStatus()==0) {  
				   return new Reponse(101, "DEVICE", null);   // Le compte est transferé donc on doit supprimer ce compte
			   }			
			User user=userDAO.findbyEmailAndPassword(email, password);
			if(user==null) {
				return new Reponse(-1, "NOT EXIST", null);
			}
			ArrayList<CategorieDTO> categories=mediaService.findSubscribedRootCategorieByUser(user.getEmail(), typeMedias);
			Gson json=new Gson();
			String result=json.toJson(categories);
			return new Reponse(1, "OK", categories);		
		}	

		@RequestMapping(value="/android/client_categories", method = RequestMethod.POST)
		private Reponse showClientRootCategories(String email, String password, String MAC, String typeMedias) {
			   TransfertAction transfert=transfertDAO.findTransfertByMACAndEmailAndPassword(email, password, MAC);
			   if(transfert!=null && transfert.getStatus()==0) {  
				   return new Reponse(101, "DEVICE", null);   // Le compte est transferé donc on doit supprimer ce compte
			   }
			   System.err.println(" ===>  ... ===> ");
			ArrayList<Categorie> categories=new ArrayList<Categorie>();
			ArrayList<CategorieDTO> cdtos=new ArrayList<CategorieDTO>();
			try {
				categories=mediaService.findRootCategoriesByType(typeMedias);
				for(Categorie c:categories)
				cdtos.add(CategorieDTO.toDTO(c));
			}catch(Exception e) {	
				e.printStackTrace();
			}
			   Gson gson = new Gson();
			   String m=gson.toJson(cdtos);
			   System.err.println(" ===>  ... ===> "+m);
			   return new Reponse(1, "OK", cdtos);
		}
		
	   @PostMapping(value="/android/lesCategories")
	public Reponse  lesCategories(String email, String password, String MAC) {
			ArrayList<CategorieDTO> cdtos=mediaService.lesCategoriesDTO();
			   Gson gson = new Gson();
			   String m=gson.toJson(cdtos);
			return new Reponse(1, "OK", m);
			//return new ResponseEntity<>(1, "OK", cdtos);
	}
	@GetMapping(value="/android/lesCategoriesAndroid")
	public ArrayList<CategorieDTO> lesCategoriesAndroid() {
			ArrayList<CategorieDTO> cdtos=mediaService.lesCategoriesDTO();
			return cdtos;
	}

	   @PostMapping(value="/lesArticlesAndroid")
	public Reponse lesArticlesAndroid(@RequestParam("login") String login, @RequestParam("password") String password, @RequestParam("mac") String mac, @RequestParam("ids") ArrayList<Long> ids) {
		   TransfertAction transfert=transfertDAO.findTransfertByMACAndEmailAndPassword(login, password, mac);
		   if(transfert!=null && transfert.getStatus()==0) {  
			   return new Reponse(101, "DEVICE", null);   
		   }	
		   ArrayList<Assignation> assignations=new ArrayList<Assignation>();
			assignations=assignationDAO.findLastArticles();
		   Gson gson = new Gson();
		   String m=gson.toJson(assignations);
		   System.err.println("=======================>"+m);		
		return new Reponse(1, "OK", assignations);	
	}
	   @PostMapping(value="/android/recommendations")
	public Reponse lesArticlesRecommandés(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("MAC") String MAC) {
		   TransfertAction transfert=transfertDAO.findTransfertByMACAndEmailAndPassword(email, password, MAC);
		   if(transfert!=null && transfert.getStatus()==0) {  
			   return new Reponse(101, "DEVICE", null);   // Le compte est transferé donc on doit supprimer ce compte
		   }	
	
		   Categorie categorie=mediaService.recommandes();
		   CategorieDTO cdto=CategorieDTO.toDTO(categorie);
		   Gson gson = new Gson();
		   String m=gson.toJson(cdto);
		   System.err.println("=======================>"+m);		
		return new Reponse(1, "OK", m);	
	}
	   

	   
	   
	   
	   @PostMapping(value="/android/lesDerniersArticles")
	   @ResponseBody
		public Reponse lesArticlesAndroid(@RequestParam("email") String login,@RequestParam("password")  String password, @RequestParam("MAC") String MAC) {
		   System.err.println("=======================>");   
		   TransfertAction transfert=transfertDAO.findTransfertByMACAndEmailAndPassword(login, password, MAC);
		   if(transfert!=null && transfert.getStatus()==0) {  
			   return new Reponse(101, "DEVICE", null);   // Le compte est transferé donc on doit supprimer ce compte
		   }
			   ArrayList<Integer> idcs=assignationDAO.findRootCategoriesClient();
			   ArrayList<CategorieDTO> categories=new ArrayList<CategorieDTO>();
			   for(int id:idcs) {
				   Categorie c=categorieDAO.findById(id);
				   categories.add(CategorieDTO.toDTO(c));
				   
			   }
			   
				//assignations=assignationDAO.findLastArticles();	
			return new Reponse(1, "OK", categories);	
		}
	   @PostMapping(value="/android/lesIds")
	   public Reponse lesIds(@RequestParam("email") String email) {
		   try {
			   ArrayList<Long> ids=articleDAO.lesIds();
			   return new Reponse(1, "OK", ids);			   
		   }catch(Exception e) {
			   return new Reponse(-1, "ERROR", new ArrayList<String>());
		   }
	   }
	   @PostMapping(value="/article")
	   @ResponseBody
	   public Reponse getArticle(@RequestParam("email") String email, @RequestParam("id") long id) {

		   ArticleDTO dto=null;
		   try {
			   Article a=mediaService.unArticle(id);
			   dto=ArticleDTO.toArticleDTO(a);
			   Gson gson = new Gson();
			   String m=gson.toJson(dto);
			   System.err.println("=======================>"+m);
			   return new Reponse(1, "OK",  dto);
		   }catch(Exception e) {
			    return new Reponse(1, "OK",  dto);
		   }
	   }
	   private boolean checkMaxDevice(Achat achat, User user) {
		   boolean resultat=true;
		   String macs=achat.getMACs();
		   String[] tabMAC=null;
		   if(macs.trim().contains(";")) {
			   tabMAC=macs.split(";");
		   }
		   else {
			   if(!macs.trim().equalsIgnoreCase("")){
				   tabMAC= new String[1];
				   tabMAC[0]=macs;
			   }
		   }
		   
		   int MAX=achat.getTotalMAC();
		   if(macs.contains(user.getMAC())) {
			   return true;
		   }else {
			   if(tabMAC!=null && tabMAC.length>=MAX) { // atteinte du nombre max de device
				   return false;
			   }else {
				   macs+=";"+user.getMAC();
				   macs=macs.trim().substring(0, macs.length() - 1); 
				   achat.setMACs(macs);
				   achat.setMAC(user.getMAC());
				   achatDAO.save(achat);
				   return true;
			   }			   
		   }
	   }

	   @PostMapping(value="/android/validateProduct")
	   private Reponse validateProduct(@RequestParam("email")  String email,@RequestParam("password")  String password, @RequestParam("MAC")  String MAC,@RequestParam("code")  int code, @RequestParam("typeAchat")  String typeAchat, @RequestParam("id") int id) {
		   TransfertAction transfert=transfertDAO.findTransfertByMACAndEmailAndPassword(email, password, MAC);
		   if(transfert!=null && transfert.getStatus()==0) {  
			   return new Reponse(101, "DEVICE", null);   // Le compte est transferé donc on doit supprimer ce compte
		   }
		   User user=userDAO.findbyEmailAndPassword(email, password);
		   Reponse reponse=new Reponse(-1, "ERROR", null);
		   if(user==null) {
			   return new Reponse(-1, "NOT EXIST", null);
		   }
		   Achat achat=null;
		   AchatDTO adto=null;
		   boolean choice=false;
		   switch(typeAchat) {
		   case "A":
			   achat=achatProduitDAO.findAchatbyUserAndProduct(email, id, code);
			   if(achat==null) {
				   return new Reponse(-1, "INVALID", null);
			   }
			   choice=checkMaxDevice(achat, user);
			   if(!choice) {
				   return new Reponse(-1, "MAX", null);
			   }
			   adto=AchatDTO.toDTO(achat);
			   Gson gson=new Gson();
			   String result=gson.toJson(adto);
			   reponse= new Reponse(1, "OK", result);
			   break;
		   case "P":
			   achat=achatCategorieDAO.findAchatbyUserAndProduct(email, id, code);
			   if(achat==null) {
				   return new Reponse(-1, "INVALID", null);
			   }
			   choice=checkMaxDevice(achat, user);
			   if(!choice) {
				   return new Reponse(-1, "MAX", null);
			   }
			   adto=AchatDTO.toDTO(achat);
			   Gson json=new Gson();
			   String res=json.toJson(adto);
			   reponse= new Reponse(1, "OK", res);
			   break;
		   }
		   return reponse;
	   }
	   @Autowired EmailService emailService;
	   private Reponse acheterBundle(int id /*Id de la categorie auquel appartient le produit*/, User user, String macs) {
	
		   AchatCategorie achat=new AchatCategorie();
		   Categorie c=categorieDAO.findById(id);
		   if(c!=null) {
			   ArrayList<Assignation> ac=assignationDAO.findArticleByCategorie(c.getId());
			   ((AchatCategorie) achat).setBundle(c);
			   achat.setClient(user);
			   achat.setCommentaire("");
			   achat.setMAC(user.getMAC());
			   achat.setTotalMAC(ac.get(0).getTotalMac());
			   achat.setPeriode(ac.get(0).getPeriode());
			   achat.setMACs(macs);
			   long d=new Date().getTime();
			   long d1=24*60*60*1000*ac.get(0).getPeriode();
			   long expirationDate=d+d1;
			   achat.setDateExpiration(expirationDate);	
			   achat.setProductKey(new Util().generateCode(100000, 999999));
			   int prix=ac.get(0).getPrix();
			   achat.setEditeLe(new Date().getTime());
			   Achat result=achatDAO.save(achat);
			   if(prix>0) {     // envoyer le product key au client par mail si le produit est payant
				   //sendEmailText(email, "Following your subscription to the product whose id is "+((AchatCategorie) achat).getBundle().getId()+" and bundle's name "+((AchatCategorie) achat).getBundle().getCategorie()+", your activation code is "+achat.getProductKey(), "Genius Product key ");						   
				   DataMailDTO data=new DataMailDTO();
				   data.setActivationCode(achat.getProductKey());
				   data.setDateSubscription(achat.getEditeLe()+"");
				   data.setName(user.getName());
				   data.setPrix(prix);
				   data.setType("BUNDLE");
				   int remaining=0;
				   if(macs.contains(";")) {
					   String[] tabMacs=macs.split(";");
					   remaining=achat.getTotalMAC()-tabMacs.length;
				   }else {
					   remaining=achat.getTotalMAC()-1;
				   }
				   data.setProductName(ac.get(0).getCategorie().getCategorie());
				   data.setRemainingMaxDevices(remaining);
				   try {
					   emailService.sendMail(data, user.getEmail(), "PRODUCT KEY");
				   } catch (MessagingException e1) {
					   e1.printStackTrace();
				   	 }
				   return new Reponse(1, "CHECK", null);
			   }else {     // Ne pas envoyer le product key par mail au client si le produit est gratuit
				   Gson json=new Gson();
				   String res =json.toJson(AchatDTO.toDTO(result));
				   return new Reponse(1, "FREE", res);
			   }
		   }
		   return new Reponse(-1, "ERROR", null);
	   }
	   private Reponse acheterArticle(User  user, int id /*id de la categorie*/, String macs) {
		   AchatProduit achat=new AchatProduit();    // creation d'un new achat
		   Assignation article=assignationDAO.findById(id).get(); // recuperation du produit associé
		   if(article!=null)
		   achat.setProduit(article); // liaison du produit a l'achat
		   achat.setClient(user);    // liaison du user au nouvel achat
		   achat.setCommentaire(""); // 
		   long d=new Date().getTime();    // date aujourd'hui
		   long d1=24*60*60*1000*article.getArticle().getPeriode();  // periode de validité
		   long expirationDate=d+d1;  // date d'expiration
		   long tempsRestant=expirationDate-d; // temps restant d'utilisation du produit.
		   tempsRestant=tempsRestant/1000/60/60/24;
		   achat.setDateExpiration(expirationDate);	 // date 'expiration de l'achat du produit
		   achat.setMAC(user.getMAC());  // Association du MAC device du client à l'achat
		   achat.setMACs(macs);
		   
		   achat.setPeriode((int) article.getArticle().getPeriode());
		   achat.setTotalMAC(article.getArticle().getTotalMAC());
		   achat.setProductKey(new Util().generateCode(100000, 999999));
		   achat.setEditeLe(new Date().getTime());
		   Achat result=achatDAO.save(achat);
		   int prix=(int) article.getArticle().getPrix();
		   if(prix>0) {

			   DataMailDTO data=new DataMailDTO();
			   data.setActivationCode(achat.getProductKey());
			   data.setDateSubscription(new Date(achat.getEditeLe())+"");
			   data.setPrix(prix);
			   data.setType("ARTICLE");
			   data.setRemainingPeriod((int) tempsRestant);
			   data.setName(user.getName());
			   int remaining=0;
			   if(macs.contains(";")) {
				   String[] tabMacs=macs.split(";");
				   remaining=achat.getTotalMAC()-tabMacs.length;
			   }else {
				   remaining=achat.getTotalMAC()-1;
			   }
			   data.setProductName(article.getArticle().getTitle());
			   data.setRemainingMaxDevices(remaining);
			   //sendEmailText(user.getEmail(), mailService.build(getMailFormatMessage()), "PRODUCT KEY");
			   try {
				   emailService.sendMail(data, user.getEmail(), "PRODUCT KEY");
			   } catch (MessagingException e1) {
				// TODO Auto-generated catch block
				   e1.printStackTrace();
			   }
			   return new Reponse(1, "CHECK", null);
		   }else {
			   Gson json=new Gson();
			   String res =json.toJson(AchatDTO.toDTO(result));
			   return new Reponse(1, "FREE", res);
		   }
	   }
	   /*
	    * Le service acheter permet d'enregistrer les informations liées à l'achat d'un produit genius en ligne.
	    * 
	    * */
	   @RequestMapping(value="/android/acheter", method = RequestMethod.POST)
	   public Reponse acheter(String email, String password, int id, String typeAchat, int repeat) {    // La variable repeat est un indicateur de resouscription. Elle vaut 1 pour la resouscription et 0 pour rien du tout.

		   User user=userDAO.findbyEmailAndPassword(email, password);    // recuperer le client ou partnaire dans le serveur pour voir s'il l'existe
		   if(user==null)   // Si le user n'existe pas alors retourner une reponse de non existance
			   return new Reponse(-1, "NOT EXIST", null);
		   TransfertAction transfert=transfertDAO.findTransfertByMACAndEmailAndPassword(email, password, user.getMAC());
		   if(transfert!=null && transfert.getStatus()==0) {  
			   return new Reponse(101, "DEVICE", null);   // Le compte est transferé donc on doit supprimer ce compte
		   }
		   Achat achat=null;	
		   Achat exist=null;
		   String macs="";
		   macs+=user.getMAC()+";";
		   macs=macs.trim().substring(0, macs.length() - 1);
		   switch(typeAchat) {   // Distinguer les type d'achat
		   case "P":   // s'il s'agit de l'achat d'un package alors voici ci-dessous le workflow
			   if(repeat>0) { //Cas de re-achat d'un package
				   return acheterBundle(id, user, macs);
			   }
			   java.util.List<AchatCategorie> ab1=achatCategorieDAO.findAll().stream().filter(a->a.getClient().getEmail().equalsIgnoreCase(email)).filter(e->e.getBundle().getId()==id).sorted(Comparator.comparingInt(AchatCategorie::getId).reversed()).collect(Collectors.toList());   // Recuperer tous les achats effectué  par le client sur le bundle
			   if(ab1.size()<=0) {    // si pour ce client, il nya aucun achat, alors il faut en creer
				   achat=new AchatCategorie();
				   Categorie c=categorieDAO.findById(id);
				   return acheterBundle(id, user, macs);
			   }else {    // si le client a réalisé des achats sur ce produit alors
				  
				   Categorie c=categorieDAO.findById(id);
				   ArrayList<Assignation> ac=assignationDAO.findArticleByCategorie(c.getId());
				   achat=ab1.get(ab1.size()-1);    // On considère le dernière achat réalisé
				   boolean choice=checkMaxDevice(achat, user);
				   if(!choice) {
					   
					   return new Reponse(111, "MAX", null);
				   }
				   int prix=((AchatCategorie) achat).getBundle().getPrix();
				   long tempsRestant=achat.getDateExpiration()-new Date().getTime();
				   if(tempsRestant<0) {   // Si le temps restant est négatif, alors le bundle est expiré. Il faut alors renouveler l'abonnement
					   return new Reponse(1, "EXPIRED", null);   
				   }
				   if(prix>0) {
					   DataMailDTO data=new DataMailDTO();
					   data.setActivationCode(achat.getProductKey());
					   data.setDateSubscription(achat.getEditeLe()+"");
					   data.setName(user.getName());
					   data.setPrix(prix);
					   data.setType("BUNDLE");
					   tempsRestant=tempsRestant/1000/60/60/24;
					   data.setRemainingPeriod((int) tempsRestant);
					   int remaining=0;
					   if(macs.contains(";")) {
						   String[] tabMacs=macs.split(";");
						   remaining=achat.getTotalMAC()-tabMacs.length;
					   }else {
						   remaining=achat.getTotalMAC()-1;
					   }
					   data.setProductName(ac.get(0).getCategorie().getCategorie());
					   data.setRemainingMaxDevices(remaining);
					   //sendEmailText(email, mailService.build(getMailFormatMessage()), "PRODUCT KEY");
					   try {
						   emailService.sendMail(data, email, "PRODUCT KEY");
					   } catch (MessagingException e1) {
						// TODO Auto-generated catch block
						   e1.printStackTrace();
					   }

				   }else {
					   Gson json=new Gson();
					   String res =json.toJson(AchatDTO.toDTO(achat));
					   return new Reponse(1, "FREE", res);
				   }

			   }
			   break;
		   case "A":
			   if(repeat>0) {    // cas de ré-achat d'un produit 
				   return acheterArticle(user, id, macs);
			   }
			   java.util.List<AchatProduit> ab=achatProduitDAO.findAll().stream().filter(a->a.getClient().getEmail().equalsIgnoreCase(email)).filter(e->e.getProduit().getId()==id).sorted(Comparator.comparingInt(AchatProduit::getId).reversed()).collect(Collectors.toList());
			   if(ab.size()<=0) { //Le produit article  n'est pas encore acheté
				   achat=new AchatProduit();    // creation d'un new achat
				   
				   return acheterArticle(user, id, macs);
			   }else {   // Le client a déjà acheté ce produit
				   achat=ab.get(0);
				   int prix=(int) ((AchatProduit) achat).getProduit().getArticle().getPrix();
				   long tempsRestant=achat.getDateExpiration()-new Date().getTime();
				   tempsRestant=tempsRestant/1000/60/60/24;
				   if(tempsRestant<0) {   // Si le temps restant est négatif, alors l'article est expirée. Il faut alors renouveler l'abonnement
					   return new Reponse(1, "EXPIRED", null);   
				   }
				   if(achat==null) {
					   return new Reponse(-1, "INVALID", null);
				   }
				   boolean choice=checkMaxDevice(achat, user);
				   if(!choice) {
					   return new Reponse(111, "MAX", null);
				   }
				   if(prix>0) {
					   DataMailDTO data=new DataMailDTO();
					   data.setActivationCode(achat.getProductKey());
					   data.setDateSubscription(achat.getEditeLe()+"");
					   data.setPrix(prix);
					   data.setName(user.getName());
					   data.setRemainingPeriod((int) tempsRestant);
					   data.setType("Article");
					   int remaining=0;
					   if(macs.contains(";")) {
						   String[] tabMacs=macs.split(";");
						   remaining=achat.getTotalMAC()-tabMacs.length;
					   }else {
						   remaining=achat.getTotalMAC()-1;
					   }
					   data.setProductName(((AchatProduit) achat).getProduit().getArticle().getTitle());
					   data.setRemainingMaxDevices(remaining);
					   data.setRemainingPeriod((int) tempsRestant);
					   try {
						   emailService.sendMail(data, email, "PRODUCT KEY");
					   } catch (MessagingException e1) {
						// TODO Auto-generated catch block
						   e1.printStackTrace();
					   }
				   }else {
					   Gson json=new Gson();
					   String res =json.toJson(AchatDTO.toDTO(achat));
					   return new Reponse(1, "FREE", res);
				   }
			   }
			   break;
		   }

		   return new Reponse(1, "CHECK", null);
	   }
	   
		
		@Async
		public void sendHtmlMail(String to, String subject, String templateName, String corps) throws MessagingException {
			/*
			 * MimeMessage mail = javaMailSender.createMimeMessage();
			 * 
			 * MimeMessageHelper helper = new MimeMessageHelper(mail, true); Context
			 * contexte = new Context(); contexte.setVariable("message", corps); String body
			 * = templateEngine.process(templateName, contexte); try {
			 * helper.setFrom(mailProperties.getProperties().get("from"),mailProperties.
			 * getProperties().get("personal")); } catch (UnsupportedEncodingException e) {
			 * e.printStackTrace(); } helper.setTo(to); helper.setSubject(subject);
			 * helper.setText(body, true); javaMailSender.send(mail);
			 */
	        Context context = new Context();
	        context.setVariable("message", "<strong>Cool</strong>");

	        String process = templateEngine.process("mailTemplate", context);
	        javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
	        helper.setSubject("PRODUCT KEY " );
	        helper.setText(process, true);
	        helper.setTo(to);
	        javaMailSender.send(mimeMessage);
	     

		}
		@Autowired
		private TemplateEngine templateEngine;
		@Autowired
		MailProperties  mailProperties;
		@Autowired	 
		private JavaMailSender javaMailSender;
	    public  boolean sendEmailText(String email, String message, String titre) {
	    	boolean result=false;
	    	if(Util.validateEmail(email)) {
		   	    SimpleMailMessage msg = new SimpleMailMessage();
		        msg.setTo(email);
		        msg.setSubject(titre);
		        msg.setText(message);
		       
		        mailSender.send(msg);
		        result=true; 		
	    	}
	    	return result;
	    }  
	    @Autowired MailContentBuilder mailService;
		@RequestMapping(value="/createCategorie", method = RequestMethod.POST)
		public Reponse addCategories(@RequestParam("login") String email, ArrayList<CategorieDTO> categories) {
			int count=0;
			User user=userDAO.findByEmail(email);
			if(user==null)
				return new Reponse(-1, "User not exist", null);
			for(CategorieDTO c:categories) {
				Categorie categ=new Categorie();
				categ.setCategorie(c.getCategorie());
				categ.setCode(c.getCode());
				categ.setCreatedBy(user);
				categ.setCreatedAt(new Date().getTime());
				CategorieDTO parentDTO=c.getParent();
				if(parentDTO!=null) {
					Categorie parent=categorieDAO.findByCode(parentDTO.getCode());
					if(parent!=null)
						categ.setParent(parent);
									
				}
				Categorie cc=categorieDAO.save(categ);	
				if(cc!=null)
					count++;
			}
			if(count>0)
			return new Reponse(1, "OK", null);
			else
				return new Reponse(-1, "An error occured", null);
		}
	   

		@Bean
		public RestTemplate restTemplate(RestTemplateBuilder builder) {
		   // Do any additional configuration here
		   return builder.build();
		}
		@Autowired
		private RestTemplate restTemplate;
		@PostMapping("/download/large/file")
		public ResponseEntity<String> downloadFile(@RequestBody Input input) {
			// Optional Accept header
			RequestCallback requestCallback = request -> request.getHeaders()
					.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));

			// Streams the response instead of loading it all in memory
			ResponseExtractor<Void> responseExtractor = response -> {
				String filename = input.getDownloadUrl().substring(input.getDownloadUrl().lastIndexOf("/") + 1);
				System.out.println("filename: " + filename);

				Path path = Paths
						.get(input.getDownloadPath() == null ? "/" + filename : input.getDownloadPath() + "/" + filename);

				Files.copy(response.getBody(), path);

				return null;
			};

			// URL - https://speed.hetzner.de/100MB.bin, https://speed.hetzner.de/10GB.bin
			restTemplate.execute(URI.create(input.getDownloadUrl()), HttpMethod.GET, requestCallback, responseExtractor);

			return new ResponseEntity<String>("File Downloaded Successfully", HttpStatus.OK);
		}
}
