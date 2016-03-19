package controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.JpaUtil;
import model.Fornecedor;

@ManagedBean
public class FornecedorBean {

	FacesContext msg = FacesContext.getCurrentInstance();
	EntityManager em = JpaUtil.getEntityManager();
	
	private Fornecedor fornecedor = new Fornecedor();
	private List<Fornecedor> fornecedores;
	private List<Fornecedor> filtroFornecedores;
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public List<Fornecedor> getFornecedores() {
		if (this.fornecedores == null){
			try {
				this.fornecedores = listar();
			} catch (Exception e) {
				msg.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: " + e.getMessage(), null));
			}
		}
		return fornecedores;
	}
	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}
	
	public List<Fornecedor> getFiltroFornecedores() {
		return filtroFornecedores;
	}
	public void setFiltroFornecedores(List<Fornecedor> filtroFornecedores) {
		this.filtroFornecedores = filtroFornecedores;
	}
	
	@SuppressWarnings("unchecked")
	public List<Fornecedor> listar(){
		List<Fornecedor> listagem;
		Query q = em.createQuery("select f from Fornecedor f", Fornecedor.class);
		listagem = q.getResultList();
		
		return listagem;
	}
	
	public String salva(Fornecedor fornecedor){
		
		if(this.totalFornecedores(fornecedor) > 0){
			msg.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O fornecedor " + fornecedor.getNome() + " já existe no banco de dados", null));
		}else{		
			em.getTransaction().begin();
			try {
				if(fornecedor.getId() == null){
					em.persist(fornecedor); 
				}else{ 
					fornecedor = em.merge(fornecedor);
				}
				
				em.getTransaction().commit(); 
				this.fornecedores = listar(); 
				msg.addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_INFO, "O fornecedor " + fornecedor.getNome() +
						 	   " foi armazenada com sucesso!", null));
				
				return "_listafornec";		
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
		return "_addfornec";
	}
	
	public long totalFornecedores(Fornecedor fornecedor){
		long total = 0;
		
		try{
			Query sql = em.createQuery("select count(nome) from Fornecedor f where f.nome = :nome and f.id <> :id");
			sql.setParameter("id", fornecedor.getId());
			sql.setParameter("nome", fornecedor.getNome());
			total = (Long) sql.getSingleResult(); 
		}catch (Exception e){
			System.out.println("Erro na query:" + e.getMessage());
		}
		
		return total;		
	}
	
	public String altera(){
		return "_addfornec";
	}
	
	public void excluir(Fornecedor fornecedor){
		try {
			
			Fornecedor f = em.find(Fornecedor.class, this.fornecedor.getId());
			
			em.getTransaction().begin(); 	
			em.remove(f);				 				
			em.getTransaction().commit();	
			this.fornecedores = listar();			
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
