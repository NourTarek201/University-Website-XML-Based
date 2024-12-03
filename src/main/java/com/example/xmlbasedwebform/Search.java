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
    private String path = "D:\\My Faculty Material\\Fourth Year\\First Semester\\Service Oriented Architecture\\Assign1\\XML-Based-Web-Form\\src\\main\\webapp\\university.xml";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("Search.html");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        String search_value = request.getParameter("value");
        String type = request.getParameter("search");
        int cnt = 0;
        try {
            File xmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize(); // Normalize the XML document

            NodeList studentList = doc.getElementsByTagName("Student");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<head><link rel=\"stylesheet\" href=\"form.css\"> <link rel=\"stylesheet\" href=\"table.css\"></head>");
            out.println("<div class=\"form-container-table\"><a href=\"./\" class=\"home-btn\"> Home page </a><h2><h2><table border='1' id='isoutput'><tr><th>Student ID</th><th>First Name</th><th>Last Name</th><th>Gender</th><th>GPA</th><th>Level</th><th>Address</th></tr>");

            boolean studentFound = false;
            String specific="";
            for (int i = 0; i < studentList.getLength(); i++) {
                Node studentNode = studentList.item(i);

                if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element studentElement = (Element) studentNode;
                    if(type.equals("ID")){
                        specific = studentElement.getAttribute("ID");
                    }
                    else if(!type.equals("-1")){
                       specific = studentElement.getElementsByTagName(type).item(0).getTextContent();
                    }
                    String id = studentElement.getAttribute("ID");
                    String firstName = studentElement.getElementsByTagName("FirstName").item(0).getTextContent();
                    String lastName = studentElement.getElementsByTagName("LastName").item(0).getTextContent();
                    String gender = studentElement.getElementsByTagName("Gender").item(0).getTextContent();
                    String gpa = studentElement.getElementsByTagName("GPA").item(0).getTextContent();
                    String level = studentElement.getElementsByTagName("Level").item(0).getTextContent();
                    String address = studentElement.getElementsByTagName("Address").item(0).getTextContent();

                    if ( (search_value != null || !search_value.isEmpty()) && ( type.equals("-1") ) &&(
                    search_value.equals(id) || search_value.equals(firstName) || search_value.equals(lastName)
                    || search_value.equals(gender) || search_value.equals(gpa) || search_value.equals(level) || search_value.equals(address))) {
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
                        cnt++;
                    }
                    else if(type!= null && !type.isEmpty() && search_value.equals(specific)){
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
                        cnt++;
                    }


                }
            }
            if (!studentFound) {
                out.println("<tr><td colspan='7'>Student not found</td></tr>");
            }
            out.println("</table></div>" +
                    "<script>var h2 = document.getElementsByTagName(\"h2\")[0];\n" +
                    "h2.innerText = \"All Students "+cnt+"\";</script></body></html>");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing the request.");
        }
    }
}
