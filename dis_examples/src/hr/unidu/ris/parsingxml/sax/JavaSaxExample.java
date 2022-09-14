package hr.unidu.ris.parsingxml.sax;

	import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
	import javax.xml.parsers.SAXParserFactory;
	import java.io.IOException;

	public class JavaSaxExample {


	    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
	    	final String FILENAME = "data/kolegiji.xml";
	        SAXParserFactory factory = SAXParserFactory.newInstance();


	            // XXE attack, see https://rules.sonarsource.com/java/RSPEC-2755
	            SAXParser saxParser = factory.newSAXParser();

	            KolegijiHandlerSax handler = new KolegijiHandlerSax();

	            saxParser.parse(FILENAME, handler);

	    

	    }

	}
