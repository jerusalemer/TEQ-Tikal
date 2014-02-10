package service;

import com.sun.org.apache.xpath.internal.NodeSet;
import model.Candidate;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import play.Logger;
import play.Play;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Created by Art on 2/10/14.
 */
@Component
public class CsvExporter {

    public String toExcel(Candidate candidate){
        String excelTemplate = getExcelTemplate();
        return generateExcel(excelTemplate, candidate);
    }

    private String generateExcel(String excelTemplate, Candidate candidate) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new ByteArrayInputStream(excelTemplate.getBytes("utf-8"))));
            // Do something with the document here.

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            //XPathExpression expr = xpath.compile("//text:p//*[contains(., '{question}')]");
            Node str = (Node) xpath.compile("//*[local-name()='p'][text()=\"{question}\"]").evaluate(doc, XPathConstants.NODE);

            return toString(doc);
        } catch (Exception e) {
            Logger.error("Error parsing template file", e);
            throw new RuntimeException(e);
        }
    }

    private String toString(Document doc) throws Exception {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        StringWriter buffer = new StringWriter();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(doc),
                new StreamResult(buffer));
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + buffer.toString();
    }

    private String getExcelTemplate() {
        InputStream inputStream = Play.application().resourceAsStream("excel_template.xml");
        try {
            return IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            Logger.error("Error reading template file", e);
            throw new RuntimeException(e);
        }
    }
}