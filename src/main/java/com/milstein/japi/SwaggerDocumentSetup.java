package com.milstein.japi;

import io.swagger.jaxrs.config.BeanConfig;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class SwaggerDocumentSetup extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.0.0");
		beanConfig.setTitle("Milstein, General API's");
		beanConfig
				.setDescription("Our core API's that can be used for all sorts of stuff. Please Enjoy.");

		beanConfig.setSchemes(new String[] { "http" });
		beanConfig.setHost("localhost:8181");
		beanConfig.setBasePath("/japi/services");
		beanConfig.setResourcePackage("com.milstein.japi.services");

		beanConfig.setScan(true);

	}

}
