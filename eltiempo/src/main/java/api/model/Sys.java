package api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sys {

	private String country;
	private String sunrise;
	private String sunset;
	
	public String getCountry() {
		return country;
	}
	public String getSunrise() {
		return sunrise;
	}
	public String getSunset() {
		return sunset;
	}
	
	
}
