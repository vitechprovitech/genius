package com.faguy.genius.controller;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.faguy.genius.entity.Achat;
import com.faguy.genius.entity.AchatCategorie;
import com.faguy.genius.entity.Action;
import com.faguy.genius.entity.Article;
import com.faguy.genius.entity.Assignation;
import com.faguy.genius.entity.Categorie;
import com.faguy.genius.entity.Dossier;
import com.faguy.genius.entity.Fichier;
import com.faguy.genius.entity.Profil;
import com.faguy.genius.entity.Token;
import com.faguy.genius.entity.User;
import com.faguy.genius.model.Reponse;
import com.faguy.genius.model.DAO.AchatCategorieRepository;
import com.faguy.genius.model.DAO.AchatProduitRepository;
import com.faguy.genius.model.DAO.AchatRepository;
import com.faguy.genius.model.DAO.ActionRepository;
import com.faguy.genius.model.DAO.ArticleRepository;
import com.faguy.genius.model.DAO.AssignationRepository;
import com.faguy.genius.model.DAO.CategorieRepository;
import com.faguy.genius.model.DAO.DossierRepository;
import com.faguy.genius.model.DAO.FichierRepository;
import com.faguy.genius.model.DAO.PaysRepository;
import com.faguy.genius.model.DAO.ProfilRepository;
import com.faguy.genius.model.DAO.TokenRepository;
import com.faguy.genius.model.DAO.TransfertActionRepository;
import com.faguy.genius.model.DAO.UserRepository;
import com.faguy.genius.model.DAO.VilleRepository;
import com.faguy.genius.model.DTO.ArticleDTO;
import com.faguy.genius.model.DTO.CategorieDTO;
import com.faguy.genius.model.DTO.PaysDTO;
import com.faguy.genius.model.DTO.UserDTO;
import com.faguy.genius.service.AccountService;
import com.faguy.genius.service.MediasService;
import com.google.gson.Gson;

import antlr.collections.List;
import javassist.expr.Instanceof;

@Controller
public class WebController {
	
	@Autowired
	private UserRepository userDAO;
	@Autowired private ActionRepository actionDAO;
	@Autowired private AchatRepository achatDAO;
	@Autowired private AchatProduitRepository achatProduitDAO;
	@Autowired private AchatCategorieRepository achatPackDAO;
	@Autowired
	private ProfilRepository profilDAO;
	@Autowired 
	private ArticleRepository articleDAO;
    @Autowired
    private JavaMailSender mailSender;
	@Autowired
	private PaysRepository paysDAO;	
	@Autowired
	private FichierRepository fichierDAO;
	@Autowired
	private VilleRepository villeDAO;	
	@Autowired
	private TokenRepository tokenDAO;
	@Autowired 
	CategorieRepository categorieDAO;
	@Autowired DossierRepository dossierDAO;
	@Autowired
	private AssignationRepository assignationDAO;
	@Autowired private MediasService mediaService;
	@Autowired
	private TransfertActionRepository transfertDAO;
	@RequestMapping(value="/")
	public String index(Model model, HttpServletRequest request) {
		HttpSession session=request.getSession();
		if(session!=null) {
			if(session.getAttribute("user")!=null && session.getAttribute("user")!=null) {
				return showHomeRoot(model);
			}else {
				return "login";
			}
		}
		return "login";
	}
	public boolean isLogged(HttpServletRequest request) {
		HttpSession session=request.getSession();
			if(session.getAttribute("user")==null) {
				return false;
			}else {
				return true;
			}	
	}
	@RequestMapping(value="/login")
	public String login(Model model, HttpServletRequest request) {
		HttpSession session=request.getSession();
		if(session!=null) {
			if(session.getAttribute("user")!=null) {
				return showHomeRoot(model);
			}else {
				return "login";
			}
		}
		return "login";
	}	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logOut(Model model, HttpServletRequest request) {
		HttpSession session=request.getSession();
		session.invalidate();
		return "forward:/";
	}	
	
