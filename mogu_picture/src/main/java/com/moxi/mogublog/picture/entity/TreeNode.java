package com.moxi.mogublog.picture.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树节点
 */
public class TreeNode {
    /**
     * 节点id
     */
    private long id;
    /**
     * 节点名
     */
    private String label;
    /**
     * 深度
     */
    private int depth;
    /**
     * 是否被关闭
     */
    private String state = "closed";

    /**
     * 属性集合
     */
    private Map<String, String> attributes = new HashMap<>();
    /**
     * 子节点列表
     */
    private List<TreeNode> children = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getLabel() {
        return label;
    }

    public void setNodeName(String nodeName) {
        this.label = nodeName;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildNode(List<TreeNode> childNode) {
        this.children = childNode;
    }
}
