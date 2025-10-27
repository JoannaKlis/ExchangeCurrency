package data;

import model.ExchangeRate;
import model.ExchangeTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class XMLParser implements IParser {

    @Override
    public ExchangeTable parse(String xmlData) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8)));
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();

        // parsowanie informacji o tabeli
        ExchangeTable table = parseTableInfo(root);

        // parsowanie pozycji walut
        NodeList positionList = root.getElementsByTagName("pozycja");
        for (int i = 0; i < positionList.getLength(); i++) {
            Element positionNode = (Element) positionList.item(i);
            ExchangeRate rate = parsePosition(positionNode);
            table.addRate(rate);
        }
        return table;
    }

    private ExchangeTable parseTableInfo(Element root) {
        // parsowanie informacji o walutach
        String tableNumber = root.getElementsByTagName("numer_tabeli").item(0).getTextContent();
        String publicationDate = root.getElementsByTagName("data_publikacji").item(0).getTextContent();
        String tableType = root.getAttribute("typ");
        String uid = root.getAttribute("uid");

        return new ExchangeTable();
    }

    private ExchangeRate parsePosition(Element positionNode) {
        // przelicznik jako int
        int multiplier = Integer.parseInt(positionNode.getElementsByTagName("przelicznik").item(0).getTextContent());
        String currencyCode = positionNode.getElementsByTagName("kod_waluty").item(0).getTextContent();
        // zamiana , na .
        String rateString = positionNode.getElementsByTagName("kurs_sredni").item(0).getTextContent().replace(',', '.');
        double averageRate = Double.parseDouble(rateString);

        return new ExchangeRate(multiplier, currencyCode, averageRate);
    }
}