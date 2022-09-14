package hr.unidu.ris.parsingxml.dom;
/**
 * Rekurzivna "šetnja" kroz XML stablo pomoću DOM parsera i klase TreeWalker
 * Prilagođeno s https://zetcode.com/java/dom/
 */
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;
import org.xml.sax.SAXException;

public class JavaDomWalk2 {
	private ObradiDijeloveXml ob;
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		JavaDomWalk2 p = new JavaDomWalk2();
		final String FILENAME = "data/kolegiji.xml";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder loader = factory.newDocumentBuilder();
		Document document = loader.parse(FILENAME);
		DocumentTraversal trav = (DocumentTraversal) document;
		p.ob = new ObradiDijeloveXml();
		TreeWalker w = trav.createTreeWalker(document.getDocumentElement(), NodeFilter.SHOW_ALL, null, true);
		String indent = "";
		// ispis svega (elemenata, atributa i teksta
		p.rekIspis(document, trav, w, indent);
		
	}

	private void rekIspis(Document document, DocumentTraversal trav, TreeWalker w, String indent){
		Node node = w.getCurrentNode();
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			// dohvati i obradi elemente
			ob.obradiElemente(node, indent);
			// dohvati i ispiši atribute ako ih element ima 
			ob.obradiAtribute(node, "  <ATRIBUT> ");
			System.out.println();
        }
        if (node.getNodeType() == Node.TEXT_NODE) {
        	ob.obradiText(node, indent);
        }
        for (Node n = w.firstChild(); n != null;
            n = w.nextSibling()) {
        	rekIspis(document, trav, w, indent + "  ");
        }
        w.setCurrentNode(node);
	}		
}
