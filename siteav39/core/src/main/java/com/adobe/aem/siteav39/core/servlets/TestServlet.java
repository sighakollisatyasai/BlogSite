package com.adobe.aem.siteav39.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

 

import javax.servlet.Servlet;
import javax.servlet.ServletException;

 

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.adobe.cq.dam.cfm.ContentElement;
//import com.adobe.cq.dam.cfm.ContentFragment;
//import com.adobe.cq.dam.cfm.ContentFragmentException;
//import com.adobe.cq.dam.cfm.FragmentTemplate;

 

@Component(service = Servlet.class, property = { "Sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "Sling.servlet.paths=" + "/bin/createCF" })
public class TestServlet extends SlingSafeMethodsServlet {

 

    private static final Logger Log = LoggerFactory.getLogger(TestServlet.class);

 

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

 

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

 

        ResourceResolver resolver = null;

 

        Map<String, Object> param = new HashMap<String, Object>();
        param.put(resourceResolverFactory.SUBSERVICE, "test");
        try {
            resolver = resourceResolverFactory.getServiceResourceResolver(param);
            Resource fragResource = resolver.getResource("/conf/siteav39/settings/dam/cfm/models/offers");
            if (fragResource != null) {
                Log.info(fragResource.toString());
//                FragmentTemplate fragTemplate = fragResource.adaptTo(FragmentTemplate.class);
                Resource resource = resolver.getResource("/content/dam/offers-fragments");
//                Log.info(resource.toString());
//                ContentFragment cf = fragTemplate.createFragment(resource, "TestCf", "testCf");
//                cf.setDescription("Fragment Description");
//                Iterator<ContentElement> itrator=cf.getElements();
//                while(itrator.hasNext())
//                {
//                	ContentElement element=itrator.next();
//                	switch(element.getName())
//                	{
//                	case "jobTitle":element.setContent(getServletInfo(), getServletName());
//                	}
//                }
                response.getWriter().write("Fragment Created");
                resolver.commit();
 

//                Log.info(cf.getTitle() + "" + cf.getName());
                /*
                 * if (cf != null) {
                 * 
                 * response.getWriter().write("Content Fragment Created"); } else {
                 * response.getWriter().write("Not Created"); }
                 */
            }
        } catch (LoginException  e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response.getWriter().write("Error While Creating Fragment");
        }
    }

 

}
