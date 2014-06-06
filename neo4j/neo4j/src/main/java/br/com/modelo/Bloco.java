package br.com.modelo;

public class Bloco extends Entidade{


	private String modelo;
	
	
	public Bloco(){
		super();
		modelo = new String();
	}

	public Bloco(String nome, String tipo, String modelo){
		super(nome,tipo);
		this.modelo=modelo;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	
}
