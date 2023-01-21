package com.example.ripetizioni;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import dao.DAO;
import dao.Insegna;
import dao.Prenotazione;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ServletInserimentoRipetizioni", value = "/ServletInserimentoRipetizioni")
public class ServletInserimentoRipetizioni extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }
    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println("siamo in doPost in ServletInserimentoRipetizioni");
//        response.setContentType("application/json");
//        PrintWriter out = response.getWriter();
//        HttpSession session = request.getSession();
//        Gson gson =  new Gson();
//        boolean res = false;
//        if(session.getAttribute("ruolo").equals("admin") || session.getAttribute("ruolo").equals("cliente")){
//            Type token = new TypeToken<ArrayList<Prenotazione>>(){}.getType();
//            System.out.println("token = " + token);
//            ArrayList<Prenotazione> prenotazioni = gson.fromJson(request.getParameter("prenotazioni"), token);
//            res = dao.insertPrenotazione(prenotazioni);
//        }
//        String s = gson.toJson(res);
//        out.print(s);
//        out.flush();
//    }
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println("siamo in doGet in ServletInserimentoRipetizioni");
//        response.setContentType("application/json");
//        PrintWriter out = response.getWriter();
//        HttpSession session = request.getSession();
//        Gson gson =  new Gson();
//        boolean res = false;
//        if(session.getAttribute("ruolo").equals("admin") || session.getAttribute("ruolo").equals("cliente")){
//            Type token = new TypeToken<ArrayList<Prenotazione>>(){}.getType();
//            System.out.println("token = " + token);
//            ArrayList<Prenotazione> prenotazioni = gson.fromJson(request.getParameter("prenotazioni"), token);
//            res = dao.insertPrenotazione(prenotazioni);
//        }
//        String s = gson.toJson(res);
//        out.print(s);
//        out.flush();
//    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("siamo in doPost in ServletInserimentoRipetizioni");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson =  new Gson();
        ArrayList<Integer> res = new ArrayList<>();


        String sessionID;
        String bodyArr;

        if(request.getParameter("session") == null){
            JsonObject obj = new JsonParser().parse(request.getReader().readLine()).getAsJsonObject();
            sessionID = obj.get("session").getAsString();
            bodyArr = String.valueOf(obj.get("prenotazione"));
            System.out.println(bodyArr);
        } else {
            bodyArr =  request.getParameter("prenotazioni");
            sessionID = request.getParameter("session");
            System.out.println("bodyArr: " + bodyArr);
            System.out.println("sessionID: " + sessionID);
        }
        HttpSession session = SessionUtils.sessionMap.get(sessionID);
        System.out.println("session ruolo" + session.getAttribute("ruolo"));
        if(session.getAttribute("ruolo").equals("cliente")){
            System.out.println("dentro if");
            Type token = new TypeToken<ArrayList<Prenotazione>>(){}.getType();
            ArrayList<Prenotazione> prenotazioni = gson.fromJson(bodyArr, token);
            res = dao.insertPrenotazione(prenotazioni);
        }
        String s = gson.toJson(res);
        System.out.println("s: " + s);
        out.print(s);
        out.flush();
    }

}
