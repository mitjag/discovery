package si.skycoin.discovery.rest;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
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
import si.skycoin.discovery.transfer.LocationStatistic;
import si.skycoin.discovery.transfer.TimeChart;

@Path("/discovery")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DiscoveryService {
    
    @Inject
    private Logger log;
    
    @EJB
    private StatisticManager statisticManager;
    
    @GET
    public List<TimeChart> discoveryStatistic(@Context HttpServletRequest request, @QueryParam("months") @NotNull @DefaultValue("1") Integer months) {
        log.info("discoveryStatistic remoteAddr: {} X-Forwarded-For: {} months: {}", request.getRemoteAddr(), request.getHeader("X-Forwarded-For"), months);
        return statisticManager.discoveryNodesByMonth(new Date(), months).stream()
                .map(dn -> new TimeChart(dn.getDay(), dn.getNodeCount()))
                .collect(Collectors.toList());
    }
    
    @GET
    @Path("/location")
    public List<LocationStatistic> locationStatistic(@Context HttpServletRequest request) {
        log.info("locationStatistic remoteAddr: {} X-Forwarded-For: {}", request.getRemoteAddr(), request.getHeader("X-Forwarded-For"));
        return statisticManager.nodesByCountry(new Date());
    }
}
