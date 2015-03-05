package api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Main {
	
	private String temp;
	private String humidity;
	private String temp_min;
	private String temp_max;
	
	public String getTemp() {
		return temp;
	}
	public String getHumidity() {
		return humidity;
	}
	public String getTemp_min() {
		return temp_min;
	}
	public String getTemp_max() {
		return temp_max;
	}

	
}
