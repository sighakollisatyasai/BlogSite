package com.adobe.aem.siteav39.core.servlets;

import java.io.IOException;


import javax.jcr.LoginException;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.siteav39.core.services.BlogCategoryService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component(service=Servlet.class,property= {"sling.servlet.methods="+HttpConstants.METHOD_GET,"sling.servlet.resourceTypes="+"/apps/testServlet"})
public class BlogCategoryServlet extends SlingAllMethodsServlet{
	static final Logger Log=LoggerFactory.getLogger(BlogCategoryServlet.class);
	@Reference
	private BlogCategoryService service;
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		JsonArray result = new JsonArray();
		long offset=0;
		if(request.getParameter("offset")!=null) {
		 offset=Integer.parseInt(request.getParameter("offset"));}
		long limit=6;
		try {
			String[] testTag=request.getParameterValues("category");
			String tagCategory = request.getParameter("category");
			String searchText=request.getParameter("search");
			if (StringUtils.isNotBlank(searchText)) {
				offset=0;limit=6;
				result = service.getSearchBarResults(searchText,offset,limit);
				response.getWriter().write(result.toString());
			}
			if (StringUtils.isNotBlank(tagCategory)) {
				result = service.getBlogSearchResults(testTag,offset, limit);
				response.getWriter().write(result.toString());
			} /*else {
				JsonObject error = new JsonObject();
				error.addProperty("errorStatus", "400");
				error.addProperty("errorMessage", "Search Parameter Missing");
				response.getWriter().write(error.toString());
				response.setStatus(400);
			}*/
		} catch (RepositoryException | org.apache.sling.api.resource.LoginException e) {
			Log.error("Error occured in searching blog... ", e);
			JsonObject error = new JsonObject();
			error.addProperty("errorStatus", "500");
			error.addProperty("errorMessage", "Internal Server Error");
			response.getWriter().write(error.toString());
			response.setStatus(500);
		}
	}
}
