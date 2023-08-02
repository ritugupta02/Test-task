package com.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DeleteCustomer extends HttpServlet {
	private String DELETE_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp";
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
        StringBuilder stringData = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringData.append(line);
        }
		String uuid1= stringData.toString();
		String uuid =		uuid1.substring(9,uuid1.length()-2);
		
		HttpSession session = req.getSession();
		String bearerTokenJson= (String) session.getAttribute("bearerToken");
		String bearerToken =bearerTokenJson.substring(19,bearerTokenJson.length()-3);

		HttpRequest deleteRequest = HttpRequest.newBuilder()
	       		  .uri(URI.create(DELETE_URL+"?cmd=delete&uuid="+uuid))
	       		  .header("Authorization", "Bearer " + bearerToken)
	       		  .POST(HttpRequest.BodyPublishers.noBody())
	       		  .build();
		
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpResponse<String> deleteResponse = null;
		try {
			deleteResponse = httpClient.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
       
       if (deleteResponse.statusCode() == 200) {
    	   res.setStatus(HttpServletResponse.SC_OK);
           res.setContentType("application/json");

           // Write a JSON response back to the client
           String responseJson = "{\"message\":\"user deleted\"}";

           PrintWriter out = res.getWriter();
           out.println(responseJson);
           out.flush();
       } else {
           res.sendError(HttpServletResponse.SC_UNAUTHORIZED,"error while deleting the user");
       }
	   
	}

}
