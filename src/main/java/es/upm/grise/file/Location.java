package es.upm.grise.file;

import java.nio.file.Path;

public class Location {
	
	Path location;
	
	public Location(Path location) {
		
		this.location = location;
		
	}

	public Path getLocation() {
		
		return location;
		
	}

}
