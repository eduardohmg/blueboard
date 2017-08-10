package br.com.blueboard.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import br.com.blueboard.entity.SchoolPeriod;

@Transactional
@Repository
public class SchoolPeriodDAO {
	
	@PersistenceContext
	private EntityManager entityManager;

	public SchoolPeriod save(SchoolPeriod schoolPeriod) {
		this.entityManager.persist(schoolPeriod);
		return schoolPeriod;
	}
	
	public SchoolPeriod insertIfNotExists(SchoolPeriod schoolPeriod) {
		try {
			schoolPeriod = findByCodSol(schoolPeriod.getCodSol());
		} catch (NoResultException ex){
			save(schoolPeriod);
		}
		return schoolPeriod;
	}
	
	public SchoolPeriod findByCodSol(String codSol) {
		return entityManager.createQuery("SELECT c FROM SchoolPeriod c WHERE codSol = :codSol", SchoolPeriod.class)
				.setParameter("codSol", codSol)
				.getSingleResult();
	}

	public List<SchoolPeriod> list() {
		return this.entityManager.createQuery("SELECT c FROM SchoolPeriod c", SchoolPeriod.class).getResultList();
	}
}
