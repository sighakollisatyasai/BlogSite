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
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;
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

import com.adobe.aem.siteav39.core.beans.Data;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ParentBlogComponentModel {

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

	private static final Logger Log = LoggerFactory.getLogger(ParentBlogComponentModel.class);

	@ValueMapValue
	private String name;

	public List<Data> getCardData() {
		return cardData;
	}

	private String tagTitle;
	public String getTagTitle() {
		return tagTitle;
	}

	List<Data> cardData;

	public String getName() {
		return name;
	}

	List<String> PagePaths = new ArrayList<>();

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
		cardData = new ArrayList<Data>();
		
		try {

			Map<String, Object> param = new HashMap<>();
			param.put(ResourceResolverFactory.SUBSERVICE, "test");
			resolver = resolverFactory.getServiceResourceResolver(param);
			session = resolver.adaptTo(Session.class);
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			StringBuilder queryString = new StringBuilder();
			StringBuilder qS=new StringBuilder();

String query = String.format("SELECT * FROM [cq:Page] AS page WHERE ISDESCENDANTNODE(page, '/content/siteav39/us/en/article') AND page.[jcr:content/cq:tags] = '%s' ORDER BY [jcr:created] DESC",name);
			queryString.append(query);

			JsonArray pagePaths = queryResults(queryString, queryManager, offset, limit);
			Iterator<JsonElement> itrator = pagePaths.iterator();
			while (itrator.hasNext()) {
				String path = itrator.next().getAsString();
				Resource tagCheck=resolver.getResource(path.concat("/jcr:content"));
				tagTitle=tagCheck.getValueMap().get("cq:tags",String.class);
				Resource container = resolver.getResource(path + "/jcr:content/root/responsivegrid");
				Iterator<Resource> comps = container.listChildren();
				while (comps.hasNext()) {
					Resource item = comps.next();
					Node itemNode = item.adaptTo(Node.class);
					if (itemNode.getProperty("sling:resourceType").getValue().toString()
							.equals("siteav39/components/customComponents/blogCardComponent")) {
						String listPath = itemNode.getPath();
						Resource resource1 = resolver.getResource(listPath);
						BlogCardModel bg=resource1.adaptTo(BlogCardModel.class);
						Log.info("OK!!!"+bg.getImage()+bg.getName());
						cardData.add(new Data(bg.getName(),
								bg.getProfession(),
								bg.getImage(),path));
						Log.info(cardData.toString());
					}
				}

			
			}
		} finally {
			if (session != null && session.isLive()) {
				session.logout();
			}

		}

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
