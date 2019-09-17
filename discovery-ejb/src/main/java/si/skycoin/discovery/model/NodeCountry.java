package si.skycoin.discovery.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "node_country")
@NamedQueries({
        @NamedQuery(name = NodeCountry.BY_COUNTRY, query = "SELECT nc FROM NodeCountry nc WHERE nc.country = :country")
})
public class NodeCountry implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static final String BY_COUNTRY = "NodeCountry.BY_COUNTRY";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String country;
    
    private Date created;
    
    private Date edited;
    
    @OneToMany(mappedBy = "country")
    private List<Node> nodes = new ArrayList<>(0);
    
    @OneToMany(mappedBy = "country")
    private List<NodeLocation> nodeLocations = new ArrayList<>(0);
    
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
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
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
    
    public List<Node> getNodes() {
        return nodes;
    }
    
    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
    
    public List<NodeLocation> getNodeLocations() {
        return nodeLocations;
    }
    
    public void setNodeLocations(List<NodeLocation> nodeLocations) {
        this.nodeLocations = nodeLocations;
    }
}
