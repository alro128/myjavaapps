package web.model;

import java.util.ArrayList;

public class Event {

    private String id = "";
    private String source;
    private String date;
    private String name;
    private String action;
    private String responseCode;
    private int elapsed;
    private ArrayList<String> tags = new ArrayList<String>(0);
    
    public Event() {
	super();
    }
    
    public Event(String id, String source, String date, String name, String action, String responseCode, int elapsed) {
	super();
	this.id = id;
	this.source = source;
	this.date = date;
	this.name = name;
	this.action = action;
	this.responseCode = responseCode;
	this.elapsed = elapsed;
    }
    
    public Event(String source, String date, String name, String action, String responseCode, int elapsed) {
	super();
	this.source = source;
	this.date = date;
	this.name = name;
	this.action = action;
	this.responseCode = responseCode;
	this.elapsed = elapsed;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public int getElapsed() {
        return elapsed;
    }
    public void setElapsed(int elapsed) {
        this.elapsed = elapsed;
    }
    public String getResponseCode() {
        return responseCode;
    }
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public ArrayList<String> getTags() {
        return tags;
    }
    
    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void addTag(String tag) {
//	if (null==this.tags) {
//	    this.tags = new ArrayList<String>(0);
//	}
        this.tags.add(tag);
    }

    public String toString() {	
	return ""+this.id+","+this.source+","+this.date+","+this.name+","+this.action+","+this.responseCode+","+this.elapsed+","+this.getTags().toString();
    }
}
