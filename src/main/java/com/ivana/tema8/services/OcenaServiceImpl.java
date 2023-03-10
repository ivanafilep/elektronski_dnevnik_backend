package com.ivana.tema8.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ivana.tema8.dto.EmailDTO;
import com.ivana.tema8.entities.Korisnik;
import com.ivana.tema8.entities.Nastavnik;
import com.ivana.tema8.entities.NastavnikPredmet;
import com.ivana.tema8.entities.Ocena;
import com.ivana.tema8.entities.Polugodiste;
import com.ivana.tema8.entities.Predmet;
import com.ivana.tema8.entities.Ucenik;
import com.ivana.tema8.repositories.KorisnikRepository;
import com.ivana.tema8.repositories.NastavnikPredmetRepository;
import com.ivana.tema8.repositories.OcenaRepository;
import com.ivana.tema8.repositories.PolugodisteRepository;
import com.ivana.tema8.repositories.UcenikRepository;

@Service
public class OcenaServiceImpl implements OcenaService {

	@PersistenceContext
	private EntityManager em;

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	@Autowired
	private OcenaRepository ocenaRepository;
	@Autowired
	private NastavnikPredmetRepository nastavnikPredmetRepository;
	@Autowired
	private UcenikRepository ucenikRepository;
	@Autowired
	private PolugodisteRepository polugodisteRepository;
	@Autowired
	private EmailServiceImpl emailService;
	@Autowired
	private KorisnikRepository korisnikRepository;

