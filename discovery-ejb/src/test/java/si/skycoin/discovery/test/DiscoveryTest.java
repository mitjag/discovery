package si.skycoin.discovery.test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import si.skycoin.discovery.queue.DiscoveryWorker;
import si.skycoin.discovery.service.Discovery;
import si.skycoin.discovery.service.DiscoveryNode;
import si.skycoin.discovery.service.SkycoinDiscovery;

public class DiscoveryTest {
    
    private static final Logger log = LoggerFactory.getLogger(DiscoveryTest.class);
    
    @Test
    public void SkycoinDiscoveryEmptyTest() {
        SkycoinDiscovery sd = new SkycoinDiscovery();
        File[] findFiles = sd.findFiles("");
        Assert.assertEquals(0, findFiles.length);
    }

    @Test
    public void SkycoinDiscoveryFolderTest() throws JsonParseException, JsonMappingException, IOException {
        SkycoinDiscovery sd = new SkycoinDiscovery();
        File[] findFiles = sd.findFiles("C:\\Users\\mitja\\Desktop\\20180604");
        Assert.assertEquals(1440, findFiles.length);
        log.info("length: {}", findFiles.length);
        //File[] findFiles2 = sd.findFiles("C:\\Users\\mitja\\Desktop\\20180605");
        //File[] findFiles3 = sd.findFiles("C:\\Users\\mitja\\Desktop\\20180606");
        List<File> files = new ArrayList<>();
        files.addAll(Arrays.asList(findFiles));
        //files.addAll(Arrays.asList(findFiles2));
        //files.addAll(Arrays.asList(findFiles3));
        ObjectMapper mapper = new ObjectMapper();
        //Set<String> nodes = new HashSet<>();
        Map<String, List<DiscoveryNode>> nodes = new HashMap<>();
        int nodeOnline = 0;
        for (File f : files ) {
            DiscoveryWorker dw = new DiscoveryWorker();
            Date ts = dw.fromFilename(f.getName());
            List<Discovery> list = mapper.readValue(f, new TypeReference<List<Discovery>>() {});
            log.info("list.size: {}", list.size());
            for (Discovery d : list) {
                if (!nodes.containsKey(d.getKey())) {
                    List<DiscoveryNode> l = new ArrayList<>();
                    DiscoveryNode dn = new DiscoveryNode();
                    dn.setKey(d.getKey());
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
                    if (o.getStartTime() < d.getStartTime())
                    {
                        o.setCount(o.getCount() + 1);
                        o.setTimestampOffline(ts);
                        o.setType(d.getType());
                        o.setSendBytes(d.getSendBytes());
                        o.setRecvBytes(d.getRecvBytes());
                        o.setLastAckTime(d.getLastAckTime());
                        o.setStartTime(d.getStartTime());   
                    }
                    else
                    {
                        DiscoveryNode dn = new DiscoveryNode();
                        dn.setKey(d.getKey());
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
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss");
        nodes.forEach((k, l) -> {
            if (l.size() > 10) { log.info("key: {} l.size: {}", k, l.size()); }
            if (l.size() == 287) {
                log.info("key: {}", k);
                l.forEach(dn -> {
                    log.info("count: {} startTime: {} online: {} offline: {}",
                             dn.getCount(), dn.getStartTime(), formatter.format(dn.getTimestampOnline()), formatter.format(dn.getTimestampOffline()));
                });
            }
        });
        log.info("nodes.size: {} nodeOnline: {}", nodes.size(), nodeOnline);
    }
}
