package si.skycoin.discovery.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import si.skycoin.discovery.data.NodeAccess;
import si.skycoin.discovery.data.StatisticAccess;
import si.skycoin.discovery.model.Node;
import si.skycoin.discovery.transfer.DiscoveryStatistic;
import si.skycoin.discovery.transfer.LocationStatistic;
import si.skycoin.discovery.transfer.NodeData;
import si.skycoin.discovery.transfer.UptimeStatistic;
import si.skycoin.discovery.transfer.UptimeStatisticRs;

@Stateless
public class StatisticManager {
    
    @Inject
    private Logger log;
    
    @EJB
    private NodeAccess nodeAccess;
    
    @EJB
    private NodeManager nodeManager;
    
    @EJB
    private StatisticAccess statisticAccess;
    
    public List<DiscoveryStatistic> discoveryNodesByMonth(Date day, int months) {
        log.debug("discoveryNodesByMonth day: {} months: {}", day, months);
        return statisticAccess.discoveryNodesGroupByDayForMonth(day, months);
    }

    public List<NodeData> nodeStatistic(int year, int month, List<Node> nodes) {
        log.debug("nodeStatistic year: {} month: {} nodes.size: {}", year, month, nodes != null ? nodes.size() : 0);
        List<NodeData> list = new ArrayList<>();
        if (nodes == null) {
            return list;
        }
        for (Node node : nodes) {
            list.add(statisticAccess.nodeStatistic(node, year, month));
        }
        return list;
    }
    
    public List<LocationStatistic> nodesByCountry(Date day) {
        log.debug("nodesByCountry day: {}", day);
        return statisticAccess.locationByDay(day);
    }
    
    public List<UptimeStatistic> nodesByUptime(int maxResult) {
        log.debug("nodesByUptime maxResult: {}", maxResult);
        List<UptimeStatistic> uptimeStatistics = statisticAccess.uptimeStatistic(maxResult);
        for (UptimeStatistic uptimeStatistic : uptimeStatistics) {
            if (!uptimeStatistic.getNode().getNodeApps().isEmpty()) {
                uptimeStatistic.setAppKey(uptimeStatistic.getNode().getNodeApps().get(0).getAppKey());
            }
        }
        return uptimeStatistics;
    }
    
    public List<UptimeStatisticRs> nodesByUptimeRs(int maxResult) {
        return nodesByUptime(maxResult)
                .stream()
                .map(us -> new UptimeStatisticRs(us.getNode().getKey(), us.getNode().getCountry() != null ? us.getNode().getCountry().getCountry() : null, us.getAppKey(), us.getSumCount()))
                .collect(Collectors.toList());
    }
}
