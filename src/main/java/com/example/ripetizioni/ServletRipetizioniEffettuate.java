package com.example.ripetizioni;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import dao.DAO;
import dao.Prenotazione;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.FileStore;
import java.util.List;

@WebServlet(name = "ServletRipetizioniEffettuate", value = "/ServletRipetizioniEffettuate")
public class ServletRipetizioniEffettuate extends HttpServlet {

    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson =  new Gson();
        boolean res = false;

        String sessionID;
        if(request.getParameter("session") == null){
            JsonObject obj = new JsonParser().parse(request.getReader().readLine()).getAsJsonObject();
            sessionID = obj.get("session").getAsString();
        } else {
            sessionID = request.getParameter("session");
        }
        HttpSession session = SessionUtils.sessionMap.get(sessionID);

        if(session.getAttribute("ruolo").equals("admin") || session.getAttribute("ruolo").equals("cliente")){
            Prenotazione prenEff = gson.fromJson(request.getParameter("prenotazione"), Prenotazione.class);
            res = dao.prenotazioneEff(prenEff);
        }
        out.print(res);
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ciao");
        doPost(request, response);
    }
}
