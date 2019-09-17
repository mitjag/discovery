package si.skycoin.discovery.service;

import java.io.Serializable;

public class DiscoveryNodeResult implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String nodeKey;
    
    private String appKey;
    
    private String country;
    
    private String location;
    
    public String getNodeKey() {
        return nodeKey;
    }
    
    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }
    
    public String getAppKey() {
        return appKey;
    }
    
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
}