	@RequestMapping(value="/home", method = RequestMethod.POST)
	public String home(Model model, HttpServletRequest request, @RequestParam("username")  String email, @RequestParam("password")  String password) {
		Reponse reponse=seConnecter(email, getMd5(password), "WEB");

		UserDTO user=null;
		if(reponse!=null && reponse.getCode()==1) {
			if(reponse.getMessage().trim().equalsIgnoreCase("TOKEN")) {
				model.addAttribute("code", 1);
				model.addAttribute("email", email);
				model.addAttribute("message", "An activation code has been sent to your mailbox.");
				return "validationCode";
			}
			user= (UserDTO) reponse.getObject();
			model.addAttribute("user", user);

			HttpSession session=request.getSession();
			session.setAttribute("user", user);
			return showHome(model, user);
		}else {
			model.addAttribute("code", -1);
			model.addAttribute("message", "Login or password is not correct");
			return "login";
		}
	}
	private String showHome(Model model, UserDTO user) {
		model.addAttribute("user", user);
		return showHomeRoot(model);
	}
	private String showAdminHome(Model model) {
		ArrayList<Action> actions=(ArrayList<Action>) actionDAO.findAll();
		model.addAttribute("actions", actions);
		return showHomeRoot(model);
	}
   @RequestMapping(method = RequestMethod.GET, value = "/terms_conditions")
   public String termAndCondition() {
	   System.out.println("Conditions and terms");
	   return "policy";
   }
	@Autowired private AchatCategorieRepository achatCategorieDAO;
	private String showHomeRoot(Model model) {
		ArrayList<Assignation> assignations=new ArrayList<Assignation>();
		assignations=assignationDAO.findLastArticles();
		ArrayList<Assignation> videos= (ArrayList<Assignation>) assignations.stream().filter(a->a.getArticle().getTypeMedia().equalsIgnoreCase("VIDEO")).collect(Collectors.toList());
		ArrayList<Assignation> audios=(ArrayList<Assignation>)assignations.stream().filter(a->a.getArticle().getTypeMedia().equalsIgnoreCase("AUDIO")).collect(Collectors.toList());
		ArrayList<Assignation> books=(ArrayList<Assignation>) assignations.stream().filter(a->a.getArticle().getTypeMedia().equalsIgnoreCase("BOOK")).collect(Collectors.toList());
		ArrayList<Long> packVideosId=new ArrayList<Long>();
		ArrayList<Assignation> packVideos=new ArrayList<Assignation>();
		ArrayList<Assignation> packAudios=new ArrayList<Assignation>();
		ArrayList<Assignation> packBooks=new ArrayList<Assignation>();
		
		
		ArrayList<Achat> sales=(ArrayList<Achat>) achatDAO.findAll().stream().collect(Collectors.toList());
		/**/
		
		for(Achat a:sales) {
			if(a instanceof AchatCategorie) {
				ArrayList<Assignation> ass=assignationDAO.findArticleByCategorie(((AchatCategorie) a).getBundle().getId());	
				a.setPeriode(ass.get(0).getPeriode());
				a.setAmount(ass.get(0).getPrix());	
				Achat adv=achatDAO.save(a);
				System.out.println();
			}
		}
		/**/
		ArrayList<Long> packAudiossId=new ArrayList<Long>();
		ArrayList<Long> packBooksId=new ArrayList<Long>();
		for(Assignation v:videos) {
			Categorie c=v.getCategorie();
			if(c.getParent()!=null && !packVideosId.contains(c.getId())) {
				packVideosId.add(c.getId());
			}
		}
		for(Assignation v:books) {
			Categorie c=v.getCategorie();
			if(c.getParent()!=null && !packBooksId.contains(c.getId())) {
				packBooksId.add(c.getId());
			}
		}
		
		for(Assignation v:audios) {
			Categorie c=v.getCategorie();
			if(c.getParent()!=null && !packAudiossId.contains(c.getId())) {
				packAudiossId.add(c.getId());
			}
		}
		
		for(long i:packBooksId) {
			Assignation ass=books.stream().filter(z->z.getCategorie().getId()==i).findFirst().get();
			Categorie c=ass.getCategorie();
			packBooks.add(ass);
		}
		for(long i:packAudiossId) {
			Assignation ass=audios.stream().filter(z->z.getCategorie().getId()==i).findFirst().get();
			Categorie c=ass.getCategorie();
			packAudios.add(ass);
		}
		for(long i:packVideosId) {
			Assignation ass=videos.stream().filter(z->z.getCategorie().getId()==i).findFirst().get();
			Categorie c=ass.getCategorie();
			packVideos.add(ass);
		}
		ArrayList<Action> tracks=(ArrayList<Action>) actionDAO.findAll().stream().collect(Collectors.toList());
		model.addAttribute("videos",videos.stream().filter(a->a.getCategorie().getParent()==null).collect(Collectors.toList()));
		model.addAttribute("audios", audios.stream().filter(a->a.getCategorie().getParent()==null).collect(Collectors.toList()));
		model.addAttribute("books", books.stream().filter(a->a.getCategorie().getParent()==null).collect(Collectors.toList()));
		model.addAttribute("packBooks", packBooks);
		model.addAttribute("packVideos", packVideos);
		model.addAttribute("packAudios", packAudios);
		model.addAttribute("achats", sales);
		model.addAttribute("connections", tracks);
		return "index";
	}
	
