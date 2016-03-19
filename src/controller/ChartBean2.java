package controller;

import java.io.Serializable;  
import java.util.List;


import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;


import model.JpaUtil;
import org.primefaces.model.chart.PieChartModel;  


@ManagedBean 
public class ChartBean2 implements Serializable {  
	// obtendo o Gerenciador de Entidades
	EntityManager em = JpaUtil.getEntityManager();
	private static final long serialVersionUID = 1L;
	private PieChartModel pieModel; 

	public ChartBean2() {  
		carregaDados();  
	}

	public PieChartModel getPieModel() {  
		return pieModel;  
	}


	private void carregaDados() {  
		pieModel = new PieChartModel();
		try {
			//Query sql = em.createQuery("select ma.descricao, count(mo.descricao) from Modelo mo, Marca ma where ma.id = mo.marca group by ma.descricao");
			//Query sql = em.createQuery("select p.nome, count(f.nome) from Fornecedor f, Produto p where f.id = p.fornecedor group by p.quantidade");
			Query sql = em.createQuery("select g.nome, p.quantidade from Produto p, Grupo g where g.id = p.grupo group by p.quantidade");
			@SuppressWarnings("unchecked")
			List<Object[]> listagem = sql.getResultList();
			for(Object[] registro : listagem) {
				pieModel.set((String) registro[0], (Integer) registro[1]);
			}
		} catch (Exception e) {            
			System.out.println("Erro no select do grafico:" + e.getMessage());
		} 
	} 

}  


