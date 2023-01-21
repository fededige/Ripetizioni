package com.example.ripetizioni;

import com.google.gson.Gson;
import dao.DAO;
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
        HttpSession session = request.getSession();
        String login =  request.getParameter("login");
        String password = request.getParameter("password");
        PrintWriter out = response.getWriter();
        String res;
        String s;
        Gson gson = new Gson();
        if (login != null && password != null) {
            Utente user = dao.utenteEsistente(login, password);
            if(user.getNome_utente() == null && !user.isStato()){
                s = "UtenteInesistente";
            } else if(user.getNome_utente() == null && user.isStato()){
                s = "PasswordErrata";
            }else{
                response.setContentType("application/json");
                session.setAttribute("login", user.getNome_utente());
                session.setAttribute("ruolo", user.getRuolo());
                SessionUtils.sessionMap.put(session.getId(), session);
                s = gson.toJson(session.getId() + ";" + user.getRuolo());
            }
            System.out.println(s);
            out.print(s);
        }
        out.flush();
    }
}
