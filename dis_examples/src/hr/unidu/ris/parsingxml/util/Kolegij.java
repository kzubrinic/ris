package hr.unidu.ris.parsingxml.util;

import java.util.List;

public class Kolegij {
	private int id;
	private String naziv;
	private int ects;
	private List<Nastavnik> nastavnici;
	public Kolegij() {}
	public Kolegij(int id, String naziv, int ects) {
		this.id = id;
		this.naziv = naziv;
		this.ects = ects;
	}
	public Kolegij(int id, String naziv, int ects, List<Nastavnik> nastavnici) {
		this.id = id;
		this.naziv = naziv;
		this.ects = ects;
		this.nastavnici = nastavnici;
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public int getEcts() {
		return ects;
	}
	public void setEcts(int ects) {
		this.ects = ects;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Nastavnik> getNastavnici() {
		return nastavnici;
	}
	public void setNastavnici(List<Nastavnik> nastavnici) {
		this.nastavnici = nastavnici;
	}
}
