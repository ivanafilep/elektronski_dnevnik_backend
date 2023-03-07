package com.ivana.tema8.dto;


import javax.validation.Valid;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
@Valid
public class OcenaDTO {

	
	@NotNull(message = "Ocena mora biti navedena.")
	
	@Min(value = 1, message = "Vrednost ocene ne sme biti manja od 1")
    @Max(value = 5, message = "Vrednost ocene ne sme biti veća od 5")
	private Integer vrednostOcene;
	
	
	public OcenaDTO() {
		super();
	}


	public OcenaDTO(
			@NotNull(message = "Ocena mora biti navedena.") @Min(value = 1, message = "Vrednost ocene ne sme biti manja od 1") @Max(value = 5, message = "Vrednost ocene ne sme biti veća od 5") Integer vrednostOcene) {
		super();
		this.vrednostOcene = vrednostOcene;
	}


	public Integer getVrednostOcene() {
		return vrednostOcene;
	}

	public void setVrednostOcene(Integer vrednostOcene) {
		this.vrednostOcene = vrednostOcene;
	}
	
	
	
	
}
