package si.skycoin.discovery.transfer;

import java.io.Serializable;

public class UptimeStatisticRs implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String nodeKey;
    
    private String country;
    
    private String appKey;
    
    private Long sumCount;
    
    public UptimeStatisticRs() {
    }
    
    public UptimeStatisticRs(String nodeKey, String country, String appKey, Long sumCount) {
        this.nodeKey = nodeKey;
        this.country = country;
        this.appKey = appKey;
        this.sumCount = sumCount;
    }
    
    public String getNodeKey() {
        return nodeKey;
    }
    
    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getAppKey() {
        return appKey;
    }
    
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    
    public Long getSumCount() {
        return sumCount;
    }
    
    public void setSumCount(Long sumCount) {
        this.sumCount = sumCount;
    }
}
