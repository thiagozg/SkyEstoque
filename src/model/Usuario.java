 package model;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import javax.persistence.*;
import javax.validation.constraints.*;

/** 
* Classe Usuario 
* <p>Classe que representa os Usuarios do Sistema</p> 
* @param null, nao aplicavel 
* @author Prof. Ricardo Leme 
* @exception Nao aplicavel
* @version 1.0 
* @return Nao aplicavel 
*/
/* A anotacao @Entity indica que iremos mapear a classe
   Usuario em uma entidade no Banco de Dados */
@Entity 
public class Usuario implements Serializable {
/* Indicamos que o objeto sera serializavel, ou seja,
 * permitimos que os seus atributos possam ser armazenados.
 */
	private static final long serialVersionUID = 1L;

    /* @Id indica que o atributo sera uma PK e 
	*  @GeneratedValue ele sera um campo autonumerado
	*/
    @Id @GeneratedValue
    private long id;
    /* @Column possui varios parametros, como o tamanho,
    * se sera obrigatorio e se sera unico no Banco
    */
    @Column(nullable=false, length=20, unique=true)
    // Been Validations
    @Size(
    			min=2, 
    			max=20, 
    			message="O login deve ter entre {min} e {max} caracteres"
    		)
    @NotNull(message="O login e obrigatorio")
    private String login;
    @Column(nullable=false, length=100, unique=false)
    private String senha;
    @Column(nullable=false, length=7, unique=false)
    private String status="inativo";
    private String nomearquivo="semfoto.png";
   
	public void setSenha(String senha) throws NoSuchAlgorithmException {
	    //objeto para a criptografia
	 	util.Criptografia cripto = new util.Criptografia();	    
		this.senha = cripto.criptografarSenha(senha);
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNomearquivo() {
		return nomearquivo;
	}

	public void setNomearquivo(String nomearquivo) {
		this.nomearquivo = nomearquivo;
	}

	public String getSenha() {
		return senha;
	}
	
	
}