	@RequestMapping(value="/be_partner", method = RequestMethod.GET)
	private String beAPartner(Model model, HttpServletRequest request) {
		if(!isLogged(request)) {
			return "forward:/";
		}
		model.addAttribute("user", request.getSession().getAttribute("user"));
		HttpSession session=request.getSession();
		Object obj=session.getAttribute("user");
		User user=null;
		UserDTO scope=(UserDTO) obj;
		user=userDAO.findByLogin(scope.getEmail());

		if(user==null) {
			return "/";	
		}
		dossierDAO.save(new Dossier(0, user, "", new Date().getTime(), 0, 0));
		return "be_partner";
	}
	
	private String showHome(Model model, User user) {
		model.addAttribute("user", user);		
		return showHomeRoot(model);
	}
	@RequestMapping(value="/403")
	public String accessDenied403(Model model, HttpServletRequest request, @RequestParam("username")  String email, @RequestParam("password")  String password) {
		Reponse reponse=seConnecter(email, getMd5(password), "WEB");
		UserDTO user=null;
		if(reponse!=null && reponse.getCode()==1) {
			if(reponse.getMessage().trim().equalsIgnoreCase("TOKEN")) {
				model.addAttribute("code", 1);
				model.addAttribute("email", email);
				model.addAttribute("message", "An activation code has been sent to your mailbox.");
				return "validationCode";
			}
			user= (UserDTO) reponse.getObject();
			model.addAttribute("user", user);
			HttpSession session=request.getSession();
			session.setAttribute("user", user);
			return "index";
		}else {
			model.addAttribute("code", -1);
			model.addAttribute("message", "Login or password is not correct");
			return "login";
		}
	}
	@RequestMapping(value="/signUpView", method = RequestMethod.GET)
	public String signUpView(Model model, UserDTO user) {
        ArrayList<PaysDTO> result = accountService.listePays();
        model.addAttribute("pays", result);
        model.addAttribute("paysJSON", accountService.listePaysString());
        return "pages/sign-up";
	}

	@RequestMapping(value="/creerUnCompteWeb", method = RequestMethod.POST)
	public String creerCompte(Model model, UserDTO user, String password, String confirm_password, RedirectAttributes redirectAttrs) {
        if(!getMd5(password).equalsIgnoreCase(getMd5(confirm_password))) {
        	model.addAttribute("code",-1);
        	model.addAttribute("message", "Les mots de passes saisis ne sont pas corrects");
        	return "pages/sign-up";
        }
        Reponse result =accountService. creerCompte(user.getSurname(), user.getName(), user.getBirthDate(), user.getAddress(), user.getVille(),user.getPays(),user.getEmail(), user.getPhone(), getMd5(user.getPassword()), user.getPhoto(), "WEB");
        if(result!=null) {
        	if(result.getCode()==1){
        		UserDTO u=(UserDTO) result.getObject();
        		model.addAttribute("code", 1);
        		  model.addAttribute("email", user.getEmail());
        		  model.addAttribute("password", user.getPassword());
        		model.addAttribute("message", "A validation code has been sent to your mailbox. Check it and fill the  field below");
        		return "validationCode";
        	}else {
        		  model.addAttribute("user", user);
        		model.addAttribute("code", -1);
        		model.addAttribute("message", result.getMessage());
        		return "pages/sign-up";
        	}

        }else {
    		model.addAttribute("code", -1);
    		  model.addAttribute("user", user);
    		  
    		model.addAttribute("message", "An error occured");
    		return "sign-up";        	
        }
	}
	@RequestMapping(value="/resetPasswordWeb", method = RequestMethod.POST)
	public String resetPasswordAndroid(Model model, String email,  String password, String confirmPassword, HttpServletRequest request) {
        if(!getMd5(password).equalsIgnoreCase(getMd5(confirmPassword))) {	
        	model.addAttribute("code",-1);
        	model.addAttribute("email", email);
        	model.addAttribute("message", "Les mots de passes saisis ne sont pas corrects");
        	return "resetPassword";
        }
        
        Reponse result =  accountService.resetPassword(email, getMd5(password), "WEB");
        
        if(result!=null) {
        	if(result.getCode()==1){
        		UserDTO u=(UserDTO) result.getObject();
        		model.addAttribute("code", 1);
        		  model.addAttribute("user", u);
        		  model.addAttribute("password", password);
        		model.addAttribute("message", "Your password has been successfully reseted");
    		
    			model.addAttribute("user", u);
    			HttpSession session=request.getSession();
    			session.setAttribute("user", u);
        		return "index";
        	}else {
        		model.addAttribute("email", email);
        		model.addAttribute("code", -1);
        		model.addAttribute("message", result.getMessage());
        		return "resetPassword";
        	}

        }else {
    		model.addAttribute("email", email);
    		model.addAttribute("code", -1);
    		model.addAttribute("message", result.getMessage());
    		return "resetPassword";      	
        }
	}
	
