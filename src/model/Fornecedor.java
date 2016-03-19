package model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.*;

import controller.BaseEntity;

@Entity
public class Fornecedor implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue
	private long id;
	
	@Column(nullable=false, length=20, unique=true)
	@Size(min=1, max=20, message="O nome deve ter entre {min} e {max} caracteres.")
	@NotNull(message="Campo obrigatório!")
	private String nome;
	
	@Column(nullable=false, length=60)
	@Size(min=1, max=60, message="O endereço deve ter entre {min} e {max} caracteres.")
	@NotNull(message="Campo obrigatório!")
	private String endereco;
	
	@Column(nullable=false, length=9, unique=true)
	@Size(min=8, max=9, message="O telefone deve ter {min} ou {max} caracteres.")
	@NotNull(message="Campo obrigatório!")
	private String telefone;

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

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result
				+ ((telefone == null) ? 0 : telefone.hashCode());
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
		Fornecedor other = (Fornecedor) obj;
		if (endereco == null) {
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		if (id != other.id)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		return true;
	}
	
	

}
