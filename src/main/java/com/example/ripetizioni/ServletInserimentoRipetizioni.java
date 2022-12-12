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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ciao post");
        //response.addHeader("Access-Control-Allow-Origin", "http://localhost:54317");
        response.addHeader("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null){
                jb.append(line);
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println("errore in post");
        }
        System.out.println("valore di jb: " + jb);
        Gson gson =  new Gson();
        Type token = new TypeToken<ArrayList<Prenotazione>>(){}.getType();
        System.out.println("token = " + token);
        ArrayList<Prenotazione> prenotazioni = gson.fromJson(String.valueOf(jb), token);
        System.out.println("lunghezza di pp: " + prenotazioni.size());
        dao.insertPrenotazione(prenotazioni);

        PrintWriter out = response.getWriter();
        gson = new Gson();
        String s = gson.toJson(true);
        out.print(s);
        out.flush();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ciao get");
        //response.addHeader("Access-Control-Allow-Origin", "http://localhost:54317");
        response.addHeader("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        System.out.println("68" + request.getParameter("prenotazioni"));
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null){
                jb.append(line);
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println("errore in post");
        }
        System.out.println("valore di jb: " + jb);
        Gson gson =  new Gson();
        Type token = new TypeToken<ArrayList<Prenotazione>>(){}.getType();
        System.out.println("token = " + token);
        ArrayList<Prenotazione> prenotazioni = gson.fromJson(request.getParameter("prenotazioni"), token);
        System.out.println("lunghezza di pp: " + prenotazioni.size());
        dao.insertPrenotazione(prenotazioni);

        PrintWriter out = response.getWriter();
        gson = new Gson();
        String s = gson.toJson(true);
        out.print(s);
        out.flush();
    }

}
