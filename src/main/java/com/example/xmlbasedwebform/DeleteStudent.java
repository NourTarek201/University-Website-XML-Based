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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

@WebServlet(name = "deletestudent", value = "/remove-student")
public class DeleteStudent extends HttpServlet {
    private String path = "D:\\My Faculty Material\\Fourth Year\\First Semester\\Service Oriented Architecture\\Assign1\\XML-Based-Web-Form\\src\\main\\webapp\\university.xml";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("deleteStudent.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentID = req.getParameter("student-id");

        if (studentID == null || studentID.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Student ID is required");
            return;
        }

        try {
            File xmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList studentList = doc.getElementsByTagName("Student");
            boolean studentFound = false;

            for (int i = 0; i < studentList.getLength(); i++) {
                Node studentNode = studentList.item(i);

                if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element studentElement = (Element) studentNode;
                    String id = studentElement.getAttribute("ID");

                    if (id.equals(studentID)) {
                        studentElement.getParentNode().removeChild(studentNode);
                        studentFound = true;
                        break;
                    }
                }
            }

            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<html><body>");
            out.println("<head><link rel=\"stylesheet\" href=\"form.css\"></head>");
            out.println("<div style=\"display: flex; justify-content: center; align-items: center; height: 100vh;\">");
            out.println("<div style=\"text-align: center;\">");

            if (studentFound) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(xmlFile);
                transformer.transform(source, result);

                out.println("<p>Student with ID " + studentID + " has been deleted successfully.</p>");
            } else {
                out.println("<p>No student found with ID: " + studentID + ".</p>");
            }

            out.println("<a href=\"./\" class=\"submit-btn\" style=\"display: inline-block; padding: 10px 20px; margin-top: 15px; text-decoration: none; \">Home Page</a>");
            out.println("</div></div>");
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing the request.");
        }
    }
}
