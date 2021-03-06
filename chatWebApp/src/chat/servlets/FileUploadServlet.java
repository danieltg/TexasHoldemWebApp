package chat.servlets;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html
import Engine.GameDescriptor.PokerGameDescriptor;
import Engine.GameDescriptor.ReadGameDescriptorFile;
import Engine.GamesDescriptorManager;
import Engine.Lobby;
import Engine.users.UserManager;
import chat.utils.ServletUtils;
import chat.utils.SessionUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;

import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

//@WebServlet("/upload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    private final String SIGN_UP_URL = "../signup/singup.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/pages/fileupload/form.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        if (usernameFromSession == null) {
            //no username in session and no username in parameter - redirect back to the index page
            //this return an HTTP code back to the browser telling it to load
            response.sendRedirect(SIGN_UP_URL);
        }
        else
        {
            GamesDescriptorManager gamesManager = ServletUtils.getGamesDescriptorManager(getServletContext());
            Lobby lobby=ServletUtils.getLobby(getServletContext());

            System.out.println("Username: "+usernameFromSession);

            PrintWriter out = response.getWriter();
            Collection<Part> parts = request.getParts();
            StringBuilder fileContent = new StringBuilder();

            for (Part part : parts) {
                part.write("samplefile");
                fileContent.append(readFromInputStream(part.getInputStream()));
            }

            try
            {
                gamesManager.addGameDescriptor(validateFileContent(fileContent.toString(), out,usernameFromSession),lobby);
            }
            catch (Exception e)
            {
                response.setStatus(400);
                out.print(e.getMessage());
                out.flush();
            }

        }

    }


    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private void logServerMessage(String message){
        System.out.println(message);
    }


    private PokerGameDescriptor validateFileContent(String content,PrintWriter out,String username) throws Exception
    {
        ReadGameDescriptorFile fileReader= new ReadGameDescriptorFile();
        PokerGameDescriptor game=fileReader.readFileContent(content,username);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(fileReader.getGameDescriptor());
        logServerMessage(jsonResponse);
        return game;
    }

}