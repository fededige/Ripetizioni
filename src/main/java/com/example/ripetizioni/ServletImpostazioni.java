package com.example.ripetizioni;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    //ogni volta chiedere a ServletAuth
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String s = gson.toJson("non hai i permessi necessari");
        String sessionID;
        String vecchiaPassword;
        String nuovaPassword;

        String body = request.getReader().readLine();
        System.out.println("body: " + body);
        if(SessionUtils.isJson(body)){
            JsonObject obj = new JsonParser().parse(body).getAsJsonObject();
            sessionID = obj.get("session").getAsString();
            vecchiaPassword =  obj.get("vecchiaPassword").getAsString();
            nuovaPassword = obj.get("nuovaPassword").getAsString();
        } else{
            vecchiaPassword = body.split("vecchiaPassword=")[1].split("&")[0];
            nuovaPassword = body.split("nuovaPassword=")[1].split("&")[0];
            sessionID = body.split("session=")[1].split("&")[0];
        }
        System.out.println("vecchiaPassword " + vecchiaPassword);
        System.out.println("nuovaPassword " + nuovaPassword);
        System.out.println("sessionID " + sessionID);

        HttpSession session = SessionUtils.sessionMap.get(sessionID);


        if(session.getAttribute("ruolo").equals("admin") || session.getAttribute("ruolo").equals("cliente")){
            boolean res = false;
            String nomeUtente = session.getAttribute("login").toString();
            System.out.println("login from session" + nomeUtente);
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
