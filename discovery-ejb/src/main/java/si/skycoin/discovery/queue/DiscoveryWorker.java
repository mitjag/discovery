package si.skycoin.discovery.queue;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import si.skycoin.discovery.data.NodeAccess;
import si.skycoin.discovery.model.DiscoveryFile;
import si.skycoin.discovery.service.Discovery;

@Stateless
public class DiscoveryWorker {
    
    @Inject
    private Logger log;
    
    @EJB
    private NodeAccess nodeAccess;
    
    @EJB
    private NodeProducer nodeProducer;
    
    public void work(File file) {
        DiscoveryFile discoveryFile = nodeAccess.findDiscoveryFile(file.getName());
        if (discoveryFile != null) {
            log.info("file already parsed discoveryFile.id: {} discoveryFile.filename: {}",
                    discoveryFile.getId(), discoveryFile.getFilename());
            return;
        }
        discoveryFile = new DiscoveryFile();
        discoveryFile.setFilename(file.getName()); 
        discoveryFile.setTimestamp(fromFilename(file.getName()));
        nodeAccess.saveDiscoveryFile(discoveryFile);
        
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Discovery> list = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Discovery.class)); // new TypeReference<List<Discovery>>() {}
            log.info("work parsing file.name: {} list.size: {}", file.getName(), list.size());
            for (Discovery discovery : list) {
                //nodeAccess.saveNode(discoveryFile, discovery);
                nodeProducer.send(discoveryFile, discovery);
            }
        } catch (IOException ex) {
            log.error("IOException ex: {}", ex.getMessage(), ex);
        }
    }
    
    public Date fromFilename(String filename) {
        Pattern pattern = Pattern.compile(".+_(\\d+)\\.json");
        Matcher matcher = pattern.matcher(filename);
        if (matcher.find()) {
            String group1 =  matcher.group(1);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            try {
                Date date = format.parse(group1);
                return date;
            } catch (ParseException ex) {
                log.error("ParseException filename: {} ex: {}", filename, ex.getMessage(), ex);
            }
        }
        log.warn("Date part not matched filename: {}", filename);
        return new Date();
    }
}
