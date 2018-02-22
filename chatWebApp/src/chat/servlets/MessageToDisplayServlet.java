package chat.servlets;

import Engine.GameDescriptor.PokerGameDescriptor;
import Engine.Lobby;
import Engine.Room;
import chat.utils.ServletUtils;
import chat.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/getMessageToDisplay")
public class MessageToDisplayServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String username = SessionUtils.getUsername(request);
        Lobby lobby=ServletUtils.getLobby(getServletContext());

        String roomName= lobby.getRoomNameByPlayerName(username);
        Room room=lobby.getRoomByName(roomName);
        String message= room.getGameManager().getMessageToDisplay();
        PrintWriter out = response.getWriter();

        if (message== null || room.getGameManager().getPlayerByName(username).didGotMessage())
        {
            //user already got the message so we don't want to show it again
            response.setStatus(400);
            out.flush();
        }
        else
        {
            room.getGameManager().userGotMessage(username);
            out.print("Hey "+username +"&&&"+message);
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
