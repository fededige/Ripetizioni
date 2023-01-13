package com.example.ripetizioni;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.DAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletLogOut", value = "/ServletLogOut")
public class ServletLogOut extends HttpServlet {
    DAO dao = null;
    public void init(ServletConfig conf) throws ServletException{
        super.init(conf);
        ServletContext ctx = conf.getServletContext();
        dao = (DAO) ctx.getAttribute("DAO");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String sessionID = "";
        String body = request.getReader().readLine();
        if(SessionUtils.isJson(body)){
            JsonObject obj = new JsonParser().parse(body).getAsJsonObject();
            sessionID = obj.get("session").getAsString();
        } else {
            sessionID = body.split("session=")[1];
        }
        HttpSession session = SessionUtils.sessionMap.get(sessionID);

        boolean res = false;
        if(session.getAttribute("ruolo").equals("admin") || session.getAttribute("ruolo").equals("cliente")){
            SessionUtils.sessionMap.remove(session.getId());
            session.invalidate();
            res = true;
        }
        out.print(res);
        out.flush();
    }


}
