package com.goatstone.multidraw;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet {

	private static final String PARAMETER_REG_ID = "regId";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {

		String deviceId = req.getParameter(PARAMETER_REG_ID);
		// if (isEmptyOrNull(value)) {
		// value = defaultValue;
		// }
		MultiDraw.register(deviceId);
	}

}
