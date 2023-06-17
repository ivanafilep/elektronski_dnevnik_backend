
package com.ivana.tema8.dto;

import javax.persistence.Column;

public class UserDTO {

	private String korisnik;
	private String token;
	private String role;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public UserDTO() {
		super();

	}

	public UserDTO(String korisnik, String token, String role) {
		super();
		this.korisnik = korisnik;
		this.token = token;
		this.role = role;
	}

	public String getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(String korisnik) {
		this.korisnik = korisnik;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	

}

