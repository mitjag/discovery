package si.skycoin.discovery.service;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Discovery implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @JsonProperty("key")
    private String key;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("send_bytes")
    private Long sendBytes;
    
    @JsonProperty("recv_bytes")
    private Long recvBytes;
    
    @JsonProperty("last_ack_time")
    private Long lastAckTime;
    
    @JsonProperty("start_time")
    private Long startTime;
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
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
