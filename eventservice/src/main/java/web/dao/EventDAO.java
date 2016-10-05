package web.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import web.model.Event;

@Component
public class EventDAO {

    final static Logger LOG = Logger.getLogger(EventDAO.class);
	
    //Test database
    private static List<Event> events;
    {
	events = new ArrayList<Event>();
	events.add(new Event("1", "web", "20161005T141250", "test", "action", "OK", 1000));
	events.add(new Event("2", "frontlight", "20161005T141252", "light", "on", "OK", 200));
	events.add(new Event("3", "frontdoor", "20161005T141300", "door", "open", "OK", 150));
    }

    public List<Event> list() {
	return events;
    }

    public Event get(String id) {

	for (Event e : events) {
	    if (e.getId().equals(id)) {
		return e;
	    }
	}
	return null;
    }

    public Event create(Event event) {
	event.setId(""+System.currentTimeMillis());
	events.add(event);
	
	LOG.info(event.toString());
	
	return event;
    }

    // public Long delete(Long id) {
    //
    // for (Event c : events) {
    // if (c.getId().equals(id)) {
    // events.remove(c);
    // return id;
    // }
    // }
    //
    // return null;
    // }

    // public Event update(Long id, Event event) {
    //
    // for (Event c : events) {
    // if (c.getId().equals(id)) {
    // event.setId(c.getId());
    // events.remove(c);
    // events.add(event);
    // return event;
    // }
    // }
    //
    // return null;
    // }

}
