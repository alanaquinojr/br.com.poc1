package br.com.modelo;

public class Entidade {
	private String nome;
	private String tipo;
	
	public Entidade() {
		nome = new String();
		tipo = new String(); 
	}
	
	public Entidade(String nome, String tipo){
		this.nome = nome;
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
