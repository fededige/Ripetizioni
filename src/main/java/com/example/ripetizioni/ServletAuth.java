package com.example.ripetizioni;

import com.google.gson.Gson;
import dao.Corso;
import dao.DAO;
import dao.Docente;
import dao.Utente;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

@WebServlet(name = "ServletAuth", value = "/ServletAuth")
public class ServletAuth extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:54317");
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        String login =  request.getParameter("login");
        String password = request.getParameter("password");
        PrintWriter out = response.getWriter();
        if (login != null && password != null) {
            Utente user = dao.utenteEsistente(login, password);
            session.setAttribute("login", user.getNome_utente());
            session.setAttribute("ruolo", user.getRuolo());
            Gson gson = new Gson();
            String s = gson.toJson(user);
            System.out.println(s);
            out.print(s);
        }
        out.flush();
    }
}
