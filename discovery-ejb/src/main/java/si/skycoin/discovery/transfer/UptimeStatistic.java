package si.skycoin.discovery.transfer;

import java.io.Serializable;

import si.skycoin.discovery.model.Node;

public class UptimeStatistic implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Node node;
    
    private Long sumCount;
    
    private String appKey;
    
    public UptimeStatistic() {
    }
    
    public UptimeStatistic(Node node, Long sumCount) {
        this.node = node;
        this.sumCount = sumCount;
    }
    
    public Node getNode() {
        return node;
    }
    
    public void setNode(Node node) {
        this.node = node;
    }
    
    public Long getSumCount() {
        return sumCount;
    }
    
    public void setSumCount(Long sumCount) {
        this.sumCount = sumCount;
    }
    
    public String getAppKey() {
        return appKey;
    }
    
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
