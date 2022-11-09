package com.example.ripetizioni;

import dao.Corso;
import dao.DAO;
import dao.Docente;
import dao.Utente;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

@WebServlet(name = "ServletAuth", value = "/ServletAuth")
public class ServletAuth extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession s = request.getSession();
        String login =  request.getParameter("login");
        String password = request.getParameter("password");
        PrintWriter out = response.getWriter();
        String url = response.encodeURL("ServletAuth");
        if (login != null && password != null) {
            Utente user = dao.utenteEsistente(login, password);
            if(user != null && user.getNome_utente() != null) {
                s.setAttribute("login", user.getNome_utente());
                s.setAttribute("ruolo", user.getRuolo());
                out.println("<h2>Sei loggato</h2>");
            }else if(user == null){
                out.println("<h2>Utente inesistente</h2>");
            }
            else {
                out.println("<h2>Password errata</h2>");
            }
        }
        out.flush();
        out.close();
        }
    public void destroy(){
    }
}

/*int[][] c = dao.prentoazioni_disp(null, new Corso(1234, "informatica"), new Utente("ema", "Pippo", "cliente"));
                //out.println("prima del for" + c.length);
                for(int i = 0; i < 5; i++){
                    for(int j = 0; j < 4; j++){
                        out.print(c[i][j] + " ");
                        System.out.print("<p> ciao" + c[i][j] + "</p>");
                    }
                    out.println("<br>");
                    System.out.println();
                }*/
