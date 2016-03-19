package model;

import javax.persistence.*;

/** 
* Classe JpaUtil 
* <p>Classe responsavel por ler as anotacoes das entidades
*  anotadas e criar o pool de conexões com o banco de dados</p> 
* @param null, nao aplicavel 
* @author thiagozg 
* @version 1.0 
* @return Instancia do Gestor de Entidades
*/
public class JpaUtil {
	//static final indica que nao podemos atribuir valor 2 vezes ao atributo
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("default");
    //static indica que o atributo pertence a classe e nao ao objeto
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
   
} 
