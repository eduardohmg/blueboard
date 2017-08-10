package br.com.blueboard.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.blueboard.entity.Subject;

@Repository
public class SubjectDAO {
	@PersistenceContext
	private EntityManager entityManager;

	public Subject save(Subject subject) {
		entityManager.persist(subject);
		return subject;
	}
	
	public Subject insertIfNotExists(Subject Subject) {
		try {
			Subject = findByCodSol(Subject.getCodSol());
		} catch (NoResultException ex){
			save(Subject);
		}
		return Subject;
	}
	
	public Subject findByCodSol(String codSol) {
		return entityManager.createQuery("SELECT c FROM Subject c WHERE codSol = :codSol", Subject.class)
				.setParameter("codSol", codSol)
				.getSingleResult();
	}

	public List<Subject> list() {
		return entityManager.createQuery("SELECT c FROM Subject c", Subject.class).getResultList();
	}
}
