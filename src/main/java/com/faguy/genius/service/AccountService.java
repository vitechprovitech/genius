package com.faguy.genius.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.faguy.genius.entity.Action;
import com.faguy.genius.entity.Assignation;
import com.faguy.genius.entity.Pays;
import com.faguy.genius.entity.Profil;
import com.faguy.genius.entity.Token;
import com.faguy.genius.entity.User;
import com.faguy.genius.entity.Ville;
import com.faguy.genius.model.Reponse;
import com.faguy.genius.model.DAO.ActionRepository;
import com.faguy.genius.model.DAO.PaysRepository;
import com.faguy.genius.model.DAO.ProfilRepository;
import com.faguy.genius.model.DAO.TokenRepository;
import com.faguy.genius.model.DAO.TransfertAction;
import com.faguy.genius.model.DAO.TransfertActionRepository;
import com.faguy.genius.model.DAO.UserRepository;
import com.faguy.genius.model.DAO.VilleRepository;
import com.faguy.genius.model.DTO.PaysDTO;
import com.faguy.genius.model.DTO.ProfilDTO;
import com.faguy.genius.model.DTO.UserDTO;
import com.faguy.genius.model.DTO.VilleDTO;
import com.faguy.genius.util.Config;
import com.faguy.genius.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



@Service
public class AccountService {
	@Autowired
	private TokenRepository tokenDAO;
@Autowired private UserRepository userDAO;
@Autowired private ProfilRepository profilDAO;
@Autowired
private JavaMailSender mailSender;
@Autowired private PaysRepository paysDAO;
@Autowired private VilleRepository villeDAO;
@Autowired private TransfertActionRepository transfertDAO;
	
	public Token generateToken(User user) {
		   Token token=null;
		   try {
			 
			   ArrayList<Token> tokens=tokenDAO.findTokensByloginOrPhone(user.getEmail());
			   if(tokens.size()>0) {
				   tokenDAO.deleteInBatch(tokens);
			   }	   
			   token=new Token();
			   token.setToken(new Util().generateCode(100000, 999999));		
			   token.setUser(user);
			   token=tokenDAO.save(token);

		   }catch(Exception e) {
			   return null;
		   }
		   return token;
	}
	   public Reponse seConnecter( @RequestParam("email")  String email, @RequestParam("password")  String password, @RequestParam("imei") String imei) {  
		   try {
			   User user=userDAO.findbyLoginAndPassword(email, password);
			   if(user!=null) {
				   TransfertAction transfert=transfertDAO.findTransfertByMACAndEmailAndPassword(email, password, imei);
				   if(transfert!=null && transfert.getStatus()==0) {  
					   return new Reponse(101, "DEVICE", null);   // Le compte est transferé donc on doit supprimer ce compte
				   }
				   if(!user.getMAC().trim().equalsIgnoreCase(imei)) {
					   if(!imei.equalsIgnoreCase("WEB")) {
						   return new Reponse(6, "DEVICE", null);
					   }
				   }
				   if(user.getStatut()==0) {   // Si le compte n'est pas encore activé, alors voici la procédure
					   Token token=generateToken(user);
					   sendEmail(email, "Votre code d'activation est : "+token.getToken(), "Votre code d'activation Genius");
					   return new Reponse(1, "TOKEN",null);
				   }
				   UserDTO u=toUserDTO(user);
				   log("CONNECTED", "", user, null);
				   return new Reponse(1, "SUCCESS",u);
			   }else {
				   return new Reponse(-1, "ERROR", null);
			   }
			   
		   }catch(Exception e) {
			   return new Reponse(-1, "ERROR", null);
		   } 
	   }
	   @Autowired private ActionRepository actionDAO;
	   public int log(String action, String object, User user, Assignation article) {
		   Action a=new Action();
		   a.setAction(action);
		   a.setObjet(object);
		   a.setLastUpdate(new Date().getTime());
		   a.setUser(user);
		   a.setArticle(article);
		   Action res=actionDAO.save(a);
		  if(res!=null)
		   return 1;
		  else
			  return -1;
	   }

