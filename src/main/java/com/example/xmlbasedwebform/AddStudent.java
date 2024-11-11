package com.example.xmlbasedwebform;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

@WebServlet(name = "addstudent", value = "/add-student")
public class AddStudent extends HttpServlet {
    private String path = "C:\\Users\\IDEAPAD GAMING\\IdeaProjects\\XML-Based-Web-Form\\src\\main\\webapp\\university.xml";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("addStudent.html");
    }

    void writeXML(Document document) {
        try (FileOutputStream output =
                     new FileOutputStream(path)) {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(output);

            transformer.transform(source, result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, RuntimeException {
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
        Document document;

        try {
            File file = new File(path);
            document = builder.parse(file);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        Element rootElement = document.getDocumentElement();
        Element XMLStudent = document.createElement("Student");
        XMLStudent.setAttribute("ID", studentID);

        Element XMLFirstName = document.createElement("FirstName");
        XMLFirstName.appendChild(document.createTextNode(firstName));
        XMLStudent.appendChild(XMLFirstName);

        Element XMLLastName = document.createElement("LastName");
        XMLLastName.appendChild(document.createTextNode(lastName));
        XMLStudent.appendChild(XMLLastName);

        Element XMLGender = document.createElement("Gender");
        XMLGender.appendChild(document.createTextNode(gender));
        XMLStudent.appendChild(XMLGender);

        Element XMLGPA = document.createElement("GPA");
        XMLGPA.appendChild(document.createTextNode(gpa));
        XMLStudent.appendChild(XMLGPA);

        Element XMLLevel = document.createElement("Level");
        XMLLevel.appendChild(document.createTextNode(level));
        XMLStudent.appendChild(XMLLevel);

        Element XMLAddress = document.createElement("Address");
        XMLAddress.appendChild(document.createTextNode(address));
        XMLStudent.appendChild(XMLAddress);

        rootElement.appendChild(XMLStudent);
        writeXML(document);


        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<head><link rel=\"stylesheet\" href=\"form.css\"></head>");
        out.println("<p> User Added </p>");
        out.println("<a href=\"./\"> home page </a>");
        out.println("</body></html>");
    }
}
