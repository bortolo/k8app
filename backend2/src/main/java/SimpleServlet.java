/*
All servlets need to extend the abstract class HttpServlet.
HttpServlet provides all the methods to build a HTTP response – what we do here
is override some of the methods to put our own logic.

doGet method is executed when the client (browser) makes a GET request to our
servlet. In our example it will just return the text response “Hello World!”.

init() method is called once when the servlet is started for the first time
by the web container. This happens either on application deployment or the
first call to this servlet. The init method can be used to set a database
connection or other time consuming initialization operations.

destroy() method is called when the servlet is terminated by the web container.
We can use this method to shut down connections to our database or other open
resources.
*/

package com.myservlet.experiment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class SimpleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Preprocess request: we actually don't need to do any business stuff, so just display JSP.
        request.getRequestDispatcher("/WEB-INF/hello.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Postprocess request: gather and validate submitted data and display the result in the same JSP.

        // Prepare messages.
        Map<String, String> messages = new HashMap<String, String>();
        request.setAttribute("messages", messages);

        // Get and validate name.
        String name = request.getParameter("name");
        if (name == null || name.trim().isEmpty()) {
            messages.put("name", "Please enter name");
        } else if (!name.matches("\\p{Alnum}+")) {
            messages.put("name", "Please enter alphanumeric characters only");
        }

        // Get and validate age.
        String age = request.getParameter("age");
        if (age == null || age.trim().isEmpty()) {
            messages.put("age", "Please enter age");
        } else if (!age.matches("\\d+")) {
            messages.put("age", "Please enter digits only");
        }

        // No validation errors? Do the business job!
        if (messages.isEmpty()) {
            messages.put("success", String.format("Hello, your name is %s and your age is %s!", name, age));
        }

        request.getRequestDispatcher("/WEB-INF/hello.jsp").forward(request, response);
    }

}