	@RequestMapping(value="/forgetPassword", method = RequestMethod.GET)
	private String forgetPasswordView(Model model) {
		return "passwordRecovery";
	}
	
	@RequestMapping(value="/categories", method = RequestMethod.GET)
	private String categories(Model model, HttpServletRequest request) {
		if(!isLogged(request)) {
			return "forward:/";
		}
		ArrayList<CategorieDTO> categories=mediaService.lesCategoriesDTO();
		model.addAttribute("categories", categories);
		return "categories";
	}
	@RequestMapping(value="/infosCategorie", method = RequestMethod.GET)
	private String infosCategorie(Model model, int id, HttpServletRequest request) {
		if(!isLogged(request)) {
			return "forward:/";
		}
		CategorieDTO categorie=mediaService.findCategorieById(id); 
		ArrayList<Assignation> assignations=assignationDAO.findArticleByCategorie(id);
		
		model.addAttribute("categorie", categorie);
		model.addAttribute("assignations", assignations);
		return "categorie/infos";
	}
		
	@RequestMapping(value="/users", method = RequestMethod.GET)
	private String users(Model model, HttpServletRequest request) {
		if(!isLogged(request)) {
			return "forward:/";
		}
		ArrayList<User> users=(ArrayList<User>) userDAO.findAll();
		model.addAttribute("users", users);
		return "users";
	}
	
	@RequestMapping(value="/client_categories", method = RequestMethod.GET)
	private String showClientRootCategories(Model model, String typeMedias, HttpServletRequest request) {
		//ArrayList<Article> articles=articleDAO.findArticlesByType(typeMedias);
		if(!isLogged(request)) {
			return "forward:/";
		}
		ArrayList<Categorie> categories=mediaService.findRootCategoriesByType(typeMedias);
		model.addAttribute("categories", categories);
		model.addAttribute("typeMedia", typeMedias);
		return "client_categories";
	}

	@RequestMapping(value="/subscribedRootCategories", method = RequestMethod.GET)
	private String showSubscribedRootCategorieByTypeAnUser(Model model, String typeMedias, HttpServletRequest request) {

		HttpSession session=request.getSession();
		if(!isLogged(request)) {
			return "forward:/";
		}
		Object obj=session.getAttribute("user");
		User user=null;
		UserDTO scope=(UserDTO) obj;
		user=userDAO.findByLogin(scope.getEmail());
		
		ArrayList<CategorieDTO> categories=mediaService.findSubscribedRootCategorieByUser(user.getEmail(), typeMedias);
		
		model.addAttribute("categories", categories);
		model.addAttribute("typeMedia", typeMedias);
		return "client_categories";		
	}

	
	@RequestMapping(value="/infosClientCategorieInfos", method = RequestMethod.GET)
	private String showClientCategorieInfos(Model model, String typeMedia, int id, HttpServletRequest request) {
		if(!isLogged(request)) {
			return "forward:/";
		}
		//ArrayList<Article> articles=articleDAO.findArticlesByType(typeMedias);
		Categorie categorie=mediaService.showArticleByCategorieAndTypeMedia(id, typeMedia);
		model.addAttribute("categorie", categorie);
		model.addAttribute("typeMedia", typeMedia);
		return "categorie/infos_client";
	}
	@RequestMapping(value="/subscribedContentsByCategorie", method = RequestMethod.GET)
	private String subscribedContentsByCategorie(Model model, String typeMedia, int id, HttpServletRequest request) {
		HttpSession session=request.getSession();
		if(!isLogged(request)) {
			return "forward:/";
		}
		Object obj=session.getAttribute("user");
		User user=null;
		UserDTO scope=(UserDTO) obj;
		user=userDAO.findByLogin(scope.getEmail());		
		//ArrayList<Article> articles=articleDAO.findArticlesByType(typeMedias);
		Categorie categorie=mediaService.findPurchasesByUserAndCategorie(user.getEmail(), typeMedia, id);
		model.addAttribute("categorie", categorie);
		model.addAttribute("typeMedia", typeMedia);
		return "categorie/infos_client";
	}
	
