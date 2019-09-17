package si.skycoin.discovery.transfer;

import java.io.Serializable;
import java.util.Date;

public class TimeChart implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Date x;
    
    private Long y;
    
    public TimeChart() {
    }
    
    public TimeChart(Date x, Long y) {
        this.x = x;
        this.y = y;
    }
    
    public Date getX() {
        return x;
    }
    
    public void setX(Date x) {
        this.x = x;
    }
    
    public Long getY() {
        return y;
    }
    
    public void setY(Long y) {
        this.y = y;
    }
}
