package si.skycoin.discovery.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "node_app")
@NamedQueries({
    @NamedQuery(name = NodeApp.BY_NODE_AND_TYPE, query = "SELECT na FROM NodeApp na WHERE na.node = :node AND na.type = :type"),
    @NamedQuery(name = NodeApp.BY_NODE_AND_TYPE_AND_APPKEY, query = "SELECT na FROM NodeApp na WHERE na.node = :node AND na.type = :type AND na.appKey = :appKey")
})
public class NodeApp implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static final String BY_NODE_AND_TYPE = "NodeApp.BY_NODE_AND_TYPE";
    public static final String BY_NODE_AND_TYPE_AND_APPKEY = "NodeApp.BY_NODE_AND_TYPE_AND_APPKEY";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Node node;
    
    private String type;
    
    private String appKey;
    
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getAppKey() {
        return appKey;
    }
    
    public void setAppKey(String appKey) {
        this.appKey = appKey;
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
