package hr.unidu.ris.parsingxml.dom;
/**
 * Primjer stvaranja novog XML dokumenta jednostavne strukture:
 * <?xml version="1.0" encoding="UTF-8"?>
 *	<kolegiji>
 *   <kolegij id="id_kolegija">
 *       <naziv>naziv_kolegija</naziv>
 *		<nastavnici>
 *			<nastavnik tip="tip_nastavnika">ime_nastavnika</nastavnik>
 *		</nastavnici>	
*       <ects>broj_ects</ects>
 *   </kolegij>
 */
	import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
	import javax.xml.parsers.DocumentBuilderFactory;
	import javax.xml.parsers.ParserConfigurationException;
	import javax.xml.transform.OutputKeys;
	import javax.xml.transform.Transformer;
	import javax.xml.transform.TransformerException;
	import javax.xml.transform.TransformerFactory;
	import javax.xml.transform.dom.DOMSource;
	import javax.xml.transform.stream.StreamResult;
	import org.w3c.dom.Document;
	import org.w3c.dom.Element;
	import org.w3c.dom.Node;

import hr.unidu.ris.parsingxml.util.Kolegij;
import hr.unidu.ris.parsingxml.util.Nastavnik;

	public class JavaDomStvoriXml {

	    public static void main(String[] args) throws ParserConfigurationException, TransformerException {
	    	JavaDomStvoriXml p = new JavaDomStvoriXml();
	    	final String FILENAME = "data/stvoreni-kolegiji.xml";
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.newDocument();

	        Element root = doc.createElementNS("ris.unidu.hr", "kolegiji");
	        doc.appendChild(root);
	        for(Kolegij k: p.pripremiPodatkeZaPunjenje()) {
	        	root.appendChild(p.stvoriKolegij(doc, k));
	        }
	        

	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transf = transformerFactory.newTransformer();

	        transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        transf.setOutputProperty(OutputKeys.INDENT, "yes");
	        transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

	        DOMSource source = new DOMSource(doc);

	        File myFile = new File(FILENAME);

	        StreamResult console = new StreamResult(System.out);
	        StreamResult file = new StreamResult(myFile);

	        transf.transform(source, console);
	        transf.transform(source, file);
	        System.out.println("Završio!");
	    }

	    private Node stvoriKolegij(Document doc, Kolegij ko) {
	        Element k = doc.createElement("kolegij");

	        k.setAttribute("id", Integer.toString(ko.getId()));
	        k.appendChild(stvoriElement(doc, "naziv", ko.getNaziv()));
	        k.appendChild(stvoriElement(doc, "ects", Integer.toString(ko.getEcts())));
	        Element nastEl = doc.createElement("nastavnici");
	        k.appendChild(nastEl);
	        for(Nastavnik n : ko.getNastavnici()) {
	        	nastEl.appendChild(stvoriElement(doc, "nastavnik", n.getIme(), "tip", n.getTip()));
	        }
	        
	        return k;
	    }

	    private Node stvoriElement(Document doc, String name, String value) {
	        Element node = doc.createElement(name);
	        node.appendChild(doc.createTextNode(value));
	        return node;
	    }
	    private Node stvoriElement(Document doc, String name, String value, String attName, String attValue) {
	        Element node = doc.createElement(name);
	        node.setAttribute(attName, attValue);
	        node.appendChild(doc.createTextNode(value));
	        return node;
	    }
	    
	    private List<Kolegij> pripremiPodatkeZaPunjenje() {
	    	List<Kolegij> lk = new ArrayList<>();
	    	Kolegij k1 = new Kolegij(111, "Uvod u programiranje", 7);
	    	List<Nastavnik> n1 = new ArrayList<>();
	    	n1.add(new Nastavnik("Mario Miličević", "nositelj"));
	    	n1.add(new Nastavnik("Tomo Sjekavica", "asistent"));
	    	k1.setNastavnici(n1);
	    	lk.add(k1);
	    	Kolegij k2 = new Kolegij(222, "Matematička analiza", 7);
	    	List<Nastavnik> n2 = new ArrayList<>();
	    	n2.add(new Nastavnik("Ivica Martinjak", "nositelj"));
	    	k2.setNastavnici(n2);
	    	lk.add(k2);
	    	Kolegij k3 = new Kolegij(333, "Arhitektura računala", 6);
	    	List<Nastavnik> n3 = new ArrayList<>();
	    	n3.add(new Nastavnik("Vladimir Lipovac", "nositelj"));
	    	n3.add(new Nastavnik("Ante Mihaljević", "asistent"));
	    	k3.setNastavnici(n3);
	    	lk.add(k3);
	    	Kolegij k4 = new Kolegij(444, "Tehničko pisanje", 5);
	    	List<Nastavnik> n4 = new ArrayList<>();
	    	n4.add(new Nastavnik("Krunoslav Žubrinić", "nositelj"));
	    	n4.add(new Nastavnik("Ines Obradović", "asistent"));
	    	k4.setNastavnici(n4);
	    	lk.add(k4);
	    	return lk;
	    }
	}
