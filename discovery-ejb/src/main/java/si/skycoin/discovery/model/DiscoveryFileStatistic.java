package si.skycoin.discovery.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "discovery_file_statistic", indexes = {@Index(columnList = "day")})
@NamedQueries({
    @NamedQuery(name = DiscoveryFileStatistic.BY_DAY, query = "SELECT dfs FROM DiscoveryFileStatistic dfs WHERE dfs.day = :day"),
    @NamedQuery(name = DiscoveryFileStatistic.FROM_TO_DAY, query = "SELECT dfs FROM DiscoveryFileStatistic dfs WHERE dfs.day >= :from AND dfs.day < :to ORDER BY dfs.day")
})
public class DiscoveryFileStatistic implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static final String BY_DAY = "DiscoveryFileStatistic.BY_DAY";
    public static final String FROM_TO_DAY = "DiscoveryFileStatistic.FROM_TO_DAY";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Date day;
    
    private Integer count;
    
    private Date created;
    
    private Date edited;
    
    @OneToMany(mappedBy = "discoveryFileStatistic")
    private List<DiscoveryFile> discoveryFile = new ArrayList<>(0);
    
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
    
    public List<DiscoveryFile> getDiscoveryFile() {
        return discoveryFile;
    }
    
    public void setDiscoveryFile(List<DiscoveryFile> discoveryFile) {
        this.discoveryFile = discoveryFile;
    }
}
