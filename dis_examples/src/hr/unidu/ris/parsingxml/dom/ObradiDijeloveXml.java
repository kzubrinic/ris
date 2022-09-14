package hr.unidu.ris.parsingxml.dom;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ObradiDijeloveXml {
	public void obradiElemente(Node node, String prefix) {
		System.out.print(prefix + node.getNodeName());
	}
	
	public void obradiAtribute(Node node, String prefix) {
		NamedNodeMap atr = node.getAttributes();
		for(int i = 0;i < atr.getLength();i++) {
			Node n = atr.item(i);
			if(n.getNodeType() == Node.ATTRIBUTE_NODE)
				System.out.print(prefix + "(" + n.getNodeName() + " = " + n.getTextContent()+")");
		}
	}
	
	public void obradiText(Node node, String prefix) {
		String text = node.getTextContent().trim();
		if (!text.isBlank()) {
			System.out.println(prefix + text);
		}
	}

}
