package controller;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import model.Usuario;

@ManagedBean
public class loginBean {
	private Usuario usuario = new Usuario();
		
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String login(){
		FacesContext msg = FacesContext.getCurrentInstance(); // objeto para mensagens
		HttpSession session = (HttpSession) msg.getExternalContext().getSession(false); // definindo os atributos da sessão
		session.setAttribute("usuario_nome", null);
		UsuarioBean usuarioBean = new UsuarioBean();
		if(usuarioBean.validaLogin(this.usuario.getLogin(), this.usuario.getSenha())){
			session.setAttribute("usuario_nome", this.usuario.getLogin());
			return "restrito/_conteudo";
		} else { 
			FacesMessage mensagem = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário ou senha inválidos", null);
			msg.addMessage(null, mensagem);
			return "login";
		}
	}
	
	public void logout() throws IOException{
		FacesContext msg = FacesContext.getCurrentInstance();
		msg.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout efetuado com sucesso!", null));
		HttpSession session = (HttpSession) msg.getExternalContext().getSession(false);	// invalidamos a sessao atual
		session.setAttribute("usuario_nome", null);
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.invalidateSession(); // esse metodo limpa a sessao do navegador
		ec.redirect(ec.getRequestContextPath() + "/login.jsf"); //inserir o throw Excpetion
	}
}