	@RequestMapping(value="/validateArticle", method = RequestMethod.POST)
	private String validateArticle(Model model, HttpServletRequest request, int idCategorie, int state,  int id, long prix, int periode, int totalMAC) {
		//ArrayList<Article> articles=articleDAO.findArticlesByType(typeMedias);
		HttpSession session=request.getSession();
		Object obj=session.getAttribute("user");
		if(!isLogged(request)) {
			return "forward:/";
		}
		User user=null;
		UserDTO scope=(UserDTO) obj;
		user=userDAO.findByLogin(scope.getEmail());

		if(user==null) {
			return "/";	
		}
		if(!user.getProfil().getProfil().equalsIgnoreCase("ADMIN") && !user.getProfil().getProfil().equalsIgnoreCase("SUPER_ADMIN")) {
			return "/";
		}
		Article article=articleDAO.findArticleById(id);
		article.setState(state);
		article.setTotalMAC(totalMAC);
		article.setPrix(prix);
		article.setPeriode(periode);
		article.setValidatedBy(user);
		article.setValideLe(new Date().getTime());
		articleDAO.save(article);
		Assignation a=assignationDAO.findAssignationByArticleAndCategorie(idCategorie, id);
		
		model.addAttribute("assignation", a);
		model.addAttribute("code", 1);
		model.addAttribute("message", "The product has been updated successfully");
		 return "validation";
	}
	
	@RequestMapping(value="/medias", method = RequestMethod.GET)
	private String medias(Model model, HttpServletRequest request) {
		if(!isLogged(request)) {
			return "forward:/";
		}
		ArrayList<Action> actions=actionDAO.findActionbyArticleNotNull();
		model.addAttribute("actions", actions);
		return "medias";
	}
	@RequestMapping(value="/newCategoryView", method = RequestMethod.GET)
	private String showNewCategoryView(Model model, @RequestParam(required = false, defaultValue = "0") int id, HttpServletRequest request) {
		if(!isLogged(request)) {
			return "forward:/";
		}
		
		if(id!=0) {
			CategorieDTO cdto=mediaService.findCategorieById(id);
			model.addAttribute("categorie", cdto);
		}
		return "new_category";
	}
	@RequestMapping(value="/saveCategory", method = RequestMethod.POST)
	private String saveNewCategorie(Model model, @RequestParam(required = false, defaultValue = "0") int id, String description, String categorie, HttpServletRequest request) {
		HttpSession session=request.getSession();
		if(!isLogged(request)) {
			return "forward:/";
		}
		Object obj=session.getAttribute("user");
		User user=null;
		UserDTO scope=(UserDTO) obj;
		user=userDAO.findByLogin(scope.getEmail());

		if(user==null) {
			return "/";	
		}
		Reponse reponse=mediaService.saveCategorie(id, categorie, description, user);
		if(reponse.getCode()==-1) {
			model.addAttribute("code", -1);
			model.addAttribute("message",  "The category already exist");
			return "forward:/newCategoryView";
		}
		CategorieDTO categorieDTO=null;
		if(id==0) {
			Categorie cat=(Categorie) reponse.getObject();
			categorieDTO=CategorieDTO.toDTO(cat);
		}else {
			categorieDTO=mediaService.findCategorieById(id);
		}
		
		ArrayList<Assignation> assignations=assignationDAO.findArticleByCategorie(id);
		model.addAttribute("assignations", assignations);
		model.addAttribute("categorie", categorieDTO);
		return "categorie/infos";
	}
	@RequestMapping(value="/newUploadView", method = RequestMethod.GET)
	private String showNewUploadyView(Model model, HttpServletRequest request,  @RequestParam( required = false, defaultValue ="0") int id) {

		if(!isLogged(request)) {
			return "forward:/";
		}
		return returnNewMediaView(id, model);

	}
	public String returnNewMediaView(int id, Model model) {
		CategorieDTO cdto=mediaService.findCategorieById(id);
		ArrayList<CategorieDTO> cdtos=mediaService.lesCategoriesDTO();
		model.addAttribute("categorie", cdto);
		model.addAttribute("categories", cdtos);
		return "new_medias";
	}


