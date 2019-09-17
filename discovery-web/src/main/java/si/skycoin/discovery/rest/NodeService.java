package si.skycoin.discovery.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;

import si.skycoin.discovery.manager.NodeManager;
import si.skycoin.discovery.manager.StatisticManager;
import si.skycoin.discovery.model.Node;
import si.skycoin.discovery.transfer.NodeData;
import si.skycoin.discovery.transfer.TimeChart;
import si.skycoin.discovery.transfer.TimeData;

@Path("/node")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NodeService {
    
    @Inject
    private Logger log;
       
    @EJB
    private NodeManager nodeManager;

    @EJB
    private StatisticManager statisticManager;
    
    @GET
    public List<TimeData> nodes(@Context HttpServletRequest request,
            @QueryParam("year") Integer year,
            @QueryParam("month") Integer month,
            @QueryParam("nodeKey") String nodeKey,
            @QueryParam("groupTag") String groupTag) {
        log.info("nodes remoteAddr: {} X-Forwarded-For: {} year: {} month: {} nodeKey: {} groupTag: {}",
                request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), year, month, nodeKey, groupTag);
        List<TimeData> list = new ArrayList<>();
        List<Node> nodes = nodeManager.nodeGroup(nodeKey, groupTag);
        for (NodeData nodeData : statisticManager.nodeStatistic(year, month, nodes)) {
            TimeData timeData = new TimeData();
            timeData.setNodeKey(nodeData.getNodeKey());
            timeData.setTimeChart(nodeData.getNodeStatistic().stream()
                .map(ns -> new TimeChart(ns.getDay(), (long) (ns.getNodeCount() * 100 / ns.getFileCount())))
                .collect(Collectors.toList()));
            list.add(timeData);
        }
        return list;
    }
}
