package si.skycoin.discovery.service;

import java.io.Serializable;
import java.util.List;

public class NodeSearchServices implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private List<NodeSearchServicesResult> result;
    
    private int seq;
    
    private int count;
    
    public List<NodeSearchServicesResult> getResult() {
        return result;
    }
    
    public void setResult(List<NodeSearchServicesResult> result) {
        this.result = result;
    }
    
    public int getSeq() {
        return seq;
    }
    
    public void setSeq(int seq) {
        this.seq = seq;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
}
