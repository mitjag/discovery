package si.skycoin.discovery.transfer;

import java.io.Serializable;
import java.util.List;

public class NodeData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String nodeKey;
    private Integer nodeOnlinePercent;
    private List<NodeStatisticRs> nodeStatistic;
    
    public String getNodeKey() {
        return nodeKey;
    }
    
    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }
    
    public Integer getNodeOnlinePercent() {
        return nodeOnlinePercent;
    }
    
    public void setNodeOnlinePercent(Integer nodeOnlinePercent) {
        this.nodeOnlinePercent = nodeOnlinePercent;
    }
    
    public List<NodeStatisticRs> getNodeStatistic() {
        return nodeStatistic;
    }
    
    public void setNodeStatistic(List<NodeStatisticRs> nodeStatistic) {
        this.nodeStatistic = nodeStatistic;
    }
}
