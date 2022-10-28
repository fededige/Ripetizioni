package com.example.ripetizioni;

import dao.DAO;
import dao.Docente;

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

@WebServlet(name = "docentiServlet", value = "/docenti-servlet")
public class
DocentiServlet extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession s = request.getSession();
        PrintWriter out = response.getWriter();
        String url = response.encodeURL("ServletAuth");
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet Auth</title>");
        out.println("</head>");
        out.println("<body>");
        if(s.getAttribute("ruolo") != null && s.getAttribute("ruolo").equals("admin")){
            String matricola = request.getParameter("matricola");
            String nome = request.getParameter("nome");
            String cognome = request.getParameter("cognome");
            try {
                if (matricola != null && nome != null && cognome != null) {
                    ArrayList<Docente> docenti = new ArrayList<>();
                    docenti.add(new Docente(Integer.parseInt(matricola), nome, cognome));
                    dao.insertDocenti(docenti);
                    out.println("<h2>Inserimento docenti avvenuto con successo! </h2>");
                }
            }finally{
            }
        }else{
            out.println("<h2>Solo gli admin possono inserire nuovi docenti </h2>");
        }
        out.println("</body>");
        out.println("</html>");
        out.close();
    }


    public void destroy(){
    }
}