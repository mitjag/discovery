package si.skycoin.discovery.service;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NodeSearchServicesResult implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @JsonProperty("node_key")
    private String nodeKey;
    
    @JsonProperty("app_key")
    private String appKey;
    
    @JsonProperty("location")
    private String location;
    
    @JsonProperty("version")
    private String version;
    
    @JsonProperty("node_version")
    private List<String> nodeVersion;
    
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
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public List<String> getNodeVersion() {
        return nodeVersion;
    }
    
    public void setNodeVersion(List<String> nodeVersion) {
        this.nodeVersion = nodeVersion;
    }
}
