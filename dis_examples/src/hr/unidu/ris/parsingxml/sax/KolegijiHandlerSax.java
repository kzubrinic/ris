package hr.unidu.ris.parsingxml.sax;

/**
 * Handler za obradu sadržaja XML dokumenta kolegiji.xml
 * Opisuje strukturu (elemente i atribute)
 */

	import org.xml.sax.Attributes;
	import org.xml.sax.helpers.DefaultHandler;

	public class KolegijiHandlerSax extends DefaultHandler {

	  private StringBuilder currentValue = new StringBuilder();

	  @Override
	  public void startElement(String uri, String localName, String qName, Attributes attributes) {
	      // reset the tag value
	      currentValue.setLength(0);

	      if (qName.equalsIgnoreCase("kolegij")) {
	          // dohvat atributa po nazivu
	          String id = attributes.getValue("id");
	          System.out.printf("id kolegija : %s%n", id);
	      }

	      if (qName.equalsIgnoreCase("nastavnik")) {
	    	// dohvat atributa po indeksu; 0 = prvi atribut
	          String tip = attributes.getValue(0);
	          System.out.printf("Tip nastavnika: %s%n", tip);
	      }
	  }

	  @Override
	  public void endElement(String uri, String localName, String qName) {
	      if (qName.equalsIgnoreCase("naziv")) {
	          System.out.printf("Naziv kolegija : %s%n", currentValue.toString());
	      }
	      if (qName.equalsIgnoreCase("nastavnik")) {
	    	  System.out.printf("Ime nastavnika: %s%n", currentValue.toString());
	      }
	      if (qName.equalsIgnoreCase("ects")) {
	          System.out.printf("ECTS: %s%n", currentValue.toString());
	      }
	  }

	  // http://www.saxproject.org/apidoc/org/xml/sax/ContentHandler.html#characters%28char%5B%5D,%20int,%20int%29
	  // SAX parsers may return all contiguous character data in a single chunk,
	  // or they may split it into several chunks
	  @Override
	  public void characters(char ch[], int start, int length) {

	      // The characters() method can be called multiple times for a single text node.
	      // Some values may missing if assign to a new string

	      // avoid doing this
	      // value = new String(ch, start, length);

	      // better append it, works for single or multiple calls
	      currentValue.append(ch, start, length);

	  }

	}
