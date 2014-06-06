package br.com.processamento;

public class Ligacao {
	
	private String par;
	private String identificacao;
	private Destino destino;
	
	public Ligacao() {
		this.par= new String();
		this.identificacao = new String(); 
		this.destino= new Destino();		
	}
	
	@Override
	public String toString() {	
		return "par " + par + " identificaçao " + identificacao +  " destino " + destino;
	}
	public Ligacao(String par, String identificacao, Destino d){
		this.par= par;
		this.identificacao = identificacao;
		this.destino=  destino;	
	}
	public String getPar() {
		return par;
	}
	public void setPar(String par) {
		this.par = par;
	}
	public String getIdentificacao() {
		return identificacao;
	}
	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}
	public Destino getDestino() {
		return destino;
	}
	public void setDestino(Destino destino) {
		this.destino = destino;
	}
}
