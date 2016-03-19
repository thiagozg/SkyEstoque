package model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.*;

import controller.BaseEntity;

@Entity
public class Grupo implements BaseEntity, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue
	private long id;
	
	@Column(nullable=false, length=20, unique=true)
	@Size(min=1, max=20, message="O nome deve ter entre {min} e {max} caracteres.")
	@NotNull(message="Campo obrigatório!")
	private String nome;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Grupo other = (Grupo) obj;
		if (id != other.id)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	
	
}
