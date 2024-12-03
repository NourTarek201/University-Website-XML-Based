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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "sort", value = "/sort")
public class SortStudents extends HttpServlet {
    private String path = "D:\\My Faculty Material\\Fourth Year\\First Semester\\Service Oriented Architecture\\Assign1\\XML-Based-Web-Form\\src\\main\\webapp\\university.xml";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("sort.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sortBy = request.getParameter("sort");
        String order = request.getParameter("order");

        if ("-1".equals(sortBy) || "-1".equals(order)) {
            response.sendRedirect("sort.html");
            return;
        }

        try {
            File xmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList studentList = doc.getElementsByTagName("Student");
            List<Element> students = new ArrayList<>();

            for (int i = 0; i < studentList.getLength(); i++) {
                Node node = studentList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    students.add((Element) node);
                }
            }

            students.sort((s1, s2) -> {
                String value1 = getSortValue(s1, sortBy);
                String value2 = getSortValue(s2, sortBy);

                int comparison;
                if (isNumeric(value1) && isNumeric(value2)) {
                    comparison = Double.compare(Double.parseDouble(value1), Double.parseDouble(value2));
                } else {
                    comparison = value1.compareToIgnoreCase(value2);
                }
                return "Asc".equals(order) ? comparison : -comparison;
            });
            Element root = doc.getDocumentElement();
            while (root.hasChildNodes()) {
                root.removeChild(root.getFirstChild());
            }
            for (Element student : students) {
                Node importedNode = doc.importNode(student, true);
                root.appendChild(importedNode);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<head><link rel=\"stylesheet\" href=\"form.css\"></head>");
            out.println("<div style=\"display: flex; justify-content: center; align-items: center; height: 100vh;\">");
            out.println("<div style=\"text-align: center;\">");
            out.println("<p>Data is sorted successfully.</p>");
            out.println("<a href=\"./\" class=\"home-btn\" style=\"display: inline-block; padding: 10px 20px; margin-top: 15px; text-decoration: none;\">Home Page</a>");
            out.println("</div></div>");
            out.println("</body></html>");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing the request.");
        }
    }

    private String getSortValue(Element student, String attribute) {
        if ("ID".equals(attribute)) {
            return student.getAttribute("ID");
        }
        return student.getElementsByTagName(attribute).item(0).getTextContent();
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
