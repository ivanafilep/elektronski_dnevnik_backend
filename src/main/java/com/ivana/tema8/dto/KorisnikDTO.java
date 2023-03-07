package com.ivana.tema8.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class KorisnikDTO {
	
	@NotNull(message = "Korisnicko ime mora biti navedeno.")
	@Size(min = 2, max = 15, message = "Korisnicko ime mora biti izmedju {min} and {max} karaktera.")
	private String korisnickoIme;
	
	@NotNull(message = "Lozinka mora biti navedena.")
	@Size(min = 2, max = 15, message = "Lozinka mora biti izmedju {min} and {max} karaktera.")
	private String lozinka;
	
	@NotNull(message = "Ime mora biti navedeno.")
	@Size(min = 2, max = 30, message = "Ime mora biti izmedju {min} and {max} karaktera.")
	private String ime;
	
	@NotNull(message = "Prezime mora biti navedeno.")
	@Size(min = 2, max = 30, message = "Prezime mora biti izmedju {min} and {max} karaktera.")
	private String prezime;
	
	@NotNull(message = "Email mora biti naveden.")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
	message="Email is not valid.")
	private String email;
	
	/*
	@NotNull(message = "Potvrdjena lozinka mora biti navedena.")
	@Size(min = 2, max = 15, message = "Potvrdjena lozinka mora biti izmedju {min} and {max} karaktera.")
	private String potvrdjenaLozinka;
	*/
	
	
	public KorisnikDTO() {
		super();
	}

	

	public KorisnikDTO(
			@NotNull(message = "Korisnicko ime mora biti navedeno.") @Size(min = 2, max = 15, message = "Korisnicko ime mora biti izmedju {min} and {max} karaktera.") String korisnickoIme,
			@NotNull(message = "Lozinka mora biti navedena.") @Size(min = 2, max = 15, message = "Lozinka mora biti izmedju {min} and {max} karaktera.") String lozinka,
			@NotNull(message = "Ime mora biti navedeno.") @Size(min = 2, max = 30, message = "Ime mora biti izmedju {min} and {max} karaktera.") String ime,
			@NotNull(message = "Prezime mora biti navedeno.") @Size(min = 2, max = 30, message = "Prezime mora biti izmedju {min} and {max} karaktera.") String prezime,
			@NotNull(message = "Email mora biti naveden.") @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email is not valid.") String email) {
		super();
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.email = email;
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

	
	
	
	
	

}
