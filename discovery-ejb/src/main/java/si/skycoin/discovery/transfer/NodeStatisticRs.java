package si.skycoin.discovery.transfer;

import java.io.Serializable;
import java.util.Date;

public class NodeStatisticRs implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Date day;
    
    private Integer nodeCount;
    
    private Integer fileCount;
    
    public NodeStatisticRs() {
    }
    
    public NodeStatisticRs(Date day, Integer nodeCount, Integer fileCount) {
        this.day = day;
        this.nodeCount = nodeCount;
        this.fileCount = fileCount;
    }
    
    public Date getDay() {
        return day;
    }
    
    public void setDay(Date day) {
        this.day = day;
    }
    
    public Integer getNodeCount() {
        return nodeCount;
    }
    
    public void setNodeCount(Integer nodeCount) {
        this.nodeCount = nodeCount;
    }
    
    public Integer getFileCount() {
        return fileCount;
    }
    
    public void setFileCount(Integer fileCount) {
        this.fileCount = fileCount;
    }
}
