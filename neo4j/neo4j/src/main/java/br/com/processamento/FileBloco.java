package br.com.processamento;

import java.util.ArrayList;
import java.util.List;
/*
 * Representação de um arquivo texto
 */


public class FileBloco {
	
	private String dg;
	private String modelo;
	private String numero;
	private String interligacoExterna;
	private String circuito;
	private String vertical;
	private List<Ligacao> ligacoes;
	
	public FileBloco() {
		 this.dg = new String();
		 this.modelo = new String();
		 this.numero = new String();
		 this.interligacoExterna = new String();
		 this.ligacoes = new ArrayList<Ligacao>();
		 this.circuito = new String();
	}
	
	@Override
	public String toString() {
		
	String bloco=  "dg " + dg + "\n"  + "modelo  " + modelo + "\n"  +"id " + numero +"\n"  + "interligacoExterna " +"\n"  + interligacoExterna  +"\n"  +				
				"circuito" +"\n"  + circuito +"\n"  + "vertical " +"\n"  + vertical; 
	String lig = new String() ; 
	
	for(int i=0;i<ligacoes.size();i++){
		lig += "\n" +ligacoes.get(i);
	}
		  
		  return bloco + "\n" + lig;
	}
	
	public String getDg() {
		return dg;
	}
	public void setDg(String dg) {
		this.dg = dg;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String id) {
		this.numero = id;
	}
	public String getInterligacoExterna() {
		return interligacoExterna;
	}
	public void setInterligacoExterna(String interligacoExterna) {
		this.interligacoExterna = interligacoExterna;
	}


	public List<Ligacao> getLigacoes() {
		return ligacoes;
	}


	public void setLigacoes(List<Ligacao> ligacoes) {
		this.ligacoes = ligacoes;
	}


	public String getCircuito() {
		return circuito;
	}


	public void setCircuito(String circuito) {
		this.circuito = circuito;
	}


	public String getVertical() {
		return vertical;
	}


	public void setVertical(String vertical) {
		this.vertical = vertical;
	}
	
}
