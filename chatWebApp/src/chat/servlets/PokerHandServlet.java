package chat.servlets;

import Engine.Lobby;
import Engine.PokerHand;
import Engine.Room;
import Engine.Utils.RoomState;
import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//@WebServlet("/getPokerHand")
public class PokerHandServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Lobby lobby = ServletUtils.getLobby(getServletContext());
            String userName = SessionUtils.getUsername(request);
            if (userName != null) {
                String roomName = lobby.getRoomNameByPlayerName(userName);

                if (roomName == null)
                    System.out.println("roomName is null");

                Room roomToDisplay = lobby.getRoomByName(roomName);
                if (roomToDisplay == null)
                    System.out.println("roomToDisplay is null");
                if (roomToDisplay.getRoomState() == RoomState.RUNNING) {
                    PokerHand currHand = roomToDisplay.getGameManager().getCurrHand();
                    Gson gson = new Gson();
                    String json = gson.toJson(currHand);
                    out.println(json);
                    out.flush();
                }
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
