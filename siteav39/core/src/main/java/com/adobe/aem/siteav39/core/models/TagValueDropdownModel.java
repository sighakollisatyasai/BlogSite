package com.adobe.aem.siteav39.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.QueryManager;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.siteav39.core.services.impl.QueryExecutor;
import com.google.gson.JsonArray;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TagValueDropdownModel {

	@SlingObject
	private SlingHttpServletRequest request;
	@Inject
	private ResourceResolverFactory resolverFactory;

	Session session = null;

	@Inject
	ResourceResolver resolver;

	public SlingHttpServletRequest getRequest() {
		return request;
	}

	public ResourceResolver getResolver() {
		return resolver;
	}

	private static final Logger Log = LoggerFactory.getLogger(TagValueDropdownModel.class);

	@ValueMapValue
	private String name;

	public List<String> getCardData() {
		return cardData;
	}

	private String tagTitle;

	public String getTagTitle() {
		return tagTitle;
	}

	List<String> cardData;

	public String getName() {
		return name;
	}

	List<String> PagePaths = new ArrayList<>();
	Map<String, Object> tagValues = new HashMap<>();

	public Map<String, Object> getTagValues() {
		return tagValues;
	}

	public ResourceResolverFactory getResolverFactory() {
		return resolverFactory;
	}

	public Session getSession() {
		return session;
	}

	public static Logger getLog() {
		return Log;
	}

	public List<String> getPagePaths() {
		return PagePaths;
	}

	@PostConstruct
	protected void init() throws RepositoryException, LoginException {
		long offset = 0, limit = 6;

		try {
			QueryExecutor queryExecutor = new QueryExecutor();

			Map<String, Object> param = new HashMap<>();
			param.put(ResourceResolverFactory.SUBSERVICE, "test");
			resolver = resolverFactory.getServiceResourceResolver(param);
			session = resolver.adaptTo(Session.class);
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			StringBuilder queryString = new StringBuilder();
			queryString.append("SELECT * from [cq:Tag] As tag Where ISCHILDNODE(tag,'/content/cq:tags/default')");
			JsonArray result = queryExecutor.queryResults(queryString, queryManager, offset, limit);
			int i = 0;
			while (i < result.size()) {
				StringBuilder qS = new StringBuilder();
				String z=result.get(i).toString().replaceAll("\"","");
				if(i!=0) {
				qS.append("SELECT * from [cq:Tag] As tag Where ISCHILDNODE(tag," + "'" + z+ "'" + ")");
				JsonArray subResult = queryExecutor.queryResults(qS, queryManager, offset, limit);
				int j = 0;
				cardData = new ArrayList<String>();

				while (j < subResult.size()) {
					String subCategory=subResult.get(j).toString().replaceAll("\"","");
					String[] fetchSubCategory=subCategory.split("/");
					cardData.add(fetchSubCategory[fetchSubCategory.length-2]+"/"+fetchSubCategory[fetchSubCategory.length-1]);
//					cardData.add("Hello");
					j++;
				}
				String[] fetchCategory=z.split("/");
				tagValues.put(fetchCategory[fetchCategory.length-1], cardData);
				}
				i++;
			}
			Log.info(result.toString());
			
			 /* Resource container = resolver.getResource("/content/cq:tags/default");
			  Iterator<Resource> comps = container.listChildren(); while (comps.hasNext())
			  { Resource item = comps.next(); Node itemNode = item.adaptTo(Node.class); if
			  (!item.getName().equals("do-not-translate")) { Iterator<Resource> mainNodes =
			  item.listChildren(); cardData = new ArrayList<String>(); while
			 (mainNodes.hasNext()) { Resource subNode = mainNodes.next();
			  Log.info(subNode.getName()); cardData.add(subNode.getParent().getName() + "/"
			  + subNode.getName()); tagValues.put(subNode.getParent().getName(), cardData);
			  } Log.info(item.getName()); } }*/
			 

		} finally {
			if (session != null && session.isLive()) {
				session.logout();
			}

		}

	}

}
