/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.views.jsp.ui.ajax;

import com.opensymphony.webwork.TestAction;
import com.opensymphony.webwork.views.jsp.AbstractUITagTest;

import javax.servlet.jsp.tagext.TagSupport;


/**
 * @author Ian Roughley<a href="mailto:ian@fdar.com">&lt;ian@fdar.com&gt;</a>
 * @version $Id$
 */
public class RemoteLinkTest extends AbstractUITagTest {
    //~ Methods ////////////////////////////////////////////////////////////////


    public void testSimple() throws Exception {
        TestAction testAction = (TestAction) action;
        testAction.setFoo("bar");

        RemoteLinkTag tag = new RemoteLinkTag();
        tag.setPageContext(pageContext);

        tag.setId("mylink");
        tag.setTheme("ajax");
        tag.setHref( "a" );
        tag.setErrorText( "c" );
        tag.setShowErrorTransportText( "true" );
        tag.setNotifyTopics( "g" );
        tag.setAfterLoading( "h" );

        assertEquals( tag.doStartTag(), TagSupport.EVAL_BODY_INCLUDE );
        assertEquals( tag.doEndTag(), TagSupport.EVAL_PAGE );

        verify(RemoteLinkTest.class.getResource("remotelink-1.txt"));
    }

}