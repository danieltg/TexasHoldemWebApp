package chat.servlets;

import Engine.Lobby;
import Engine.users.Info;
import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/gameuserlist")
public class GameUsersListServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");

        String usernameFromSession = SessionUtils.getUsername(request);
        Lobby lobby=ServletUtils.getLobby(getServletContext());
        if (usernameFromSession!=null) {
            String roomName = lobby.getRoomNameByPlayerName(usernameFromSession);
            Map<String, String> users = lobby.getRoomByName(roomName).getUsersInGame();

            Info foo = new Info(usernameFromSession, users);

            try (PrintWriter out = response.getWriter()) {

                Gson gson = new Gson();
                String json = gson.toJson(foo);
                out.println(json);
                out.flush();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

