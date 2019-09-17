package si.skycoin.discovery.data;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import si.skycoin.discovery.model.EPropertyKey;
import si.skycoin.discovery.model.Property;

@Stateless
public class PropertyAccess {
    
    @Inject
    private EntityManager em;
    
    public String getProperty(EPropertyKey key, String defaultValue) {
        try {
            return em.createNamedQuery(Property.BY_KEY, Property.class)
                    .setParameter("key", key)
                    .getSingleResult()
                    .getValue();
        } catch (NoResultException ex) {
            return defaultValue;
        }
    }
}
