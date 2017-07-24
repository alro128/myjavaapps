package web.service;

import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import web.configuration.HelloConfiguration;
import web.model.Hello;

@Service
public class HelloService {

    final Logger LOG = Logger.getLogger(HelloService.class);
    
    @Autowired
    HelloConfiguration hc;
            
    public Hello get(String name) {
        
        LOG.debug(hc.toString());
        
        Hello h = new Hello(new Random().nextInt(hc.getRandomLimit()), name);
        
        LOG.info(h.toString());
        return h;
    }

}
