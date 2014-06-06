package br.com.backup;

import br.com.modelo.Bloco;
import br.com.modelo.DG;
import br.com.modelo.Par;
import br.com.modelo.Vertical;

public class Ponto {
	private DG dg; 
	private Vertical v;  
	private Bloco b; 
	private Par p; 
	
	public Ponto() {
		 dg = new DG();
		 v = new Vertical();
		 b = new Bloco();
		 p = new Par();
	}

	public DG getDg() {
		return dg;
	}

	public void setDg(DG dg) {
		this.dg = dg;
	}

	public Vertical getV() {
		return v;
	}

	public void setV(Vertical v) {
		this.v = v;
	}

	public Bloco getB() {
		return b;
	}

	public void setB(Bloco b) {
		this.b = b;
	}

	public Par getP() {
		return p;
	}

	public void setP(Par p) {
		this.p = p;
	}
}
