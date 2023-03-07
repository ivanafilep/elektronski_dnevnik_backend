package com.ivana.tema8.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.ivana.tema8.entities.Ocena;

@Service
public class OcenaServiceImpl implements OcenaService {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Iterable<Ocena> findOcenaByPredmet(String nazivPredmeta) {
		String hql = "SELECT o.vrednostOcene "
				+ "FROM Ocena o JOIN o.nastavnikPredmet np "
				+"JOIN np.predmet p "
				+ "WHERE p.nazivPredmeta = :nazivPredmeta ";
		Query query  = em.createQuery(hql);
		query.setParameter("nazivPredmeta", nazivPredmeta);
		return query.getResultList();
	}
	
	@Override
	public Iterable<Ocena> findOcenaByIme(String ime) {
		String hql = "SELECT o.vrednostOcene "
				+ "FROM Ocena o "
				+ "INNER JOIN o.ucenik u "
				+ "WHERE u.ime = :imeUcenika";
		Query query  = em.createQuery(hql);
		query.setParameter("imeUcenika", ime);
		return query.getResultList();
	}
	
	@Override
	public Iterable<Ocena> findOcenaByImePredmet(String ime) {
		String hql = "SELECT o.vrednostOcene, p.nazivPredmeta "
				+ "FROM Ocena o "
				+ "JOIN o.ucenik u "
				+ "JOIN o.nastavnikPredmet np "
				+ "JOIN np.predmet p "
				+ "WHERE u.ime = :ime ";


		Query query  = em.createQuery(hql);
		query.setParameter("ime", ime);
		return query.getResultList();
	
	}

	
	public Iterable<Ocena> findByPredmetIIme (String ime, String nazivPredmeta) {
		String hql = "SELECT o.vrednostOcene, p.nazivPredmeta "
				+ "FROM Ocena o "
				+ "INNER JOIN o.ucenik u "
				+ "INNER JOIN o.nastavnikPredmet np "
				+ "INNER JOIN np.predmet p "
				+ "WHERE u.ime = :ime AND p.nazivPredmeta = :nazivPredmeta ";
		Query query  = em.createQuery(hql);
		query.setParameter("ime", ime);
		query.setParameter("nazivPredmeta", nazivPredmeta);
		return query.getResultList();
	}

}
