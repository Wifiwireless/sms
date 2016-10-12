package com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wifiwireless.constant.JndiLookup;
import com.wifiwireless.interfaces.MessagesInterface;

/**
 * Servlet implementation class Login
 */
@WebServlet("/browser")
public class BrowserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public BrowserServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String location = "main.html";
		System.out.println("in Servlet");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		System.out.println("Username:"+username);
		MessagesInterface messageinterface = JndiLookup.getMessageDao();
		String msidn = messageinterface.checkNumber(username, password);
		System.out.println(msidn);
		if (!msidn.equals("")) {
			response.sendRedirect("http://www.google.com");

		} else {
			System.out.println("in else");
			response.sendRedirect("http://70.182.179.17:8080/Wifiwirelessweb-0.0.1-SNAPSHOT/main.html");
		}
		System.out.println(request.getParameter("password"));
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

}
