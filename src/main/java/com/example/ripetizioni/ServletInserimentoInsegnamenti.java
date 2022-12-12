package com.example.ripetizioni;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

@WebServlet(name = "ServletInserimentoInsegnamenti", value = "/ServletInserimentoInsegnamenti")
public class ServletInserimentoInsegnamenti extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("siamo in doPost in ServletInserimentoInsegnamenti");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        System.out.println("36" + request.getParameter("insegnamento"));
        Insegna insegnamento = new Gson().fromJson(request.getParameter("insegnamento"), Insegna.class);
        boolean flag = dao.insertInsegnamento(insegnamento);
        out.print(flag);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("siamo in doPost in ServletInserimentoInsegnamenti");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        System.out.println("36" + request.getParameter("insegnamento"));
        Insegna insegnamento = new Gson().fromJson(request.getParameter("insegnamento"), Insegna.class);
        boolean flag = dao.insertInsegnamento(insegnamento);
        out.print(flag);
        out.flush();
    }
}
