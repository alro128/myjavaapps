package app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.DirectoryWalker;
import org.apache.log4j.Logger;


public class MyDirectoryWalker extends DirectoryWalker {
	
	final static Logger log = Logger.getLogger(MyDirectoryWalker.class);
	
	private String tipo = ".mp3";
	public MyDirectoryWalker() {
		super();
	}

	public List<File> getMyFiles(File dir) throws IOException {
		List<File> results = new ArrayList<File>();
		walk(dir, results);
		return results;
	}

	protected void handleFile(File file, int depth, Collection results) throws IOException {
		if (file.getCanonicalPath().endsWith(tipo)) {
			if (depth > 2) {
				log.debug("depth is " + depth + ", so " + file.getCanonicalPath() + " will be skipped");
			} else {
				log.debug("depth is " + depth + ", so " + file.getCanonicalPath() + " will be added");
				results.add(file);
			}
		} else {
			log.debug("file doesn't end in "+tipo+", so " + file.getCanonicalPath() + " will be skipped");
		}
	}

	protected boolean handleDirectory(File directory, int depth, Collection results) {
		return true;

	}
}
