/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.dispatcher;

import com.opensymphony.util.FileManager;

import com.opensymphony.webwork.WebWorkStatics;
import com.opensymphony.webwork.config.Configuration;
import com.opensymphony.webwork.dispatcher.multipart.MultiPartRequest;
import com.opensymphony.webwork.dispatcher.multipart.MultiPartRequestWrapper;
import com.opensymphony.webwork.views.velocity.VelocityManager;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionProxy;
import com.opensymphony.xwork.ActionProxyFactory;
import com.opensymphony.xwork.config.ConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Rickard �berg (rickard@middleware-company.com)
 * @author Matt Baldree (matt@smallleap.com)
 * @author jcarreira
 * @author Cameron Braid
 * @version $Revision$
 */
public class ServletDispatcher extends HttpServlet implements WebWorkStatics {
    //~ Static fields/initializers /////////////////////////////////////////////

    protected static final Log log = LogFactory.getLog(ServletDispatcher.class);

    //~ Instance fields ////////////////////////////////////////////////////////

    Integer maxSize;
    String saveDir;

    //~ Methods ////////////////////////////////////////////////////////////////

    public static String getNamespaceFromServletPath(String servletPath) {
        servletPath = servletPath.substring(0, servletPath.lastIndexOf("/"));

        return servletPath;
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // initialize the VelocityEngine
        VelocityManager.init(config.getServletContext());

        //check for configuration reloading
        if ("true".equalsIgnoreCase(Configuration.getString("webwork.configuration.xml.reload"))) {
            FileManager.setReloadingConfigs(true);
        }

        //load multipart configuration
        //saveDir
        saveDir = Configuration.getString("webwork.multipart.saveDir").trim();

        if (saveDir.equals("")) {
            File tempdir = (File) config.getServletContext().getAttribute("javax.servlet.context.tempdir");
            log.warn("Unable to find 'webwork.multipart.saveDir' property setting. Defaulting to javax.servlet.context.tempdir");

            if (tempdir != null) {
                saveDir = tempdir.toString();
            }
        } else {
            File multipartSaveDir = new File(saveDir);

            if (!multipartSaveDir.exists()) {
                multipartSaveDir.mkdir();
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("saveDir=" + saveDir);
        }

        //maxSize
        try {
            String maxSizeStr = Configuration.getString("webwork.multipart.maxSize");

            if (maxSizeStr != null) {
                try {
                    maxSize = new Integer(maxSizeStr);
                } catch (NumberFormatException e) {
                    maxSize = new Integer(Integer.MAX_VALUE);
                    log.warn("Unable to format 'webwork.multipart.maxSize' property setting. Defaulting to Integer.MAX_VALUE");
                }
            } else {
                maxSize = new Integer(Integer.MAX_VALUE);
                log.warn("Unable to format 'webwork.multipart.maxSize' property setting. Defaulting to Integer.MAX_VALUE");
            }
        } catch (IllegalArgumentException e1) {
            maxSize = new Integer(Integer.MAX_VALUE);
            log.warn("Unable to format 'webwork.multipart.maxSize' property setting. Defaulting to Integer.MAX_VALUE");
        }

        if (log.isDebugEnabled()) {
            log.debug("maxSize=" + maxSize);
        }
    }

    /**
    * Service a request.
    * The request is first checked to see if it is a multi-part. If it is, then the request
    * is wrapped so WW will be able to work with the multi-part as if it was a normal request.
    * Then the request is handed to GenericDispatcher and executed.
    *
    * @param request
    * @param response
    * @exception javax.servlet.ServletException
    */
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //wrap request if needed
        try {
            request = wrapRequest(request);
        } catch (IOException e) {
            String message = "Could not wrap servlet request with MultipartRequestWrapper!";
            log.error(message, e);
            throw new ServletException(message, e);
        }

        // Get action
        String servletPath = (String) request.getAttribute("javax.servlet.include.servlet_path");

        if (servletPath == null) {
            servletPath = request.getServletPath();
        }

        // Path is always original path, even if it is included in page with another path
        String actionPath = request.getServletPath();
        actionPath = getNamespaceFromServletPath(actionPath);

        String actionName = getActionName(servletPath);

        HashMap extraContext = new HashMap();
        extraContext.put(ActionContext.PARAMETERS, request.getParameterMap());
        extraContext.put(ActionContext.SESSION, new SessionMap(request.getSession()));
        extraContext.put(ActionContext.APPLICATION, new ApplicationMap(getServletContext()));
        extraContext.put(HTTP_REQUEST, request);
        extraContext.put(HTTP_RESPONSE, response);
        extraContext.put(SERVLET_CONFIG, getServletConfig());
        extraContext.put(COMPONENT_MANAGER, request.getAttribute("DefaultComponentManager"));
        extraContext.put(SERLVET_DISPATCHER, this);

        try {
            ActionProxy proxy = ActionProxyFactory.getFactory().createActionProxy(actionPath, actionName, extraContext);
            request.setAttribute("webwork.valueStack", proxy.getValueStack());
            proxy.execute();
        } catch (ConfigurationException e) {
            sendError(request, response, HttpServletResponse.SC_NOT_FOUND, e);
            log.error("Could not find action", e);
        } catch (Exception e) {
            sendError(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
            log.error("Could not execute action", e);
        }
    }

    /**
    * Determine action name by extracting last string and removing
    * extension. (/.../.../Foo.action -> Foo)
    */
    String getActionName(String name) {
        // Get action name ("Foo.action" -> "Foo" action)
        int beginIdx = name.lastIndexOf("/");
        int endIdx = name.lastIndexOf(".");

        return name.substring(((beginIdx == -1) ? 0 : (beginIdx + 1)), (endIdx == -1) ? name.length() : endIdx);
    }

    private void sendError(HttpServletRequest request, HttpServletResponse response, int code, Exception e) {
        try {
            // send a http 500 INTERNAL SERVER ERROR to use the servlet defined error handler
            // make the exception availible to the web.xml defined error page
            request.setAttribute("javax.servlet.error.exception", e);

            // for compatibility 
            request.setAttribute("javax.servlet.jsp.jspException", e);

            // send the error response
            response.sendError(code, e.getMessage());

            /*
                                            response.setContentType("text/html");
                                            response.setLocale(Configuration.getLocale());

                                            PrintWriter writer = response.getWriter();
                                            writer.write("Error executing action: " + e.getMessage());
                                            writer.println("<pre>\n");
                                            e.printStackTrace(response.getWriter());
                                            writer.print("</pre>\n");
            */
        } catch (IOException e1) {
        }
    }

    /**
    * Wrap servlet request with the appropriate request. It will check to
    * see if request is a multipart request and wrap in appropriately.
    *
    * @param request
    * @return wrapped request or original request
    */
    private HttpServletRequest wrapRequest(HttpServletRequest request) throws IOException {
        // don't wrap more than once
        if (request instanceof MultiPartRequestWrapper) {
            return request;
        }

        if (MultiPartRequest.isMultiPart(request)) {
            request = new MultiPartRequestWrapper(request, saveDir, maxSize.intValue());
        }

        return request;
    }
}
