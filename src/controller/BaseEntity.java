package controller;

/*
 * BaseEntity e uma interface com um metodo em comum entre as entidades, ou seja, nossas entidades precisarao implementar 
 * esta interface para que o converter generico funcione corretamente.
 */

public interface BaseEntity {  
	  
    public Long getId();  
  
}
