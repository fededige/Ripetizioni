package com.example.ripetizioni;

import com.google.gson.Gson;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String s = gson.toJson("non hai i permessi necessari");
        if(session.getAttribute("ruolo").equals("admin")){
            List<Corso> listRes = dao.mostraCorsi();
            s = gson.toJson(listRes);
        }
        out.print(s);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
