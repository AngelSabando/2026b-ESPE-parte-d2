package es.upm.grise.file;

import java.util.ArrayList;

import es.upm.grise.file.exceptions.InvalidContentException;
import es.upm.grise.file.exceptions.WrongEncodingException;
import es.upm.grise.file.exceptions.WrongFileTypeException;

public class File {

    private ArrayList<Character> content;
    private FileType type;
    private Location location;

    /*
     * Method to test
     */
    public File(FileType type, Location location) {
    	
    	content = new ArrayList<Character>();
    	this.type = type;
    	this.location = location;
    	
    }

    /*
     * Method to test
     */
    public void addProperty(char[] content) throws InvalidContentException, WrongFileTypeException {
    	
    	if(content == null) {
    		
    		throw new InvalidContentException();
    		
    	}

    	if(type == FileType.IMAGE) {

    		throw new WrongFileTypeException();
    		
    	}
    	
    	int numEquals = 0;
    	for(char c : content) {
    		
    		if(c == '=') {
    			
    			numEquals++;
    			
    		}
    				
    	}
    	
    	if (content.length == 0 || numEquals != 1 || content[0] == '=' || content[content.length - 1] == '=') {

    	    throw new InvalidContentException();
    	    
    	}
		
    	for(char c : content) {

        	this.content.add(c);
    				
    	}

    }
    
    /*
     * Method to test
     */
    public void addImageBytes(char[] content) throws InvalidContentException, WrongFileTypeException, WrongEncodingException {
    	
    	if(content == null) {

    		throw new InvalidContentException();
    		
    	}

    	if(type == FileType.PROPERTY) {

    		throw new WrongFileTypeException();
    		
    	}
    	
    	for(char c : content) {
    		
    		if(c > 255) {

    			throw new WrongEncodingException();
    		}
    	    		
    	}
    	
    	for(char c : content) {

        	this.content.add(c);
    				
    	}

    }			
    
    /*
     * Method to test
     */
    public void removeContent(int numberChars) {
    	
    	if(numberChars >= content.size()) {
    		
    		content.clear();
    		
    	} else {
    		
        	int position = content.size()-numberChars;
        	
        	content.subList(position, content.size()).clear();	
    		
    	}

    }
    
    /*
     * getters
     */
    
    public ArrayList<Character> getContent() {
        return content;
    }

    public FileType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }
    
}
