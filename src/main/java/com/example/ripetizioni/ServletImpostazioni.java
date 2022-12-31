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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.addHeader("Access-Control-Allow-Origin", "http://localhost:54317");
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String s = gson.toJson("non hai i permessi necessari");
        if(session.getAttribute("ruolo").equals("admin") || session.getAttribute("ruolo").equals("cliente")){
            boolean res = false;
            String nomeUtente = session.getAttribute("login").toString(); //TODO: questi parametri li possiamo recuperare dalla session
            String vecchiaPassword =  request.getParameter("vecchiaPassword");
            String nuovaPassword = request.getParameter("nuovaPassword");
            String confermaNuovaPassword = request.getParameter("confermaNuovaPassword");
            System.out.println(nomeUtente);
            System.out.println(vecchiaPassword);
            Utente utente = dao.utenteEsistente(nomeUtente, vecchiaPassword);
            if(utente != null && utente.getNome_utente() != null){
                if(confermaNuovaPassword.equals(nuovaPassword)){
                    res = dao.editPassword(nomeUtente, nuovaPassword);
                }
            }
            s = gson.toJson(res);
        }
        System.out.println(s);
        out.print(s);
        out.flush();
    }
}
