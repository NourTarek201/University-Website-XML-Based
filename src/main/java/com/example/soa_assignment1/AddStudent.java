package com.example.soa_assignment1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "addstudent", value = "/add-student")
public class AddStudent extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("addStudent.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String studentID = request.getParameter("student-id");
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("last-name");
        String gender = request.getParameter("gender");
        String level = request.getParameter("level");
        String gpa = request.getParameter("gpa");
        String address = request.getParameter("address");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        File file = new File("university.xml");
        // Load the input XML document, parse it and return an instance of the
        // Document class.
        try {
            Document document = builder.parse(file);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
//        response.setContentType("text/html");
//        PrintWriter out = response.getWriter();
//        out.println("<html><body>");
//        out.println("<p>" + studentID + "</p>");
//        out.println("<p>" + firstName + "</p>");
//        out.println("<p>" + lastName + "</p>");
//        out.println("<p>" + gender + "</p>");
//        out.println("<p>" + level + "</p>");
//        out.println("<p>" + gpa + "</p>");
//        out.println("<p>" + address + "</p>");
//        out.println("</body></html>");
    }
}
