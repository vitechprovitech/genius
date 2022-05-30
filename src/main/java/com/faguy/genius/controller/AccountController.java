package com.faguy.genius.controller;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.faguy.genius.entity.Pays;
import com.faguy.genius.entity.Profil;
import com.faguy.genius.entity.Token;
import com.faguy.genius.entity.User;
import com.faguy.genius.entity.Ville;
import com.faguy.genius.model.Reponse;
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
import com.faguy.genius.service.AccountService;
import com.faguy.genius.util.Config;
import com.faguy.genius.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import antlr.collections.List;

@RestController
public class AccountController {

	@Autowired
	private UserRepository userDAO;

	@Autowired
	private ProfilRepository profilDAO;
    @Autowired
    private JavaMailSender mailSender;
	@Autowired
	private PaysRepository paysDAO;	
	@Autowired
	private VilleRepository villeDAO;	
	@Autowired
	private TokenRepository tokenDAO;
	@Autowired
	private TransfertActionRepository transfertDAO;
	@Autowired AccountService accountService;
	
	   @PostMapping(value="/afficher")
	   @ResponseBody
	    public String test(int b) {
	
	    	return b+"";
	    }
	 

	   @PostMapping(value="/supprimerCompteAndroid")
	   public Reponse supprimerCompte( @RequestParam("email")  String email, @RequestParam("password")  String password, @RequestParam("mac") String mac) {  
		   try {
			   TransfertAction transfert=transfertDAO.findTransfertByMACAndEmailAndPassword(email, password, mac);
			   if(transfert!=null) {
				   transfert.setStatus(1);
				   transfertDAO.save(transfert);
				   return new Reponse(1, "SUCCESS", null);
			   }else {
				   return new Reponse(-1, "ERROR", null);   
			   }
		   }catch(Exception e) {
			   return new Reponse(-1, "ERROR", null);
		   } 
	   }
	   @PostMapping(value="/transfertCompteAndroid")
	   public Reponse transfertCompte( @RequestParam("email")  String email, @RequestParam("password")  String password, @RequestParam("imei") String imei) {  
		   try {
			   User user=userDAO.findbyEmailAndPassword(email, password);

			   if(user!=null) {
				   user=accountService.transfererCompte(user, imei);			   
				   if(user.getStatut()==0) {   // Si le compte n'est pas encore activé, alors voici la procédure
					   Token token=accountService.generateToken(user);
					   sendEmail(email, "Votre code d'activation est : "+token.getToken(), "Votre code d'activation Genius");
					   return new Reponse(1, "TOKEN",null);
				   }
				   UserDTO u=accountService.toUserDTO(user);
				   return new Reponse(1, "SUCCESS",u);
			   }else {
				   return new Reponse(-1, "ERROR", null);
			   }
		   }catch(Exception e) {
			   return new Reponse(-1, "ERROR", null);
		   } 
	   }
	   @PostMapping(value="/seConnecter")
	   public Reponse seConnecter( @RequestParam("email")  String email, @RequestParam("password")  String password, @RequestParam("imei") String imei) {  
		   	return accountService.seConnecter(email, password, imei);
	   }
	   
	   @PostMapping(value="/sAuthentifier")
	   @ResponseBody
	   public Reponse seConnecter( @RequestBody String requete) {  
		   JsonObject jsonObject = new JsonParser().parse(requete).getAsJsonObject();
		   System.out.println("------> email"+jsonObject.get("login"));
		   String email=jsonObject.get("login").getAsString();
		   String password=jsonObject.get("password").getAsString();
		   
		   User user=userDAO.findbyEmailAndPassword(email, password);
		   if(user!=null) {
			   if(user.getStatut()==0) {
				   Token token=accountService.generateToken(user);
				   sendEmail(email, "Votre code d'activation est : "+token.getToken(), "Votre code d'activation Genius");
				   return new Reponse(1, "TOKEN",null);
			   }
			   UserDTO u= new UserDTO();
			   u.setLogin(email);
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
			   return new Reponse(1, "SUCCESS",u);
		   }else {		   
			   return new Reponse(-1, "ERROR", null);
		   }
	   }
	   

