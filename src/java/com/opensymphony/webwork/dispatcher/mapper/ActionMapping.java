package com.opensymphony.webwork.dispatcher.mapper;

import java.util.Map;

/**
 * Simple class that holds the action mapping information used to invoke a
 * WebWork action. The name and namespace are required, but the params map
 * is optional, and as such may be null. If a params map is supplied,
 * it <b>must</b> be a mutable map, such as a HashMap.
 *
 * @author Patrick Lightbody
 */
public class ActionMapping {
    private String name;
    private String namespace;
    private Map params;

    public ActionMapping(String name, String namespace, Map params) {
        this.name = name;
        this.namespace = namespace;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public Map getParams() {
        return params;
    }
}