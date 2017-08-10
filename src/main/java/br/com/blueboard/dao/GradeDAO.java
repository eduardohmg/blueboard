package br.com.blueboard.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.blueboard.entity.Grade;

@Repository
public class GradeDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public Grade save(Grade Grade) {
		this.entityManager.persist(Grade);
		return Grade;
	}
	
	public Grade insertIfNotExists(Grade grade) {
		try {
			grade = findByCodSol(grade.getCodSol());
		} catch (NoResultException ex){
			save(grade);
		}
		return grade;
	}
	
	public Grade findByCodSol(String codSol) {
		return entityManager.createQuery("SELECT c FROM Grade c WHERE codSol = :codSol", Grade.class)
				.setParameter("codSol", codSol)
				.getSingleResult();
	}

	public List<Grade> list() {
		return this.entityManager.createQuery("SELECT c FROM Grade c", Grade.class).getResultList();
	}
}
