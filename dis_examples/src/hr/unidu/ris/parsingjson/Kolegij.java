package hr.unidu.ris.parsingjson;

import java.util.List;

public class Kolegij {
	public String naziv;
	public List<String> nastavnici;
	public int ects;
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Naziv: ");
		sb.append(naziv);
		sb.append("\nNastavnici: ");
		if(nastavnici != null) {
			for(String n : nastavnici) {
				sb.append(n);
				sb.append(", ");
			}
			sb.append("\n");
		}
		sb.append("ECTS: ");
		sb.append(ects);
		sb.append("\n");
		return sb.toString();
	}
}