	    public static String getMd5(String input)
	    {
	        try {
	  
	            // Static getInstance method is called with hashing MD5
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            // digest() method is called to calculate message digest
	            //  of an input digest() return array of byte
	            byte[] messageDigest = md.digest(input.getBytes());
	  
	            // Convert byte array into signum representation
	            BigInteger no = new BigInteger(1, messageDigest);
	  
	            // Convert message digest into hex value
	            String hashtext = no.toString(16);
	            while (hashtext.length() < 32) {
	                hashtext = "0" + hashtext;
	            }
	            return hashtext;
	        }
	  
	        // For specifying wrong message digest algorithms
	        catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException(e);
	        }
	    }
	    public boolean sendEmail(String email, String message, String titre) {
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

		   @PostMapping(value="/resetPassword")
		   @ResponseBody 
		   public Reponse resetPassword(@RequestBody String requete) {  
			   JsonObject jsonObject = new JsonParser().parse(requete).getAsJsonObject();
			   String password=jsonObject.get("password").getAsString();
			   String login=jsonObject.get("login").getAsString();
			   User user=userDAO.findByEmail(login);
			   if(user==null)
			   return new Reponse(-2, "NOT EXIST", null);
			   else {
				   user.setPassword(password);
				   user.setStatut(Config.USER_ACTIF);
				   UserDTO u= new UserDTO();
				   u.setLogin(user.getLogin());
				   u.setSurname(user.getSurname());
				   u.setName(user.getName());
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
				   userDAO.save(user);
				   return new Reponse(1, "SUCCESS", u);
			   }
		   }
		   @PostMapping(value="/resetPasswordAndroid")
		   @ResponseBody 
		   public Reponse resetPassword(@RequestParam("login") String login, @RequestParam("password") String password, @RequestParam("mac") String mac) {  
			   	return accountService.resetPassword(login, password, mac);
		   }
			@RequestMapping(value="/allUsers", method = RequestMethod.GET)
			public String allUsers(@RequestParam("login") String email) {
				User user=userDAO.findByEmail(email);
				ArrayList<User> users=new ArrayList<User>();
				if(user.getProfil().getProfil()=="ADMIN") {
						users=(ArrayList<User>) userDAO.findAll();	
					   Gson gson = new Gson();
					   String m=gson.toJson(users);
					   System.err.println(m);
					   return m;
				}else {
					   Gson gson = new Gson();
					   String m=gson.toJson(users);			
					return m;
				}
			}
			
			@RequestMapping(value="/updateUser", method = RequestMethod.POST)
			public Reponse updateUser(@RequestParam("login") UserDTO user) {
				User ancien=userDAO.findByEmail(user.getEmail());
				if(ancien!=null) {
					ancien.setAddress(user.getAddress());
					ancien.setName(user.getName());
					ancien.setSurname(user.getSurname());
					ancien.setAddress(user.getAddress());
					ancien.setBirthDate(new Date(user.getBirthDate()));
					Ville v=villeDAO.findByName(user.getVille());
					if(v!=null) {
						ancien.setCity(v);
					}
					User r=userDAO.save(ancien);
					return new Reponse(1, "SUCCESS", null);
				}else {
					return new Reponse(-1, "User not exist", null);
				}
				
			}  
			
			@RequestMapping(value="/switchProfile", method = RequestMethod.POST)
			public Reponse switchProfile(@RequestParam("login") String email, @RequestParam("login") String emailAdmin,  @RequestParam("profile") String profile) {

				Profil profil=profilDAO.findByName(profile);
				User admin=userDAO.findByEmail(emailAdmin);
				if(admin==null)
					return new Reponse(-1, "NOT EXIST", null);
				if(profil==null) {
					return new Reponse(-1, "NOT EXIST", null);
				}
				User user =userDAO.findByEmail(email);
				if(user==null) {
					return new Reponse(-1, "User not exist", null);
				}
				user.setProfil(profil);
				userDAO.save(user);
				return new Reponse(1, "SUCCESS", null);				
			}   
	   @PostMapping(value="/updateCompteAndroid")
	   @ResponseBody 
	   public Reponse updateUnCompte(@RequestParam("prenom") String surname, @RequestParam("nom") String name, @RequestParam("birthDate") long birthDate_as_long, @RequestParam("address") String address,@RequestParam("city") String city, @RequestParam("country") String country, @RequestParam("email")String email, String phone,@RequestParam("password") String password, @RequestParam("MAC") String MAC, @RequestParam("photo") String photo) {  
		   
			User ancien=userDAO.findByEmail(email);
			if(ancien!=null) {
				ancien.setAddress(address);
				ancien.setName(name);
				ancien.setSurname(surname);
				ancien.setBirthDate(new Date(birthDate_as_long));
				Ville v=villeDAO.findByName(city);
				Pays p=paysDAO.findByName(country);
				if(v!=null) {
					ancien.setCity(v);
				}
				ancien.setPhoto(photo);
				UserDTO udto=UserDTO.toDTO(ancien);
				Gson gson=new Gson();
				String m=gson.toJson(udto);
				User r=userDAO.save(ancien);
				return new Reponse(1, "SUCCESS", udto );
			}else {
				return new Reponse(-1, "User not exist", null);
			}
			
	   }

	   
	   @PostMapping(value="/creerUnCompte")
	   @ResponseBody 
	   public Reponse creerCompte(@RequestBody String requete) {  
		   JsonObject jsonObject = new JsonParser().parse(requete).getAsJsonObject();
		   String name=jsonObject.get("name").getAsString();
		   long birthDate_as_long=jsonObject.get("birthDate").getAsLong(); 
		   String address=jsonObject.get("address").getAsString();
		   String city=jsonObject.get("city").getAsString();
		   String country=jsonObject.get("country").getAsString();
		   String email=jsonObject.get("email").getAsString(); 
		   String phone=jsonObject.get("phone").getAsString();
		   String password=jsonObject.get("password").getAsString(); 
		   String surname=jsonObject.get("surname").getAsString(); 
		   String MAC=jsonObject.get("MAC").getAsString();
		   Date birthDate = new Date(birthDate_as_long);
		   User user=null;
		   try {
			   User exist=userDAO.findByEmail(email);
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
				   user.setPhone(phone);
				   user.setMAC(MAC);
				   user.setStatut(0);
				   user.setAddress(address);
				   user.setPassword(password);
				   user.setProfil(profil);
				   user.setCity(ville);
				   user.setBirthDate(birthDate);
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
				   u1.setSurname(user.getSurname());
				   u1.setName(user.getName());
				   u1.setProfil(new ProfilDTO(user.getProfil().getId(), user.getProfil().getProfil(), user.getProfil().getDetails()));
				   u1.setEmail(user.getEmail());
				   u1.setPhone(user.getPhone());
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
	   @PostMapping(value="/creerUnCompteAndroid")
	   @ResponseBody 
	   public Reponse creerCompte(@RequestParam("surname") String surname, @RequestParam("name") String name, @RequestParam("birthDate") long birthDate_as_long, @RequestParam("address") String address,@RequestParam("city") String city, @RequestParam("country") String country, @RequestParam("email")String email, String phone,@RequestParam("password") String password, @RequestParam("MAC") String MAC, @RequestParam("photo") String photo) {  
		   	return accountService.creerCompte(surname, name, birthDate_as_long, address, city, country, email, phone, password, photo, MAC);
	   }
	   @PostMapping(value="/generateTokenAndroid")
	   @ResponseBody 
	   public Reponse generateCodeAndroid(@RequestParam("login") String login, @RequestParam("MAC") String MAC) {  
		   return accountService.generateCodeAndroid(login, MAC);
	   }

	   @PostMapping(value="/generateToken")
	   @ResponseBody 
	   public Reponse generateCode(@RequestBody String requete) {  
		   JsonObject jsonObject = new JsonParser().parse(requete).getAsJsonObject();
		   String login=jsonObject.get("login").getAsString();
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
			   return new Reponse(-1, "ERROR", null);
		   }
		   

	   }
	   
	   public boolean validateEmail(String email) {  
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
	   
	   @GetMapping(value="/listPays")
	   @ResponseBody	 
	   public ArrayList<PaysDTO> getPays() {
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
		   return paysDTO;
	   }
	   @GetMapping(value="/android/listePays") 
	   public Reponse listePaysJson() {
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
		   return new Reponse(1, "OK", m);
	   }	   
	   @GetMapping(value="/listePays") 
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

	   @PostMapping(value="/activerCompte")
	   @ResponseBody
       public Reponse validateAccount(@RequestBody String body)
       {
           try
           {
    		   JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
    		   int code=jsonObject.get("code").getAsInt();
    		   String email=jsonObject.get("login").getAsString();
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
	   @PostMapping(value="/activerCompteAndroid")
	   @ResponseBody
       public Reponse validateAccount(@RequestParam("code") int code, @RequestParam("login") String email, @RequestParam("MAC") String mac)
       {
		   return accountService.validateAccount(code, email, mac);
       }

	   @PostMapping(value="/checkToken")
	   @ResponseBody
       public Reponse checkToken(@RequestBody String body)
       {
           try
           {
    		   JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
    		   int code=jsonObject.get("code").getAsInt();
    		   String email=jsonObject.get("login").getAsString();
               User u = userDAO.findByEmail(email);
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
	   @PostMapping(value="/checkTokenAndroid")
	   @ResponseBody
       public Reponse checkToken(@RequestParam("code") int code, @RequestParam("login") String email )
       {
		   	return accountService.checkToken(code, email);
       }
	
	
}
