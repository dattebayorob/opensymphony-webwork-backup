package com.opensymphony.webwork.webFlow.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: plightbo
 * Date: Jun 26, 2005
 * Time: 4:53:57 PM
 */
public class SubGraph implements Render {
    protected String name;
    protected SubGraph parent;
    protected List subGraphs;
    protected List nodes;

    public SubGraph(String name) {
        this.name = name;
        this.subGraphs = new ArrayList();
        this.nodes = new ArrayList();
    }

    public String getName() {
        return name;
    }

    public void addSubGraph(SubGraph subGraph) {
        subGraph.setParent(this);
        subGraphs.add(subGraph);
    }

    public void setParent(SubGraph parent) {
        this.parent = parent;
    }

    public void addNode(WebFlowNode node) {
        node.setParent(this);
        Graph.nodeMap.put(node.getFullName(), node);
        nodes.add(node);
    }

    public void render(IndentWriter writer) throws IOException {
        // write the header
        writer.write("subgraph cluster_" + getPrefix() + " {", true);
        writer.write("color=grey;");
        writer.write("fontcolor=grey;");
        writer.write("label=\"" + name + "\";");

        // write out the subgraphs
        for (Iterator iterator = subGraphs.iterator(); iterator.hasNext();) {
            SubGraph subGraph = (SubGraph) iterator.next();
            subGraph.render(new IndentWriter(writer));
        }

        // write out the actions
        for (Iterator iterator = nodes.iterator(); iterator.hasNext();) {
            WebFlowNode webFlowNode = (WebFlowNode) iterator.next();
            webFlowNode.render(writer);
        }

        // .. footer
        writer.write("}", true);
    }

    public String getPrefix() {
        if (parent == null) {
            return name;
        } else {
            String prefix = parent.getPrefix();
            if (prefix.equals("")) {
                return name;
            } else {
                return prefix + "_" + name;
            }
        }
    }

    public SubGraph create(String namespace) {
        if (namespace.equals("")) {
            return this;
        }

        String[] parts = namespace.split("\\/");
        SubGraph last = this;
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.equals("")) {
                continue;
            }

            SubGraph subGraph = findSubGraph(part);
            if (subGraph == null) {
                subGraph = new SubGraph(part);
                last.addSubGraph(subGraph);
            }

            last = subGraph;
        }

        return last;
    }

    private SubGraph findSubGraph(String name) {
        for (Iterator iterator = subGraphs.iterator(); iterator.hasNext();) {
            SubGraph subGraph = (SubGraph) iterator.next();
            if (subGraph.getName().equals(name)) {
                return subGraph;
            }
        }

        return null;
    }
}