package org.mrk.learning.rest.util;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String id;
    private String field;
    private List<Node> children = new ArrayList<>();
    private Node parent;


    public String getId() {
        return id;
    }

    public Node setId(String id) {
        this.id = id;
        return this;
    }

    public Node(String id){
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public Node setField(String field) {
        this.field = field;
        return this;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Node addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
        return child;
    }

    public void addChildren(List<Node> children) {
        children.forEach(each -> each.setParent(this));
        this.children.addAll(children);
    }

    public Node getParent() {
        return parent;
    }

    public Node setParent(Node parent) {
        this.parent = parent;
        return this;
    }
}
