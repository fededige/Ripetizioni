package com.example.ripetizioni;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.DAO;
import dao.Prenotazione;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(name = "ServletRipetizionieff", value = "/ServletRipetizionieff")
public class ServletRipetizioniEff extends HttpServlet {

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
        List<Prenotazione> RipetizioniEff = null;
        Gson gson = new Gson();
        String s = gson.toJson("Non hai i permessi necessari");
        int id_ultima_prenotazione = 0;
        String sessionID;
        if(request.getParameter("session") == null){
            JsonObject obj = new JsonParser().parse(request.getReader().readLine()).getAsJsonObject();
            sessionID = obj.get("session").getAsString();
            id_ultima_prenotazione = obj.get("id").getAsInt();
        } else {
            sessionID = request.getParameter("session");
            id_ultima_prenotazione = Integer.parseInt(request.getParameter("id"));
        }
        HttpSession session = SessionUtils.sessionMap.get(sessionID);

        if(session.getAttribute("ruolo").equals("admin")){
            RipetizioniEff = dao.mostraPrenotazioni(null,id_ultima_prenotazione);
            s = gson.toJson(RipetizioniEff);
        }
        else if(session.getAttribute("ruolo").equals("cliente")){
            RipetizioniEff = dao.mostraPrenotazioni(session.getAttribute("login").toString(),id_ultima_prenotazione);
            s = gson.toJson(RipetizioniEff);
        }
        System.out.println("52"+s);
        out.print(s);
        out.flush();
    }
}
