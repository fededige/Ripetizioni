package com.example.ripetizioni;

import com.google.gson.Gson;
import dao.DAO;
import dao.Utente;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletImpostazioni", value = "/ServletImpostazioni")
public class ServletImpostazioni extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:54317");
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        String mode =  request.getParameter("mode");
        PrintWriter out = response.getWriter();
        boolean res = false;
        String nomeUtente = request.getParameter("nomeUtente"); //TODO: questi parametri li possiamo recuperare dalla session
        String vecchiaPassword =  request.getParameter("vecchiaPassword");
        String nuovaPassword = request.getParameter("nuovaPassword");
        String confermaNuovaPassword = request.getParameter("confermaNuovaPassword");
        Utente utente = dao.utenteEsistente(nomeUtente, vecchiaPassword);
        if(utente != null && !utente.getNome_utente().equals("")){
            if(confermaNuovaPassword.equals(nuovaPassword)){
                res = dao.editPassword(nomeUtente, nuovaPassword);
            }
        }
        Gson gson = new Gson();
        String s = gson.toJson(res);
        System.out.println(s);
        out.print(s);
        out.flush();
    }
}
