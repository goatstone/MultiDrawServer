package com.goatstone.multidraw;

import java.util.Date;
import java.util.logging.Logger;

public class Util {
	
	private static final Logger logger = Logger.getLogger(MultiDraw.class
			.getName());
	
	public static void log( String message, String className){
		String logMsg =  className + " : \n "+ message +"\n:"+ new Date();
		logger.info(" - - - - -  i log : " +logMsg);
		logger.warning(" - - -  - - w log : "+ logMsg);
	}

}