	@RequestMapping(value="/showAssignationView", method = RequestMethod.GET)
	private String showAssignationView(Model model, int id, HttpServletRequest request) {
		if(!isLogged(request)) {
			return "forward:/";
		}
		return assignationModel(model, id);
	}
	public String assignationModel(Model model, int id) {
		Categorie cdto=categorieDAO.findById(id);
		ArrayList<Assignation>  articles=new ArrayList<Assignation>();
		if(cdto!=null && cdto.getAncestor()>0) {
			articles=assignationDAO.findArticleByCategorie(cdto.getAncestor());
		}else {
			articles=assignationDAO.findArticleByCategorie(cdto.getId());
		}
		model.addAttribute("assignations", articles);
		model.addAttribute("categorie", cdto);	
		return "assignation";
	}

	
	@RequestMapping(value="/saveAssignation", method = RequestMethod.POST)
	private String saveAssignation(Model model, HttpServletRequest request,  int idCategorie, int[] ids, @RequestParam(name = "etiquette", defaultValue = "") String label, @RequestParam(name = "description", defaultValue = "") String description, int prix, int periode, int totalMac,  RedirectAttributes attr) {
		HttpSession session=request.getSession();
		if(!isLogged(request)) {
			return "forward:/";
		}
		Object obj=session.getAttribute("user");
		User user=null;
		UserDTO scope=(UserDTO) obj;
		user=userDAO.findByLogin(scope.getEmail());
		if(user==null) {
			attr.addAttribute("id", idCategorie);
			
			return "redirect:/showAssignationView";
		}
		Categorie categorie=categorieDAO.findById(idCategorie);
		if(categorie==null) {
			model.addAttribute("code", 0);
			model.addAttribute("message", "Une erreur est survenue");
			return assignationModel(model, idCategorie);
			
		}
		ArrayList<Article> articles=new ArrayList<Article>();
		if(ids==null) {
			//attr.addAttribute("id", idCategorie);
			model.addAttribute("code", 0);
			model.addAttribute("message", "Une erreur est survenue");
			return assignationModel(model, idCategorie);
		}
		if(ids.length<=0) {
			model.addAttribute("code", 0);
			model.addAttribute("message", "Une erreur est survenue");
			return assignationModel(model, idCategorie);

		}		
		for(int i=0;i<ids.length;i++) {
			Article a=articleDAO.findArticleById(ids[i]);
			articles.add(a);		
		}
		Reponse reponse=mediaService.creerBundle(articles, categorie, user, description, label, prix, totalMac, periode);
		if(reponse.getCode()<=0) {
			model.addAttribute("code", reponse.getCode());
			model.addAttribute("message", reponse.getCode());
			model.addAttribute("categorie", categorie);
			return assignationModel(model, idCategorie);
		}
		model.addAttribute("code", reponse.getCode());
		model.addAttribute("message", "Your package has been updated successfuly");
		ArrayList<Assignation> assignations=(ArrayList<Assignation>) reponse.getObject();
		model.addAttribute("assignations", assignations);
		return "/assignation/infos";
	}
	
	@RequestMapping(value = "/uploadPdfAudioWeb", method = RequestMethod.POST)
	   @ResponseBody 
		public Reponse uploadPdf(@RequestParam("fichier") MultipartFile fichier,  @RequestParam("titre") String titre, @RequestParam("contenu")String contenu, @RequestParam("categorie")String categorie, @RequestParam("typeMedia")String typeMedia, HttpServletRequest request) {
		HttpSession session=request.getSession();

		Object obj=session.getAttribute("user");
		User user=null;
		UserDTO scope=(UserDTO) obj;
		user=userDAO.findByLogin(scope.getEmail());
		if(user==null) {
			return new Reponse(-1, "you are not signed in", null);	
		}
			
		Reponse reponse=mediaService.uploadPdf(fichier, titre, contenu, user.getEmail(), categorie, typeMedia);
			return reponse;
	   }
	@RequestMapping(value = "/uploadMediasWeb", method = RequestMethod.POST)
	public String uploadMedias(Model model, @RequestParam("fichier") MultipartFile fichier, @RequestParam("thumbnail") MultipartFile thumbnail, @RequestParam("titre") String titre, @RequestParam("contenu")String contenu, @RequestParam("categorie")int idCategorie, @RequestParam("typeMedia") String typeMedia, HttpServletRequest request) {
		if(!isLogged(request)) {
			return "forward:/";
		}
		HttpSession session=request.getSession();
		Object obj=session.getAttribute("user");
		User user=null;
		UserDTO scope=(UserDTO) obj;
		user=userDAO.findByLogin(scope.getEmail());
		Reponse reponse=null;
		if(user==null) {
			model.addAttribute("code", -1);
			model.addAttribute("message", "User not exist");
			return returnNewMediaView(idCategorie, model)	;
		}
		Categorie categorie=categorieDAO.findById(idCategorie);
		switch(typeMedia) {
			case "VIDEO":
				reponse=mediaService.uploadMedias(fichier, thumbnail, titre, contenu, user, categorie.getCategorie(), typeMedia);
				break; 
			case "AUDIO":
				reponse=mediaService.uploadPdf(fichier, titre, contenu, user.getEmail(), categorie.getCategorie(), typeMedia);
				break;
			case "BOOK":
				reponse=mediaService.uploadPdf(fichier, titre, contenu, user.getEmail(), categorie.getCategorie(), typeMedia);
				break;
		}
		if(reponse!=null && reponse.getCode()>0) {
			model.addAttribute("code", 1);
			model.addAttribute("message", "The media has been created successfuly");
			return serviceShowUploadsView(model, request);
		}else {
			model.addAttribute("code", -1);
			model.addAttribute("message", "User not exist");
			return returnNewMediaView(idCategorie, model)	;			
		}
	}
	@RequestMapping(value="/uploads", method = RequestMethod.GET)
	private String showUploadsView(Model model, HttpServletRequest request) {
		if(!isLogged(request)) {
			return "forward:/";
		}
		return serviceShowUploadsView(model, request);
	}
	
