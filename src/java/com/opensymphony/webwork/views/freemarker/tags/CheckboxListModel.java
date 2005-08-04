package com.opensymphony.webwork.views.freemarker.tags;

import com.opensymphony.webwork.components.UIBean;
import com.opensymphony.webwork.components.CheckboxList;
import com.opensymphony.xwork.util.OgnlValueStack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: plightbo
 * Date: Aug 1, 2005
 * Time: 8:46:31 PM
 */
public class CheckboxListModel extends TagModel {
    public CheckboxListModel(OgnlValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        super(stack, req, res);
    }

    protected UIBean getBean() {
        return new CheckboxList(stack, req, res);
    }
}