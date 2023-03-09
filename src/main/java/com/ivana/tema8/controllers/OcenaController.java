package com.ivana.tema8.controllers;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ivana.tema8.dto.EmailDTO;
import com.ivana.tema8.entities.NastavnikPredmet;
import com.ivana.tema8.entities.Ocena;
import com.ivana.tema8.entities.Polugodiste;
import com.ivana.tema8.entities.Ucenik;
import com.ivana.tema8.repositories.NastavnikPredmetRepository;
import com.ivana.tema8.repositories.OcenaRepository;
import com.ivana.tema8.repositories.PolugodisteRepository;
import com.ivana.tema8.repositories.UcenikRepository;
import com.ivana.tema8.services.EmailServiceImpl;
import com.ivana.tema8.services.FileHandlerServiceImpl;
import com.ivana.tema8.services.OcenaService;

@RestController
@RequestMapping(path = "/api/v1/ocena")

public class OcenaController {
	
	@Autowired
	private OcenaRepository ocenaRepository;
	@Autowired
	private NastavnikPredmetRepository nastavnikPredmetRepository;
	@Autowired
	private UcenikRepository ucenikRepository;
	@Autowired
	private PolugodisteRepository polugodisteRepository;
	@Autowired
	OcenaService ocenaService;
	@Autowired
	private EmailServiceImpl emailService;
	
	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);
	
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(){
		logger.info("Getting all ocene.");
		return new ResponseEntity<Iterable<Ocena>>(ocenaRepository.findAll(), HttpStatus.OK);
	}
	
	
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateOcena(@PathVariable Integer id, @RequestParam Integer vrednostOcene,
			@RequestParam("nastavnikId") Integer nastavnikId, @RequestParam("predmetId") Integer predmetId) {
		Optional<Ocena> ocenaOptional = ocenaRepository.findById(id);

		if (!ocenaOptional.isPresent()) {
			return new ResponseEntity<>("Ocena nije pronadjena", HttpStatus.BAD_REQUEST);
		}

		Ocena existingOcena = ocenaOptional.get();
		NastavnikPredmet nastavnikPredmet = nastavnikPredmetRepository.findByNastavnikIdAndPredmetId(nastavnikId,
				predmetId);

		if (nastavnikPredmet == null) {
			return new ResponseEntity<>("Nastavnik ne predaje ovaj predmet.", HttpStatus.BAD_REQUEST);
		}

		existingOcena.setVrednostOcene(vrednostOcene);
		existingOcena.setNastavnikPredmet(nastavnikPredmet);

		Ocena updatedOcena = ocenaRepository.save(existingOcena);
		
		logger.info("Ocena je uspesno azurirana.");
		return new ResponseEntity<>("Ocena je uspesno azurirana.", HttpStatus.OK);
	}

	
	@RequestMapping(method = RequestMethod.POST, path = "/{vrednostOcene}/{nastavnikPredmetId}/{ucenikId}/{polugodisteId}")
	public ResponseEntity<?> createOcena( @PathVariable Integer vrednostOcene, @PathVariable Integer nastavnikPredmetId, @PathVariable Integer ucenikId, @PathVariable Integer polugodisteId) {
	    
	    Optional<NastavnikPredmet> nastavnikPredmet = nastavnikPredmetRepository.findById(nastavnikPredmetId);
	    Optional<Ucenik> ucenik = ucenikRepository.findById(ucenikId);
	    Optional<Polugodiste> polugodiste = polugodisteRepository.findById(polugodisteId);

	    if (!nastavnikPredmet.isPresent() || !ucenik.isPresent() || !polugodiste.isPresent()) {
	        return new ResponseEntity<>("NastavnikPredmet, ucenik ili polugodište nisu pronađeni", HttpStatus.NOT_FOUND);
	    }
	    
	    if (vrednostOcene > 5 || vrednostOcene < 1) {
	    	return new ResponseEntity<>("Ocena mora biti izmedju 1 i 5.", HttpStatus.NOT_FOUND);
	    }

	    Ocena ocena = new Ocena();
	    ocena.setVrednostOcene(vrednostOcene);
	    ocena.setNastavnikPredmet(nastavnikPredmet.get());
	    ocena.setUcenik(ucenik.get());
	    ocena.setPolugodiste(polugodiste.get());
	    
	    logger.info("Dodavanje nove ocene.");
		ocenaRepository.save(ocena);
	
		
	    EmailDTO emailDTO = new EmailDTO();
	    emailDTO.setTo("filepivana95@gmail.com");
	    emailDTO.setSubject("Nova ocena");
	    
	    emailDTO.setText("Vaše dete " + ocena.getUcenik().getIme() + " " + ocena.getUcenik().getPrezime() + " je dobilo ocenu " + ocena.getVrednostOcene() + " iz predmeta " + ocena.getNastavnikPredmet().getPredmet().getNazivPredmeta() + " od nastavnika " 
	    		 + ocena.getNastavnikPredmet().getNastavnik().getIme() + " " + ocena.getNastavnikPredmet().getNastavnik().getPrezime() + " .");
	    
	  
	    emailService.sendSimpleMessage(emailDTO);

	    logger.info("Nova ocena uspešno dodata.");
	    return new ResponseEntity<>("Ocena je uspešno dodata",  HttpStatus.OK);
	}
	


	// PRETRAGA OCENE PO ID-U
	@RequestMapping(method =RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getOcenaById (@PathVariable Integer id) {
		Optional<Ocena> ocena = ocenaRepository.findById(id);
		
		if (!ocena.isPresent()) {
			logger.warn("Ocena sa ID: {} nije pronadjena", id);
	        return new ResponseEntity<>("Ocena nije pronadjena", HttpStatus.NOT_FOUND);
	    }
		logger.info("Nova ocena uspešno nadjena.");
	    return new ResponseEntity<>(ocena.get(), HttpStatus.OK);
	}	
	
	
	
	//OBRISI OCENU
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteOcena(@PathVariable Integer id) {
		Optional<Ocena> ocena = ocenaRepository.findById(id);
			if (ocena.isEmpty()) {
				logger.warn("Zahtev sa brisanje ocene sa nepostojecim ID {}", id);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				logger.info("DELETE zahtev za brisanje ocene sa ID {}", id);
				ocenaRepository.delete(ocena.get());
				return new ResponseEntity<>("Ocena je obrisana", HttpStatus.OK);
			}
			
	}
	
	//pretraga ocena po predmetu I KADA NAPISEM NPR FIZIKA VRATI MI 200 OK PROVERI 
	@RequestMapping(method = RequestMethod.GET, path = "/predmet")
	public ResponseEntity<Iterable<Ocena>> findOcenaByPredmet(@RequestParam String nazivPredmeta) {
		
	    Iterable<Ocena> ocene = ocenaService.findOcenaByPredmet(nazivPredmeta);
	    logger.info("Ocene iz predmeta: {} su uspesno pronadjene", nazivPredmeta);
	    return new ResponseEntity<>(ocene, HttpStatus.OK);
	}
	
	
	//pretraga ocena po imenu ucenika I KADA NAPISEM NPR NECIJE IME KOJE NE POSTOJI VRATI MI 200 OK PROVERI 
	@RequestMapping(method = RequestMethod.GET, path = "/ime")
	public ResponseEntity<Iterable<Ocena>> findOcenaByIme(@RequestParam String ime) {
	    Iterable<Ocena> ocene = ocenaService.findOcenaByIme(ime);
	    logger.info("Ocene od ucenika sa imenom: {} su uspesno pronadjene", ime);
	    return new ResponseEntity<>(ocene, HttpStatus.OK);
	}
	
	//pretraga ocena po imenu ucenika, ali da ima i predmete iz kog je ta ocena ISTO KAO PRETHODNO
	@RequestMapping(method = RequestMethod.GET, path = "/imePredmet")
	public ResponseEntity<Iterable<Ocena>> findOcenaByImePredmet(@RequestParam String ime) {
	    Iterable<Ocena> ocene = ocenaService.findOcenaByImePredmet(ime);
	    logger.info("Ocene od ucenika sa imenom: {} su uspesno pronadjene", ime);
	    return new ResponseEntity<>(ocene, HttpStatus.OK);
	}
	
	//TAKODJE KAO PRETHODNO SVE
	@RequestMapping(method = RequestMethod.GET, path = "/imeIPredmet")
	public ResponseEntity<Iterable<Ocena>> findOcenaByImeIPredmet(@RequestParam String ime, @RequestParam String nazivPredmeta) {
	    Iterable<Ocena> ocene = ocenaService.findByPredmetIIme(ime, nazivPredmeta);
	    logger.info("Ocene od ucenika sa imenom: {}, iz predmeta: {} su uspesno pronadjene", ime, nazivPredmeta) ;
	    return new ResponseEntity<>(ocene, HttpStatus.OK);
	}
	
	
}
