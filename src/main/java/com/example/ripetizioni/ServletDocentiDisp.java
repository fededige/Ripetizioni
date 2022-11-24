package com.example.ripetizioni;

import com.google.gson.Gson;
import dao.DAO;
import dao.Docente;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "ServletDocentiDisp", value = "/ServletDocentiDisp")
public class ServletDocentiDisp extends HttpServlet {
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
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        System.out.println(request.getParameter("corso"));
        System.out.println(request.getParameter("giorno"));
        System.out.println(request.getParameter("ora"));
        int codiceCorso = Integer.parseInt(request.getParameter("corso"));
        String giorno = request.getParameter("giorno");
        String ora = request.getParameter("ora");
        List<Docente> listRes = dao.docentiDisponibili(codiceCorso, giorno, ora);
        Gson gson = new Gson();
        String s = gson.toJson(listRes);
        System.out.println(s);
        out.print(s);
        out.flush();
    }

    private String convertUrlStr(String str){
        String giorno = null;
        switch (str){
            case "luned%C3%AC": giorno = "lunedì";
                break;
            case "marted%C3%AC": giorno = "lunedì";
                break;
            case "mercoled%C3%AC": giorno = "lunedì";
                break;
            case "gioved%C3%AC": giorno = "lunedì";
                break;
            case "venerd%C3%AC": giorno = "lunedì";
                break;
        }
        return giorno;
    }
}
