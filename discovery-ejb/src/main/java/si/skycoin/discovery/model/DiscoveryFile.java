package si.skycoin.discovery.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
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
@Table(name = "discovery_file")
@NamedQueries({
        @NamedQuery(name = DiscoveryFile.BY_FILENAME, query = "SELECT df FROM DiscoveryFile df WHERE df.filename = :filename"),
        @NamedQuery(name = DiscoveryFile.COUNT_FROM_TO, query = "SELECT COUNT(df) FROM DiscoveryFile df WHERE df.timestamp >= :from AND df.timestamp < :to")
})
public class DiscoveryFile implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static final String BY_FILENAME = "DiscoveryFile.BY_FILENAME";
    public static final String COUNT_FROM_TO = "DiscoveryFile.COUNT_FROM_TO";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String filename;
    
    private Date timestamp;
    
    private Integer count;
    
    private Date created;
    
    private Date edited;
    
    @ManyToOne
    private DiscoveryFileStatistic discoveryFileStatistic;
    
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
    
    public String getFilename() {
        return filename;
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

    public DiscoveryFileStatistic getDiscoveryFileStatistic() {
        return discoveryFileStatistic;
    }

    public void setDiscoveryFileStatistic(DiscoveryFileStatistic discoveryFileStatistic) {
        this.discoveryFileStatistic = discoveryFileStatistic;
    }
}
