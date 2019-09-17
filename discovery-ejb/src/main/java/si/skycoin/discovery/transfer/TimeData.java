package si.skycoin.discovery.transfer;

import java.io.Serializable;
import java.util.List;

public class TimeData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String nodeKey;
    private List<TimeChart> timeChart;
    
    public String getNodeKey() {
        return nodeKey;
    }
    
    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }
    
    public List<TimeChart> getTimeChart() {
        return timeChart;
    }
    
    public void setTimeChart(List<TimeChart> timeChart) {
        this.timeChart = timeChart;
    }
}
