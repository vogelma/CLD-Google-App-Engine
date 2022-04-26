package ch.heigvd.cld.lab;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;

@WebServlet(name = "DatastoreWrite", value = "/datastorewrite")
public class DatastoreWriteSimple extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                         throws ServletException, IOException {
		
        resp.setContentType("text/plain");
        PrintWriter pw = resp.getWriter();
        pw.println("Writing entity to datastore.");

        //parse parameter name
        ArrayList<String> parameterNames = new ArrayList<String>();
        Enumeration<String> enumeration = req.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String parameterName = (String) enumeration.nextElement();
            parameterNames.add(parameterName);
        }


        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Entity book;

        if(parameterNames.contains("_key")){
            book = new Entity(req.getParameter("_kind"), req.getParameter("_key"));
            parameterNames.remove("_key");
        }else{
            book = new Entity(req.getParameter("_kind"));
        }

        parameterNames.remove("_kind");

        for(String param : parameterNames){
            book.setProperty(param, req.getParameter(param));
            pw.println(param + ": " + req.getParameter(param));
        }

        datastore.put(book);

    }
}
