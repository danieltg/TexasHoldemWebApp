package chat.servlets;

import Engine.GameDescriptor.PokerGameDescriptor;
import Engine.GamesDescriptorManager;
import Engine.Lobby;
import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static chat.constants.Constants.GAME_TITLE;

@WebServlet("/joinRoom")
public class JoinRoomServlet extends HttpServlet {

    private final String LOBBY_ROOM_URL = "../pages/gameRoom/room.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        String roomName = request.getParameter(GAME_TITLE);
        Lobby lobby=ServletUtils.getLobby(getServletContext());

        lobby.addPlayerToRoom(roomName,usernameFromSession,"computer");
        System.out.println("User: "+ usernameFromSession +" join room: "+roomName);
        PrintWriter out = response.getWriter();
        out.print(roomName);

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