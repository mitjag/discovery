package si.skycoin.discovery.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "node_online", indexes = {
        @Index(columnList = "timestampOnline"),
        @Index(columnList = "timestampOffline")
})
@NamedQueries({
    @NamedQuery(name = NodeOnline.BY_DATE_AND_OFFLINE, query = "SELECT no FROM NodeOnline no WHERE no.discoveryFile.timestamp >= :from AND no.discoveryFile.timestamp < :to AND no.node = :node ORDER BY no.timestampOffline DESC")
})
public class NodeOnline implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static final String BY_DATE_AND_OFFLINE = "NodeOnline.BY_OFFLINE";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    private Node node;
    
    @ManyToOne
    private DiscoveryFile discoveryFile;
    
    private Integer discoveryCount;
    
    private Date timestampOnline;
    
    private Date timestampOffline;
    
    private String type;
    
    private Long sendBytes;
    
    private Long recvBytes;
    
    private Long lastAckTime;
    
    private Long startTime;
    
    private Date created;
    
    private Date edited;
    
    @PrePersist
    public void prePersist() {
        created = new Date();
        edited = new Date();
    }
    
    @PreUpdate
    public void preUpdate() {
        edited = new Date();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Node getNode() {
        return node;
    }
    
    public void setNode(Node node) {
        this.node = node;
    }
    
    public DiscoveryFile getDiscoveryFile() {
        return discoveryFile;
    }
    
    public void setDiscoveryFile(DiscoveryFile discoveryFile) {
        this.discoveryFile = discoveryFile;
    }
    
    public Integer getDiscoveryCount() {
        return discoveryCount;
    }
    
    public void setDiscoveryCount(Integer discoveryCount) {
        this.discoveryCount = discoveryCount;
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
    
    public Date getCreated() {
        return created;
    }
    
    public void setCreated(Date created) {
        this.created = created;
    }
    
    public Date getEdited() {
        return edited;
    }
    
    public void setEdited(Date edited) {
        this.edited = edited;
    }
}
