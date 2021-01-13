package com.moxi.mogublog.picture.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树节点
 */
@Data
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

    public void setNodeName(String nodeName) {
        this.label = nodeName;
    }

    public void setChildNode(List<TreeNode> childNode) {
        this.children = childNode;
    }

}
