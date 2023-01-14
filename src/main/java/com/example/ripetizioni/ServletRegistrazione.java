package com.example.ripetizioni;

import com.google.gson.Gson;
import dao.DAO;
import dao.Utente;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletRegistrazione", value = "/ServletRegistrazione")
public class ServletRegistrazione extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        String username =  request.getParameter("username");
        String password = request.getParameter("password");
        String confPassword = request.getParameter("confpassword");
        System.out.println(username + password + confPassword);
        PrintWriter out = response.getWriter();
        String s = null;
        if (username != null && password != null && confPassword != null) {
            if(password.equals(confPassword)){
                boolean f = dao.insertUtente(new Utente(username, password, "cliente"));
                if(f){
                    session.setAttribute("login", username);
                    session.setAttribute("ruolo", "cliente");
                    SessionUtils.sessionMap.put(session.getId(), session);
                    Gson gson = new Gson();
                    s = gson.toJson(session.getId() + ";" + session.getAttribute("ruolo"));
                }
                System.out.println("s = " + s);
                out.print(s);
            }
        }
        out.flush();
    }
}
