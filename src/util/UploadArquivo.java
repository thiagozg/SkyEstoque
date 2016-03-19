package util;

import java.io.File;
import java.io.FileOutputStream;

import javax.faces.application.FacesMessage;
import javax.faces.context.*;
import javax.servlet.ServletContext;
//import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.FileUploadEvent;

public class UploadArquivo {

	private String caminho;
	private byte[] arquivo;
	private String nome;

	public UploadArquivo() {
	}


	public String getNome() {
		return nome;
	}



	public String getRealPath() {

		FacesContext aFacesContext = FacesContext.getCurrentInstance();
		ServletContext context = 
				(ServletContext) aFacesContext.getExternalContext().getContext();

		return context.getRealPath("/");
	}

	public void fileUpload(FileUploadEvent event, String extensao, String diretorio) {
		try {
			this.nome = new java.util.Date().getTime() + extensao;
			this.caminho = getRealPath() + diretorio + getNome();
			this.arquivo = event.getFile().getContents();

			File file = new File(getRealPath() + diretorio);
			file.mkdirs();
			System.out.println("Upload efetuado em "+getRealPath()+diretorio);
			FacesContext mensagem = FacesContext.getCurrentInstance();
			mensagem.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "O upload do arquivo foi efetuado com sucesso!", "O upload foi efetuado com sucesso!"));

		} catch (Exception ex) {
			System.out.println("Erro no upload do arquivo" + ex);
		}
	}

	public void gravar(){

		try {

			FileOutputStream fos;
			fos = new FileOutputStream(this.caminho);
			fos.write(this.arquivo);
			fos.close();

		} catch (Exception ex) {
			System.out.println(ex);
		}

	}
}