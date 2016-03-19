package controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import com.lowagie.text.*; /* Biblioteca do PDF */
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.persistence.*;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;


import model.*;

@ManagedBean
@SessionScoped

public class UsuarioBean {
	// objeto para as mensagens
	FacesContext mensagem = FacesContext.getCurrentInstance();
	// obtendo o Gerenciador de Entidades
	EntityManager em = JpaUtil.getEntityManager();
	// atributos
	private Usuario usuario = new Usuario();
	// list para o datatable
	private List<Usuario> usuarios;
	// list para utilizar no filtro
	private List<Usuario> filtroUsuarios;
	// atributo para o upload do arquivo
	private util.UploadArquivo arquivo = new util.UploadArquivo();

	// metodos
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = (usuario);
	}

	public List<Usuario> getFiltroUsuarios() {
		return filtroUsuarios;
	}

	public void setFiltroUsuarios(List<Usuario> filtroUsuarios) {
		this.filtroUsuarios = filtroUsuarios;
	}

	// Metodos para acessar o SGBD via JPA
	public String salva(Usuario usuario) {
		System.out.println("Salvando...");
		// verificamos se a usuario ja existe no SGBD
		if (this.totalUsuarios(usuario) > 0) {
			mensagem.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"O login " + usuario.getLogin()
							+ " ja existe na base de dados!", null));
		} else {
			// iniciamos a transacao no SGBD
			em.getTransaction().begin();
			try {
				// mandamos persistir o objeto
				if (usuario.getId() == null) { // Se id e nulo, salva os dados
					em.persist(usuario);
				} else { // Se id ja existe, atualiza os dados
					usuario = em.merge(usuario);
				}
				// fazemos o commit da transacao
				em.getTransaction().commit();
				// atualizamos o list
				this.usuarios = listar();
				// devido ao Session, instanciamos novo objeto
				this.usuario = new Usuario();
				// voltamos para a listagem
				return "_listausuario";
			} catch (Exception e) {
				// a transacao esta ativa?
				if (em.getTransaction().isActive()) {
					try { // tentamos fazer o rollback
						em.getTransaction().rollback();
					} catch (Exception er) {
						mensagem.addMessage(null, new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								"Erro no Rollback: " + er.getMessage(), null));

					}
				}

				mensagem.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Erro: " + e.getMessage(),
						null));
			}
			// permanecemos no formulario
		}
		return "_usuario";

	}

	public String altera() {
		return "_usuario";
	}

	public List<Usuario> getUsuarios() {
		// carregamos apenas se estiver vazio
		// e fundamental para funcionar o SortBy
		if (this.usuarios == null) {
			try {
				this.usuarios = listar();
				this.filtroUsuarios = null;
				// devido ao Session, instanciamos novo objeto
				this.usuario = new Usuario();
			} catch (Exception e) {
				mensagem.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Erro: " + e.getMessage(),
						null));
			}
		}
		return usuarios;
	}

	public void excluir(Usuario usuario) {
		System.out.println("Excluindo..."+this.usuario.getLogin());
		try {
			// obtendo o objeto que sera eliminado
			Usuario m = em.find(Usuario.class, this.usuario.getId());
			// iniciamos a transacao no SGBD
			em.getTransaction().begin();
			// removemos o objeto
			em.remove(m);
			// fazemos o commit da transacao
			em.getTransaction().commit();
			// atualizamos o list
			this.usuarios = listar();
			//return; "_listausuario";
		} catch (Exception e) {
			// a transacao esta ativa?
			if (em.getTransaction().isActive()) {
				try { // tentamos fazer o rollback
					em.getTransaction().rollback();
				} catch (Exception er) {
					mensagem.addMessage(null, new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Erro no Rollback: "
									+ er.getMessage(), null));

				}
			}

			mensagem.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro: " + e.getMessage(),
					null));
		}
		return;
	}

	/*
	 * A anotacao unchecked e utilizada para suprimir avisos do compilador sobre
	 * operacoes genericas, como o retorno do JPA ser igual ao tipo List<>
	 * informado
	 */
	@SuppressWarnings("unchecked")
	public List<Usuario> listar() {
		List<Usuario> listagem;
		Query q = em.createQuery("select u from Usuario u order by u.login", Usuario.class);
		listagem = q.getResultList();
		//em.close();
		return listagem;
	}

	public long totalUsuarios(Usuario usuario) {
		long total = 0;
		try {
			Query sql = em
					.createQuery("select count(login) from Usuario u where u.login = :login and u.id <> :codigo");
			sql.setParameter("login", usuario.getLogin());
			sql.setParameter("codigo", usuario.getId());
			System.out.println("Total:" + (Long) sql.getSingleResult());
			total = (Long) sql.getSingleResult();
		} catch (Exception e) {
			mensagem.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro: " + e.getMessage(),
					null));
		}
		return total;
	}

		// Upload do Arquivo
	public void uploadAction (FileUploadEvent event){
		String nomearquivo = event.getFile().getFileName();
		String extensao = nomearquivo.substring(nomearquivo.lastIndexOf('.'),nomearquivo.length());
		this.arquivo.fileUpload(event, extensao, "/upload/");
		this.arquivo.gravar();
		this.usuario.setNomearquivo(this.arquivo.getNome()); 		
	}
	// Definicao para a exportacao PDF
	public void preProcessPDF(Object document) throws IOException,
	BadElementException, DocumentException {
		Document pdf = (Document) document;
		pdf.setMargins(30, 20, 30, 15);
		pdf.addCreationDate();
		pdf.addTitle("Listagem dos Usuarios");
		pdf.addAuthor("EstacionaFatec");
		pdf.setPageSize(PageSize.A4);

		ServletContext servletContext = (ServletContext) FacesContext
				.getCurrentInstance().getExternalContext().getContext();

		Date data = new Date();
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy hh:mm");

		String logo = servletContext.getRealPath("") + File.separator
				+ "images" + File.separator + "logo.png";

		// Cria Cabeçalho
		HeaderFooter header = new HeaderFooter(new Phrase(
				"Listagem dos Usuarios", new Font(Font.NORMAL, 14)), false);
		// O último argumento é se você quer que mostre o número da página no
		// cabeçalho.
		header.setAlignment(Element.ALIGN_CENTER); // Alinhamento Centralizado.
		header.setBackgroundColor(new java.awt.Color(0xE3, 0xF1, 0xFC));
		header.setBorder(Rectangle.BOX);
		pdf.setHeader(header);

		// O Cabeçalho mesmo procedimento acima mudando apenas o final
		HeaderFooter footer = new HeaderFooter(new Phrase("PDF Gerado em "
				+ formatador.format(data) + " - Página ", new Font(
						Font.NORMAL, 12)), true);
		footer.setAlignment(Element.ALIGN_RIGHT);
		footer.setBorder(Rectangle.TOP);
		pdf.setFooter(footer);

		if (!pdf.isOpen()) {
			pdf.open();
			pdf.add(Image.getInstance(logo));
		}
	}
	
	// Validar Login
	public boolean validaLogin(String login, String senha){
		long total = 0;
		boolean valido = false;
		//util.Criptografia cripto = new util.Criptografia();
		try{
			Query sql = em.createQuery("select count(u.login) from Usuario u where u.login = :login and u.senha = :senha and upper(u.status) = 'ATIVO'");
			sql.setParameter("login", login);
			sql.setParameter("senha", senha);
			total = (Long) sql.getSingleResult();
			if(total > 0){
				valido = true;
			}
		}catch (Exception e){
			System.out.println("erro no select: " + e.getMessage());
		}
		return valido;
	}
}
