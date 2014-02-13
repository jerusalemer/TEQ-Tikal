package service;

import model.Candidate;
import model.Expertise;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;
import play.Logger;
import play.Play;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by Art on 2/10/14.
 */
@Component
public class CsvExporter {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    public String toExcel(Candidate candidate) {
        InputStream excelTemplate = getExcelTemplate();
        return generateExcel(excelTemplate, candidate);
    }

    private String generateExcel(InputStream excelTemplate, Candidate candidate) {

        String excelWithExpertises = generateExpertises(excelTemplate, candidate);
        excelWithExpertises = excelWithExpertises.replace("{full_name}", candidate.getFirstName() + " " + candidate.getLastName());
        String dateStr = DATE_FORMAT.format(candidate.getRegistrationDate());
        excelWithExpertises = excelWithExpertises.replace("{date}", dateStr);
        excelWithExpertises = excelWithExpertises.replace("{candidate_group}", prettyPrintGroups(candidate));

        return excelWithExpertises;

    }

    private String prettyPrintGroups(Candidate candidate) {
        String groupsStr = Arrays.toString(candidate.getGroups().toArray());
        return groupsStr.substring(1, groupsStr.length() - 1);
    }

    private String generateExpertises(InputStream excelTemplate, Candidate candidate) {
        Document document = getDocument(excelTemplate);

        Node subCategoryTemplate = getRowWithText(document, "{sub_category}");
        Node expertiseRowTemplate = getRowWithText(document, "{question}");
        Node categoryTemplate = getRowWithText(document, "{category}");

        try {
            for (Map.Entry<String, ? extends SortedMap<String, List<Expertise>>> groupEntry : candidate.getExpertises().entrySet()) {
                addNewNode(document, categoryTemplate, "{category}", groupEntry.getKey());
                for (Map.Entry<String, List<Expertise>> subGroup : groupEntry.getValue().entrySet()) {
                    addNewNode(document, subCategoryTemplate, "{sub_category}", subGroup.getKey());
                    for (Expertise expertise : subGroup.getValue()) {
                        String level = expertise.getLevel() == null ? "" : expertise.getLevel().prettyPrint();
                        String years = expertise.getYearsOfExperience() == null ? "" : String.valueOf(expertise.getYearsOfExperience());
                        addExpertiseRow(document, expertiseRowTemplate, expertise.getName(), level, years);
                    }
                }
            }
        }catch (Exception e) {
            Logger.error("Error building excel", e);
            throw new RuntimeException(e);
        }

        categoryTemplate.detach();
        expertiseRowTemplate.detach();
        subCategoryTemplate.detach();

        return document.asXML();
    }

    public Document getDocument(final InputStream xmlFile) {
        Document document = null;
        SAXReader reader = new SAXReader();
        try {
            document = reader.read(xmlFile);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;
    }

    private Node getRowWithText(Document document, String text) {
        String xPath = "//table:table-row/table:table-cell/text:p[text()=\"" + text + "\"]')[0]";
        return document.selectSingleNode(xPath).getParent().getParent();

    }

    private void addNewNode(Document document, Node categoryTemplate, String nodeText, String nodePlaceholder) throws DocumentException {
        Element newRow = addAsLastRow(document, categoryTemplate);
        updateText(newRow, nodeText, nodePlaceholder);
    }

    private void updateText(Element rowElement, String placeHolder, String newText) {
        Node node = rowElement.selectSingleNode(".//text:p[text()=\"" + placeHolder + "\"]");
        node.setText(newText);
    }


    private void addExpertiseRow(Document document, Node questionRowTemplate, String question, String level, String years) {
        Element newRow = addAsLastRow(document, questionRowTemplate);
        updateQuestionRowData(newRow, question, level, years);
    }

    private Element addAsLastRow(Document document, Node questionRowTemplate) {
        Element newRow = (Element) questionRowTemplate.clone();
        Element lastRow = (Element) document.selectSingleNode("//table:table");
        lastRow.add(newRow);
        return newRow;
    }

    private void updateQuestionRowData(Node questionRowNode, String question, String level, String years) {
        Node questionNode = questionRowNode.selectSingleNode(".//text:p[text()=\"{question}\"]");
        questionNode.setText(question);

        Node levelNode = questionRowNode.selectSingleNode(".//text:p[text()=\"{level}\"]");
        levelNode.setText(level);

        Node yearsNode = questionRowNode.selectSingleNode(".//text:p[text()=\"{years}\"]");
        yearsNode.setText(years);
    }


    private InputStream getExcelTemplate() {
        return Play.application().resourceAsStream("excel_template.xml");
    }
}