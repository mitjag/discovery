package si.skycoin.discovery.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jboss.ejb3.annotation.TransactionTimeout;
import org.slf4j.Logger;

import com.mashape.unirest.http.exceptions.UnirestException;

import si.skycoin.discovery.data.PropertyAccess;
import si.skycoin.discovery.model.EPropertyKey;
import si.skycoin.discovery.queue.NodeProducer;

@Stateless
@TransactionTimeout(value = 60, unit = TimeUnit.MINUTES)
public class SkycoinLocation {
    
    @Inject
    private Logger log;
    
    @EJB
    private PropertyAccess propertyAccess;
    
    @EJB
    private NodeProducer nodeProducer;
    
    @Asynchronous
    public void location() {
        SkycoinDiscoveryRest rest = new SkycoinDiscoveryRest();
        rest.setUrl(propertyAccess.getProperty(EPropertyKey.REST_NODE_API_URL, "http://127.0.0.1"));
        rest.setDiscoveryKey(propertyAccess.getProperty(EPropertyKey.DISCOVERY_KEY, ""));
        try {
            rest.token();
            List<NodeSearchServicesResult> list = rest.allNodes();
            log.info("location list.size: " + list.size());
            for (NodeSearchServicesResult n : list) {
                String[] split = n.getLocation().split(",");
                if (split == null) {
                    continue;
                }
                DiscoveryNodeResult discoveryNodeResult = new DiscoveryNodeResult();
                discoveryNodeResult.setNodeKey(n.getNodeKey());
                discoveryNodeResult.setAppKey(n.getAppKey());
                discoveryNodeResult.setCountry(split.length == 1 ? split[0].trim() : split[split.length - 1].trim());
                discoveryNodeResult.setLocation(split.length == 1 ? "" : split[0].trim());
                nodeProducer.location(discoveryNodeResult);
            }
        } catch (UnirestException | InterruptedException ex) {
            log.error("location ex: {}", ex.getMessage(), ex);
        }
    }
}
