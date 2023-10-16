package com.adobe.aem.siteav39.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BlogCardModel {

	private static final Logger Log = LoggerFactory.getLogger(BlogCardModel.class);

	@ValueMapValue
	private String name;

	@ValueMapValue
	private String profession;

	@ValueMapValue
	private String image;
	public String getName() {
		return name;
	}

	public String getProfession() {
		return profession;
	}

	public String getImage() {
		return image;
	}

}
