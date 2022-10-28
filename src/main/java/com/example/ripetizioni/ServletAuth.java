package com.example.ripetizioni;

import dao.DAO;
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
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet Auth</title>");
        out.println("</head>");
        out.println("<body>");
        String azione = request.getParameter("action");
        if (azione!=null && azione.equals("invalida")) {
            s.invalidate();
            out.println("<p>Sessione invalidata!</p>");
            out.println("<p> <a href=\"" + url + "\">Ricarica</a></p>");
        }
        else{
            if (login != null && password != null) {
                Utente user = DAO.utenteEsistente(login, password);
                if(user != null) {
                    s.setAttribute("login", user.getNome_utente());
                    s.setAttribute("ruolo", user.getRuolo());
                    out.println("<p> Ciao, sei loggato con " + s.getAttribute("login") + "</p>");
                    out.println(" E hai ruolo: " + s.getAttribute("ruolo") + "<br>");
                    if (s.isNew())
                        out.println(" nuova sessione</p>");
                    else
                        out.println(" vecchia sessione</p>");
                    out.println("<p>ID di sessione: "+s.getId() + "</p>");
                    out.println("<p>Data di creazione: " + new Date(s.getCreationTime()) + "</p>");
                    out.println("<p>Max inactive time interval (in second): "
                            + s.getMaxInactiveInterval() + "</p>");
                    out.println("<p><a href=\"" + url + "?action=invalida\">Invalida</a></p>");
                }else{
                    System.out.println("VERIFICA");
                    out.println("<h2>Utente inesistente<h2>");
                }
            }
            else{
                out.println("<p> Ciao, sei loggato con " + s.getAttribute("login") + "</p>");
                out.println("<br> E hai ruolo: " + s.getAttribute("ruolo"));
                if (s.isNew())
                    out.println(" nuova sessione</p>");
                else
                    out.println(" vecchia sessione</p>");
                out.println("<p>ID di sessione: "+s.getId() + "</p>");
                out.println("<p>Data di creazione: " + new Date(s.getCreationTime()) + "</p>");
                out.println("<p>Max inactive time interval (in second): "
                        + s.getMaxInactiveInterval() + "</p>");
                out.println("<p><a href=\"" + url + "?action=invalida\">Invalida</a></p>");
            }
            out.println("</body>");
            out.println("</html>");
            out.close();
        }
    }
    public void destroy(){
    }
}
