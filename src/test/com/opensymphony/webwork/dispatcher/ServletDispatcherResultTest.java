/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.webwork.dispatcher;

import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.Mock;
import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.webwork.WebWorkStatics;
import com.opensymphony.webwork.WebWorkTestCase;
import com.opensymphony.xwork.ActionContext;
import ognl.Ognl;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class ServletDispatcherResultTest extends WebWorkTestCase implements WebWorkStatics {

    public void testInclude() {
        ServletDispatcherResult view = new ServletDispatcherResult();
        view.setLocation("foo.jsp");

        Mock dispatcherMock = new Mock(RequestDispatcher.class);
        dispatcherMock.expect("include", C.ANY_ARGS);

        Mock requestMock = new Mock(HttpServletRequest.class);
        requestMock.expectAndReturn("getRequestDispatcher", C.args(C.eq("foo.jsp")), dispatcherMock.proxy());

        Mock responseMock = new Mock(HttpServletResponse.class);
        responseMock.expectAndReturn("isCommitted", Boolean.TRUE);

        ActionContext ac = new ActionContext(Ognl.createDefaultContext(null));
        ActionContext.setContext(ac);
        ServletActionContext.setRequest((HttpServletRequest) requestMock.proxy());
        ServletActionContext.setResponse((HttpServletResponse) responseMock.proxy());

        try {
            view.execute(null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        dispatcherMock.verify();
        requestMock.verify();
        dispatcherMock.verify();
    }

    public void testSimple() {
        ServletDispatcherResult view = new ServletDispatcherResult();
        view.setLocation("foo.jsp");

        Mock dispatcherMock = new Mock(RequestDispatcher.class);
        dispatcherMock.expect("forward", C.ANY_ARGS);

        Mock requestMock = new Mock(HttpServletRequest.class);
        requestMock.expectAndReturn("getAttribute", "javax.servlet.include.servlet_path", null);
        requestMock.expectAndReturn("getRequestDispatcher", C.args(C.eq("foo.jsp")), dispatcherMock.proxy());
        requestMock.expect("setAttribute", C.ANY_ARGS); // this is a bad mock, but it works
        requestMock.expect("setAttribute", C.ANY_ARGS); // this is a bad mock, but it works
        requestMock.matchAndReturn("getRequestURI", "foo.jsp");

        Mock responseMock = new Mock(HttpServletResponse.class);
        responseMock.expectAndReturn("isCommitted", Boolean.FALSE);

        ActionContext ac = new ActionContext(Ognl.createDefaultContext(null));
        ActionContext.setContext(ac);
        ServletActionContext.setRequest((HttpServletRequest) requestMock.proxy());
        ServletActionContext.setResponse((HttpServletResponse) responseMock.proxy());

        try {
            view.execute(null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        dispatcherMock.verify();
        requestMock.verify();
        dispatcherMock.verify();
    }
}
