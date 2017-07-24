package web.controller;

import org.springframework.web.bind.annotation.RestController;

import web.configuration.HelloConfiguration;
import web.model.Hello;
import web.service.HelloService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class HelloController {

    @Autowired
    HelloService service;
    
    @Autowired
    HelloConfiguration hc;
    
    final Logger LOG = Logger.getLogger(HelloController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Hello from Spring Boot Rest!";
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseEntity<Hello> hello() {
        
        return this.helloName(hc.getDefaultName());
    }
    
    @RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
    public ResponseEntity<Hello> helloName(@PathVariable("name") String name) {
        
        LOG.debug("hello: " + name);
        return new ResponseEntity<Hello>(service.get(name), HttpStatus.OK);
    }

}