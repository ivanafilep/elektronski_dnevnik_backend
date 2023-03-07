package com.ivana.tema8.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PolugodisteDTO {
	

	@NotNull(message = "Polugodiste mora biti navedeno.")
	@Max (value = 2, message = "Polugodiste ne moze biti vece od 2.")
	private Integer brojPolugodista;
	
	
	public PolugodisteDTO() {
		super();
	}



	public PolugodisteDTO(
			@NotNull(message = "Polugodiste mora biti navedeno.") @Max(value = 2, message = "Polugodiste ne moze biti vece od 2.") Integer brojPolugodista) {
		super();
		this.brojPolugodista = brojPolugodista;
	}



	public Integer getBrojPolugodista() {
		return brojPolugodista;
	}
	public void setBrojPolugodista(Integer brojPolugodista) {
		this.brojPolugodista = brojPolugodista;
	}
	
	

}
