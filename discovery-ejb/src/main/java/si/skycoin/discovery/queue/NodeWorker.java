package si.skycoin.discovery.queue;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import si.skycoin.discovery.data.NodeAccess;
import si.skycoin.discovery.model.DiscoveryFile;
import si.skycoin.discovery.model.Node;
import si.skycoin.discovery.model.NodeApp;
import si.skycoin.discovery.model.NodeCountry;
import si.skycoin.discovery.model.NodeLocation;
import si.skycoin.discovery.service.Discovery;
import si.skycoin.discovery.service.DiscoveryNode;
import si.skycoin.discovery.service.DiscoveryNodeResult;

@Stateless
public class NodeWorker {
    
    @Inject
    private Logger log;
    
    @EJB
    private NodeAccess nodeAccess;
    
    public void work(DiscoveryFile discoveryFile, Discovery discovery) {
        nodeAccess.updateNode(discoveryFile, discovery);
    }
    
    public void work(DiscoveryNode discoveryNode) {
        nodeAccess.saveNode(discoveryNode);
    }
    
    public void location(DiscoveryNodeResult discoveryNodeResult) {
        Node node = nodeAccess.findOrCreateNode(discoveryNodeResult.getNodeKey());
        NodeApp nodeApp = nodeAccess.updateOrCreateNodeApp(node, "sockss", discoveryNodeResult.getAppKey());
        log.info("Node appKey node.key: {} nodeApp.id: {} nodeApp.appKey: {}", node.getKey(), nodeApp.getId(), nodeApp.getAppKey());
        NodeCountry country = nodeAccess.findOrCreateNodeCountry(discoveryNodeResult.getCountry());
        NodeLocation location = nodeAccess.findOrCreateNodeLocation(discoveryNodeResult.getLocation(), country);
        if (node.getCountry() != null) {
            if (!node.getCountry().getId().equals(country.getId())) {
                log.info("Update country node.key: {} node.country.id: {} node.country.country: {} country.id: {} country.country: {}",
                        node.getKey(), node.getCountry().getId(), node.getCountry().getCountry(), country.getId(), country.getCountry());
                node.setCountry(country);
            }
        } else {
            log.info("New country node.key: {} country.id: {} country.country: {}", node.getKey(), country.getId(), country.getCountry());
            node.setCountry(country);
        }
        if (node.getLocation() != null) {
            if (!node.getLocation().getId().equals(location.getId())) {
                log.info("Update location node.key: {} node.location.id: {} node.location.location: {} location.id: {} location.location: {}",
                        node.getKey(), node.getLocation().getId(), node.getLocation().getLocation(), location.getId(), location.getLocation());
                node.setLocation(location);
            }
        } else {
            log.info("New location node.key: {} location.id: {} location.location: {}", node.getKey(), location.getId(), location.getLocation());
            node.setLocation(location);
        }
    }
}
