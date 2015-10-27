package me.alro.controller;

import me.alro.model.ModuloModel;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value="/modulo")
@Controller
public class ModuloController implements ApplicationContextAware {

	final static Logger log = Logger.getLogger(ModuloController.class);
	
	private ApplicationContext applicationContext;

	@RequestMapping(method=RequestMethod.GET)
	public String index(@RequestParam(value = "name", required = false, defaultValue = "home") String name, Model model) {
		log.info("Welcome Page");
		model.addAttribute("name", name);
		return "modulo/index";
	}
	
	@RequestMapping(value="/{company}/{campaign}", method=RequestMethod.GET)
	public String getCampaign(@PathVariable String company, @PathVariable String campaign, Model model) {
		log.info("Campaign Page");
		model.addAttribute("company", company);
		model.addAttribute("campaign", campaign);
		return "modulo/"+company+"_"+campaign;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String index(@ModelAttribute("moduloForm")ModuloModel modulo, Model model) {
		log.info("..form processing");
		log.info("Empresa: "+ modulo.getCompany() + ", Campana: " + modulo.getCampaign() + ", email:" + modulo.getEmail());
		model.addAttribute("name", modulo.getEmail());
		return "modulo/index";
	}
	

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}

}