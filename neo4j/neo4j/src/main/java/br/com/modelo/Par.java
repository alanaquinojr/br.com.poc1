package br.com.modelo;

public class Par extends Entidade {

	private String identificao;
	public Par() {
		super();
	}

	public Par(String nome, String tipo) {
		super(nome,tipo);
	}

	public String getIdentificao() {
		return identificao;
	}

	public void setIdentificao(String identificao) {
		this.identificao = identificao;
	}
	
}
