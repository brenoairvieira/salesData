package com.ilegra.salesData.fileWatcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.apache.log4j.Logger;

public class FileWatcher {

	final static Logger logger = Logger.getLogger(FileWatcher.class);
	
	public static Path watch(String path, Class<?> caller)  throws IOException {
		
		WatchService wacthService  = FileSystems.getDefault().newWatchService();
		
		Path directory = Paths.get(path);
		
		WatchKey watchKey = directory.register(wacthService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
		
		while (true) {
			for (WatchEvent<?> event : watchKey.pollEvents()) {
				
				Path filePath = directory.resolve((Path) event.context());
				
				logger.info("New event: " + event.kind() + " - File: " + filePath + " - from: " + caller.getCanonicalName());
				
				return filePath;

			}
		}
	}
	
		
}
