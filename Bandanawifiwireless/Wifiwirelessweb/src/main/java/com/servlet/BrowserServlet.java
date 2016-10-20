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
	Cipher dcipher;
	int iterationCount = 1024;
    int keyStrength = 256;
    SecretKey key;
    byte[] iv;
    byte[] salt = new String("12345678").getBytes();

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
			throws ServletException, IOException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		String location = "main.html";
		String check = "";
		System.out.println("in Servlet");
		String username = request.getParameter("username");
		String password = request.getParameter("Passkey");
		System.out.println(password);
		/*SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount, keyStrength);
        SecretKey tmp = factory.generateSecret(spec);
        key = new SecretKeySpec(tmp.getEncoded(), "AES");
        dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        dcipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
	        byte[] decryptedData = new sun.misc.BASE64Decoder().decodeBuffer(password);
	        byte[] utf8 = dcipher.doFinal(decryptedData);
	       String newpassword=new String(utf8, "UTF8");*/
	    
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
			if(number.equals(check) )
				response.sendRedirect("http://70.182.179.17:8080/Wifiwirelessweb-0.0.1-SNAPSHOT/index.html");
			else
			response.sendRedirect("http://www.google.com");
		}
		System.out.println(request.getParameter("password"));
		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());
	}

}
