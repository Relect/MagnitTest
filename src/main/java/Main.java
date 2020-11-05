import org.w3c.dom.Document;
import org.w3c.dom.Element;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.SQLOutput;

public class Main {
    public static void main(String[] args) throws SQLException {
        long m = System.currentTimeMillis();

        TestService service = new TestService();
        service.setUrldb("jdbc:mysql://localhost:3306/java?rewriteBatchedStatements=true&serverTimezone=UTC");
        service.setUsername("root");
        service.setPassword("root");
        service.setN(500);

        service.createTestTable();
        service.saveTestTableN();
        System.out.println("время сохранения "+ service.getN()+ " значений " + (System.currentTimeMillis() - m)/1000 +
                 " секунд");

        int N = service.readTestTableN();

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("entries");
            doc.appendChild(rootElement);

            for (int i = 0; i < N; i++) {
                Element entry = doc.createElement("entry");
                rootElement.appendChild(entry);

                Element field = doc.createElement("field");
                field.appendChild(doc.createTextNode(Integer.toString(i+1)));
                entry.appendChild(field);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("C:\\1.xml"));
            transformer.transform(source, result);


            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(new File("form.xsl"));
            Transformer transformer1 = factory.newTransformer(xslt);
            Source xml = new StreamSource(new File("C:\\1.xml"));
            transformer1.transform(xml, new StreamResult(new File("C:\\2.xml")));
            

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
        try (StaxStreamProcessor processor = new StaxStreamProcessor(Files.newInputStream
                (Paths.get("C:\\2.xml")))) {
            XMLStreamReader reader = processor.getReader();
            long summ = 0;
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamReader.END_ELEMENT && reader.hasNext()) {
                    event = reader.next();
                }
                if (event != XMLStreamReader.END_DOCUMENT && "entry".equals(reader.getLocalName())) {
                    summ = summ + Integer.parseInt(reader.getAttributeValue(0));
                }
            }
            System.out.println("Сумма всех записей в таблице 2.XML = " + summ);
        } catch (Exception e) {e.printStackTrace();}
    }
}
