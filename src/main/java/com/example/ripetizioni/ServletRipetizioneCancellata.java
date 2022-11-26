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
        StringBuffer jb = new StringBuffer();
        String line = null;
        PrintWriter out = response.getWriter();
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) { /*report an error*/ }
        System.out.println("siamo in 38"+jb);
        Gson gson =  new Gson();
        Type token = new TypeToken<Prenotazione>(){}.getType();
        System.out.println(token);
        Prenotazione prenEff = gson.fromJson(String.valueOf(jb), token);
        boolean cambio = dao.rimuoviPrenotazioni(prenEff);
        System.out.println(prenEff);
        System.out.println(cambio);
        out.print(cambio);
        out.flush();
    }
}