    public Reponse validateAccount(int code, String email,  String mac)
    {
        try
        {

            User user = userDAO.findByEmail(email);
            if (user != null)
            {
                Token token = tokenDAO.findByLoginOrPhone(email, code);
                if (token != null)
                {
             	
                        user.setStatut( Config.USER_ACTIF);
                        userDAO.save(user);
                        tokenDAO.delete(token);
                        
     				   UserDTO u1= new UserDTO();
     				   u1.setLogin(user.getLogin());
     				   u1.setName(user.getName());
     				  u1.setSurname(user.getSurname());
     				   u1.setProfil(new ProfilDTO(user.getProfil().getId(), user.getProfil().getProfil(), user.getProfil().getDetails()));
     				   u1.setEmail(user.getEmail());
     				   u1.setPhone(user.getPhone());
     				   u1.setBirthDate(user.getBirthDate().getTime());
     				   u1.setCity(new VilleDTO(user.getCity().getId(), user.getCity().getNom()));
     				   u1.setAddress(user.getAddress());
     				   u1.setMAC(user.getMAC());
     				   u1.setPassword(user.getPassword());
     				   u1.setPhoto(user.getPhoto());
     				   u1.setStatut(user.getStatut());
     				   u1.setVille(user.getCity().getNom());
     				   u1.setPays(user.getCity().getCountry().getNom());
     				   u1.setProfile(user.getProfil().getProfil());
     			//////////////////////////////////////////////////////	   
                        
                        return new Reponse(1, "SUCCESS", u1);                 		            
                }
                else
                {
                    return new Reponse(Config.TOKEN_NOT_EXIST, "Invalid token", null);
                }
            }
            else
            {
         	   return new Reponse(Config.USER_NOT_EXIST, "", null);
         
            }
        }
        catch(Exception e)
        {
     	   return new Reponse(Config.ERROR, "", null);
        }
    }

    public Reponse checkToken( int code, String email )
    {
        try
        {
            User u = userDAO.findByLogin(email);
            if (u != null)
            {
                Token token = tokenDAO.findByLogin(email, code);
                if (token != null)
                {
                        tokenDAO.delete(token);
                        return new Reponse(1, "SUCCESS", null);                 		   

                }
                else
                {
                    return new Reponse(Config.TOKEN_NOT_EXIST, "Invalid token", null);
                }
            }
            else
            {
         	   return new Reponse(Config.USER_NOT_EXIST, "", null);
         
            }
        }
        catch(Exception e)
        {
     	   return new Reponse(Config.ERROR, "", null);
        }
    }

	public Reponse generateCodeAndroid( String login, String MAC) {
		   User user=null;
		   try {
			   User exist=userDAO.findByEmail(login);
			   if(exist!=null) {
				   ArrayList<Token> tokens=tokenDAO.findTokensByloginOrPhone(login);
				   if(tokens.size()>0) {
					   tokenDAO.deleteInBatch(tokens);
				   }
				   
				   Token token=new Token();
				   token.setToken(new Util().generateCode(100000, 999999));	
				
				   token.setUser(exist);
				   Token t=tokenDAO.save(token);
				   boolean res=sendEmail(login, "Votre code d'activation est : "+token.getToken(), "Votre code d'activation Genius");
				   return new Reponse(1, "SUCCESS", null);   
				  
			   }else {
				   return new Reponse(-1, "NOT EXIST", null);
			   }
		   }catch(Exception e) {
			   return new Reponse(-4, "ERROR", null);
		   }
	}
	   public Reponse resetPassword(String login, String password,  String mac) {  
		   try {
			   User user=userDAO.findByLogin(login);
			   if(user==null)
				   return new Reponse(-2, "NOT EXIST", null);
			   else {
				   user.setPassword(password);
				   user.setStatut(Config.USER_ACTIF);
				   UserDTO u= toUserDTO(user);
				   userDAO.save(user);
				   if(!user.getMAC().trim().equalsIgnoreCase(mac)) {
					   return new Reponse(6, "DEVICE", null);
				   }
				   return new Reponse(1, "SUCCESS", u);
			   }
			   
		   }catch(Exception e) {
			   return new Reponse(-1, "ERROR", null);
		   }
		   
		   
		   
	   }