	@Override
	public ResponseEntity<?> findOcenaByPredmet(String nazivPredmeta, Authentication authentication) {

		String email = (String) authentication.getName();

		Korisnik ulogovanKorisnik = korisnikRepository.findByEmail(email);
		
		String hql = "SELECT o.vrednostOcene " + "FROM Ocena o JOIN o.nastavnikPredmet np " + "JOIN np.predmet p "
				+ "WHERE p.nazivPredmeta = :nazivPredmeta ";
		Query query = em.createQuery(hql);
		query.setParameter("nazivPredmeta", nazivPredmeta);
		List<Ocena> rezultat = query.getResultList();

		if (rezultat.isEmpty()) {
			return new ResponseEntity<>("Predmet ne postoji.", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<Ocena>>(rezultat, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> findOcenaByIme(String ime, Authentication authentication) {
		
		String email = (String) authentication.getName();
		
		Korisnik ulogovanKorisnik = korisnikRepository.findByEmail(email);
		
		

		String hql = "SELECT o.vrednostOcene " + "FROM Ocena o " + "INNER JOIN o.ucenik u "
				+ "WHERE u.ime = :imeUcenika";
		Query query = em.createQuery(hql);
		query.setParameter("imeUcenika", ime);
		List<Ocena> rezultat =  query.getResultList();
		
		if (rezultat.isEmpty()) {
			return new ResponseEntity<>("Ucenik ne postoji.", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<Ocena>>(rezultat, HttpStatus.OK);
	}


	@Override
	public ResponseEntity<?> findOcenaByImePredmet(String ime, Authentication authentication) {
		
		String email = (String) authentication.getName();

		Korisnik ulogovanKorisnik = korisnikRepository.findByEmail(email);
		String hql = "SELECT o.vrednostOcene, p.nazivPredmeta " + "FROM Ocena o " + "JOIN o.ucenik u "
				+ "JOIN o.nastavnikPredmet np " + "JOIN np.predmet p " + "WHERE u.ime = :ime ";

		Query query = em.createQuery(hql);
		query.setParameter("ime", ime);
		List<Ocena> rezultat = query.getResultList();

		if (rezultat.isEmpty()) {
			return new ResponseEntity<>("Ucenik ne postoji.", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<Ocena>>(rezultat, HttpStatus.OK);
	}
	
	// nalazi ocene po imenu ucenika i imenu predmeta
	public ResponseEntity<?> findByPredmetIIme(String ime, String nazivPredmeta, Authentication authentication) {

		String email = (String) authentication.getName();

		Korisnik ulogovanKorisnik = korisnikRepository.findByEmail(email);

		String hql = "SELECT o.vrednostOcene, p.nazivPredmeta " + "FROM Ocena o " + "INNER JOIN o.ucenik u "
				+ "INNER JOIN o.nastavnikPredmet np " + "INNER JOIN np.predmet p "
				+ "WHERE u.ime = :ime AND p.nazivPredmeta = :nazivPredmeta ";
		Query query = em.createQuery(hql);
		query.setParameter("ime", ime);
		query.setParameter("nazivPredmeta", nazivPredmeta);
		List<Ocena> rezultat = query.getResultList();

		if (rezultat.isEmpty()) {
			return new ResponseEntity<>("Predmet ne postoji.", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<Ocena>>(rezultat, HttpStatus.OK);
	}

	// UPDATE OCENA
	public ResponseEntity<?> updateOcena(@PathVariable Integer id, @RequestParam Integer vrednostOcene,
			@RequestParam("nastavnikId") Integer nastavnikId, @RequestParam("predmetId") Integer predmetId, Authentication authentication) {
		Optional<Ocena> ocenaOptional = ocenaRepository.findById(id);

		String email = (String) authentication.getName();

		Korisnik ulogovanKorisnik = korisnikRepository.findByEmail(email);

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

	//DAVANJE OCENE
	public ResponseEntity<?> createOcena(@PathVariable Integer vrednostOcene, @PathVariable Integer nastavnikPredmetId,
			@PathVariable Integer ucenikId, @PathVariable Integer polugodisteId, Authentication authentication) {

		String email = (String) authentication.getName();

		Korisnik ulogovanKorisnik = korisnikRepository.findByEmail(email);
		
		

		Optional<NastavnikPredmet> nastavnikPredmet = nastavnikPredmetRepository.findById(nastavnikPredmetId);
		Optional<Ucenik> ucenik = ucenikRepository.findById(ucenikId);
		Optional<Polugodiste> polugodiste = polugodisteRepository.findById(polugodisteId);

		
		if (!nastavnikPredmet.isPresent() || !ucenik.isPresent() || !polugodiste.isPresent()) {
			return new ResponseEntity<>("NastavnikPredmet, ucenik ili polugodište nisu pronađeni",
					HttpStatus.NOT_FOUND);
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

		emailDTO.setText("Vaše dete " + ocena.getUcenik().getIme() + " " + ocena.getUcenik().getPrezime()
				+ " je dobilo ocenu " + ocena.getVrednostOcene() + " iz predmeta "
				+ ocena.getNastavnikPredmet().getPredmet().getNazivPredmeta() + " od nastavnika "
				+ ocena.getNastavnikPredmet().getNastavnik().getIme() + " "
				+ ocena.getNastavnikPredmet().getNastavnik().getPrezime() + " .");

		emailService.sendSimpleMessage(emailDTO);

		logger.info("Nova ocena uspešno dodata.");
		return new ResponseEntity<>("Ocena je uspešno dodata", HttpStatus.OK);
	}

	// OBRISI OCENU
	public ResponseEntity<?> deleteOcena(@PathVariable Integer id, Authentication authentication) {
		Optional<Ocena> ocena = ocenaRepository.findById(id);
		String email = (String) authentication.getName();

		Korisnik ulogovanKorisnik = korisnikRepository.findByEmail(email);

		if (ocena.isEmpty()) {
			logger.warn("Zahtev sa brisanje ocene sa nepostojecim ID {}", id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			logger.info("DELETE zahtev za brisanje ocene sa ID {}", id);
			ocenaRepository.delete(ocena.get());
			return new ResponseEntity<>("Ocena je obrisana", HttpStatus.OK);
		}

	}
	

	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

	}

}
