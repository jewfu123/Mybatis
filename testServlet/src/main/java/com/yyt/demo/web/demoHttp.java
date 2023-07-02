package com.yyt.demo.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/demo4")
public class demoHttp extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse rep) throws ServletException, IOException {
		System.out.println("get ...");
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse rep) throws ServletException, IOException {
		System.out.println("post ...");
	}

}
