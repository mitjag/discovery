package si.skycoin.discovery.timer;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.slf4j.Logger;

import si.skycoin.discovery.service.SkycoinLocation;

@Singleton
public class TriggerLocation {
    
    @Inject
    private Logger log;
    
    @EJB
    private SkycoinLocation skycoinLocation;
    
    @Schedule(second = "0", minute = "15", hour = "1", persistent = false)
    public void trigger() {
        log.debug("Trigger location");
        skycoinLocation.location();
    }
}
