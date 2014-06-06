package br.com.modelo;

public class UnidadeMilitar extends Entidade {
	

	private String latitude;
	private String longitude;
	
	public UnidadeMilitar() {
		super();
		latitude = new String();
		longitude= new String();
	}
	public UnidadeMilitar(String nome, String tipo, String latitude, String longitude) {
		super(nome,tipo);
		this.latitude = latitude;
		this.longitude=  longitude;
	}

	
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	

}
