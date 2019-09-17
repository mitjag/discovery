package si.skycoin.discovery.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jboss.ejb3.annotation.TransactionTimeout;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import si.skycoin.discovery.data.NodeAccess;
import si.skycoin.discovery.model.DiscoveryFile;
import si.skycoin.discovery.queue.DiscoveryMessage;
import si.skycoin.discovery.queue.DiscoveryProducer;
import si.skycoin.discovery.queue.DiscoveryWorker;
import si.skycoin.discovery.queue.NodeProducer;

@Stateless
@TransactionTimeout(value = 60, unit = TimeUnit.MINUTES)
public class SkycoinDiscovery {
    
    @Inject
    private Logger log;
    
    @EJB
    private DiscoveryProducer discoveryProducer;
    
    @EJB
    private NodeAccess nodeAccess;
    
    @EJB
    private DiscoveryWorker discoveryWorker;
    
    @EJB
    private NodeProducer nodeProducer;
    
    public File[] findFiles(String folderPath) {
        File file = new File(folderPath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            Arrays.sort(files, new Comparator<File>() {
                
                @Override
                public int compare(File f1, File f2) {
                    return f1.getName().compareTo(f2.getName());
                }
            });
            return files;
        }
        return new File[0];
    }
    
    public void parse(String folderPath) {
        File[] files = findFiles(folderPath);
        for (File f : files) {
            DiscoveryMessage discoveryMessage = new DiscoveryMessage();
            discoveryMessage.setFile(f);
            discoveryProducer.send(discoveryMessage);
        }
    }
    
    @Asynchronous
    public void parseMemory(String folderPath) {
        File[] files = findFiles(folderPath);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<DiscoveryNode>> nodes = new HashMap<>();
        int nodeOnline = 0;
        for (File file : files) {
            DiscoveryFile discoveryFile = nodeAccess.findDiscoveryFile(file.getName());
            if (discoveryFile != null) {
                log.info("file already parsed discoveryFile.id: {} discoveryFile.filename: {}",
                        discoveryFile.getId(), discoveryFile.getFilename());
                continue;
            }
            discoveryFile = new DiscoveryFile();
            discoveryFile.setFilename(file.getName()); 
            Date ts = discoveryWorker.fromFilename(file.getName());
            discoveryFile.setTimestamp(ts);
            try {
                List<Discovery> list;
                list = mapper.readValue(file, new TypeReference<List<Discovery>>() {});
                discoveryFile.setCount(list.size());
                nodeAccess.saveDiscoveryFile(discoveryFile);
                for (Discovery d : list) {
                    if (!nodes.containsKey(d.getKey())) {
                        List<DiscoveryNode> l = new ArrayList<>();
                        DiscoveryNode dn = new DiscoveryNode();
                        dn.setKey(d.getKey());
                        dn.setDiscoveryFile(discoveryFile);
                        dn.setCount(1);
                        dn.setTimestampOnline(ts);
                        dn.setTimestampOffline(ts);
                        dn.setType(d.getType());
                        dn.setSendBytes(d.getSendBytes());
                        dn.setRecvBytes(d.getRecvBytes());
                        dn.setLastAckTime(d.getLastAckTime());
                        dn.setStartTime(d.getStartTime());
                        nodes.put(d.getKey(), l);
                        l.add(dn);
                        nodeOnline++;
                    } else {
                        List<DiscoveryNode> l = nodes.get(d.getKey());
                        DiscoveryNode o = l.get(l.size() - 1);
                        if (o.getStartTime() < d.getStartTime()) {
                            o.setCount(o.getCount() + 1);
                            o.setTimestampOffline(ts);
                            o.setType(d.getType());
                            o.setSendBytes(d.getSendBytes());
                            o.setRecvBytes(d.getRecvBytes());
                            o.setLastAckTime(d.getLastAckTime());
                            o.setStartTime(d.getStartTime());
                        } else {
                            DiscoveryNode dn = new DiscoveryNode();
                            dn.setKey(d.getKey());
                            dn.setDiscoveryFile(discoveryFile);
                            dn.setCount(1);
                            dn.setTimestampOnline(ts);
                            dn.setTimestampOffline(ts);
                            dn.setType(d.getType());
                            dn.setSendBytes(d.getSendBytes());
                            dn.setRecvBytes(d.getRecvBytes());
                            dn.setLastAckTime(d.getLastAckTime());
                            dn.setStartTime(d.getStartTime());
                            nodes.put(d.getKey(), l);
                            l.add(dn);
                            nodeOnline++;
                        }
                    }
                }
            } catch (IOException ex) {
                log.error("IOException file.name: {} ex: {}", file.getName(), ex.getMessage(), ex);
            }
        }
        log.info("nodes.size: {} nodeOnline: {}", nodes.size(), nodeOnline);
        nodes.forEach((k, l) -> {
            l.forEach(dn -> {
                nodeProducer.send(dn);
            });
        });
        log.info("nodes added to queue nodeOnline: {}", nodeOnline);
    }
}
