package si.skycoin.discovery.timer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.slf4j.Logger;

import si.skycoin.discovery.service.SkycoinDiscovery;

@Singleton
public class TriggerFolder {
    
    @Inject
    private Logger log;
    
    @EJB
    private SkycoinDiscovery skycoinDiscovery;
    
    @Schedule(second = "0", minute = "15", hour = "0", persistent = false)
    public void trigger() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date yesterday = Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        String date = formatter.format(yesterday);
        String folderPath = "/var/skycoin/" + date;
        log.info("Trigger folderPath: {}", folderPath);
        skycoinDiscovery.parseMemory(folderPath);
    }
}
