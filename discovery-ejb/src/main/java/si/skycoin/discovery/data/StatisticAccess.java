package si.skycoin.discovery.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import si.skycoin.discovery.model.DiscoveryFileStatistic;
import si.skycoin.discovery.model.Node;
import si.skycoin.discovery.model.NodeStatistic;
import si.skycoin.discovery.transfer.DiscoveryStatistic;
import si.skycoin.discovery.transfer.LocationStatistic;
import si.skycoin.discovery.transfer.NodeData;
import si.skycoin.discovery.transfer.NodeStatisticRs;
import si.skycoin.discovery.transfer.UptimeStatistic;

@Stateless
public class StatisticAccess {
    
    @Inject
    private EntityManager em;
    
    @EJB
    private NodeAccess nodeAccess;
    
    public DiscoveryFileStatistic findOrNew(Date day) {
        try {
            return em.createNamedQuery(DiscoveryFileStatistic.BY_DAY, DiscoveryFileStatistic.class).setParameter("day", day).getSingleResult();
        } catch (NoResultException ex) {
            DiscoveryFileStatistic discoveryFileStatistic = new DiscoveryFileStatistic();
            discoveryFileStatistic.setDay(day);
            discoveryFileStatistic.setCount(0);
            em.persist(discoveryFileStatistic);
            return discoveryFileStatistic;
        }
    }
    
    public void updateDiscoveryFileStatistics(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date from = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date to = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        
        Integer count = nodeAccess.countDiscoveryFile(from, to);
        DiscoveryFileStatistic discoveryFileStatistic = findOrNew(from);
        discoveryFileStatistic.setCount(count);
    }
    
    public DiscoveryFileStatistic updateDiscoveryFileStatistics(Date date, Integer count) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date day = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        
        DiscoveryFileStatistic discoveryFileStatistic = findOrNew(day);
        discoveryFileStatistic.setCount(discoveryFileStatistic.getCount() + count);
        return discoveryFileStatistic;
    }
    
    public NodeStatistic findOrNew(Node node, Date day) {
        try {
            return em.createNamedQuery(NodeStatistic.BY_NODE_AND_DAY, NodeStatistic.class)
                    .setParameter("node", node)
                    .setParameter("day", day).getSingleResult();
        } catch (NoResultException ex) {
            NodeStatistic nodeStatistic = new NodeStatistic();
            nodeStatistic.setNode(node);
            nodeStatistic.setDay(day);
            nodeStatistic.setCount(0);
            em.persist(nodeStatistic);
            return nodeStatistic;
        }
    }
    
    public void updateNodeStatistic(Node node, Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date from = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date to = Date.from(localDate.plusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        
        Integer count = nodeAccess.countNode(node, from, to);
        NodeStatistic nodeStatistic = findOrNew(node, from);
        nodeStatistic.setCount(count);
    }
    
    public void updateNodeStatistic(Node node, Date online, Integer count) {
        LocalDate localDate = online.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date day = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        NodeStatistic nodeStatistic = findOrNew(node, day);
        nodeStatistic.setCount(nodeStatistic.getCount() + count);
    }
    
    public List<DiscoveryStatistic> discoveryNodesGroupByDay(Date from, Date to) {
        return em.createNamedQuery(NodeStatistic.COUNT_GROUP_BY_DAY, DiscoveryStatistic.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }
    
    public List<DiscoveryStatistic> discoveryNodesGroupByDayForMonth(Date day, int months) {
        LocalDateTime localDateTime = day.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay();
        Date to = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date from;
        if (months == 0) {
            from = Date.from(LocalDateTime.of(2018, 6, 1, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
        } else {
            from = Date.from(localDateTime.minusMonths(months).atZone(ZoneId.systemDefault()).toInstant());
        }
        
        return discoveryNodesGroupByDay(from, to);
    }
    
    public List<DiscoveryStatistic> nodeSumCountGroupByDay(Node node, Date from, Date to) {
        return em.createNamedQuery(NodeStatistic.SUM_COUNT_GROUP_BY_DAY, DiscoveryStatistic.class)
                .setParameter("node", node)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }
    
    public List<DiscoveryStatistic> nodeSumCountGroupByDayForMonth(Node node, Date day) {
        LocalDateTime localDateTime = day.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay().withDayOfMonth(1);
        Date from = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date to = Date.from(localDateTime.plusMonths(1).atZone(ZoneId.systemDefault()).toInstant());

        return nodeSumCountGroupByDay(node, from, to);
    }
    
    public List<DiscoveryFileStatistic> discoveryFileStatistic(Date from, Date to) {
        return em.createNamedQuery(DiscoveryFileStatistic.FROM_TO_DAY, DiscoveryFileStatistic.class)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }
    
    public NodeData nodeStatistic(Node node, int year, int month) {
        LocalDateTime localDateTime = LocalDateTime.of(year, month, 1, 0, 0);
        Date from = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date to = Date.from(localDateTime.plusMonths(1).atZone(ZoneId.systemDefault()).toInstant());
        
        List<DiscoveryStatistic> nodes = nodeSumCountGroupByDay(node, from, to);
        List<DiscoveryFileStatistic> files = discoveryFileStatistic(from, to);
        
        NodeData nodeData = new NodeData();
        nodeData.setNodeKey(node.getKey());
        nodeData.setNodeStatistic(new ArrayList<>());
        int i = 0;
        int nodeCount = 0;
        int fileCount = 0;        
        for(LocalDateTime ldt = localDateTime; ldt.isBefore(localDateTime.plusMonths(1)); ldt = ldt.plusDays(1)) {
            Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
            
            Optional<DiscoveryStatistic> ds = nodes.stream().filter(d -> d.getDay().equals(date)).findFirst();
            Optional<DiscoveryFileStatistic> dfs = files.stream().filter(d -> d.getDay().equals(date)).findFirst();
            if (ds.isPresent() && dfs.isPresent()) {
                nodeData.getNodeStatistic().add(new NodeStatisticRs(ds.get().getDay(), ds.get().getNodeCount().intValue(), dfs.get().getCount()));
                nodeCount += ds.get().getNodeCount().intValue();
                fileCount += dfs.get().getCount();
            }
        }
        nodeData.setNodeOnlinePercent(fileCount != 0 ? nodeCount * 100 / fileCount : 0);
        
        return nodeData;
    }
    
    public List<LocationStatistic> locationByDay(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date day = Date.from(localDate.atStartOfDay().minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return em.createNamedQuery(NodeStatistic.LOCATION_BY_DAY, LocationStatistic.class)
                .setParameter("day", day)
                .getResultList();
    }
    
    public List<UptimeStatistic> uptimeStatistic(int maxResult) {
        Date from = Date.from(LocalDate.now().atStartOfDay().minusMonths(1).atZone(ZoneId.systemDefault()).toInstant());
        return em.createNamedQuery(NodeStatistic.BY_UPTIME_FROM, UptimeStatistic.class)
                .setParameter("from", from)
                .setMaxResults(maxResult)
                .getResultList();
    }
    
    public List<UptimeStatistic> uptimeStatistic(Date from, int maxResult) {
        return em.createNamedQuery(NodeStatistic.BY_UPTIME_FROM, UptimeStatistic.class)
                .setParameter("from", from)
                .setMaxResults(maxResult)
                .getResultList();
    }
}
