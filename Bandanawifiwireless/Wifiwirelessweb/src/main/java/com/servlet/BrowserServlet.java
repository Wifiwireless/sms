package com.servlet;

import java.io.IOException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wifiwireless.constant.JndiLookup;
import com.wifiwireless.interfaces.CustomerCheckDaoInterface;
import com.wifiwireless.interfaces.CustomerDaoInterface;
import com.wifiwireless.interfaces.MessagesInterface;
import com.wifiwireless.interfaces.NumberDetailsInterface;
import com.wifiwireless.model.NumberDetails;

import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKeyFactory;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import javax.crypto.spec.IvParameterSpec;

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
		try {
			processRequest(request, response);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
	{
		String location = "main.html";
		String check = "";
		System.out.println("in Servlet");
		String username = request.getParameter("username");
		String password = request.getParameter("Passkey");
		System.out.println(password);
		
	    
		System.out.println("Username:" + username +"Password:");
		CustomerDaoInterface customerdao = JndiLookup.getCustomerDetails();
		// System.out.println(msidn);
		NumberDetailsInterface numberDetailsInterface=JndiLookup.getNumberDetailsDao();
		String number=numberDetailsInterface.checkNumber(username, password);
		String msisdn = customerdao.checkNumber(username, password);
		if (msisdn.equals(check)) {

			System.out.println("in else");
			response.sendRedirect("http://70.182.179.17:8080/Wifiwirelessweb-0.0.1-SNAPSHOT/main.html");

		} else {

			NumberDetails numbers=numberDetailsInterface.getNumberDetails(username, password);
			//String number=numberDetailsInterface.checkNumber(username, password);
			if(numbers.getPaidflag())
				response.sendRedirect("http://www.google.com");
				
			else
				response.sendRedirect("http://70.182.179.17:8080/Wifiwirelessweb-0.0.1-SNAPSHOT/index.html");
		}
		System.out.println(request.getParameter("password"));
		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());
	}

}