	   public Reponse creerCompte(String surname, String name, long birthDate_as_long, String address,String city, String country,String email, String phone, String password, String photo, String MAC) {  
		   try {
			   
		   }catch(Exception e) {
			   return new Reponse(-1, "ERROR", null);
		   }  
		   Date birthDate = new Date(birthDate_as_long);
		   User user=null;
		   try {
			   User exist=userDAO.findByLogin(email);
			   if(exist!=null) {
				   return new Reponse(-2, "EXIST", null);
			   }else {
				   Pays pays=paysDAO.findByName(country);
				   Ville ville=villeDAO.findByName(city);
				   Profil profil=profilDAO.findByName("CLIENT");
				   
				   if(ville==null ) {
					   return new Reponse(-2, "city not exist", null);
				   }
				   User PHONE_EXIST=userDAO.findByPhone(phone);
				   if(PHONE_EXIST!=null) {
					   return new Reponse(-4, "PHONE_EXIST", null);
				   }
				   if(pays==null) {
					   return new Reponse(-2, "Country not exist", null);
					  
				   }
					if(profil==null) {
						return new Reponse(-2, "Profil not exist", null);
					}
				   user=new User();
				   user.setBirthDate(new Date());
				   user.setName(name);
				   user.setSurname(surname);
				   user.setEmail(email);
				   user.setLogin(email);
				   user.setPhone(phone);
				   user.setMAC(MAC);
				   user.setStatut(0);
				   user.setAddress(address);
				   user.setPassword(password);
				   user.setProfil(profil);
				   user.setCity(ville);
				   user.setBirthDate(birthDate);
				   user.setPhoto(photo);
				   Token token=new Token();
				   token.setToken(new Util().generateCode(100000, 999999));
				   token.setDelai(new Date());
				 
				   User u= userDAO.save(user);

				  // u.setProfil(new Profil(u.getProfil().getProfil(), u.getProfil().getDetails()));
				   token.setUser(u);
				   Token t=tokenDAO.save(token);
				   sendEmail(email, "Votre code d'activation est : "+token.getToken(), "Votre code d'activation Genius");
				   
				   
			/////////////////////////////////////////////////////////	   
				   UserDTO u1= new UserDTO();
				   u1.setLogin(user.getLogin());
				   u1.setName(user.getName());
				   u1.setSurname(user.getSurname());
				   
				   u1.setProfil(new ProfilDTO(user.getProfil().getId(), user.getProfil().getProfil(), user.getProfil().getDetails()));
				   u1.setEmail(user.getEmail());
				   u1.setPhone(user.getPhone());
				   u1.setPhoto(photo);
				   u1.setBirthDate(user.getBirthDate().getTime());
				   u1.setCity(new VilleDTO(user.getCity().getId(), user.getCity().getNom()));
				   
				   u1.setAddress(user.getAddress());
				   u1.setMAC(user.getMAC());
				   u1.setPassword(user.getPassword());
				   u1.setStatut(user.getStatut());
				   u1.setVille(user.getCity().getNom());
				   u1.setPays(user.getCity().getCountry().getNom());
				   u1.setProfile(user.getProfil().getProfil());
			//////////////////////////////////////////////////////	   			   
				   return new Reponse(1, "OK", u1);
			   }			   
		   }catch(Exception e) {
			   return new Reponse(-1, "ERROR", null);
		   }
		   

	   }

