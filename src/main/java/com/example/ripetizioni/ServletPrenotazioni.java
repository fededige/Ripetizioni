package com.example.ripetizioni;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.DAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletPrenotazioni", value = "/ServletPrenotazioni")
public class ServletPrenotazioni extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println("siamo in doGet");
//        doPost(request, response);
//    }
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        //response.addHeader("Access-Control-Allow-Origin", "http://localhost:54317");
//        response.setContentType("application/json");
//        PrintWriter out = response.getWriter();
//        Integer corso = null;
//        Integer docente = null;
//        System.out.println("kjnj "+request.getParameter("corso"));
//        System.out.println(request.getParameter("docente"));
//        if(!request.getParameter("corso").equals("null")){
//            corso = Integer.parseInt(request.getParameter("corso"));
//        }
//        if(!request.getParameter("docente").equals("null")){
//            docente = Integer.parseInt(request.getParameter("docente"));
//        }
//        int[][] prenotazioni = dao.prentoazioni_disp(docente , corso , request.getParameter("utente"));
//        Gson gson = new Gson();
//        String s = gson.toJson(prenotazioni);
//        System.out.println(s);
//        out.print(s);
//        out.flush();
//    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String sessionID = "";
        String body = request.getReader().readLine();
        String nome_utente = null;
        Integer corso = null;
        Integer docente = null;
        boolean ospite = true;
        System.out.println(body);
        if(SessionUtils.isJson(body)){
            JsonObject obj = new JsonParser().parse(body).getAsJsonObject();
            sessionID = obj.get("session").getAsString();
        } else {
            if(!body.split("utente=")[1].equals("null")){
                sessionID = body.split("utente=")[1];
                System.out.println("sessionID: " + sessionID);
                ospite = false;
            }
            String temp = body.split("corso=")[1].split("&")[0];
            System.out.println("tempC: " + temp);
            if(!temp.equals("null")){
                corso = Integer.parseInt(temp);
                System.out.println("corso: " + corso);
            }
            temp = body.split("docente=")[1].split("&")[0];
            System.out.println("tempD: " + temp);
            if(!temp.equals("null")){
                docente = Integer.parseInt(temp);
                System.out.println("docente: " + docente);
            }
        }
        if(!ospite){
            HttpSession session = SessionUtils.sessionMap.get(sessionID);
            nome_utente = session.getAttribute("login").toString();
            System.out.println("nome_utente: " + nome_utente);
        }

        int[][] prenotazioni = dao.prenotazioni_disp(docente, corso, nome_utente);
        Gson gson = new Gson();
        String s = gson.toJson(prenotazioni);
        System.out.println(s);
        out.print(s);
        out.flush();
    }
}