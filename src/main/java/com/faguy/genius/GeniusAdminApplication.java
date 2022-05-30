package com.faguy.genius;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

import com.faguy.genius.entity.Categorie;
import com.faguy.genius.entity.ModePaiement;
import com.faguy.genius.entity.PaiementPays;
import com.faguy.genius.entity.Pays;
import com.faguy.genius.entity.Profil;
import com.faguy.genius.entity.User;
import com.faguy.genius.entity.Ville;
import com.faguy.genius.model.Reponse;
import com.faguy.genius.model.DAO.CategorieRepository;
import com.faguy.genius.model.DAO.ModePaiementtRepository;
import com.faguy.genius.model.DAO.PaiementPaysRepository;
import com.faguy.genius.model.DAO.PaysRepository;
import com.faguy.genius.model.DAO.ProfilRepository;
import com.faguy.genius.model.DAO.UserRepository;
import com.faguy.genius.model.DAO.VilleRepository;
import com.faguy.genius.model.DTO.PaysDTO;
import com.faguy.genius.model.DTO.VilleDTO;
import com.faguy.genius.service.MediasService;
import com.faguy.genius.util.Util;
import com.google.gson.Gson;

@SpringBootApplication
public class GeniusAdminApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	ServletContext context;
	@Autowired
	private   UserRepository userDAO;
	@Autowired
	private  ProfilRepository profilDAO;
	@Autowired
	private  PaysRepository paysDAO;	
	@Autowired
	private  VilleRepository villeDAO;	
	@Autowired CategorieRepository categorieDAO;
	@Autowired ModePaiementtRepository modePaiementDAO;
	@Autowired 
	private PaiementPaysRepository paiementPaysDAO;

	@Autowired
	private MediasService mediaService;
	public static void main(String[] args) {
		ApplicationContext acx=SpringApplication.run(GeniusAdminApplication.class, args);
		String fileSeparator = File.separator;
		
		File f=new File("ressources"+fileSeparator+"partenaires"+fileSeparator+"fagme2006");
		boolean t=f.mkdirs();
		System.err.println("=====> "+t+"=======> "+f.getAbsolutePath());

	}
	public void initCategories() {
		if(categorieDAO.count()<=0) {
			categorieDAO.save(new Categorie("HEALTH", "HEALTH", new Date().getTime()));
			categorieDAO.save(new Categorie("POLITIC", "POLITIC", new Date().getTime()));
			categorieDAO.save(new Categorie("ECONOMY", "ECONOMY", new Date().getTime()));
			categorieDAO.save(new Categorie("SCIENCE", "SCIENCE", new Date().getTime()));
			categorieDAO.save(new Categorie("BIBLE", "BIBLE", new Date().getTime()));
			categorieDAO.save(new Categorie("ENVIRONMENT", "ENVIRONMENT", new Date().getTime()));
			categorieDAO.save(new Categorie("ENTERTAINMENT", "ENTERTAINMENT", new Date().getTime()));
			categorieDAO.save(new Categorie("SPORT", "SPORT", new Date().getTime()));	
			categorieDAO.save(new Categorie("EDUCATION", "EDUCATION", new Date().getTime()));	
			
		}

	}
	 public Reponse getPays() {
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
		   System.err.println(m);
		   return new Reponse(1, "OK", paysDTO);
	   }
	private  void initUser() {
		String phone="695394391";
        User u = userDAO.findByPhone(phone);
        if (u == null)
        {
            User user = new User();
            user.setName("Genius");
            user.setCity(villeDAO.findByName("Douala"));
            user.setEmail("ffyasmina@gmail.com");
            user.setLogin(user.getEmail());
            user.setBirthDate(new Date());
            user.setMAC("XXXXXX");
            user.setStatut(1);
            user.setPhone(phone);
            user.setPassword(Util.getMd5("admin"));
            user.setAddress("Bd des nations unies Douala");
            user.setStatut(1);
            user.setProfil(profilDAO.findByName("SUPER_ADMIN"));
            userDAO.save(user);
        }
	}
	
	private void initPays() {
		if(paysDAO.count()<1) {
			Pays cameroun=new Pays("Cameroon", "cm", "+237", "iVBORw0KGgoAAAANSUhEUgAAACAAAAAVCAIAAACor3u9AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTExIDc5LjE1ODMyNSwgMjAxNS8wOS8xMC0wMToxMDoyMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjkzRjkxMEQ4MEMzODExRTY4MkUxOTM5NjQ5QzNFRjY1IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjkzRjkxMEQ5MEMzODExRTY4MkUxOTM5NjQ5QzNFRjY1Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6OTNGOTEwRDYwQzM4MTFFNjgyRTE5Mzk2NDlDM0VGNjUiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6OTNGOTEwRDcwQzM4MTFFNjgyRTE5Mzk2NDlDM0VGNjUiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz58c5ScAAAA20lEQVR42mJkqIpjwAVYmV1uvJq89eZ3Vias8v++Myq0fxXy+fnvGyMuM5gYaAxGLRhyFvwHI1IAC2nKmSHJkwY++P+X4c97Jl7jP7wmv4EMIJeqPvjDwKHwj0vnl6D7L0YmBhah/9+usvy4z0Q9C5gYfr9m5FT5y2//G8j7fof54wFWBkYGqlrwjunTcVY+a5AFn0+w/n7DxMzz/z8VI5mJ/T8w/dzJ5gGSHEr/mDj+UzkVMbIyfD7FCkmkv18zM7L+JzK9shCfAxhZ/iM4/0eLilEL6GcBQIABAEvyR5Ux8jVxAAAAAElFTkSuQmCCMTEzMQ==");
			Pays maroc=new Pays("Morocco", "ma", "+212", "iVBORw0KGgoAAAANSUhEUgAAACAAAAAVCAIAAACor3u9AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTExIDc5LjE1ODMyNSwgMjAxNS8wOS8xMC0wMToxMDoyMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjM3RkIxMkNBMENBRjExRTY5MzYxODE1NTNCN0FBMkIwIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjM3RkIxMkNCMENBRjExRTY5MzYxODE1NTNCN0FBMkIwIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6MzdGQjEyQzgwQ0FGMTFFNjkzNjE4MTU1M0I3QUEyQjAiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6MzdGQjEyQzkwQ0FGMTFFNjkzNjE4MTU1M0I3QUEyQjAiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz5PRKFWAAAA7klEQVR42mI8qK7LQEvAxEBjMGrBULOA6T8D83+aWcD2j+EWD+MNHkYgg3jAQqQ6jn8MPH8YDoow/WVksHj/9ysLww8mKlkADJYvLAwLZJm/MzPc4WYEirSoMXP8ZQh79pf3D8M/RoqDCGgE0CCrd/9u8DIafvpv/PH/dV5Gy/f/+IgwndggAgaL2Yf/l9/+3ygBcpDvy3+W7/+/Z6VeHABNfcPGAIzevHtAuxi2izG9Ymdg/0s9H/wHeyLv3h+xnyCu+pd/f4GCjNTzwX9wKuL6ywCMZyDg/w1y+3/qJlOIJ+BRMloWjVqABAACDAApuE9uSmfffgAAAABJRU5ErkJggjExNTA=");
			Pays nigeria=new Pays("Nigeria", "ng", "+234", "iVBORw0KGgoAAAANSUhEUgAAACAAAAAQCAIAAAD4YuoOAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTExIDc5LjE1ODMyNSwgMjAxNS8wOS8xMC0wMToxMDoyMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkVGMEExNThEMEQwMjExRTZBMUU4OEQzQzlBNkNBQkY5IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkVGMEExNThFMEQwMjExRTZBMUU4OEQzQzlBNkNBQkY5Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6RUYwQTE1OEIwRDAyMTFFNkExRTg4RDNDOUE2Q0FCRjkiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6RUYwQTE1OEMwRDAyMTFFNkExRTg4RDNDOUE2Q0FCRjkiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz7jBsypAAAAOUlEQVR42mJkaA9kwAV+fQ/Vc14VUMKAG0Rs7F15cQ8DGycuBUwMNAajFoxaMGrBqAWjFlADAAQYAH/tCPhyD12pAAAAAElFTkSuQmCCOTY5");
			Pays ghana=new Pays("Ghana", "gh", "+233", "iVBORw0KGgoAAAANSUhEUgAAACAAAAAVCAIAAACor3u9AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTExIDc5LjE1ODMyNSwgMjAxNS8wOS8xMC0wMToxMDoyMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkUwMDREOTkyMEM3MjExRTY5RTc0QzI1RUY0NTc4MDQ1IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkUwMDREOTkzMEM3MjExRTY5RTc0QzI1RUY0NTc4MDQ1Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6RTAwNEQ5OTAwQzcyMTFFNjlFNzRDMjVFRjQ1NzgwNDUiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6RTAwNEQ5OTEwQzcyMTFFNjlFNzRDMjVFRjQ1NzgwNDUiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4w4PImAAABRElEQVR42mI8J6jGQEvAxEBjMGoBQcDy/w8JqhkZGP6TagGLIAlamJlAFvz7R4qbfh4SJ1IpGzvj3hO/+HgYTbVZf/36T7QPhAkrZWRlYGRjZOBmWNL5TUKMydSfle0Lw/+f/4kJXsJxwMzMcO7CnzNX//BwMe46/IuXm9FA+fvHL//sTdjUlZj//qFGKpISYzpw+ld0+cdnr//dfPA3ovTjxZt/JESYiIlxwhb8/csgIcq0bLpArC8HRCQjjHNqDz8fLyNQinAQMTMRFV2/3/09d+13qAvbn78Mx87//vPmLwsrKFERtuDNdxaCitjZGM9c+x3ix1uWxPX/3/+u+d+OXGMw1mL9SURaYhQutiHGB8C0z83JCDERaN+X7/+ZiSsEWN5+YyEyE7//AWN/JSFPszAw/SehoMDDHS2uB60FAAEGADfTcP0ZNg1ZAAAAAElFTkSuQmCCMTIzNg==");
			Pays liberia=new Pays("Liberia", "lr", "+231", "iVBORw0KGgoAAAANSUhEUgAAACAAAAARCAIAAAAzPjmrAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTExIDc5LjE1ODMyNSwgMjAxNS8wOS8xMC0wMToxMDoyMCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkQzN0Q2OEM0MEM4RjExRTZCMTU5QThBQTRFRTk1NEE0IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkQzN0Q2OEM1MEM4RjExRTZCMTU5QThBQTRFRTk1NEE0Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6RDM3RDY4QzIwQzhGMTFFNkIxNTlBOEFBNEVFOTU0QTQiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6RDM3RDY4QzMwQzhGMTFFNkIxNTlBOEFBNEVFOTU0QTQiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz77KjdaAAABIElEQVR42mJk0MhggIB//xgYGUGImcXx3dOW5xe+MTAzUAxYoPSff1w8HL9+//nz6w/D319c6oqSIYa/GZmoYcF/BhZWZg4GhuxYxxMX7p09f/fLp++8hpqKvckM1ACMDGppvJzsNdneBQkuT16+r+pev3LbWae/Hzs/3/9OHR8wMX1+/2XjgcsZkXYfPn5bv/cCw///bKJCvAZC7AxMVIoDVpbvP34ZB7eLCfPy83O/fv9S0M5BvTuRSkEknwQkGH7/Zfj3H+h2BjYWBkZmW+Zvk1jf/qBKELlaqWFYyqz39S3L67+s1LCA8T/Q1bQEjGesI2lqAcvvtx9o6wOaB9GlwBzaWnBI1JLGQQQsRGlqwc28FtpasJtBhaYWAAQYAEkpZiSeeN0DAAAAAElFTkSuQmCCMTIwMA==')\n" + 
					"");

			ArrayList<Pays> pays=new ArrayList<Pays>() ;
			pays.add(cameroun);
			pays.add(maroc);
			pays.add(ghana);
			pays.add(liberia);
			pays.add(nigeria);	

			paysDAO.save(cameroun);
			paysDAO.save(maroc);
			paysDAO.save(ghana);
			paysDAO.save(liberia);
			paysDAO.save(nigeria);
		
		}
	
	}
	private void initVille() {
		if(villeDAO.count()<1) {
			Ville yaounde=new Ville("Yaounde");
			Ville douala=new Ville("Douala");
			Ville bamenda=new Ville("Bamenda");
			Ville buea=new Ville("Buea");
			Ville bafoussam=new Ville("Bafoussam");
			Ville nkongsamba=new Ville("Nkongsamba");
			Ville kumba=new Ville("Kumba");
			Ville yabassi=new Ville("Yabassi");
			Ville limbe=new Ville("Limbe");
			Ville bandjoun=new Ville("Bandjoun");
			Ville ngaoundere=new Ville("Ngaoundere");
			Ville edea=new Ville("Edea");
			Ville muyuka=new Ville("Muyuka");
			Ville kribi=new Ville("Kribi");
			Ville fundong=new Ville("Fundong");
			Ville ebolowa=new Ville("Ebolowa");
			
			
			/*nigeria*/
			Ville lagos=new Ville("Lagos");
			Ville abuja=new Ville("Abuja");
			
			/**/
			Pays cameroun=paysDAO.findByName("Cameroon");
			Pays nigeria=paysDAO.findByName("Nigeria");
			lagos.setCountry(nigeria);
			abuja.setCountry(nigeria);
			buea.setCountry(cameroun);
			bamenda.setCountry(cameroun);
			yaounde.setCountry(cameroun);
			douala.setCountry(cameroun);
			yabassi.setCountry(cameroun);
			fundong.setCountry(cameroun);
			kribi.setCountry(cameroun);
			ebolowa.setCountry(cameroun);
			bandjoun.setCountry(cameroun);
			muyuka.setCountry(cameroun);
			nkongsamba.setCountry(cameroun);
			bafoussam.setCountry(cameroun);
			limbe.setCountry(cameroun);
			edea.setCountry(cameroun);
			villeDAO.save(yaounde);
			villeDAO.save(buea);
			villeDAO.save(douala);
			villeDAO.save(bamenda);
			villeDAO.save(limbe);
			villeDAO.save(kumba);
			villeDAO.save(kribi);
			villeDAO.save(muyuka);
			villeDAO.save(bafoussam);
			villeDAO.save(ngaoundere);
			villeDAO.save(nkongsamba);
			villeDAO.save(bandjoun);
			villeDAO.save(yabassi);
			villeDAO.save(edea);
			villeDAO.save(fundong);
			villeDAO.save(ebolowa);
			villeDAO.save(abuja);
			villeDAO.save(lagos);
		}
		
	}
	
	private  void initProfil() {
        ArrayList<Profil> profils = new ArrayList<Profil>();
        profils.add(new Profil("ADMIN", "Admin"));
        profils.add(new Profil("CLIENT", "client "));
        profils.add(new Profil("SUPER_ADMIN", "Super admin"));
        profils.add(new Profil("PARTNER", "partner"));
        for(Profil p:profils) {
        	Profil e=profilDAO.findByName(p.getProfil());
        	if(e==null)
        	profilDAO.save(p);
        }
	}
	public void initPaymentmode() {
		System.err.println("initial ==<"+modePaiementDAO.count());
		if(modePaiementDAO.count()<=0) {
			modePaiementDAO.save(new ModePaiement("YUP", "YUP", "iVBORw0KGgoAAAANSUhEUgAAAC8AAAAgCAYAAACCcSF5AAAACXBIWXMAAAsTAAALEwEAmpwYAAAFwmlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDIgNzkuMTYwOTI0LCAyMDE3LzA3LzEzLTAxOjA2OjM5ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgKFdpbmRvd3MpIiB4bXA6Q3JlYXRlRGF0ZT0iMjAyMS0wNC0wMVQyMDoyNTozMyswMTowMCIgeG1wOk1vZGlmeURhdGU9IjIwMjEtMDQtMjdUMTA6NTI6MjArMDE6MDAiIHhtcDpNZXRhZGF0YURhdGU9IjIwMjEtMDQtMjdUMTA6NTI6MjArMDE6MDAiIGRjOmZvcm1hdD0iaW1hZ2UvcG5nIiBwaG90b3Nob3A6Q29sb3JNb2RlPSIzIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjgzZDRmMTdhLTNjYzItYjc0Yy1iNjI2LTBjYzE1MmI5Y2E3MiIgeG1wTU06RG9jdW1lbnRJRD0iYWRv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"));
			modePaiementDAO.save(new ModePaiement("ORANGE MONEY", "OM", "iVBORw0KGgoAAAANSUhEUgAAAC4AAAAgCAYAAABts0pHAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAFwmlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDIgNzkuMTYwOTI0LCAyMDE3LzA3LzEzLTAxOjA2OjM5ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgKFdpbmRvd3MpIiB4bXA6Q3JlYXRlRGF0ZT0iMjAyMS0wNC0wMVQyMDoyNzo1NiswMTowMCIgeG1wOk1vZGlmeURhdGU9IjIwMjEtMDQtMjdUMTE6MDY6MTArMDE6MDAiIHhtcDpNZXRhZGF0YURhdGU9IjIwMjEtMDQtMjdUMTE6MDY6MTArMDE6MDAiIGRjOmZvcm1hdD0iaW1hZ2UvcG5nIiBwaG90b3Nob3A6Q29sb3JNb2RlPSIzIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOmIwYzdhZGM3LWVhMWUtOGI0MC1iZGRmLWQzNTQ2MmYyNzI5YyIgeG1wTU06RG9jdW1lbnRJRD0iYWRv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"));
			modePaiementDAO.save(new ModePaiement("MTN MOBILE MONEY", "MOMO", "iVBORw0KGgoAAAANSUhEUgAAADgAAAAgCAYAAABHA7voAAAACXBIWXMAAAsTAAALEwEAmpwYAAAFwmlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDIgNzkuMTYwOTI0LCAyMDE3LzA3LzEzLTAxOjA2OjM5ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgKFdpbmRvd3MpIiB4bXA6Q3JlYXRlRGF0ZT0iMjAyMS0wNC0wMVQyMDoyNTo1NyswMTowMCIgeG1wOk1vZGlmeURhdGU9IjIwMjEtMDQtMjdUMTE6MDU6MTYrMDE6MDAiIHhtcDpNZXRhZGF0YURhdGU9IjIwMjEtMDQtMjdUMTE6MDU6MTYrMDE6MDAiIGRjOmZvcm1hdD0iaW1hZ2UvcG5nIiBwaG90b3Nob3A6Q29sb3JNb2RlPSIzIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjMxY2VjM2I5LTJiN2ItMGI0YS04ZTdmLTI1ZTgwMzhiMTBhYSIgeG1wTU06RG9jdW1lbnRJRD0iYWRv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"));
			modePaiementDAO.save(new ModePaiement("PAYPAL", "PAYPAL", "iVBORw0KGgoAAAANSUhEUgAAADkAAAAgCAMAAACfHyDkAAAA8FBMVEX///8NOIMAnNoWKWcAmNkAlNcAltgAmtkANIEAHXkAGngAKX0AMYDs7vMAI3sAk9cALH4AH3kAJnwAJHuj0OwAF3cALn/M5PQAoN4zUY+psckAE3bo8/r2+v00pdyy1+8XIWHDytqLxei93fGgqsVtuOQMdrHu8fV9v+ZXsOHg7vjX2+bW6fbg5OyGk7bN0+BneaaazOu3v9NGXpZ3hq0TL3OTnr1Wap2xus8XGlwuTI1Jq98UPYYAAHNNZJlhtOIXSIISZZ+Bj7MRWZMAC3VidKMjRYkXF1oVMm4UQHtxgasMTJIKYKMIcrMGg8IKZqjcvCivAAAACXBIWXMAAAsTAAALEwEAmpwYAAAFwmlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDIgNzkuMTYwOTI0LCAyMDE3LzA3LzEzLTAxOjA2OjM5ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgKFdpbmRvd3MpIiB4bXA6Q3JlYXRlRGF0ZT0iMjAyMS0wNC0yN1QxMDoyODo0MSswMTowMCIgeG1wOk1vZGlmeURhdGU9IjIwMjEtMDQtMjdUMTA6NTQ6MTErMDE6MDAiIHhtcDpNZXRhZGF0YURhdGU9IjIwMjEtMDQtMjdUMTA6NTQ6MTErMDE6MDAiIGRjOmZvcm1hdD0iaW1hZ2UvcG5nIiBwaG90b3Nob3A6Q29sb3JNb2RlPSIyIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjQzNTk3YjJjLWM0YjEtY2M0NS1hMmNkLWI0MGY4ZGIyYWNiMyIgeG1wTU06RG9jdW1lbnRJRD0iYWRvYmU6ZG9jaWQ6cGhvdG9zaG9wOjU5ZTA2MjQ5LWViYzctZTY0NC05Yjc1LTNhY2I4MWM4NmViYiIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOmZjZDgzOTM3LWYyNWEtMmE0MS1hZjhjLTJiMmNjZGQyYTU1ZSI+IDx4bXBNTTpIaXN0b3J5PiA8cmRmOlNlcT4gPHJkZjpsaSBzdEV2dDphY3Rpb249ImNyZWF0ZWQiIHN0RXZ0Omluc3RhbmNlSUQ9InhtcC5paWQ6ZmNkODM5MzctZjI1YS0yYTQxLWFmOGMtMmIyY2NkZDJhNTVlIiBzdEV2dDp3aGVuPSIyMDIxLTA0LTI3VDEwOjI4OjQxKzAxOjAwIiBzdEV2dDpzb2Z0d2FyZUFnZW50PSJBZG9iZSBQaG90b3Nob3AgQ0MgKFdpbmRvd3MpIi8+IDxyZGY6bGkgc3RFdnQ6YWN0aW9uPSJzYXZlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDo0MzU5N2IyYy1jNGIxLWNjNDUtYTJjZC1iNDBmOGRiMmFjYjMiIHN0RXZ0OndoZW49IjIwMjEtMDQtMjdUMTA6NTQ6MTErMDE6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAoV2luZG93cykiIHN0RXZ0OmNoYW5nZWQ9Ii8iLz4gPC9yZGY6U2VxPiA8L3htcE1NOkhpc3Rvcnk+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+8a31rwAAARFJREFUOI3tkrFKA0EURc+9k2xWNmQxahpJVCxEIiIimkKw0f9vbPwHwcJKlGgxs9khiUVszYXdmTczZ9+++wa22ipJkqTqz6R0uyEGaubfG5GOb9u+yf5hsHrQdicLa7dBp2jn72KN5tnpEvakEEIIwXH9TiPQCmnHJ2Ux5iMFB2l8YcwZ0JceJSSYStAFoLZtE1JZIYRj20Uqsyyl/o7KKyVS4tS23TuyH2zH7JIc1RqkeyS41i6VTgaaxVxFJJbJemFtHIYw1BQkHS5KjGSBaxhLI7vbWtGQGlZS/DIZ2SvsfXyBz2EGk8zFaFWft2bhMusLn19dXic8A9Bz1qdlPalcvyHAMP+V1IaXcqt/px8OBBP21FopzwAAAABJRU5ErkJggg=="));
			paiementPaysDAO.save(new PaiementPays(modePaiementDAO.findByCode("YUP"), paysDAO.findByName("Cameroon")));
			paiementPaysDAO.save(new PaiementPays(modePaiementDAO.findByCode("OM"), paysDAO.findByName("Cameroon")));
			paiementPaysDAO.save(new PaiementPays(modePaiementDAO.findByCode("MOMO"), paysDAO.findByName("Cameroon")));
			paiementPaysDAO.save(new PaiementPays(modePaiementDAO.findByCode("PAYPAL"), paysDAO.findByName("Cameroon")));

			
		}
	}

	@Override
	public void run(String... args) throws Exception {

		initProfil();
		initPays();
		initVille();
		initUser();
		getPays();
		initCategories();
		initPaymentmode();
		System.out.println("Voilaaaa");
		//mediaService.lesArticles();
		//mediaService.lesSeriesDTO(mediaService.lesSeries());
		String absolutePath = context.getRealPath("resources"+File.separator+"uploads");
		System.err.println("Ou ? ===> "+absolutePath);

	}


}
