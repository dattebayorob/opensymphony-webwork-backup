/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.views.jsp;

import com.opensymphony.webwork.views.jsp.ParamTag;
import com.opensymphony.webwork.views.jsp.WebWorkBodyTagSupport;
import com.opensymphony.xwork.TextProvider;
import com.opensymphony.xwork.util.OgnlValueStack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Access a i18n-ized message. The message must be in a resource bundle
 * with the same name as the action that it is associated with. In practice
 * this means that you should create a properties file in the same package
 * as your Java class with the same name as your class, but with .properties
 * extension.
 * <p/>
 * See examples for further info on how to use.
 * <p/>
 * If the named message is not found, then the body of the tag will be used as default message.
 * If no body is used, then the name of the message will be used.
 *
 * @author Jason Carreira
 */
public class TextTag extends WebWorkBodyTagSupport implements ParamTag.UnnamedParametric {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final Log LOG = LogFactory.getLog(TextTag.class);

    //~ Instance fields ////////////////////////////////////////////////////////

    protected String value0Attr;
    protected String value1Attr;
    protected String value2Attr;
    protected String value3Attr;
    List values;
    String actualName;
    String nameAttr;

    //~ Methods ////////////////////////////////////////////////////////////////

    public void setName(String name) {
        this.nameAttr = name;
    }

    public Map getParameters() {
        return null;
    }

    public void setValue0(String aName) {
        LOG.warn("The value attributes of TextTag are deprecated.");
        value0Attr = aName;
    }

    public void setValue1(String aName) {
        LOG.warn("The value attributes of TextTag are deprecated.");
        value1Attr = aName;
    }

    public void setValue2(String aName) {
        LOG.warn("The value attributes of TextTag are deprecated.");
        value2Attr = aName;
    }

    public void setValue3(String aName) {
        LOG.warn("The value attributes of TextTag are deprecated.");
        value3Attr = aName;
    }

    public void addParameter(String key, Object value) {
        addParameter(value);
    }

    public void addParameter(Object value) {
        if (value == null) {
            return;
        }

        if (values == null) {
            values = new ArrayList();
        }

        values.add(value);
    }

    public int doEndTag() throws JspException {
        actualName = (String) findString(nameAttr);

        // Add tag attribute values
        // These can be used to parameterize the i18n-ized message
        if (value0Attr != null) {
            addParameter(findValue(value0Attr));
        }

        if (value1Attr != null) {
            addParameter(findValue(value1Attr));
        }

        if (value2Attr != null) {
            addParameter(findValue(value2Attr));
        }

        if (value3Attr != null) {
            addParameter(findValue(value3Attr));
        }

        String defaultMessage;

        if ((bodyContent != null) && (bodyContent.getString().trim().length() > 0)) {
            defaultMessage = bodyContent.getString().trim();
        } else {
            defaultMessage = actualName;
        }

        String msg = null;
        OgnlValueStack stack = getStack();

        for (Iterator iterator = getStack().getRoot().iterator();
            iterator.hasNext();) {
            Object o = iterator.next();

            if (o instanceof TextProvider) {
                TextProvider tp = (TextProvider) o;
                msg = tp.getText(actualName, defaultMessage, values, stack);

                break;
            }
        }

        if (msg != null) {
            try {
                if (getId() == null) {
                    pageContext.getOut().write(msg);
                } else {
                    stack.getContext().put(getId(), msg);
                }
            } catch (IOException e) {
                throw new JspException(e);
            }
        }

        return EVAL_PAGE;
    }

    public int doStartTag() throws JspException {
        values = null;

        return super.doStartTag();
    }
}