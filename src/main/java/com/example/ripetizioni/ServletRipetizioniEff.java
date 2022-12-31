package com.example.ripetizioni;

import com.google.gson.Gson;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.addHeader("Access-Control-Allow-Origin", "http://localhost:54317"); //TODO: utilizzare la sessione al posto di request
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String usr =null;
        if(request.getParameter("utente") != null){
            usr=request.getParameter("utente");
        }
        List<Prenotazione> RipetizioniEff = dao.mostraPrenotazioni(usr);
        Gson gson = new Gson();
        String s = gson.toJson(RipetizioniEff);
        out.print(s);
        out.flush();
    }
}
