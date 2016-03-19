package controller;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

/* Classe para efetuar o filtro das requisicoes * 
 * Nao esquecer de configurar o web.xml!
 * */

public class FilterSessionRequest implements Filter {

	@SuppressWarnings("unused")
	private FilterConfig filterConfig = null;
	public void init(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest rq = (HttpServletRequest) request;
		HttpSession session = rq.getSession(false);
		
		HttpServletResponse rp = (HttpServletResponse) response;
		try {
		if (session.getAttribute("usuario_nome")== null) {
			System.out.println("Nome esta vazio, redirecionando para o login...");
			rp.sendRedirect(rq.getContextPath()+"/login.jsf");
		}
		else
		{
			System.out.println("Nome possui valor, continuamos...");
			chain.doFilter(request, response);
		}
		}
		catch (Exception e ) {
			rp.sendRedirect(rq.getContextPath()+"/login.jsf");
		}
		
	}
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
