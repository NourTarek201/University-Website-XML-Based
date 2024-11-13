package com.example.xmlbasedwebform;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

@WebServlet(name = "search", value = "/search")
public class Search extends HttpServlet {
    private String path = "C:\\Users\\G-15\\OneDrive\\Documents\\GitHub\\XML-Based-Web-Form\\src\\main\\webapp\\university.xml";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("Search.html");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String firstNameSearch = request.getParameter("firstName");
        String gpaSearch = request.getParameter("gpa");

        try {
            File xmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize(); // Normalize the XML document

            NodeList studentList = doc.getElementsByTagName("Student");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<head><link rel=\"stylesheet\" href=\"form.css\"></head>");
            out.println("<div class=\"form-container\"><a href=\"./\" class=\"submit-btn\"> home page </a><h2>All Students</h2><table border='1' id='isoutput'><tr><th>Student ID</th><th>First Name</th><th>Last Name</th><th>Gender</th><th>GPA</th><th>Level</th><th>Address</th></tr>");

            boolean studentFound = false;

            for (int i = 0; i < studentList.getLength(); i++) {
                Node studentNode = studentList.item(i);

                if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element studentElement = (Element) studentNode;

                    String id = studentElement.getAttribute("ID");
                    String firstName = studentElement.getElementsByTagName("FirstName").item(0).getTextContent();
                    String lastName = studentElement.getElementsByTagName("LastName").item(0).getTextContent();
                    String gender = studentElement.getElementsByTagName("Gender").item(0).getTextContent();
                    String gpa = studentElement.getElementsByTagName("GPA").item(0).getTextContent();
                    String level = studentElement.getElementsByTagName("Level").item(0).getTextContent();
                    String address = studentElement.getElementsByTagName("Address").item(0).getTextContent();

                    boolean matchesSearch = true;
                    if (firstNameSearch != null && !firstNameSearch.isEmpty() && !firstName.toLowerCase().contains(firstNameSearch.toLowerCase())) {
                        matchesSearch = false;
                    }
                    if (gpaSearch != null && !gpaSearch.isEmpty() && !gpa.equals(gpaSearch)) {
                        matchesSearch = false;
                    }

                    if (matchesSearch) {
                        studentFound = true;
                        out.println("<tr>");
                        out.println("<td>" + id + "</td>");
                        out.println("<td>" + firstName + "</td>");
                        out.println("<td>" + lastName + "</td>");
                        out.println("<td>" + gender + "</td>");
                        out.println("<td>" + gpa + "</td>");
                        out.println("<td>" + level + "</td>");
                        out.println("<td>" + address + "</td>");
                        out.println("</tr>");
                    }
                }
            }
            if (!studentFound) {
                out.println("<tr><td colspan='7'>Student not found</td></tr>");
            }
            out.println("</table></div></body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing the request.");
        }
    }
}
