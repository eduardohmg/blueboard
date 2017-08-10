package br.com.blueboard.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SchoolPeriod implements Serializable {
	private static final long serialVersionUID = 5191794227455338794L;
	
	@Id 
	@GeneratedValue
	private Long id;
	
	@Column
	private String codSol;
	
	@Column
	private String sglSol;
	
	@Column
	private Boolean closed;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodSol() {
		return codSol;
	}

	public void setCodSol(String codSol) {
		this.codSol = codSol;
	}

	public String getSglSol() {
		return sglSol;
	}

	public void setSglSol(String sglSol) {
		this.sglSol = sglSol;
	}

	public Boolean getClosed() {
		return closed;
	}

	public void setClosed(Boolean closed) {
		this.closed = closed;
	}
}
