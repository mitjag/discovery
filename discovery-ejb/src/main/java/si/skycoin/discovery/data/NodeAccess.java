package si.skycoin.discovery.data;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.commons.lang3.RandomStringUtils;

import si.skycoin.discovery.model.DiscoveryFile;
import si.skycoin.discovery.model.DiscoveryFileStatistic;
import si.skycoin.discovery.model.Node;
import si.skycoin.discovery.model.NodeApp;
import si.skycoin.discovery.model.NodeCountry;
import si.skycoin.discovery.model.NodeGroup;
import si.skycoin.discovery.model.NodeLocation;
import si.skycoin.discovery.model.NodeOnline;
import si.skycoin.discovery.service.Discovery;
import si.skycoin.discovery.service.DiscoveryNode;

@Stateless
public class NodeAccess {
    
    @Inject
    private EntityManager em;
    
    @EJB
    private StatisticAccess statisticAccess;
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveNode(DiscoveryNode discoveryNode) {
        Node node = findOrCreateNode(discoveryNode.getKey());
        
        NodeOnline nodeOnline = new NodeOnline();
        nodeOnline.setNode(node);
        nodeOnline.setDiscoveryFile(discoveryNode.getDiscoveryFile());
        nodeOnline.setDiscoveryCount(discoveryNode.getCount());
        nodeOnline.setTimestampOnline(discoveryNode.getTimestampOnline());
        nodeOnline.setTimestampOffline(discoveryNode.getTimestampOffline());
        nodeOnline.setType(discoveryNode.getType());
        nodeOnline.setSendBytes(discoveryNode.getSendBytes());
        nodeOnline.setRecvBytes(discoveryNode.getRecvBytes());
        nodeOnline.setLastAckTime(discoveryNode.getLastAckTime());
        nodeOnline.setStartTime(discoveryNode.getStartTime());
        em.persist(nodeOnline);
        
        statisticAccess.updateNodeStatistic(node, discoveryNode.getTimestampOnline(), discoveryNode.getCount());
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void updateNode(DiscoveryFile discoveryFile, Discovery discovery) {
        Node node = findOrCreateNode(discovery.getKey());
        NodeOnline nodeOnline = findNodeOnlineLast(discoveryFile, node);
        if (nodeOnline != null) {
            if (nodeOnline.getStartTime() < discovery.getStartTime()) { // check if node rebooted
                nodeOnline.setDiscoveryCount(nodeOnline.getDiscoveryCount() + 1);
                nodeOnline.setTimestampOffline(discoveryFile.getTimestamp());
                nodeOnline.setType(discovery.getType());
                nodeOnline.setSendBytes(discovery.getSendBytes());
                nodeOnline.setRecvBytes(discovery.getRecvBytes());
                nodeOnline.setLastAckTime(discovery.getLastAckTime());
                nodeOnline.setStartTime(discovery.getStartTime());
            } else {
                nodeOnline = null;
            }
        }
        
        if (nodeOnline == null) {
            nodeOnline = new NodeOnline();
            nodeOnline.setNode(node);
            nodeOnline.setDiscoveryFile(discoveryFile);
            nodeOnline.setDiscoveryCount(1);
            nodeOnline.setTimestampOnline(discoveryFile.getTimestamp());
            nodeOnline.setTimestampOffline(discoveryFile.getTimestamp());
            nodeOnline.setType(discovery.getType());
            nodeOnline.setSendBytes(discovery.getSendBytes());
            nodeOnline.setRecvBytes(discovery.getRecvBytes());
            nodeOnline.setLastAckTime(discovery.getLastAckTime());
            nodeOnline.setStartTime(discovery.getStartTime());
            em.persist(nodeOnline);
        }
        
        statisticAccess.updateNodeStatistic(node, discoveryFile.getTimestamp(), 1);
    }
    
    public Node findOrCreateNode(String key) {
        Node node = findNode(key);
        if (node == null) {
            node = new Node();
            node.setKey(key);
            em.persist(node);
        }
        return node;
    }
    
    public Node findNode(String key) {
        try {
            return em.createNamedQuery(Node.BY_KEY, Node.class)
                    .setParameter("key", key)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    public NodeOnline findNodeOnlineLast(DiscoveryFile discoveryFile, Node node) {
        try {
            LocalDate localDate = discoveryFile.getTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Date from = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            Date to = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            
            return em.createNamedQuery(NodeOnline.BY_DATE_AND_OFFLINE, NodeOnline.class)
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .setParameter("node", node)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    public DiscoveryFile findDiscoveryFile(String filename) {
        try {
            return em.createNamedQuery(DiscoveryFile.BY_FILENAME, DiscoveryFile.class)
                    .setParameter("filename", filename)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveDiscoveryFile(DiscoveryFile discoveryFile) {
        em.persist(discoveryFile);
        DiscoveryFileStatistic discoveryFileStatistic = statisticAccess.updateDiscoveryFileStatistics(discoveryFile.getTimestamp(), 1);
        discoveryFile.setDiscoveryFileStatistic(discoveryFileStatistic);
    }
    
    public Integer countDiscoveryFile(Date from, Date to) {
        return em.createNamedQuery(DiscoveryFile.COUNT_FROM_TO, Integer.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();
    }
    
    public Integer countNode(Node node, Date from, Date to) {
        return em.createNamedQuery(DiscoveryFile.COUNT_FROM_TO, Integer.class)
                .setParameter("from", from)
                .setParameter("to", to).getSingleResult();
    }
    
    public List<Node> nodes() {
        return em.createNamedQuery(Node.BY_NODE, Node.class).setMaxResults(10).getResultList();
    }
    
    public List<Node> nodeGroup(String tag) {
        return em.createNamedQuery(Node.BY_NODEGROUP_TAG, Node.class).setParameter("tag", tag).getResultList();
    }
    
    public NodeGroup findNodeGroup(String tag) {
        try {
            return em.createNamedQuery(NodeGroup.BY_TAG, NodeGroup.class).setParameter("tag", tag).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    public NodeGroup createNodeGroup() {
        NodeGroup nodeGroup = new NodeGroup();
        nodeGroup.setTag(RandomStringUtils.randomAlphanumeric(24));
        em.persist(nodeGroup);
        return nodeGroup;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public NodeCountry findOrCreateNodeCountry(String country) {
        try {
            return em.createNamedQuery(NodeCountry.BY_COUNTRY, NodeCountry.class)
                    .setParameter("country", country)
                    .getSingleResult();
        } catch (NoResultException ex) {
            NodeCountry nodeCountry = new NodeCountry();
            nodeCountry.setCountry(country);
            em.persist(nodeCountry);
            return nodeCountry;
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public NodeLocation findOrCreateNodeLocation(String location, NodeCountry country) {
        try {
            return em.createNamedQuery(NodeLocation.BY_LOCATION, NodeLocation.class)
                    .setParameter("location", location)
                    .setParameter("country", country)
                    .getSingleResult();
        } catch (NoResultException ex) {
            NodeLocation nodeLocation = new NodeLocation();
            nodeLocation.setLocation(location);
            nodeLocation.setCountry(country);
            em.persist(nodeLocation);
            return nodeLocation;
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public NodeApp updateOrCreateNodeApp(Node node, String type, String appKey) {
        try {
            NodeApp nodeApp = em.createNamedQuery(NodeApp.BY_NODE_AND_TYPE, NodeApp.class)
                    .setParameter("node", node)
                    .setParameter("type", type)
                    .getSingleResult();
            
            nodeApp.setAppKey(appKey);
            return nodeApp;
        } catch (NoResultException ex) {
            NodeApp nodeApp = new NodeApp();
            nodeApp.setNode(node);
            nodeApp.setType(type);
            nodeApp.setAppKey(appKey);
            em.persist(nodeApp);
            return nodeApp;
        }
    }
}
