package com.example.ripetizioni;

import com.google.gson.Gson;
import dao.DAO;
import dao.Insegna;
import dao.Insegnamento;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ServletInsegnamenti", value = "/ServletInsegnamenti")
public class ServletInsegnamenti extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        List<Insegnamento> insegnamenti = dao.getInsegnamenti();
        Gson gson = new Gson();
        String s = gson.toJson(insegnamenti);
        System.out.println(s);
        out.print(s);
        out.flush();
    }
}
