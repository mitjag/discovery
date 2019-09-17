package si.skycoin.discovery.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import si.skycoin.discovery.manager.StatisticManager;
import si.skycoin.discovery.transfer.UptimeStatistic;

@Named
@ViewScoped
public class TopController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private Logger log;
    
    @Inject
    private FacesContext facesContext;
    
    @EJB
    private StatisticManager statisticManager;
    
    private List<UptimeStatistic> uptimeStatistics;
    
    @PostConstruct
    public void postConstruct() {
        if (!facesContext.isPostback()) {
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            log.info("remoteAddr: {} X-Forwarded-For: {}", request.getRemoteAddr(), request.getHeader("X-Forwarded-For"));
        }
        top10();
    }
    
    public void top10() {
        uptimeStatistics = statisticManager.nodesByUptime(10);
    }
    
    public void top100() {
        uptimeStatistics = statisticManager.nodesByUptime(100);
    }
    
    public void top1000() {
        uptimeStatistics = statisticManager.nodesByUptime(1000);
    }
    
    public List<UptimeStatistic> getUptimeStatistics() {
        return uptimeStatistics;
    }
    
    public void setUptimeStatistics(List<UptimeStatistic> uptimeStatistics) {
        this.uptimeStatistics = uptimeStatistics;
    }
}
