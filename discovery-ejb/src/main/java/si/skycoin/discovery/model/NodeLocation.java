package si.skycoin.discovery.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "node_location", uniqueConstraints = { @UniqueConstraint(columnNames = { "country_id", "location"  }) })
@NamedQueries({
        @NamedQuery(name = NodeLocation.BY_LOCATION, query = "SELECT nl FROM NodeLocation nl WHERE nl.location = :location AND nl.country = :country")
})
public class NodeLocation implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static final String BY_LOCATION = "NodeLocation.BY_LOCATION";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String location;
    
    @ManyToOne
    private NodeCountry country;
    
    private Date created;
    
    private Date edited;
    
    @OneToMany(mappedBy = "location")
    private List<Node> nodes = new ArrayList<>(0);
    
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
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public NodeCountry getCountry() {
        return country;
    }
    
    public void setCountry(NodeCountry country) {
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
}
