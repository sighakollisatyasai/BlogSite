package com.adobe.aem.siteav39.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { Resource.class,SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class UrlMapperModel {

	private static final Logger Log = LoggerFactory.getLogger(UrlMapperModel.class);

	@ValueMapValue(name="link")
	private String navhref;
    
	private String formatHref;
	

	public static Logger getLog() {
		return Log;
	}

	public String getNavhref() {
		return navhref;
	}
	@PostConstruct
	protected void init()
	{
		if(navhref.startsWith("/content/"))
		{
			formatHref=navhref.concat(".html");
		}
	}
	public String getFormatHref() {
		return formatHref;
	}

}
