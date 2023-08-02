package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GetCustomer extends HttpServlet {
	private static final String GET_CUSTOMER_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		  HttpSession session = req.getSession();
		   String bearerTokenJson= (String) session.getAttribute("bearerToken");
		   String bearerToken =bearerTokenJson.substring(19,bearerTokenJson.length()-3);
		   HttpClient httpClient = HttpClient.newHttpClient();
	       
	       HttpRequest getRequest = HttpRequest.newBuilder()
	       		  .uri(URI.create(GET_CUSTOMER_URL))
	       		  .header("Authorization", "Bearer " + bearerToken)
	       		  .build();

	       HttpResponse<String> getResponse = null;
			try {
				getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
	       System.out.print("In the getCustomer  " + getResponse .statusCode());
	       if (getResponse .statusCode() == 200) {
	    	   String data = getResponse.body();
	    	   System.out.print(data);
	           PrintWriter out = res.getWriter();
	           out.println(data);
	       } else {
	           res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error while fetching user");
	       }
		   

	}
}
