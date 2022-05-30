package com.faguy.genius.model.DTO;

import com.faguy.genius.entity.Profil;
import com.faguy.genius.entity.User;

public class UserDTO {
	private String name;
	private String surname;
	private ProfilDTO profil;
	private VilleDTO city;
	private String email;
	private String phone;
	private String address;
	private String photo;
	private String login;
	private String ville;
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public String getPays() {
		return pays;
	}
	public void setPays(String pays) {
		this.pays = pays;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}

	private String pays;
	private String profile;
	@Override
	public String toString() {
		return "User []";
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public static UserDTO toDTO(User user) {
		UserDTO u=new UserDTO();
		u.setAddress(user.getAddress());
		u.setName(user.getName());
		u.setSurname(user.getSurname());
		u.setBirthDate(user.getBirthDate().getTime());
		u.setProfil(new ProfilDTO(user.getProfil().getProfil(), user.getProfil().getDetails()));
		u.setPays(user.getCity().getNom());
		u.setEmail(user.getEmail());
		u.setPassword(user.getPassword());
		u.setMAC(user.getMAC());
		u.setPhone(user.getPhone());
		u.setPhoto(user.getPhoto());
		u.setVille(user.getCity().getNom());
		return u;
	}
	public ProfilDTO getProfil() {
		return profil;
	}
	public void setProfil(ProfilDTO profil) {
		this.profil = profil;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	
	public UserDTO(String email) {
		super();
		this.email = email;
	}
	public UserDTO(String email, String name) {
		super();
		this.email = email;
		this.name=name;
	}
	public long getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(long birthDate) {
		this.birthDate = birthDate;
	}

	private long birthDate;
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMAC() {
		return MAC;
	}
	public void setMAC(String mAC) {
		MAC = mAC;
	}
	public int getStatut() {
		return statut;
	}
	public UserDTO(String login, String name,  ProfilDTO profil, VilleDTO city, String email, String phone,
			String address, long birthDate, String password, String MAC, int statut) {
		super();
		this.name = name;
		this.profil = profil;
		this.city=city;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.birthDate = birthDate;
		this.password = password;
		this.MAC = MAC;
		this.statut = statut;
		this.login=login;
	}
	
	public UserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setStatut(int statut) {
		this.statut = statut;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public VilleDTO getCity() {
		return city;
	}
	public void setCity(VilleDTO city) {
		this.city = city;
	}

	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}

	private String password;
	private String MAC;
	
    private  int statut;   // 0=EN ATTENTE, 1=ACTIF, -1=BLOQUE

	public UserDTO(String name, String surname, String email, String login) {
		super();
		this.name = name;
		this.email = email;
		this.login = login;
		this.surname=surname;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
}
