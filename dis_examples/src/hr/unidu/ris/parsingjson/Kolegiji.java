package hr.unidu.ris.parsingjson;

import java.util.List;

public class Kolegiji {
	public List<Kolegij> kolegiji;
	
	public String toString() {
		if (kolegiji == null) return "";
		StringBuilder sb = new StringBuilder();
		for(Kolegij k : kolegiji) {
			sb.append("Naziv: ");
			sb.append(k.naziv);
			sb.append("\nNastavnici: ");
			if(k.nastavnici != null) {
				for(String n : k.nastavnici) {
					sb.append(n);
					sb.append(", ");
				}
				sb.append("\n");
			}
			sb.append("ECTS: ");
			sb.append(k.ects);
			sb.append("\n\n");
		}
		return sb.toString();
	}
}
