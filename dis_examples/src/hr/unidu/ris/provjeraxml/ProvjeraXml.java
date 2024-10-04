package hr.unidu.ris.provjeraxml;
import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ProvjeraXml {

    public static void main(String[] args) {
        String xmlFile = "data/studenti.xml";
        String xmlFile2 = "data/studenti.xml";
        String xsdFile = "data/studenti.xsd";

        if (validateXMLSchema(xsdFile, xmlFile)) {
            System.out.println("XML dokument je valjan prema XSD shemi.");
        } else {
            System.out.println("XML dokument nije valjan prema XSD shemi.");
        }
        if (validateDTD(xmlFile2)) {
            System.out.println("XML dokument je valjan prema DTD shemi.");
        } else {
            System.out.println("XML dokument nije valjan prema DTD shemi.");
        }
    }

    public static boolean validateXMLSchema(String xsdPath, String xmlPath) {
    	System.out.println("-------------------- XSD validacija --------------------------------");
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
            return true;
        } catch (SAXException | IOException e) {
            System.out.println("Greška prilikom validacije: " + e.getMessage());
            return false;
        }
    }
    public static boolean validateDTD(String xmlPath) {
    	System.out.println("-------------------- DTD validacija --------------------------------");
    	try {
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        factory.setValidating(true); // Omogućuje validaciju pomoću DTD-a
	        factory.setNamespaceAware(false); // Za DTD validaciju postavi na false
	        factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", 
	                             "http://www.w3.org/TR/REC-xml");
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {
                    System.out.println("Warning: " + exception.getMessage());
                }
                @Override
                public void error(SAXParseException exception) throws SAXException {
                    System.out.println("Error: " + exception.getMessage());
                    throw exception; 
                }
                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    System.out.println("Fatal Error: " + exception.getMessage());
                    throw exception; 
                }
            });
	        builder.parse(new File(xmlPath));
	        return true;
    } catch (SAXException e) {
        System.out.println("XML dokument nije valjan: " + e.getMessage());
        return false;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
    }
 }

