package com.adobe.aem.siteav39.core.services;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.LoginException;

import com.google.gson.JsonArray;

public interface BlogCategoryService {

	JsonArray getBlogSearchResults(String[] lowerCase, long offset, long limit) throws RepositoryException, LoginException;

	JsonArray getSearchBarResults(String testTag, long offset, long limit) throws RepositoryException, LoginException;

}
