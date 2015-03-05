package api;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;
import api.model.TiempoModel;
import api.model.OpenWeatherMapModel;

@Controller
@EnableAutoConfiguration
public class TiempoController {

	@RequestMapping("/")
	@ResponseBody
	String home() {
		
		String url= "http://api.openweathermap.org/data/2.5/weather?q=Madrid,es&units=metric&lang=es";
		String res = "";
        
		RestTemplate restTemplate = new RestTemplate();
		OpenWeatherMapModel owm = restTemplate.getForObject(url, OpenWeatherMapModel.class);
        
		TiempoModel tiempo = new TiempoModel();
		tiempo.setCoordenadas(owm.getCoord());
		tiempo.setTemperatura(owm.getMain());
		tiempo.setDescripcion(owm.getWeather().get(0));
		tiempo.setHumedad(owm.getMain());
		tiempo.setNombre(owm.getName());
		
        Gson gson = new Gson();
        res+=gson.toJson(tiempo);
        
		return res;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(TiempoController.class, args);
	}
}
