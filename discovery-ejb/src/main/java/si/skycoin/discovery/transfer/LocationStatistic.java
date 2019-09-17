package si.skycoin.discovery.transfer;

import java.io.Serializable;

public class LocationStatistic implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String country;
    
    private Long nodeCount;
    
    public LocationStatistic() {
    }
    
    public LocationStatistic(String country, Long nodeCount) {
        this.country = country;
        this.nodeCount = nodeCount;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public Long getNodeCount() {
        return nodeCount;
    }
    
    public void setNodeCount(Long nodeCount) {
        this.nodeCount = nodeCount;
    }
}
