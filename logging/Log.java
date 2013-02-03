package it.unibz.testhunter.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
	static private FileHandler _logAllFile;
	static private SimpleFormatter _formatterTxt;
	static private Logger _logger;
	
	static public void setup() throws SecurityException, IOException {
		
		_logAllFile = new FileHandler("testhunter.log");
		
		_formatterTxt = new SimpleFormatter();
		_logAllFile.setFormatter(_formatterTxt);
		
		_logger = Logger.getLogger("");
		_logger.setLevel(Level.ALL);
		_logger.addHandler(_logAllFile);
	}
}
