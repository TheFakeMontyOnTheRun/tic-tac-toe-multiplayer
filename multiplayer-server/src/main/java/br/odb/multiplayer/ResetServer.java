package br.odb.multiplayer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.odb.multiplayer.model.ServerContext;

@WebServlet(urlPatterns = "/ResetServer")
public class ResetServer extends HttpServlet {

	private static final long serialVersionUID = 8259789304861242906L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		resetServer();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		resetServer();
	}

	public void resetServer() {

		ServerContext.reset(getServletContext());
	}
}
