package web.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import web.dao.EventDAO;
import web.model.Event;

@RestController
public class EventController {

    final static Logger LOG = Logger.getLogger(EventController.class);
    
	@Autowired
	private EventDAO eventDAO;

	
	@GetMapping("/events")
	public List<Event> getEvents() {
		return eventDAO.list();
	}

	@GetMapping("/events/{id}")
	public ResponseEntity getEvent(@PathVariable("id") String id) {

		Event event = eventDAO.get(id);
		if (event == null) {
			return new ResponseEntity("No Event found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity(event, HttpStatus.OK);
	}

	@PostMapping(value = "/events")
	public ResponseEntity createEvent(@RequestBody Event event) {

		eventDAO.create(event);

		return new ResponseEntity(event, HttpStatus.OK);
	}

//	@DeleteMapping("/events/{id}")
//	public ResponseEntity deleteEvent(@PathVariable Long id) {
//
//		if (null == eventDAO.delete(id)) {
//			return new ResponseEntity("No Event found for ID " + id, HttpStatus.NOT_FOUND);
//		}
//
//		return new ResponseEntity(id, HttpStatus.OK);
//
//	}

//	@PutMapping("/events/{id}")
//	public ResponseEntity updateEvent(@PathVariable Long id, @RequestBody Event event) {
//
//		event = eventDAO.update(id, event);
//
//		if (null == event) {
//			return new ResponseEntity("No Event found for ID " + id, HttpStatus.NOT_FOUND);
//		}
//
//		return new ResponseEntity(event, HttpStatus.OK);
//	}
	
}
