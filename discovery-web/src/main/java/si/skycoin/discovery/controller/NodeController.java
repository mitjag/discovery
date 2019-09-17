package si.skycoin.discovery.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import si.skycoin.discovery.manager.NodeManager;
import si.skycoin.discovery.manager.StatisticManager;
import si.skycoin.discovery.model.Node;
import si.skycoin.discovery.transfer.NodeData;
import si.skycoin.discovery.transfer.NodeStatisticRs;

@Named
@ViewScoped
public class NodeController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private Logger log;
    
    @Inject
    private FacesContext facesContext;
    
    @EJB
    private StatisticManager statisticManager;
    
    @EJB
    private NodeManager nodeManager;
    
    private Integer year;
    
    private Integer month;
    
    private String nodeKey;
        
    private String groupTag;
    
    private List<NodeData> nodeData;
    
    private NodeData selectedNodeData;
    
    private List<Node> nodeList;
    
    private String nodeKeyList;
    
    private Map<String, Integer> nodeStat;

    @PostConstruct
    public void postConstruct() {
        if (!facesContext.isPostback()) {
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            log.info("remoteAddr: {} X-Forwarded-For: {}", request.getRemoteAddr(), request.getHeader("X-Forwarded-For"));
        }
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(1);
        year = localDateTime.getYear();
        month = localDateTime.getMonthValue();
        nodeData = new ArrayList<>();
        nodeStat = new HashMap<>();
        nodeList = new ArrayList<>();
        selectedNodeData = null;
    }
    
    public void find() {
        log.info("find year: {} month: {} nodeKey: {} groupTag: {}", year, month, nodeKey, groupTag);
        nodeList.addAll(nodeManager.nodeGroup(nodeKey, groupTag));
        nodeKeyList = toNodeKeyList();
        nodeKey = "";
        nodeData = statisticManager.nodeStatistic(year, month, nodeList);
        
        for (NodeData nd : nodeData) {
            int nodeCount = 0;
            int fileCount = 0;
            for (NodeStatisticRs ns : nd.getNodeStatistic()) {
                nodeCount += ns.getNodeCount();
                fileCount += ns.getFileCount();
            }
            if (fileCount != 0) {
                nodeStat.put(nd.getNodeKey(), percent(nodeCount, fileCount));
            }
        }
        
        selectedNodeData = null;
    }
    
    public int percent(String nodeKey) {
        Integer value = nodeStat.get(nodeKey); 
        return value != null ? value : 0;
    }
    
    public void reset() {
        nodeData = new ArrayList<>();
        nodeStat = new HashMap<>();
        nodeList = new ArrayList<>();
        nodeKey = "";
        nodeKeyList = "";
        selectedNodeData = null;
    }
    
    public void selectNode(Node node) {
        Optional<NodeData> findFirst = nodeData.stream().filter(nd -> nd.getNodeKey().equals(node.getKey())).findFirst();
        if (findFirst.isPresent()) {
            selectedNodeData = findFirst.get();
        }
        log.debug("selectedNodeData.nodeKey: {}", selectedNodeData.getNodeKey());
    }
    
    private String toNodeKeyList() {
        if (nodeList == null || nodeList.isEmpty()) {
            return null;
        }
        return String.join(",", nodeList.stream().map(m -> m.getKey()).collect(Collectors.toList()));
    }
    
    public int percent(Integer a, Integer b) {
        if (a == null || b == null) {
            return 0;
        }
        return a * 100 / b;
    }
    
    public String nodeLink() {
        if (nodeList == null || nodeList.isEmpty()) {
            return null;
        }
        return String.join(", ", nodeList.stream().map(m -> m.getKey().substring(0, 8) + "...").collect(Collectors.toList()));        
    }
    
    public void addToGroup() {
        /*log.info("addToGroup nodeKey: {} groupTag: {}", nodeKey, groupTag);
        groupTag = nodeManager.addNodeToGroup(nodeKey, groupTag);
        nodeList = nodeManager.nodeGroup(null, groupTag, null);*/
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public Integer getMonth() {
        return month;
    }
    
    public void setMonth(Integer month) {
        this.month = month;
    }
    
    public String getNodeKey() {
        return nodeKey;
    }
    
    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }
    
    public String getGroupTag() {
        return groupTag;
    }
    
    public void setGroupTag(String groupTag) {
        this.groupTag = groupTag;
    }
    
    public List<NodeData> getNodeData() {
        return nodeData;
    }
    
    public void setNodeData(List<NodeData> nodeData) {
        this.nodeData = nodeData;
    }
    
    public NodeData getSelectedNodeData() {
        return selectedNodeData;
    }
    
    public void setSelectedNodeData(NodeData selectedNodeData) {
        this.selectedNodeData = selectedNodeData;
    }
    
    public List<Node> getNodeList() {
        return nodeList;
    }
    
    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }
    
    public String getNodeKeyList() {
        return nodeKeyList;
    }
    
    public void setNodeKeyList(String nodeKeyList) {
        this.nodeKeyList = nodeKeyList;
    }
}
