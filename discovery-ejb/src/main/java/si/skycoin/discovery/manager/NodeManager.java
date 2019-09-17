package si.skycoin.discovery.manager;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import si.skycoin.discovery.data.NodeAccess;
import si.skycoin.discovery.model.Node;
import si.skycoin.discovery.model.NodeGroup;

@Stateless
public class NodeManager {
    
    @Inject
    private Logger log;
    
    @EJB
    private NodeAccess nodeAccess;
    
    public String addNodeToGroup(String nodeKey, String groupTag) {
        Node node = nodeAccess.findNode(nodeKey);
        if (node == null) {
            log.debug("addNodeToGroup node not found nodeKey: {}", nodeKey);
            return null;
        }
        NodeGroup nodeGroup = nodeAccess.findNodeGroup(groupTag);
        if (groupTag == null) {
            nodeGroup = nodeAccess.createNodeGroup();
        }
        nodeGroup.getNodes().add(node);
        return nodeGroup.getTag();
    }
    
    public List<Node> nodeGroup(String nodeKey, String groupTag) {
        List<Node> nodes = new ArrayList<>();
        if (groupTag != null && !groupTag.isEmpty()) {
            NodeGroup nodeGroup = nodeAccess.findNodeGroup(groupTag.trim());
            nodes.addAll(nodeGroup.getNodes());
        } else if (nodeKey != null && !nodeKey.isEmpty()) {
            String[] keys = nodeKey.split(",");
            if (keys != null) {
                int count = 0;
                for (String key : keys) {
                    Node node = nodeAccess.findNode(key.trim());
                    if (node != null && count < 8) {
                        nodes.add(node);
                        count++;
                    }
                }
            }
        }
        return nodes;
    }
}
