package com.ivana.tema8.entities;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.ivana.tema8.security.Views;


@Entity
@Table (name = "role")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class RoleEntity {
	
	
	@Id
	@GeneratedValue
	@Column(name = "role_id")
	private Integer id;
	
	
	@Column(name = "role_ime")
	private String ime;
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<Korisnik> korisnici = new ArrayList<>();

	public RoleEntity() {
		super();
	}

	public RoleEntity(Integer id, String ime, List<Korisnik> korisnici) {
		super();
		this.id = id;
		this.ime = ime;
		this.korisnici = korisnici;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public List<Korisnik> getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(List<Korisnik> korisnici) {
		this.korisnici = korisnici;
	}
	
	
	
	
	
	

}
