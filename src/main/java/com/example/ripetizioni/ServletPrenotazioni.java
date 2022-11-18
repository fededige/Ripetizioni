package com.example.ripetizioni;

import com.google.gson.Gson;
import dao.DAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletPrenotazioni", value = "/ServletPrenotazioni")
public class ServletPrenotazioni extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:54317");
        response.setContentType("application/json");
        //HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        int[][] prenotazioni = dao.prentoazioni_disp(Integer.parseInt(request.getParameter("docente")), Integer.parseInt(request.getParameter("corso")), request.getParameter("utente"));
        Gson gson = new Gson();
        String s = gson.toJson(prenotazioni);
        System.out.println(s);
        out.print(s);
        out.flush();
    }
}

/**/