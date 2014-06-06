package br.com.modelo;

public class Sitti extends Entidade{


	private String modelo;
	private String pn;
	

	
	public Sitti() {
		super();
		modelo = new String();
		pn = new String();
	}
	public Sitti(String nome, String tipo, String modelo, String pn) {
		super(nome,tipo);
		this.modelo = modelo;
		this.pn = pn;
	}



	public String getModelo() {
		return modelo;
	}



	public void setModelo(String modelo) {
		this.modelo = modelo;
	}



	public String getPn() {
		return pn;
	}



	public void setPn(String pn) {
		this.pn = pn;
	}
}
