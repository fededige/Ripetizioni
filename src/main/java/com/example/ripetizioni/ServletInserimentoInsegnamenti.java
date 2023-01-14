package com.example.ripetizioni;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletInserimentoInsegnamenti", value = "/ServletInserimentoInsegnamenti")
public class ServletInserimentoInsegnamenti extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("siamo in doGet in ServletInserimentoInsegnamenti");
        response.setContentType("application/json");
        boolean flag = false;
        PrintWriter out = response.getWriter();

        String sessionID;
        if(request.getParameter("session") == null){
            JsonObject obj = new JsonParser().parse(request.getReader().readLine()).getAsJsonObject();
            sessionID = obj.get("session").getAsString();
        } else {
            sessionID = request.getParameter("session");
        }
        HttpSession session = SessionUtils.sessionMap.get(sessionID);

        if(session.getAttribute("ruolo").equals("admin")){
            Insegna insegnamento = new Gson().fromJson(request.getParameter("insegnamento"), Insegna.class);
            flag = dao.insertInsegnamento(insegnamento);
        }
        out.print(flag);
        out.flush();
    }

//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println("siamo in doPost in ServletInserimentoInsegnamenti");
//        response.setContentType("application/json");
//        PrintWriter out = response.getWriter();
//        System.out.println("36" + request.getParameter("insegnamento"));
//        Insegna insegnamento = new Gson().fromJson(request.getParameter("insegnamento"), Insegna.class);
//        boolean flag = dao.insertInsegnamento(insegnamento);
//        out.print(flag);
//        out.flush();
//    }
}
