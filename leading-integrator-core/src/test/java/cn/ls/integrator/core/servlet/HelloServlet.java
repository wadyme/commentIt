/* 
 * Copyright (c) 2008-2011 by LeadingSoft. 
 * All rights reserved. 
 */
package cn.ls.integrator.core.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 
 * @author zhoumb 2011-4-27
 */
public class HelloServlet extends HttpServlet {

	private static final long serialVersionUID = 2905683751065292754L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("<h1>Hello Servlet</h1>");
		response.getWriter().println("session=" + request.getSession(true).getId());
	}
}
