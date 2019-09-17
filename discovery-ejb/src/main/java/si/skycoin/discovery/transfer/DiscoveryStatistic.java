package si.skycoin.discovery.transfer;

import java.io.Serializable;
import java.util.Date;

public class DiscoveryStatistic implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Date day;
    
    private Long nodeCount;
    
    public DiscoveryStatistic() {
    }
    
    public DiscoveryStatistic(Date day, Long nodeCount) {
        this.day = day;
        this.nodeCount = nodeCount;
    }
    
    public Date getDay() {
        return day;
    }
    
    public void setDay(Date day) {
        this.day = day;
    }
    
    public Long getNodeCount() {
        return nodeCount;
    }
    
    public void setNodeCount(Long nodeCount) {
        this.nodeCount = nodeCount;
    }
}
