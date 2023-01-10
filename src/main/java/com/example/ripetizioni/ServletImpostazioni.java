package com.example.ripetizioni;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.DAO;
import dao.Utente;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

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
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    //ogni volta chiedere a ServletAuth
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String s = gson.toJson("non hai i permessi necessari");
        String vecchiaPassword;
        String nuovaPassword;
        if(session.isNew()){
            String bodyJson = request.getReader().readLine();
            JsonObject jobj = new Gson().fromJson( bodyJson, JsonObject.class);
            vecchiaPassword = gson.fromJson(jobj.get("vecchiaPassword"), String.class);
            nuovaPassword = gson.fromJson(jobj.get("nuovaPassword"), String.class);
            JsonObject sessionJson =  new Gson().fromJson( jobj.get("session"), JsonObject.class);
            Map<String, String> sessioAtt = gson.fromJson(sessionJson.get("attributes"), Map.class);
            session.setAttribute("ruolo", sessioAtt.get("ruolo"));
            session.setAttribute("login", sessioAtt.get("login"));
        } else{
            vecchiaPassword =  request.getParameter("vecchiaPassword");
            nuovaPassword = request.getParameter("nuovaPassword");
        }

        if(session.getAttribute("ruolo").equals("admin") || session.getAttribute("ruolo").equals("cliente")){
            boolean res = false;
            String nomeUtente = session.getAttribute("login").toString();
            Utente utente = dao.utenteEsistente(nomeUtente, vecchiaPassword);
            if(utente != null && utente.getNome_utente() != null){
                res = dao.editPassword(nomeUtente, nuovaPassword);
            }
            s = gson.toJson(res);
        }
        out.print(s);
        out.flush();
    }
}
