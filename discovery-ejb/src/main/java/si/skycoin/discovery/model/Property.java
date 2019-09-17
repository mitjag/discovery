package si.skycoin.discovery.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "property")
@NamedQueries({
        @NamedQuery(name = Property.BY_KEY, query = "SELECT p FROM Property p WHERE p.key = :key")
})
public class Property implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public static final String BY_KEY = "Property.BY_KEY";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "propertyKey", unique = true)
    @Enumerated(EnumType.STRING)
    private EPropertyKey key;
    
    @Column(name = "propertyValue")
    private String value;
    
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
    
    public EPropertyKey getKey() {
        return key;
    }
    
    public void setKey(EPropertyKey key) {
        this.key = key;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
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
