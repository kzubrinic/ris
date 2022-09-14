package hr.unidu.ris.parsingxml.dom;
/**
 * Primjer izmjene postojećeg XML dokumenta.
 * U popis nastavnika kolegija sa id-jem 222 dodaje se novi nastavnik tipa asistent.
 * Izmijenjeni XML se sprema u datoteku izmjena-kolegiji.xml
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class JavaDomIzmijeniXml {

	public static void main(String[] args) throws ParserConfigurationException, TransformerException, FileNotFoundException, SAXException, IOException, XPathExpressionException {
		JavaDomIzmijeniXml p = new JavaDomIzmijeniXml();
    	final String OLD_FILENAME = "data/kolegiji.xml";
    	final String NEW_FILENAME = "data/izmjena-kolegiji.xml";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
       
        Document doc = builder.parse(new FileInputStream(OLD_FILENAME));

        NodeList listaKolegija = doc.getElementsByTagName("kolegij");
        
        
        for (int i = 0; i < listaKolegija.getLength(); i++) {
            // dohvati kolegij
            Node lk = listaKolegija.item(i);
            if (lk.getNodeType() == Node.ELEMENT_NODE) {
            	System.out.println(lk.getNodeName());
                String id = lk.getAttributes().getNamedItem("id").getTextContent();
                // Mijenjamo kolegij "Matematička analiza" (atribut id=222)
                if ("222".equals(id.trim())) {
                    NodeList childNodes = lk.getChildNodes();

                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node item = childNodes.item(j);
                        if (item.getNodeType() == Node.ELEMENT_NODE) {
                            // Dodajemo novog nastavnika u listu nastavnika
                        	if ("nastavnici".equalsIgnoreCase(item.getNodeName())) {
                        		item.appendChild(p.stvoriElement(doc, "nastavnik", "Novi asistent", "tip", "asistent"));
                        	}

                        }
                    }
                }
            }
        }
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transf = transformerFactory.newTransformer();

        transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transf.setOutputProperty(OutputKeys.INDENT, "yes");
        transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        // UKlanja višestruke oznake za prijelaz u novi red
        doc = p.removeDoubleNewLines(doc);
        
        DOMSource source = new DOMSource(doc);

        File myFile = new File(NEW_FILENAME);

        StreamResult console = new StreamResult(System.out);
        StreamResult file = new StreamResult(myFile);

        transf.transform(source, console);
        transf.transform(source, file);
        System.out.println("Završio!");
    }

    private Node stvoriElement(Document doc, String name, String value, String attName, String attValue) {
        Element node = doc.createElement(name);
        node.setAttribute(attName, attValue);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
    
    // You can find empty text nodes using XPath, then remove them programmatically like so:
    // https://stackoverflow.com/a/979606
    private Document removeDoubleNewLines(Document doc) throws XPathExpressionException {
    	XPathFactory xpathFactory = XPathFactory.newInstance();
    	// XPath to find empty text nodes.
    	XPathExpression xpathExp = xpathFactory.newXPath().compile("//text()[normalize-space(.) = '']");  
    	NodeList emptyTextNodes = (NodeList)xpathExp.evaluate(doc, XPathConstants.NODESET);
    	// Remove each empty text node from document.
    	for (int i = 0; i < emptyTextNodes.getLength(); i++) {
    		Node emptyTextNode = emptyTextNodes.item(i);
    		emptyTextNode.getParentNode().removeChild(emptyTextNode);
    	}
    	return doc;
    }
}
