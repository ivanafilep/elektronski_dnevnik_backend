package com.ivana.tema8.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "korisnik")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Korisnik {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column
	@NotNull(message = "Korisnicko ime mora biti navedeno.")
	@Size(min = 2, max = 15, message = "Korisnicko ime mora biti izmedju {min} and {max} karaktera.")
	private String korisnickoIme;
	
	@Column
	@NotNull(message = "Lozinka mora biti navedena.")
	@Size(min = 2, max = 15, message = "Lozinka mora biti izmedju {min} and {max} karaktera.")
	private String lozinka;
	
	@Column
	@NotNull(message = "Ime mora biti navedeno.")
	@Size(min = 2, max = 30, message = "Ime mora biti izmedju {min} and {max} karaktera.")
	private String ime;
	
	@Column
	@NotNull(message = "Prezime mora biti navedeno.")
	@Size(min = 2, max = 30, message = "Prezime mora biti izmedju {min} and {max} karaktera.")
	private String prezime;
	
	@Column
	@NotNull(message = "Email mora biti naveden.")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
	message="Email nije validan.")
	private String email;
	
	@JsonIgnore // da li ovde ovo ili sta
	@NotNull(message = "Potvrdjena lozinka mora biti navedena.")
	@Size(min = 2, max = 15, message = "Potvrdjena lozinka mora biti izmedju {min} and {max} karaktera.")
	private String potvrdjenaLozinka;
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "role")
	protected RoleEntity role;

	 
	public Korisnik() {
		super();
	}

	public Korisnik(Integer id, String korisnickoIme, String lozinka, String ime, String prezime, String email, String potvrdjenaLozinka, RoleEntity role) {
		super();
		
		this.id = id;
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.email = email;
		this.potvrdjenaLozinka = potvrdjenaLozinka;
		this.role = role;
		
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

	public String getPotvrdjenaLozinka() {
		return potvrdjenaLozinka;
	}

	public void setPotvrdjenaLozinka(String potvrdjenaLozinka) {
		this.potvrdjenaLozinka = potvrdjenaLozinka;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	
	 
}
