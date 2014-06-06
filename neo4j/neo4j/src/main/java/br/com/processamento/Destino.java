package br.com.processamento;

public class Destino{

	private String vertical;
	private String bloco;
	private String par; 
	
	public Destino(	String vertical,String bloco,String par) {
		this.vertical = vertical;
		this.bloco = bloco;
		this.par =  par;
	}
	@Override
	public String toString() {	
		return "vertical " + vertical + "bloco " + bloco + "par " + par;
	}
	
	public Destino() {
		this.vertical = new String();
		this.bloco =new String();
		this.par =  new String();
	}

	public String getVertical() {
		return vertical;
	}

	public void setVertical(String vertical) {
		this.vertical = vertical;
	}

	public String getBloco() {
		return bloco;
	}

	public void setBloco(String bloco) {
		this.bloco = bloco;
	}

	public String getPar() {
		return par;
	}

	public void setPar(String par) {
		this.par = par;
	}
}
