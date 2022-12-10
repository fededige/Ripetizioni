package com.example.ripetizioni;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.Corso;
import dao.DAO;
import dao.Docente;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;

@WebServlet(name = "ServletInsertCorso", value = "/ServletInsertCorso")
public class ServletInsertCorso extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        System.out.println("siamo in insert Corso");
        PrintWriter out = response.getWriter();
        Gson gson =  new Gson();
        Type token = new TypeToken<Corso>(){}.getType();
        System.out.println(request.getParameter("corso"));
        Corso corso = gson.fromJson(request.getParameter("corso"), token);
        boolean cancellazione = dao.insertCorsi(corso);
        System.out.println("34 insert corso "+cancellazione);
        out.print(cancellazione);
        out.flush();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ciao");
        doPost(request, response);
    }
}
