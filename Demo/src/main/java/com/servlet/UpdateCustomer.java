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

public class UpdateCustomer extends HttpServlet {
	

		private static final String UPDATE_CUSTOMER_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
		public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
			String uuid= (String) req.getParameter("uuid");
			 String fname = (String) req.getParameter("first_name");
			   String lname = (String) req.getParameter("last_name");
			   String street = (String) req.getParameter("street");
			   String address = (String) req.getParameter("address");
			   String city = (String) req.getParameter("city");
			   String state = (String) req.getParameter("state");
			   String email = (String) req.getParameter("email");
			   String phone = (String) req.getParameter("phone");
			   
			   
			   if (fname == null || lname == null || fname.equals("") ||lname.equals("")) {
		           res.sendError(HttpServletResponse.SC_BAD_REQUEST, "First name and last name are mandatory.");
		           return;
		       }
			   
			   StringBuilder jsonStringBuilder = new StringBuilder();
			   jsonStringBuilder.append("{");
			   jsonStringBuilder.append("\"first_name\":\"").append(fname).append("\",");
			   jsonStringBuilder.append("\"last_name\":\"").append(lname).append("\",");
			   jsonStringBuilder.append("\"street\":\"").append(street).append("\",");
			   jsonStringBuilder.append("\"address\":\"").append(address).append("\",");
			   jsonStringBuilder.append("\"city\":\"").append(city).append("\",");
			   jsonStringBuilder.append("\"state\":\"").append(state).append("\",");
			   jsonStringBuilder.append("\"email\":\"").append(email).append("\",");
			   jsonStringBuilder.append("\"phone\":\"").append(phone).append("\"");
			   jsonStringBuilder.append("}");

			   String json = jsonStringBuilder.toString();
			   HttpSession session = req.getSession();
			   String bearerTokenJson= (String) session.getAttribute("bearerToken");
			   String bearerToken =bearerTokenJson.substring(19,bearerTokenJson.length()-3);
			   HttpClient httpClient = HttpClient.newHttpClient();
		       
		       HttpRequest createRequest = HttpRequest.newBuilder()
		       		  .uri(URI.create(UPDATE_CUSTOMER_URL+"?cmd=update"+"&uuid="+uuid))
		       		  .POST(HttpRequest.BodyPublishers.ofString(json))
		       		  .header("Authorization", "Bearer " + bearerToken)
		       		  .build();

		       HttpResponse<String> updateResponse = null;
				try {
					updateResponse = httpClient.send(createRequest, HttpResponse.BodyHandlers.ofString());
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
		       
		       if (updateResponse .statusCode() == 200) {
		    	   res.setContentType("text/html;charset=UTF-8");
		           res.getWriter().write("<script>alert('Update successful');</script>");
		       } else {
		           res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error while updating");
		       }
			   
			   

		}
}
