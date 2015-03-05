package api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherMapModel {

	private Coord coord;
	private Sys sys;
	private List<Weather> weather;
	private Main main;
	private String name;
	
	public Coord getCoord() {
		return coord;
	}
	public Sys getSys() {
		return sys;
	}
	public List<Weather> getWeather() {
		return weather;
	}
	public Main getMain() {
		return main;
	}
	public String getName() {
		return name;
	}
	
	
}
