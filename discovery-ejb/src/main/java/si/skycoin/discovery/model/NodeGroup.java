package si.skycoin.discovery.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "node_group")
@NamedQueries({
    @NamedQuery(name = NodeGroup.BY_TAG, query = "SELECT ng FROM NodeGroup ng WHERE ng.tag = :tag")
    
})
public class NodeGroup implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static final String BY_TAG = "NodeGroup.BY_TAG";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String tag;
    
    private String name;
    
    @ManyToMany
    @JoinTable(name = "node_group_has_node")
    private Set<Node> nodes = new HashSet<>(0);
    
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
    
    public String getTag() {
        return tag;
    }
    
    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Set<Node> getNodes() {
        return nodes;
    }
    
    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
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