	private String serviceShowUploadsView(Model model, HttpServletRequest request) {
		User user=null;
		if(!isLogged(request)) {
			return "forward:/";
		}
		HttpSession session=request.getSession();
		Object obj=session.getAttribute("user");
		UserDTO scope=(UserDTO) obj;
		user=userDAO.findByLogin(scope.getEmail());
		if(user==null) {
			return "User not exists";	
		}
		ArrayList<Assignation> assignations=new ArrayList<>();
		if(user.getProfil().getProfil().equalsIgnoreCase("SUPER_ADMIN"))
			assignations=assignationDAO.findAllArticles();
		else {
			assignations=assignationDAO.findArticlesByUser(user.getEmail());
		}
		ArrayList<Assignation> videos= (ArrayList<Assignation>) assignations.stream().filter(a->a.getArticle().getTypeMedia().equalsIgnoreCase("VIDEO")).collect(Collectors.toList());
		ArrayList<Assignation> audios=(ArrayList<Assignation>)assignations.stream().filter(a->a.getArticle().getTypeMedia().equalsIgnoreCase("AUDIO")).collect(Collectors.toList());
		ArrayList<Assignation> books=(ArrayList<Assignation>) assignations.stream().filter(a->a.getArticle().getTypeMedia().equalsIgnoreCase("BOOK")).collect(Collectors.toList());
		
		//model.addAttribute("assignations", assignations);
		model.addAttribute("videos", videos);
		model.addAttribute("audios", audios);
		model.addAttribute("books", books);
		return "uploads";
	}
	@RequestMapping(value="/showValidationMediaWeb", method = RequestMethod.GET)
	private String showMediaValidationView(Model model, int id, HttpServletRequest request) {
		if(!isLogged(request)) {
			return "forward:/";
		}
		Assignation a=assignationDAO.findById(id).get();
		model.addAttribute("assignation", a);
		return "validation";
	}
	@RequestMapping(value="/showUserDetails", method = RequestMethod.GET)
	private String showUserInfos(Model model, HttpServletRequest request, String email) {
		User user=null;
		if(!isLogged(request)) {
			return "forward:/";
		}
		HttpSession session=request.getSession();
		Object obj=session.getAttribute("user");
		UserDTO scope=(UserDTO) obj;
		user=userDAO.findByLogin(scope.getEmail());
		if(user==null) {
			return "forward:/";
		}
		User personne=userDAO.findByLogin(email);
		ArrayList<Profil> profiles=(ArrayList<Profil>) profilDAO.findAll();
		model.addAttribute("user", personne);
		model.addAttribute("profils", profiles);
		return "infos_user";
	}	
	@RequestMapping(value="/setProfile", method = RequestMethod.POST)
	private String setProfile(Model model, HttpServletRequest request, String profil, String email) {
		User user=null;
		if(!isLogged(request)) {
			return "forward:/";
		}
		HttpSession session=request.getSession();
		Object obj=session.getAttribute("user");
		UserDTO scope=(UserDTO) obj;
		user=userDAO.findByLogin(scope.getEmail());
		if(user==null) {
			return "forward:/";
		}
		Profil p=profilDAO.findByName(profil);
		if(p==null) {
			return "forward:/";
		}
		User personne =userDAO.findByLogin(email);
		if(personne==null) {
			return "forward:/";
		}
		personne.setProfil(p);
		personne.setLastUpdate(new Date().getTime());
		
		userDAO.save(personne);
		ArrayList<Profil> profiles=(ArrayList<Profil>) profilDAO.findAll();
		model.addAttribute("user", personne);
		model.addAttribute("profils", profiles);
		return "infos_user";
	}
	@RequestMapping(value="/generateTokenWeb", method = RequestMethod.POST)
	private String generateTokenAndroid(Model model, String code, String email) {
		
	        Reponse result = accountService.generateCodeAndroid(email, "WEB");		  
	        if(result!=null) {
	        	if(result.getCode()==1){
	        		model.addAttribute("code", 1);
	        		  model.addAttribute("email", email);
	        		  model.addAttribute("type", "password");
	        		  model.addAttribute("message", "An activation code has been sent to your mailbox. Check it and fill the  field below");
	        		return "validationCode";
	        	}else {
	        		  model.addAttribute("email", email);
	        		model.addAttribute("code", -1);
	        		model.addAttribute("message", result.getMessage());
	        		return "passwordRecovery";
	        	}

	        }else {
	        	model.addAttribute("email", email);
	      		model.addAttribute("code", -1);
	      		model.addAttribute("message", result.getMessage());
	      		return "validationCode";       	
	        }		  
	}	
	
