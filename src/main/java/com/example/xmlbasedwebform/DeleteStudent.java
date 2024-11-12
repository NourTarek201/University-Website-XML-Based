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
            // Load the XML file
            File xmlFile = new File("C:\\Users\\G-15\\OneDrive\\Documents\\GitHub\\XML-Based-Web-Form\\src\\main\\webapp\\university.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            // Normalize the XML structure
            doc.getDocumentElement().normalize();

            // Use XPath to find the student with the given ID
            NodeList studentList = doc.getElementsByTagName("Student");
            boolean studentFound = false;

            for (int i = 0; i < studentList.getLength(); i++) {
                Node studentNode = studentList.item(i);

                if (studentNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element studentElement = (Element) studentNode;
                    String id = studentElement.getAttribute("ID");

                    if (id.equals(studentID)) {
                        studentElement.getParentNode().removeChild(studentNode);  // Remove the student element
                        studentFound = true;
                        break;
                    }
                }
            }

            if (studentFound) {
                // Save the modified XML back to the file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(xmlFile);
                transformer.transform(source, result);

                resp.getWriter().write("Student with ID " + studentID + " has been deleted successfully.");
                resp.setContentType("text/html");
                PrintWriter out = resp.getWriter();
                out.println("<html><body>");
                out.println("<head><link rel=\"stylesheet\" href=\"form.css\"></head>");
                out.println("<p> User Added </p>");
                out.println("<a href=\"./\"> home page </a>");
                out.println("</body></html>");
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Student not found with ID: " + studentID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing the request.");
        }
    }
}
