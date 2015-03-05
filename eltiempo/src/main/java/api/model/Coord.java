package api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Coord {

	private String lon;
	private String lat;
	
	public String getLon() {
		return lon;
	}
	public String getLat() {
		return lat;
	}
	
}
