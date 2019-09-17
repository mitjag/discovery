package si.skycoin.discovery.service;

import java.io.Serializable;
import java.util.Date;

import si.skycoin.discovery.model.DiscoveryFile;

public class DiscoveryNode implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String key;
    
    private DiscoveryFile discoveryFile;
    
    private Integer count;
    
    private Date timestampOnline;
    
    private Date timestampOffline;
    
    private String type;
    
    private Long sendBytes;
    
    private Long recvBytes;
    
    private Long lastAckTime;
    
    private Long startTime;
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public DiscoveryFile getDiscoveryFile() {
        return discoveryFile;
    }
    
    public void setDiscoveryFile(DiscoveryFile discoveryFile) {
        this.discoveryFile = discoveryFile;
    }
    
    public Integer getCount() {
        return count;
    }
    
    public void setCount(Integer count) {
        this.count = count;
    }
    
    public Date getTimestampOnline() {
        return timestampOnline;
    }
    
    public void setTimestampOnline(Date timestampOnline) {
        this.timestampOnline = timestampOnline;
    }
    
    public Date getTimestampOffline() {
        return timestampOffline;
    }
    
    public void setTimestampOffline(Date timestampOffline) {
        this.timestampOffline = timestampOffline;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Long getSendBytes() {
        return sendBytes;
    }
    
    public void setSendBytes(Long sendBytes) {
        this.sendBytes = sendBytes;
    }
    
    public Long getRecvBytes() {
        return recvBytes;
    }
    
    public void setRecvBytes(Long recvBytes) {
        this.recvBytes = recvBytes;
    }
    
    public Long getLastAckTime() {
        return lastAckTime;
    }
    
    public void setLastAckTime(Long lastAckTime) {
        this.lastAckTime = lastAckTime;
    }
    
    public Long getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
}
