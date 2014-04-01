package com.goatstone.multidraw;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.*;

import com.google.android.gcm.server.Message;

@SuppressWarnings("serial")
public class PingServlet extends HttpServlet {
    String str = "hello pingPost!!!";

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
	    throws IOException {

	Message message = new Message.Builder()
		.addData("data", "{\"backgroundProps\":{\"red\":100,\"green\":100,\"blue\":100}}")
		.build();

	Util.log("doGet : "+message.toString(), this.getClass().getName());
	MultiDraw.broadcastMessag(message);

	resp.setContentType("text/plain");
	resp.getWriter().println("Hello, ping  9 9 9     ");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
	    throws IOException {

	String data;
	BufferedReader reader = req.getReader();
	StringBuilder sb = new StringBuilder();
	String line = reader.readLine();

	while (line != null) {
	    sb.append(line + "\n");
	    line = reader.readLine();
	}

	reader.close();
	data = sb.toString();

	Util.log("doPost  = = = ", this.getClass().getName());

	Message message = new Message.Builder()
		.addData("data", data)
		.build();
	MultiDraw.broadcastMessag(message);		
	//HelloGCM.broadcastData(data);

	resp.setContentType("text/plain");
	resp.getWriter().println(data);

    }
}
