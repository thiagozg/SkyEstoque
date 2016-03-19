package model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.*;

import controller.BaseEntity;

@Entity
public class Produto implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue
	private long id;
	
	@Column(nullable=false, length=20, unique=true)
	@Size(min=1, max=20, message="O nome deve ter entre {min} e {max} caracteres.")
	@NotNull(message="Campo obrigatório!")
	private String nome;
	
	@Column(nullable=false, length=60)
	@Size(min=1, max=60, message="A descrição deve ter entre {min} e {max} caracteres.")
	@NotNull(message="Campo obrigatório!")
	private String descricao;
	
	@Column(nullable=false, length=4)
	@DecimalMin(value="1", message="A quantidade mínima deve ser 1.")
	@DecimalMax(value="9999", message="A quantidade máxima deve ser 9999.")
	@NotNull(message="Campo obrigatório!")
	private int quantidade;
	
	@Column(nullable=false, length=4)
	private int nvQuantidade;
	
	@ManyToOne
	@JoinColumn(name="grupo_id")
	private Grupo grupo;
	
	@ManyToOne
	@JoinColumn(name="fornec_id")
	private Fornecedor fornecedor;
	
	@Column(nullable=false, length=5)
	@DecimalMin(value="1", message="O valor mínima deve ser 1.")
	@DecimalMax(value="99999", message="O valor máximo deve ser 99999.")
	@NotNull(message="Campo obrigatório!")
	private int valor;

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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public int getNvQuantidade() {
		return nvQuantidade;
	}

	public void setNvQuantidade(int nvQuantidade) {
		this.nvQuantidade = nvQuantidade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nvQuantidade;
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result
				+ ((fornecedor == null) ? 0 : fornecedor.hashCode());
		result = prime * result + ((grupo == null) ? 0 : grupo.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + quantidade;
		result = prime * result + valor;
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
		Produto other = (Produto) obj;
		if (nvQuantidade != other.nvQuantidade)
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (fornecedor == null) {
			if (other.fornecedor != null)
				return false;
		} else if (!fornecedor.equals(other.fornecedor))
			return false;
		if (grupo == null) {
			if (other.grupo != null)
				return false;
		} else if (!grupo.equals(other.grupo))
			return false;
		if (id != other.id)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (quantidade != other.quantidade)
			return false;
		if (valor != other.valor)
			return false;
		return true;
	}

}
