package controller;

import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.*;


@ManagedBean
public class GrupoBean {
	
	FacesContext msg = FacesContext.getCurrentInstance();
	EntityManager em = JpaUtil.getEntityManager();
	
	private Grupo grupo = new Grupo();
	private List<Grupo> grupos;
	private List<Grupo> filtroGrupos;
	
	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	
	public List<Grupo> getGrupos() {
		if (this.grupos == null){
			try {
				this.grupos = listar();
			} catch (Exception e) {
				msg.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: " + e.getMessage(), null));
			}
		}
		return grupos;
	}
	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}
	
	public List<Grupo> getFiltroGrupos() {
		return filtroGrupos;
	}
	public void setFiltroGrupos(List<Grupo> filtroGrupos) {
		this.filtroGrupos = filtroGrupos;
	}
	
	@SuppressWarnings("unchecked")
	public List<Grupo> listar(){
		List<Grupo> listagem;
		Query q = em.createQuery("select g from Grupo g", Grupo.class);
		listagem = q.getResultList();
		
		return listagem;
	}
	
	public String salva(Grupo grupo){
		
		if(this.totalGrupos(grupo) > 0){
			msg.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O grupo " + grupo.getNome() + " já existe no banco de dados", null));
		}else{		
			em.getTransaction().begin();
			try {
				if(grupo.getId() == null){
					em.persist(grupo); 
				}else{ 
					grupo = em.merge(grupo);
				}
				
				em.getTransaction().commit(); 
				this.grupos = listar(); 
				msg.addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_INFO, "O grupo " + grupo.getNome() +
						 	   " foi armazenada com sucesso!", null));
				
				return "_listagrupo";		
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
		return "_addgrupo";
	}
	
	public long totalGrupos(Grupo grupo){
		long total = 0;
		
		try{
			Query sql = em.createQuery("select count(nome) from Grupo g where g.nome = :nome and g.id <> :id");
			sql.setParameter("id", grupo.getId());
			sql.setParameter("nome", grupo.getNome());
			total = (Long) sql.getSingleResult(); 
		}catch (Exception e){
			System.out.println("Erro na query:" + e.getMessage());
		}
		
		return total;		
	}
	
	public String altera(){
		return "_addgrupo";
	}
	
	public void excluir(Grupo grupo){
		try {
			
			Grupo g = em.find(Grupo.class, this.grupo.getId());
			
			em.getTransaction().begin(); 	
			em.remove(g);				 				
			em.getTransaction().commit();	
			this.grupos = listar();			
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
