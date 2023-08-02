package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Authentication extends HttpServlet {
    private static final String AUTH_API_URL = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String loginId = request.getParameter("username");
        String password = request.getParameter("password");

        String jsonBody = String.format("{\"login_id\":\"%s\",\"password\":\"%s\"}", loginId, password);
        

        HttpClient httpClient = HttpClient.newHttpClient();
        
        HttpRequest authRequest = HttpRequest.newBuilder()
        		  .uri(URI.create(AUTH_API_URL))
        		  .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        		  .build();

        HttpResponse<String> authResponse = null;
		try {
			authResponse = httpClient.send(authRequest, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
        
        if (authResponse.statusCode() == 200) {
            String bearerToken = authResponse.body();
            HttpSession session = request.getSession();
    		session.setAttribute("bearerToken",bearerToken);
            response.sendRedirect("dashboard.html");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed.");
        }
    }

}
