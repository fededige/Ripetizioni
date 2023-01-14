package com.example.ripetizioni;

import com.google.gson.Gson;
import dao.DAO;
import dao.Utente;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletRegistrazione", value = "/ServletRegistrazione")
public class ServletRegistrazione extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        System.out.println(request.getParameter("nuovoUtente"));
        Gson gson = new Gson();
        Utente nuovoUtente = gson.fromJson(request.getParameter("nuovoUtente"), Utente.class);
        System.out.println("nome_ut: " + nuovoUtente.getNome_utente());
        System.out.println("pass: " + nuovoUtente.getPassword());
        boolean f = dao.insertUtente(nuovoUtente);
        String s = "Nome utente esistente";
        if(f){
            session.setAttribute("login", nuovoUtente.getNome_utente());
            session.setAttribute("ruolo", "cliente");
            SessionUtils.sessionMap.put(session.getId(), session);
            s = gson.toJson(session.getId() + ";" + session.getAttribute("ruolo"));
        }
        System.out.println("s: " + s);
        out.print(s);
        out.flush();
        out.close();
    }
}