	@RequestMapping(value="/checkTokenWeb", method = RequestMethod.POST)
	private String checkTokenAndroid(Model model, String code, String email) {
	        Reponse result = accountService.checkToken(Integer.parseInt(code), email);		  
	        if(result!=null) {
	        	if(result.getCode()==1){
	        		model.addAttribute("email", email);
	        		return "resetPassword";
	        	}else {
	        		model.addAttribute("email", email);
	        		model.addAttribute("code", -1);
	        		model.addAttribute("message", result.getMessage());
	        		return "passwordRecovery";
	        	}
	        }else {
	        	model.addAttribute("email", email);
	      		model.addAttribute("code", -1);
	      		model.addAttribute("message", "An error occured");
	      		return "passwordRecovery";
	        }
	}
	
	@RequestMapping(value="/validationCodeWeb", method = RequestMethod.POST)
	private String validationCode(Model model, String code, String email, HttpServletRequest request) {
	        Reponse result = accountService.validateAccount(Integer.parseInt(code), email, "WEB");		  
	        if(result!=null) {
	        	if(result.getCode()==1){
	        		UserDTO u=(UserDTO) result.getObject();
	        		model.addAttribute("code", 1);
	        		model.addAttribute("user", u);
	    			HttpSession session=request.getSession();
	    			session.setAttribute("user", u);
	    			return showHomeRoot(model);
	        	}else {
	        		model.addAttribute("email", email);
	        		model.addAttribute("code", -1);
	        		model.addAttribute("message", result.getMessage());
	        		return "validationCode";
	        	}
	        }else {
	        	model.addAttribute("email", email);
	      		model.addAttribute("code", -1);
	      		model.addAttribute("message", "An error occured");
	      		return "validationCode";       	
	        }		  
	}

	private  Reponse seConnecter(String email, String password, String imei)
	{
        Reponse result = accountService.seConnecter(email, password, imei);
        return result;
	}
    public UserDTO inflateUser(LinkedHashMap <Object,Object> t){
     
        UserDTO u= new UserDTO();
            double statut=Double.parseDouble( t.get("statut").toString());
            double birthDate=Double.parseDouble( t.get("birthDate").toString());
            u.setBirthDate((long) birthDate);
            u.setStatut((int) statut);
            u.setPassword( t.getOrDefault("password", "").toString());
            u.setMAC( t.get("mac").toString());
            u.setPays( t.get("pays").toString());
            u.setEmail(t.get("email").toString());
            u.setPhone( t.get("phone").toString());
            u.setAddress(t.get("address").toString());
            u.setName(t.get("name").toString());
            u.setVille(t.get("ville").toString());
            u.setProfile( t.get("profile").toString());
         return u; 
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

	@Autowired AccountService accountService;
	   @PostMapping(value="/seConnecterWeb")
	   public String seConnecterWeb( @RequestParam("email")  String email, @RequestParam("password")  String password, @RequestParam("imei") String imei) {  
		   Gson gson = new Gson();  
		   try {
			   User user=userDAO.findbyLoginAndPassword(email, password);
			  
			   if(user!=null) {
				   if(!user.getMAC().trim().equalsIgnoreCase(imei)) {
					   if(!imei.equalsIgnoreCase("WEB")) {
						   return gson.toJson(new Reponse(6, "DEVICE", null));
					   }
				   }
				   if(user.getStatut()==0) {   // Si le compte n'est pas encore activé, alors voici la procédure
					   Token token=accountService.generateToken(user);
					   accountService.sendEmail(email, "Votre code d'activation est : "+token.getToken(), "Votre code d'activation Genius");
					   return gson.toJson(new Reponse(1, "TOKEN",null));
				   }
				   UserDTO u=accountService.toUserDTO(user);
				   return gson.toJson(new Reponse(1, "SUCCESS",u));
			   }else {		   
				   return gson.toJson(new Reponse(-1, "ERROR", null));
			   }
			   
		   }catch(Exception e) {
			   return gson.toJson(new Reponse(-1, "ERROR", null));
		   } 
	   }

}
