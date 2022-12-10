package com.example.ripetizioni;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.DAO;
import dao.Docente;
import dao.Prenotazione;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;

@WebServlet(name = "ServletRimuoviDocenti", value = "/ServletRimuoviDocenti")
public class ServletRimuoviDocenti extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("application/json");
            System.out.println("siamo in cancelllaasdadads");
            PrintWriter out = response.getWriter();
            Gson gson =  new Gson();
            Type token = new TypeToken<Docente>(){}.getType();
            System.out.println(request.getParameter("docente"));
            Docente docente = gson.fromJson(request.getParameter("docente"), token);
            boolean cancellazione = dao.rimuoviDocenti(docente.getMatricola());
            System.out.println("39 cancella "+cancellazione);
            out.print(cancellazione);
            out.flush();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ciao");
        doPost(request, response);
    }
}