	   public String listePaysString() {
		   ArrayList<Pays> pays=(ArrayList<Pays>) paysDAO.findAll();
		   ArrayList<PaysDTO> paysDTO=new ArrayList<PaysDTO>();
		   
		   for(Pays p:pays) {
			   p.addAll(villeDAO.findByIdPays(p.getId()));
			   ArrayList<Ville> villes=villeDAO.findByIdPays(p.getId());
			   ArrayList<VilleDTO> villesDTO=new ArrayList<VilleDTO>();
			   for(Ville v:villes){
				   villesDTO.add(new VilleDTO(v.getId(), v.getNom()));
			   }
			   paysDTO.add(new PaysDTO(p.getId(), p.getNom(), p.getCode(), p.getDrapeau(), p.getCodeIndicatif(), villesDTO));
		   }
		   Gson gson = new Gson();  
		   String m=gson.toJson(paysDTO);
		   return m;
	   }
	   public ArrayList<PaysDTO> listePays() {
		   ArrayList<Pays> pays=(ArrayList<Pays>) paysDAO.findAll();
		   ArrayList<PaysDTO> paysDTO=new ArrayList<PaysDTO>();
		   
		   for(Pays p:pays) {
			   p.addAll(villeDAO.findByIdPays(p.getId()));
			   ArrayList<Ville> villes=villeDAO.findByIdPays(p.getId());
			   ArrayList<VilleDTO> villesDTO=new ArrayList<VilleDTO>();
			   for(Ville v:villes){
				   villesDTO.add(new VilleDTO(v.getId(), v.getNom()));
			   }
			   paysDTO.add(new PaysDTO(p.getId(), p.getNom(), p.getCode(), p.getDrapeau(), p.getCodeIndicatif(), villesDTO));
		   }

		   return paysDTO;
	   }
	public UserDTO toUserDTO(User user) {
		   UserDTO u= new UserDTO();
		   u.setLogin(user.getLogin());
		   u.setName(user.getName());
		   u.setSurname(user.getSurname());
		   u.setProfil(new ProfilDTO(user.getProfil().getId(), user.getProfil().getProfil(), user.getProfil().getDetails()));
		   u.setEmail(user.getEmail());
		   u.setPhone(user.getPhone());
		   u.setBirthDate(user.getBirthDate().getTime());
		   u.setCity(new VilleDTO(user.getCity().getId(), user.getCity().getNom()));
		   u.setAddress(user.getAddress());
		   u.setMAC(user.getMAC());
		   u.setPassword(user.getPassword());
		   u.setStatut(user.getStatut());
		   u.setVille(user.getCity().getNom());
		   u.setPays(user.getCity().getCountry().getNom());
		   u.setProfile(user.getProfil().getProfil());
		   u.setPhoto(user.getPhoto());
		   return u;
	}
	   public  boolean isAccountDeletableAndroid(String email, String password, String mac) {  
		   try {
			   TransfertAction transfert=transfertDAO.findTransfertByMACAndEmailAndPassword(email, password, mac);
			   if(transfert!=null && transfert.getStatus()==0) {
				   
				   return true;
			   }else {
				   return false;  
			   }
		   }catch(Exception e) {
			   return false;
		   } 
	   }
	public User transfererCompte(User user, String newMAC) {
		TransfertAction transfert=new TransfertAction();
		transfert.setMAC1(user.getMAC());
		transfert.setUser(user);
		transfert.setStatus(0);
		transfert=transfertDAO.save(transfert);
		user.setMAC(newMAC);
		user=userDAO.save(user);
		return user;
	}
    public  boolean sendEmail(String email, String message, String titre) {
    	boolean result=false;
    	if(validateEmail(email)) {
	   	    SimpleMailMessage msg = new SimpleMailMessage();
	        msg.setTo(email);
	        msg.setSubject(titre);
	        msg.setText(message);
	        mailSender.send(msg);
	        result=true; 		
    	}
    	return result;
    }
	   public static  boolean validateEmail(String email) {  
		   String regex = "^(.+)@(.+)$";   
		   Pattern pattern = Pattern.compile(regex);
		   Matcher matcher = pattern.matcher(email); 
		   return matcher.matches();
	   }
	   public boolean validatePhone(String phone) {
		   String regex = "^\\d{10}$";   
		   Pattern pattern = Pattern.compile(regex);
		   Matcher matcher = pattern.matcher(phone); 
		   return matcher.matches();
	   }
}
