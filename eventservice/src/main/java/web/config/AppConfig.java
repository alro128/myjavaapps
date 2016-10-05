package web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


/**
 * 
 *    Set Spring mvc application and set package to scan controllers and resources
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "web")
public class AppConfig {

}