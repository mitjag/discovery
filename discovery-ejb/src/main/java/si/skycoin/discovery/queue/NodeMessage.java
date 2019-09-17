package si.skycoin.discovery.queue;

import java.io.Serializable;

import si.skycoin.discovery.model.DiscoveryFile;
import si.skycoin.discovery.service.Discovery;
import si.skycoin.discovery.service.DiscoveryNodeResult;
import si.skycoin.discovery.service.DiscoveryNode;

public class NodeMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private ENodeMessageType messageType;
    
    private DiscoveryFile discoveryFile;
    
    private Discovery discovery;
    
    private DiscoveryNode discoveryNode;
    
    private DiscoveryNodeResult discoveryNodeResult;
    
    public ENodeMessageType getMessageType() {
        return messageType;
    }
    
    public void setMessageType(ENodeMessageType messageType) {
        this.messageType = messageType;
    }
    
    public DiscoveryFile getDiscoveryFile() {
        return discoveryFile;
    }
    
    public void setDiscoveryFile(DiscoveryFile discoveryFile) {
        this.discoveryFile = discoveryFile;
    }
    
    public Discovery getDiscovery() {
        return discovery;
    }
    
    public void setDiscovery(Discovery discovery) {
        this.discovery = discovery;
    }
    
    public DiscoveryNode getDiscoveryNode() {
        return discoveryNode;
    }
    
    public void setDiscoveryNode(DiscoveryNode discoveryNode) {
        this.discoveryNode = discoveryNode;
    }
    
    public DiscoveryNodeResult getDiscoveryNodeResult() {
        return discoveryNodeResult;
    }
    
    public void setDiscoveryNodeLocation(DiscoveryNodeResult discoveryNodeResult) {
        this.discoveryNodeResult = discoveryNodeResult;
    }
}
