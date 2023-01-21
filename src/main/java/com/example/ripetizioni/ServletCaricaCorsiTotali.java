package com.example.ripetizioni;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.Corso;
import dao.DAO;
import dao.Docente;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ServletCaricaCorsiTotali", value = "/ServletCaricaCorsiTotali")
public class ServletCaricaCorsiTotali extends HttpServlet {
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
        Gson gson = new Gson();
        String s = gson.toJson("non hai i permessi necessari");
        int ultimo_codice_corso=0;
        String sessionID;
        if(request.getParameter("session") == null){
            JsonObject obj = new JsonParser().parse(request.getReader().readLine()).getAsJsonObject();
            sessionID = obj.get("session").getAsString();
            ultimo_codice_corso = obj.get("id").getAsInt();
        } else {
            sessionID = request.getParameter("session");
            ultimo_codice_corso =  Integer.parseInt(request.getParameter("id"));
        }
        HttpSession session = SessionUtils.sessionMap.get(sessionID);

        if(session.getAttribute("ruolo").equals("admin")){
            List<Corso> listRes = dao.mostraCorsi(ultimo_codice_corso);
            s = gson.toJson(listRes);
        }
        out.print(s);
        out.flush();
    }
}
