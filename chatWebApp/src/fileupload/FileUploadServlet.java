package fileupload;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html
import Engine.GameDescriptor.ReadGameDescriptorFile;
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

@WebServlet("/upload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/pages/fileupload/form.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Collection<Part> parts = request.getParts();

        StringBuilder fileContent = new StringBuilder();

        for (Part part : parts) {
            part.write("samplefile");
            fileContent.append(readFromInputStream(part.getInputStream()));
        }

        String res=validateFileContent(fileContent.toString(), out);
        out.print(res);
        out.flush();
    }


    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private void logServerMessage(String message){
        System.out.println(message);
    }


    private String validateFileContent(String content,PrintWriter out)
    {
        ReadGameDescriptorFile fileReader= new ReadGameDescriptorFile();
        try {
            fileReader.readFileContent(content);
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(fileReader.getGameDescriptor());
            logServerMessage(jsonResponse);
            return jsonResponse;
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
    }

}