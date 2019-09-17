package si.skycoin.discovery.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import si.skycoin.discovery.manager.StatisticManager;

@Named
@ViewScoped
public class IndexController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private Logger log;
    
    @Inject
    private FacesContext facesContext;
    
    @EJB
    private StatisticManager statisticManager;
    
    @PostConstruct
    public void postConstruct() {
        if (!facesContext.isPostback()) {
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            log.info("remoteAddr: {} X-Forwarded-For: {}", request.getRemoteAddr(), request.getHeader("X-Forwarded-For"));
        }
    }
}
