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
@Table(name = "node_statistic", indexes = {@Index(columnList = "day")})
@NamedQueries({
    @NamedQuery(name = NodeStatistic.BY_NODE_AND_DAY, query = "SELECT ns FROM NodeStatistic ns WHERE ns.node = :node AND ns.day = :day"),
    @NamedQuery(name = NodeStatistic.COUNT_GROUP_BY_DAY, query = "SELECT NEW si.skycoin.discovery.transfer.DiscoveryStatistic(ns.day, COUNT(ns)) FROM NodeStatistic ns WHERE ns.day >= :from AND ns.day < :to GROUP BY ns.day ORDER BY ns.day"),
    @NamedQuery(name = NodeStatistic.SUM_COUNT_GROUP_BY_DAY, query = "SELECT NEW si.skycoin.discovery.transfer.DiscoveryStatistic(ns.day, SUM(ns.count)) FROM NodeStatistic ns WHERE ns.node = :node AND ns.day >= :from AND ns.day < :to GROUP BY ns.day ORDER BY ns.day"),
    @NamedQuery(name = NodeStatistic.LOCATION_BY_DAY, query = "SELECT NEW si.skycoin.discovery.transfer.LocationStatistic(c.country, COUNT(ns)) FROM NodeStatistic ns JOIN ns.node n JOIN n.country c WHERE ns.day = :day GROUP BY c ORDER BY COUNT(NS) DESC"),
    //SELECT node_id, SUM(count) FROM discovery.node_statistic WHERE day > '20190401' GROUP BY node_id ORDER BY sum(count) DESC LIMIT 100;
    @NamedQuery(name = NodeStatistic.BY_UPTIME_FROM, query = "SELECT NEW si.skycoin.discovery.transfer.UptimeStatistic(ns.node, SUM(ns.count)) FROM NodeStatistic ns WHERE ns.day >= :from GROUP BY (ns.node) ORDER BY SUM(ns.count) DESC")
})
public class NodeStatistic implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static final String BY_NODE_AND_DAY = "NodeStatistic.BY_NODE_AND_DAY";
    public static final String COUNT_GROUP_BY_DAY = "NodeStatistic.COUNT_GROUP_BY_DAY";
    public static final String SUM_COUNT_GROUP_BY_DAY = "NodeStatistic.SUM_COUNT_GROUP_BY_DAY";
    public static final String LOCATION_BY_DAY = "NodeStatistic.LOCATION_BY_DAY";
    public static final String BY_UPTIME_FROM = "NodeStatistic.BY_UPTIME_FROM";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    private Node node;
    
    private Date day;
    
    private Integer count; 
    
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
    
    public Date getDay() {
        return day;
    }
    
    public void setDay(Date day) {
        this.day = day;
    }
    
    public Integer getCount() {
        return count;
    }
    
    public void setCount(Integer count) {
        this.count = count;
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
