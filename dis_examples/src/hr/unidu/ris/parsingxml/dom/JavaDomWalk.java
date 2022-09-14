package hr.unidu.ris.parsingxml.dom;
/**
 * "Šetnja" kroz XML stablo pomoću DOM parsera i klase NodeIterator
 */
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

public class JavaDomWalk {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		JavaDomWalk p = new JavaDomWalk();
		final String FILENAME = "data/kolegiji.xml";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(FILENAME);
		DocumentTraversal trav = (DocumentTraversal) document;

		System.out.println("****************************************");
		// ispis elemenata, atributa i teksta
		p.ispis(document, trav);
		System.out.println("****************************************");
	}

	private void ispis(Document document, DocumentTraversal trav){
		NodeIterator it = trav.createNodeIterator(document.getDocumentElement(), NodeFilter.SHOW_ALL, null, true);
		ObradiDijeloveXml ob = new ObradiDijeloveXml();
		for (Node node = it.nextNode(); node != null; node = it.nextNode()) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				// dohvati i obradi elemente
				ob.obradiElemente(node, "<ELEMENT> ");
				// dohvati i ispiši atribute ako ih element ima 
				ob.obradiAtribute(node, "  <ATRIBUT> ");
				System.out.println();
			}
			// dohvati i obradi tekst elementa
			else if (node.getNodeType() == Node.TEXT_NODE) {
				ob.obradiText(node, "");
			}
		}
	}
}
