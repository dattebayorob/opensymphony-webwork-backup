/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.views.jsp;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.webwork.dispatcher.ApplicationMap;
import com.opensymphony.webwork.dispatcher.ServletDispatcher;
import com.opensymphony.webwork.dispatcher.SessionMap;

import com.opensymphony.xwork.util.OgnlValueStack;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.BodyTagSupport;


/**
 * User: plightbo
 * Date: Oct 17, 2003
 * Time: 7:09:15 AM
 */
public class WebWorkBodyTagSupport extends BodyTagSupport {
    //~ Methods ////////////////////////////////////////////////////////////////

    protected OgnlValueStack getValueStack() {
        return TagUtils.getStack(pageContext);
    }
}
