package com.ivana.tema8.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PredmetDTO {

	@NotNull(message = "Naziv predmeta mora biti naveden.")
	@Size(min = 2, max = 20, message = "Naziv predmeta mora biti izmedju {min} and {max} karaktera.")
	private String nazivPredmeta;
	@NotNull(message = "Nedeljni fond casova mora biti naveden.")
	@Max (value = 50, message = "Nedeljni fond casova ne moze biti veci od 50 casova")
	private Integer nedeljniFondCasova;
	
	
	public PredmetDTO() {
		super();
	}

	public PredmetDTO(
			@NotNull(message = "Naziv predmeta mora biti naveden.") @Size(min = 2, max = 20, message = "Naziv predmeta mora biti izmedju {min} and {max} karaktera.") String nazivPredmeta,
			@NotNull(message = "Nedeljni fond casova mora biti naveden.") @Max(value = 50, message = "Nedeljni fond casova ne moze biti veci od 50 casova") Integer nedeljniFondCasova) {
		super();
		this.nazivPredmeta = nazivPredmeta;
		this.nedeljniFondCasova = nedeljniFondCasova;
	}



	public String getNazivPredmeta() {
		return nazivPredmeta;
	}


	public void setNazivPredmeta(String nazivPredmeta) {
		this.nazivPredmeta = nazivPredmeta;
	}


	public Integer getNedeljniFondCasova() {
		return nedeljniFondCasova;
	}


	public void setNedeljniFondCasova(Integer nedeljniFondCasova) {
		this.nedeljniFondCasova = nedeljniFondCasova;
	}
	
	
}
