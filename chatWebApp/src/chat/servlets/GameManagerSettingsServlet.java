package chat.servlets;

import Engine.GameManager;
import Engine.Lobby;
import Engine.Room;
import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//@WebServlet("/gameManagerSettings")
public class GameManagerSettingsServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter())
        {
            Lobby lobby= ServletUtils.getLobby(getServletContext());
            String usernameFromSession = SessionUtils.getUsername(request);
            if (usernameFromSession!=null)
            {
                String roomName= lobby.getRoomNameByPlayerName(usernameFromSession);
                if (roomName==null)
                    System.out.println("roomName is null");
                Room roomToDisplay=lobby.getRoomByName(roomName);
                if (roomToDisplay==null)
                    System.out.println("roomToDisplay is null");
                GameManager gameManager= roomToDisplay.getGameManager();
                if (gameManager==null)
                    System.out.println("gameManager is null");

                Gson gson = new Gson();
                String json = gson.toJson(gameManager);
                out.println(json);
                out.flush();
            }

        }

        catch (Exception e)
        {
            System.out.println(e.getMessage());
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
