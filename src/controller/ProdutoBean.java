package controller;

import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.*;


@ManagedBean
public class ProdutoBean {

	FacesContext msg = FacesContext.getCurrentInstance();
	EntityManager em = JpaUtil.getEntityManager();
	
	private Produto produto = new Produto();
	private List<Produto> produtos;
	private List<Produto> filtroProdutos;
	
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public List<Produto> getProdutos() {
		if (this.produtos == null){
			try {
				this.produtos = listar();
			} catch (Exception e) {
				msg.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: " + e.getMessage(), null));
			}
		}
		return produtos;
	}
	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}
	
	public List<Produto> getFiltroProdutos() {
		return filtroProdutos;
	}
	public void setFiltroProdutos(List<Produto> filtroProdutos) {
		this.filtroProdutos = filtroProdutos;
	}
	
	@SuppressWarnings("unchecked")
	public List<Produto> listar(){
		List<Produto> listagem;
		Query q = em.createQuery("select p from Produto p", Produto.class);
		listagem = q.getResultList();
		
		return listagem;
	}
	
	public String salva(Produto produto){
		
		if(this.totalProdutos(produto) > 0){
			msg.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O produto " + produto.getNome() + " já existe no banco de dados", null));
		}else{		
			em.getTransaction().begin();
			try {
				if(produto.getId() == null){
					em.persist(produto); 
				}else{ 
					produto = em.merge(produto);
				}
				
				em.getTransaction().commit(); 
				this.produtos = listar(); 
				msg.addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_INFO, "O produto " + produto.getNome() +
						 	   " foi armazenada com sucesso!", null));
				
				return "_listaproduto";		
			} catch (Exception e) {
				
				if(em.getTransaction().isActive()){
					try {
						em.getTransaction().rollback();
					} catch (Exception er) {
						System.out.println("Erro no rollback: " + er.getMessage());
					}
					
				msg.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: " + e.getMessage(), null));
				}
			}
		}
		return "_addproduto";
	}
	
	public String adicionar(Produto produto){			
		em.getTransaction().begin();
		
		int soma = produto.getQuantidade() + produto.getNvQuantidade();		
		produto.setQuantidade(soma);
		produto.setNvQuantidade(0);
		
		try {
			if(produto.getId() == null){
				em.persist(produto); 
			}else{ 
				produto = em.merge(produto);
			}
			
			
			
			em.getTransaction().commit(); 
			this.produtos = listar(); 
			msg.addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_INFO, "O estoque do produto: " + produto.getNome() +
					 	   " foi atualizado!", null));
			
			return "_listaestoque";		
		} catch (Exception e) {
			
			if(em.getTransaction().isActive()){
				try {
					em.getTransaction().rollback();
				} catch (Exception er) {
					System.out.println("Erro no rollback: " + er.getMessage());
				}
				
			msg.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: " + e.getMessage(), null));
			}
		}
			
		return "_addestoque";
	}
	
	public String remover(Produto produto){			
		em.getTransaction().begin();
		
		int soma = produto.getQuantidade() - produto.getNvQuantidade();		
		produto.setQuantidade(soma);
		produto.setNvQuantidade(0);
		
		try {
			if(produto.getId() == null){
				em.persist(produto); 
			}else{ 
				produto = em.merge(produto);
			}
			
			
			
			em.getTransaction().commit(); 
			this.produtos = listar(); 
			msg.addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_INFO, "O estoque do produto: " + produto.getNome() +
					 	   " foi atualizado!", null));
			
			return "_listaestoque";		
		} catch (Exception e) {
			
			if(em.getTransaction().isActive()){
				try {
					em.getTransaction().rollback();
				} catch (Exception er) {
					System.out.println("Erro no rollback: " + er.getMessage());
				}
				
			msg.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: " + e.getMessage(), null));
			}
		}
			
		return "_addestoque";
	}
	
	public long totalProdutos(Produto produto){
		long total = 0;
		
		try{
			Query sql = em.createQuery("select count(nome) from Produto p where p.nome = :nome and p.id <> :id");
			sql.setParameter("id", produto.getId());
			sql.setParameter("nome", produto.getNome());
			total = (Long) sql.getSingleResult(); 
		}catch (Exception e){
			System.out.println("Erro na query:" + e.getMessage());
		}
		
		return total;		
	}
	
	public String altera(){
		return "_addproduto";
	}
	
	public void excluir(Produto produto){
		try {
			
			Produto p = em.find(Produto.class, this.produto.getId());
			
			em.getTransaction().begin(); 	
			em.remove(p);				 				
			em.getTransaction().commit();	
			this.produtos = listar();			
		} catch (Exception e) {
			if(em.getTransaction().isActive()){ 
				try {	
					em.getTransaction().rollback();
				} catch (Exception e2) {
					System.out.println("Erro no Rollback: " + e2.getMessage());
				}
			}
			msg.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao excluir: " + e.getMessage(), null));
		}
		return;
	}
	
}
