package model;

import javax.persistence.EntityManager;

public class TestaBdUsuario {

	public static void main(String[] args) {
		EntityManager em = JpaUtil.getEntityManager();
		try {
			//inserindo usuario
			em.getTransaction().begin();			
			Usuario usuario = new Usuario();
			usuario.setLogin("admin2");
			usuario.setSenha("admin2");
			usuario.setStatus("ativo");
			em.persist(usuario);
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			System.out.println("Erro ao testar o bd: "+e.getMessage());
		}

	}

}
