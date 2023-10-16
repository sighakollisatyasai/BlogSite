package com.adobe.aem.siteav39.core.services;


import com.adobe.aem.siteav39.core.beans.Data;
import com.adobe.aem.siteav39.core.models.BlogCardModel;
import com.day.cq.commons.jcr.JcrConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service = { BlogCategoryService.class })
@ServiceDescription("Simple Demo Servlet")
public class BlogCategoryBaseService implements BlogCategoryService{

	
	@SlingObject
	private SlingHttpServletRequest request;
	@Reference
	private ResourceResolverFactory resolverFactory;

	Session session = null;

	@Inject
	ResourceResolver resolver;
	
	List<Data> cardData;
    private static final long serialVersionUID = 1L;

    static final Logger Log=LoggerFactory.getLogger(BlogCategoryService.class);
    public JsonArray getBlogSearchResults(String[] tagCategory, long offset, long limit)
			throws RepositoryException, LoginException {
    
		cardData = new ArrayList<Data>();
		JsonArray respArray = new JsonArray();
		String formatter;
		try {
			
			Map<String, Object> param = new HashMap<>();
			param.put(ResourceResolverFactory.SUBSERVICE, "test");
			resolver = resolverFactory.getServiceResourceResolver(param);
			session = resolver.adaptTo(Session.class);
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			StringBuilder queryString=new StringBuilder();
            //String query = String.format("SELECT * FROM [cq:Page] AS page WHERE ISDESCENDANTNODE(page, '/content/siteav39/us/en/article') AND page.[jcr:content/cq:tags] = '%s' ORDER BY [jcr:created] DESC",tagCategory);
			String query ="SELECT * FROM [cq:Page] AS page WHERE ISDESCENDANTNODE(page,'/content/siteav39/us/en/article') AND " ;
			StringBuilder queryM=new StringBuilder();
			for(int i=0;i<tagCategory.length;i++)
			{
				if(i==0 || tagCategory.length==1)
				{
					 formatter=String.format("page.[jcr:content/cq:tags]='%s'", tagCategory[i].toLowerCase());
				}
				else
				{
				 formatter=String.format(" OR page.[jcr:content/cq:tags]='%s'", tagCategory[i].toLowerCase());}
				queryM.append(formatter);
			}
			queryString.append(query+queryM);

			JsonArray pagePaths = queryResults(queryString, queryManager, offset, limit);
			Iterator<JsonElement> itrator = pagePaths.iterator();
			
			while (itrator.hasNext()) {
				String path = itrator.next().getAsString();
				Resource container = resolver.getResource(path + "/jcr:content/root/responsivegrid");
				Iterator<Resource> comps = container.listChildren();
				while (comps.hasNext()) {
					JsonObject resp = new JsonObject();
					Resource item = comps.next();
					Node itemNode = item.adaptTo(Node.class);
					if (itemNode.getProperty("sling:resourceType").getValue().toString()
							.equals("siteav39/components/customComponents/blogCardComponent")) {
						String listPath = itemNode.getPath();
						Resource resource1 = resolver.getResource(listPath);
						resp.addProperty("image",resource1.getValueMap().get("image",String.class));
						resp.addProperty("name", resource1.getValueMap().get("name",String.class));
						resp.addProperty("profession",resource1.getValueMap().get("profession",String.class));
					    resp.addProperty("path",resolver.map(path));
					    resp.addProperty("tag","Hello");
						Log.info("Checking"+resp.toString());
					respArray.add(resp);
					}
				}
				Log.info(respArray.toString());

			
			}
		} finally {
			if (session != null && session.isLive()) {
				session.logout();
			}

		}
		return respArray;

    }
    public JsonArray getSearchBarResults(String searchText, long offset, long limit)
			throws RepositoryException, LoginException {
		JsonArray respArray = new JsonArray();
		try {
			
			Map<String, Object> param = new HashMap<>();
			param.put(ResourceResolverFactory.SUBSERVICE, "test");
			resolver = resolverFactory.getServiceResourceResolver(param);
			session = resolver.adaptTo(Session.class);
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			StringBuilder queryString=new StringBuilder();
            //String query = String.format("SELECT * FROM [cq:Page] AS page WHERE ISDESCENDANTNODE(page, '/content/siteav39/us/en/article') AND page.[jcr:content/cq:tags] = '%s' ORDER BY [jcr:created] DESC",tagCategory);
			StringBuilder queryM=new StringBuilder();
		     
			queryString.append("SELECT * FROM [cq:Page] AS page WHERE ISDESCENDANTNODE(page,'/content/siteav39/us/en/article') AND page.[jcr:content/jcr:title] LIKE '%").append(searchText).append("%'");

			JsonArray pagePaths = queryResults(queryString, queryManager, offset, limit);
			Iterator<JsonElement> itrator = pagePaths.iterator();
			
			while (itrator.hasNext()) {
				String path = itrator.next().getAsString();
				Resource container = resolver.getResource(path + "/jcr:content/root/responsivegrid");
				Iterator<Resource> comps = container.listChildren();
				while (comps.hasNext()) {
					JsonObject resp = new JsonObject();
					Resource item = comps.next();
					Node itemNode = item.adaptTo(Node.class);
					if (itemNode.getProperty("sling:resourceType").getValue().toString()
							.equals("siteav39/components/customComponents/blogCardComponent")) {
						String listPath = itemNode.getPath();
						Resource resource1 = resolver.getResource(listPath);
						resp.addProperty("image",resource1.getValueMap().get("image",String.class));
						resp.addProperty("name", resource1.getValueMap().get("name",String.class));
						resp.addProperty("profession",resource1.getValueMap().get("profession",String.class));
						resp.addProperty("path",resolver.map(path));
					    resp.addProperty("tag","Hello");
						Log.info("Checking"+resp.toString());
					respArray.add(resp);
					}
				}
				Log.info(respArray.toString());

			
			}
		} finally {
			if (session != null && session.isLive()) {
				session.logout();
			}

		}
		return respArray;

    }
	private JsonArray queryResults(StringBuilder queryString, QueryManager queryManager, long offset, long limit)
			throws RepositoryException {
		JsonArray resultsList = new JsonArray();
		Query query = queryManager.createQuery(queryString.toString(), "JCR-SQL2");
		if (limit > 0)
			query.setLimit(limit);
		if (offset > 0)
			query.setOffset(offset);
		QueryResult queryResult = query.execute();
		RowIterator rowIterator = queryResult.getRows();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.nextRow();
			resultsList.add(row.getPath());
		}
		return resultsList;
	}
}
