package hr.unidu.ris.parsingxml.util;

public class Nastavnik {
	private String ime, tip;
	public Nastavnik() {}
	public Nastavnik(String ime, String tip) {
		this.ime = ime;
		this.tip = tip;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}
	
}
