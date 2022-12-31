package com.example.ripetizioni;

import com.google.gson.Gson;
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
import java.util.List;

@WebServlet(name = "ServletRipetizioneCancellata", value = "/ServletRipetizioneCancellata")
public class ServletRipetizioneCancellata extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.addHeader("Access-Control-Allow-Origin", "http://localhost:54317");
        response.setContentType("application/json");
        System.out.println("siamo in effettuate");
        PrintWriter out = response.getWriter();
        Gson gson =  new Gson();
        boolean res = false;
        HttpSession session = request.getSession();
        if(session.getAttribute("ruolo").equals("admin") || session.getAttribute("ruolo").equals("cliente")){
            Prenotazione prenEff = gson.fromJson(request.getParameter("prenotazione"), Prenotazione.class);
            res = dao.rimuoviPrenotazioni(prenEff);
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
