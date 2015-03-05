package api.model;

public class TiempoModel {

	private String coordenadas;
	private Double temperatura;
	private String descripcion;
	private String nombre;
	private Double humedad;
	
	public String getCoordenadas() {
		return coordenadas;
	}
	public void setCoordenadas(Coord coordenadas) {
		this.coordenadas = coordenadas.getLon()+";"+coordenadas.getLat();
	}
	public double getTemperatura() {
		return temperatura;
	}
	public void setTemperatura(Main temperatura) {
		this.temperatura = Double.parseDouble(temperatura.getTemp());
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(Weather descripcion) {
		this.descripcion = descripcion.getDescription();
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getHumedad() {
		return humedad;
	}
	public void setHumedad(Main humedad) {
		this.humedad = Double.parseDouble(humedad.getHumidity());
	}
	
	
}
