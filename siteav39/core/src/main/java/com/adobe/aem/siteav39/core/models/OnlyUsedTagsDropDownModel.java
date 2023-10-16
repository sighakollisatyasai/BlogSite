package com.adobe.aem.siteav39.core.models;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
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
public class OnlyUsedTagsDropDownModel {

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

	private static final Logger Log = LoggerFactory.getLogger(OnlyUsedTagsDropDownModel.class);

	@ValueMapValue
	private String name;
	private Map<String, Set<String>> dropDown;
	private Value[] cardData;
	private Set<String> tagUsed;
    private Set<String> mainCatTags;
    private List<String> PagePaths = new ArrayList<>();
	private Map<String, Object> tagValues = new HashMap<>();
	private String tagTitle;
	public Value[] getCardData() {
		return cardData;
	}
	public String getTagTitle() {
		return tagTitle;
	}
	public Set<String> getMainCatTags() {
	return mainCatTags;
}
	public Set<String> getTagUsed() {
		return tagUsed;
	}
	public String getName() {
		return name;
	}
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

	
	public Map<String, Set<String>> getDropDown() {
		return dropDown;
	}

	@PostConstruct
	protected void init() throws RepositoryException, LoginException {
		long offset = 0, limit = 100;

		try {
			QueryExecutor queryExecutor = new QueryExecutor();
			tagUsed = new HashSet<String>();
			 dropDown = new HashMap<>();
			Map<String, Object> param = new HashMap<>();
			param.put(ResourceResolverFactory.SUBSERVICE, "test");
			resolver = resolverFactory.getServiceResourceResolver(param);
			session = resolver.adaptTo(Session.class);
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			StringBuilder queryString = new StringBuilder();
			queryString.append(
					"SELECT * FROM [cq:Page] AS page WHERE ISDESCENDANTNODE(page, '/content/siteav39/us/en/article')  ORDER BY [jcr:created]");
			JsonArray result = queryExecutor.queryResults(queryString, queryManager, offset, limit);
			int i = 0;

			while (i < result.size()) {
				Resource item = resolver.getResource(result.get(i).getAsString() + "/jcr:content");
				Node itemNode = item.adaptTo(Node.class);
				Property no = itemNode.getProperty("cq:tags");
				cardData = no.getValues();
				for (Value v : cardData) {
					Log.info("Hello" + v.toString());
					tagUsed.add(v.toString());
					if (!v.toString().contains("/")) {
						mainCatTags=new HashSet<String>();
						mainCatTags.add(v.toString());
						dropDown.put(v.toString(), mainCatTags);

					}
				}
				i++;
			}
			Iterator<String> it = tagUsed.iterator();
			while (it.hasNext()) {
				String x = it.next().toString();
				if (x.contains("/")) {
					for (String name : dropDown.keySet()) {
						System.out.println("key: " + name);
						Set<String> subTags = new HashSet<>();

						String[] mainCatAray = x.split("/");
						if (mainCatAray[0].equals(name)) {
							String mainCat = mainCatAray[0];
							subTags.add(mainCatAray[1]);
							if(dropDown.get(mainCat).equals(null)) {
							dropDown.put(mainCat, subTags);}
							else {
							dropDown.get(mainCat).add(mainCatAray[1]);}

						}
					}
				}
			}
			Log.info(dropDown.toString());

		} finally {
			if (session != null && session.isLive()) {
				session.logout();
			}

		}

	}

}
