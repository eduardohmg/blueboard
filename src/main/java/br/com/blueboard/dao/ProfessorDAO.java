package br.com.blueboard.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.blueboard.entity.Professor;

@Repository
public class ProfessorDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public Professor save(Professor professor) {
		this.entityManager.persist(professor);
		return professor;
	}
	
	public Professor insertIfNotExists(Professor professor) {
		try {
			professor = findByCodSol(professor.getCodSol());
		} catch (NoResultException ex){
			save(professor);
		}
		return professor;
	}
	
	public Professor findByCodSol(String codSol) {
		return entityManager.createQuery("SELECT c FROM Professor c WHERE codSol = :codSol", Professor.class)
				.setParameter("codSol", codSol)
				.getSingleResult();
	}

	public List<Professor> list() {
		return this.entityManager.createQuery("SELECT c FROM Professor c", Professor.class).getResultList();
	}
}
