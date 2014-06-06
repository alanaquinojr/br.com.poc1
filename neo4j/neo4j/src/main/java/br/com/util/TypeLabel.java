package br.com.util;

import org.neo4j.graphdb.Label;

/*
 * Permite inserir um label em um no, que pode ser DG, Bloco, Par, etc 
 */

public class TypeLabel implements Label {

	private String label;
	
	
	public TypeLabel(){
		label = new String();
	}
	public TypeLabel(String label){
		this.label = label;
		name();
	}
	
	@Override
	public String name() {
		return label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
