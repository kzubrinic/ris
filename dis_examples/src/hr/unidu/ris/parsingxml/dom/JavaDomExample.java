package hr.unidu.ris.parsingxml.dom;

/**
 * Dohvat svih elemenata XML dokumenta pomoću DOM parsera
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class JavaDomExample {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		final String FILENAME = "data/kolegiji.xml";
		// Instantiate the Factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// optional, but recommended
		// process XML securely, avoid attacks like XML External Entities (XXE)
		dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

		// parse XML file
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new File(FILENAME));

		// optional, but recommended
		// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

		System.out.println("KORIJEN :" + doc.getDocumentElement().getNodeName());
		System.out.println("-------------------------------------------------------");

		// dohvati element "kolegij"
		NodeList list = doc.getElementsByTagName("kolegij");
		for (int temp = 0; temp < list.getLength(); temp++) {
			Node node = list.item(temp);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				// dohvati atribut "id" elementa "kolegij"
				String id = element.getAttribute("id");
				// dohvati text elemenata
				String naziv = element.getElementsByTagName("naziv").item(0).getTextContent();
				String ects = element.getElementsByTagName("ects").item(0).getTextContent();
				System.out.println("*****************************************************");
				System.out.println("Element:" + node.getNodeName());
				System.out.println("ID kolegija: " + id);
				System.out.println("Naziv kolegija: " + naziv);
				System.out.println("Broj ECTS-ova: " + ects);
				System.out.println("Nastavnici na kolegiju: ");
				System.out.println("-----------------------------------------------------");
				// dohvati listu elemenata "nastavnik"
				NodeList listaNastavnika = element.getElementsByTagName("nastavnik");
				// dohvati tekst i atribute svakog elementa "nastavnik"
				for (int i = 0; i < listaNastavnika.getLength(); i++) {
					// dohvati text
					String nastavnik = listaNastavnika.item(i).getTextContent();
					// dohvati attribut "tip"
					String tip = listaNastavnika.item(i).getAttributes().getNamedItem("tip").getTextContent();
					System.out.println("Ime nastavnika:" + nastavnik + " (" + tip + ")");
				}
			}
		}
	}
}
