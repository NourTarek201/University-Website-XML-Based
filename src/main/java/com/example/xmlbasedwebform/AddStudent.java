package com.example.xmlbasedwebform;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
import java.nio.file.Paths;

@WebServlet(name = "addstudent", value = "/add-student")
public class AddStudent extends HttpServlet {
    private String path = Paths.get(System.getProperty("user.dir")).getParent().toString()+"\\webapps\\XML_Based_Web_Form_war\\university.xml";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("action", "add-student");
        RequestDispatcher dispatcher = request.getRequestDispatcher("addStudent.jsp");
        dispatcher.forward(request, response);
        response.sendRedirect("addStudent.jsp");
    }

    public static void writeXML(Document document, String path)  {
        try (FileOutputStream output = new FileOutputStream(path)) {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(output);
            transformer.transform(source, result);
        } catch (IOException | TransformerException e) {
            e.printStackTrace();
        }
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
        DocumentBuilder builder;
        Document document;

        try {
            builder = factory.newDocumentBuilder();
            File file = new File(path);
            document = builder.parse(file);
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }

        Element rootElement = document.getDocumentElement();

        NodeList students = rootElement.getElementsByTagName("Student");
        boolean idExists = false;
        for (int i = 0; i < students.getLength(); i++) {
            Node student = students.item(i);
            if (student.getNodeType() == Node.ELEMENT_NODE) {
                Element studentElement = (Element) student;
                String existingID = studentElement.getAttribute("ID");
                if (existingID.equals(studentID)) {
                    idExists = true;
                    break;
                }
            }
        }

        if (idExists) {
            response.setContentType("text/html");
            try (PrintWriter out = response.getWriter()) {
                out.println("<html><body>");
                out.println("<head><link rel=\"stylesheet\" href=\"form.css\"></head>");
                out.println("<div class=\"form-container\"><a href=\"./\" class=\"home-btn\"> Home page </a>");
                out.println("<h3>No students with the same ID allowed!</h3>");
                out.println("</body></html>");

            }
        } else {
            // Add new student details to XML if ID does not exist
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
            writeXML(document, path);

            response.sendRedirect("./add-student");
        }
    }
}
