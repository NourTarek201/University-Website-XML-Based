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
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import com.example.xmlbasedwebform.AddStudent;

@WebServlet(name = "updatestudent", value = "/update-student")
public class UpdateStudent extends HttpServlet {
    private String path = "D:\\My Faculty Material\\Fourth Year\\First Semester\\Service Oriented Architecture\\Assign1\\XML-Based-Web-Form\\src\\main\\webapp\\university.xml";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        request.setAttribute("student-id", id);
        request.setAttribute("action", "update-student");


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
        Element studentElement = null;

        for (int i = 0; i < students.getLength(); i++) {
            Node student = students.item(i);
            if (student.getNodeType() == Node.ELEMENT_NODE) {
                studentElement = (Element) student;
                String existingID = studentElement.getAttribute("ID");
                if (existingID.equals(id)) {
                    break;
                }
            }
        }

        response.setContentType("text/html");

        String firstName = studentElement.getElementsByTagName("FirstName").item(0).getTextContent();
        String lastName = studentElement.getElementsByTagName("LastName").item(0).getTextContent();
        String gender = studentElement.getElementsByTagName("Gender").item(0).getTextContent();
        String gpa = studentElement.getElementsByTagName("GPA").item(0).getTextContent();
        String level = studentElement.getElementsByTagName("Level").item(0).getTextContent();
        String address = studentElement.getElementsByTagName("Address").item(0).getTextContent();


        request.setAttribute("first-name", firstName);
        request.setAttribute("last-name", lastName);
        request.setAttribute("gender", gender);
        request.setAttribute("gpa", gpa);
        request.setAttribute("level", level);
        request.setAttribute("address", address);


        RequestDispatcher dispatcher = request.getRequestDispatcher("addStudent.jsp");
        dispatcher.forward(request, response);


        response.sendRedirect("./addStudent.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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
        Element studentElement = null;
        for (int i = 0; i < students.getLength(); i++) {
            Node student = students.item(i);
            if (student.getNodeType() == Node.ELEMENT_NODE) {
                studentElement = (Element) student;
                String existingID = studentElement.getAttribute("ID");
                if (existingID.equals(studentID)) {
                    idExists = true;
                    break;
                }
            }
        }

        if (idExists) {
            response.setContentType("text/html");

            studentElement.getElementsByTagName("FirstName").item(0).setTextContent(firstName);
            studentElement.getElementsByTagName("LastName").item(0).setTextContent(lastName);
            studentElement.getElementsByTagName("Gender").item(0).setTextContent(gender);
            studentElement.getElementsByTagName("GPA").item(0).setTextContent(gpa);
            studentElement.getElementsByTagName("Level").item(0).setTextContent(level);
            studentElement.getElementsByTagName("Address").item(0).setTextContent(address);

            AddStudent.writeXML(document, path);

        }
        response.sendRedirect("./show-students");

    }
}
