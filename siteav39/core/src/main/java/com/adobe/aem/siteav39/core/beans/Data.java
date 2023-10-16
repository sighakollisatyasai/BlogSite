package com.adobe.aem.siteav39.core.beans;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;

import com.adobe.cq.export.json.ComponentExporter;

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class })
public class Data {

	private String name;
	private String profession;
	private String image;
	private String blogPagePath;

	
	public String getImage() {
		return image;
	}

	public Data(String name, String profession,String image,String blogPagePath) {
		this.name = name;
		this.profession = profession;
		this.image=image;
		this.blogPagePath=blogPagePath;
	}

	public String getName() {
		return name;
	}

	public String getProfession() {
		return profession;
	}
	public String getBlogPagePath() {
		return blogPagePath;
	}

	@Override
	public String toString() {
		return "Data [name=" + name + ", profession=" + profession + ", image=" + image + ", blogPagePath="
				+ blogPagePath + "]";
	}


}