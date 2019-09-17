package si.skycoin.discovery.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "node")
@NamedQueries({
        @NamedQuery(name = Node.BY_KEY, query = "SELECT n FROM Node n WHERE n.key = :key"),
        @NamedQuery(name = Node.BY_NODE, query = "SELECT n FROM Node n ORDER BY n.created DESC"),
        @NamedQuery(name = Node.BY_NODEGROUP_TAG, query = "SELECT n FROM Node n JOIN n.nodeGroups ng WHERE ng.tag = :tag ORDER BY n.created DESC"),
})
public class Node implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static final String BY_KEY = "Node.BY_KEY";
    public static final String BY_NODE = "Node.BY_NODE";
    public static final String BY_NODEGROUP_TAG = "Node.BY_NODEGROUP_TAG";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, name = "nodeKey")
    private String key;
    
    private Date created;
    
    private Date edited;
    
    @OneToMany(mappedBy = "node")
    private List<NodeOnline> nodeOnlines = new ArrayList<>(0);
    
    @ManyToMany(mappedBy = "nodes")
    private Set<NodeGroup> nodeGroups = new HashSet<>(0);
    
    @ManyToOne
    private NodeLocation location;
    
    @ManyToOne
    private NodeCountry country;
    
    @OneToMany(mappedBy = "node")
    private List<NodeApp> nodeApps;
    
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
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
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
    
    public List<NodeOnline> getNodeOnlines() {
        return nodeOnlines;
    }
    
    public void setNodeOnlines(List<NodeOnline> nodeOnlines) {
        this.nodeOnlines = nodeOnlines;
    }
    
    public Set<NodeGroup> getNodeGroups() {
        return nodeGroups;
    }
    
    public void setNodeGroups(Set<NodeGroup> nodeGroups) {
        this.nodeGroups = nodeGroups;
    }
    
    public NodeLocation getLocation() {
        return location;
    }
    
    public void setLocation(NodeLocation location) {
        this.location = location;
    }
    
    public NodeCountry getCountry() {
        return country;
    }
    
    public void setCountry(NodeCountry country) {
        this.country = country;
    }
    
    public List<NodeApp> getNodeApps() {
        return nodeApps;
    }
    
    public void setNodeApps(List<NodeApp> nodeApps) {
        this.nodeApps = nodeApps;
    }
}
