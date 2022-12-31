package com.example.ripetizioni;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.DAO;
import dao.Docente;
import dao.Insegna;
import dao.Insegnamento;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;

@WebServlet(name = "ServletRimuoviInsegnamento", value = "/ServletRimuoviInsegnamento")
public class ServletRimuoviInsegnamento extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ciao");
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        System.out.println("siamo in ServletRimuoviInsegnamento");
        PrintWriter out = response.getWriter();
        Gson gson =  new Gson();
        HttpSession session = request.getSession();
        boolean res = false;
        if(session.getAttribute("ruolo").equals("admin")){
            Insegna insegnamento = gson.fromJson(request.getParameter("insegnamento"), Insegna.class);
            res = dao.rimuoviInsegnamento(insegnamento);
        }
        out.print(res);
        out.flush();
    }
}
