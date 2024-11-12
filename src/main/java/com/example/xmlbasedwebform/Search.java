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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirect to the allStudents page
        response.sendRedirect("Search.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String firstNameSearch = request.getParameter("firstName");
        String gpaSearch = request.getParameter("gpa");

        try {
            File xmlFile = new File("C:\\Users\\G-15\\OneDrive\\Documents\\GitHub\\XML-Based-Web-Form\\src\\main\\webapp\\university.xml");

            // Initialize the XML document parser
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize(); // Normalize the XML document

            // Get all student elements from the XML
            NodeList studentList = doc.getElementsByTagName("Student");

            // Prepare the response writer and output the table headers
            response.getWriter().println("<html><body><h2>All Students</h2><table border='1' id='isoutput'><tr><th>Student ID</th><th>First Name</th><th>Last Name</th><th>Gender</th><th>GPA</th><th>Level</th><th>Address</th></tr>");

            // Loop through all students and display their information
            for (int i = 0; i < studentList.getLength(); i++) {
                Node studentNode = studentList.item(i);

                if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element studentElement = (Element) studentNode;

                    // Extract student details
                    String id = studentElement.getAttribute("ID");
                    String firstName = studentElement.getElementsByTagName("FirstName").item(0).getTextContent();
                    String lastName = studentElement.getElementsByTagName("LastName").item(0).getTextContent();
                    String gender = studentElement.getElementsByTagName("Gender").item(0).getTextContent();
                    String gpa = studentElement.getElementsByTagName("GPA").item(0).getTextContent();
                    String level = studentElement.getElementsByTagName("Level").item(0).getTextContent();
                    String address = studentElement.getElementsByTagName("Address").item(0).getTextContent();

                    // Apply the filters if search parameters are provided
                    boolean matchesSearch = true;
                    if (firstNameSearch != null && !firstNameSearch.isEmpty() && !firstName.toLowerCase().contains(firstNameSearch.toLowerCase())) {
                        matchesSearch = false;
                    }
                    if (gpaSearch != null && !gpaSearch.isEmpty() && !gpa.equals(gpaSearch)) {
                        matchesSearch = false;
                    }

                    // If the student matches the search criteria, display their information
                    if (matchesSearch) {
                        response.getWriter().println("<tr>");
                        response.getWriter().println("<td>" + id + "</td>");
                        response.getWriter().println("<td>" + firstName + "</td>");
                        response.getWriter().println("<td>" + lastName + "</td>");
                        response.getWriter().println("<td>" + gender + "</td>");
                        response.getWriter().println("<td>" + gpa + "</td>");
                        response.getWriter().println("<td>" + level + "</td>");
                        response.getWriter().println("<td>" + address + "</td>");
                        response.getWriter().println("</tr>");
                    }
                }
            }
            response.getWriter().println("</table></body></html>");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<head><link rel=\"stylesheet\" href=\"form.css\"></head>");
            out.println("<p> User Added </p>");
            out.println("<a href=\"./\"> home page </a>");
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing the request.");
        }

    }
}
