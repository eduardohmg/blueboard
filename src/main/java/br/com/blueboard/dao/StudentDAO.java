package br.com.blueboard.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import br.com.blueboard.entity.Student;

@Transactional
@Repository
public class StudentDAO {
	@PersistenceContext
	private EntityManager entityManager;

	public Student save(Student Student) {
		entityManager.persist(Student);
		return Student;
	}
	
	public Student insertIfNotExists(Student Student) {
		try {
			Student = findByCodSol(Student.getCodSol());
		} catch (NoResultException ex){
			save(Student);
		}
		return Student;
	}
	
	public Student findByCodSol(String codSol) {
		return entityManager.createQuery("SELECT c FROM Student c WHERE codSol = :codSol", Student.class)
				.setParameter("codSol", codSol)
				.getSingleResult();
	}
	
	public Student findByToken(String token) {
		return entityManager.createQuery("SELECT c FROM Student c WHERE token = :token", Student.class)
				.setParameter("token", token)
				.getSingleResult();
	}

	public List<Student> list() {
		return entityManager.createQuery("SELECT c FROM Student c", Student.class).getResultList();
	}
	
	public Student findByLoginPass(String number, String password) {
		try {
		return entityManager.createQuery("SELECT c FROM Student c WHERE number = :number AND password = :password", Student.class)
				.setParameter("number", number)
				.setParameter("password", password)
				.getSingleResult();
		} catch (Exception ex) {
			return null;
		}
	}
	
	public Student findByLogin(String number) {
		try {
		return entityManager.createQuery("SELECT c FROM Student c WHERE number = :number", Student.class)
				.setParameter("number", number)
				.getSingleResult();
		} catch (Exception ex) {
			return null;
		}
	}
}
