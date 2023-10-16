package com.adobe.aem.siteav39.core.services.impl;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

import com.google.gson.JsonArray;

public class QueryExecutor {

	public  JsonArray queryResults(StringBuilder queryString, QueryManager queryManager, long offset, long limit)
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
