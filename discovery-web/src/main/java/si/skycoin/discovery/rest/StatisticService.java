package si.skycoin.discovery.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;

import si.skycoin.discovery.manager.StatisticManager;
import si.skycoin.discovery.transfer.UptimeStatisticRs;

@Path("/statistic")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatisticService {
    
    @Inject
    private Logger log;
    
    @EJB
    private StatisticManager statisticManager;
    
    @GET
    public List<UptimeStatisticRs> top(@Context HttpServletRequest request, @QueryParam("top") @DefaultValue("10") Integer top) {
        log.info("top remoteAddr: {} X-Forwarded-For: {} top: {}",
                request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), top);
        if (top.equals(1000)) {
            return statisticManager.nodesByUptimeRs(1000);
        } else if (top.equals(100)) {
            return statisticManager.nodesByUptimeRs(100);
        } else {
            return statisticManager.nodesByUptimeRs(10);
        }
    }
}
