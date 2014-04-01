package com.goatstone.multidraw;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Context initializer that loads the API key from the App Engine datastore.
 */

public class ContextListener implements ServletContextListener {

	private final Logger log = Logger.getLogger(getClass().getName());

	public void contextInitialized(ServletContextEvent event) {
		log.info("init");
		// event.getServletContext().setAttribute(ATTRIBUTE_ACCESS_KEY,
		// accessKey);
		new MultiDraw();
	}

	public void contextDestroyed(ServletContextEvent event) {
	}

}
