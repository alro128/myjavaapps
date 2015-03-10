package app;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class App {
	
	final static Logger log = Logger.getLogger(App.class);
	
	public static void main(String[] args) throws IOException {
		

		
		File dir = new File(args[0]);

		MyDirectoryWalker walker = new MyDirectoryWalker();
		List<File> files = walker.getMyFiles(dir);

		log.debug("\nFiles ending with .txt with a depth of 2 or less");
		for (File file : files) {
			log.info("file:" + file.getCanonicalPath());
			String playlist = file.getParentFile().getName();
			try {
				Mp3File mpt = new Mp3File(file.getCanonicalPath());
				log.info("[" + playlist + "]" + mpt.getId3v2Tag().getArtist() + " - "+ mpt.getId3v2Tag().getTitle());
			} catch (UnsupportedTagException e) {
				log.error(e.getMessage());
			} catch (InvalidDataException e) {
				log.error(e.getMessage());
			}
		}

	}
}
