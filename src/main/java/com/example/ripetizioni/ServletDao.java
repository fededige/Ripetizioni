package com.example.ripetizioni;

import dao.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(name = "ServletDao", value = "/ServletDao", loadOnStartup = 1, asyncSupported = true)

public class ServletDao extends HttpServlet {
    private String message;
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        String url = ctx.getInitParameter("DB-URL");
        String user = ctx.getInitParameter("user");
        String pwd = ctx.getInitParameter("pwd");
        dao = new DAO(url, user, pwd);
        DAO.registerDriver();
        ctx.setAttribute("DAO",dao);
    }
}