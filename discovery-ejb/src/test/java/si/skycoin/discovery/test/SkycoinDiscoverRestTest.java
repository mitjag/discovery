package si.skycoin.discovery.test;

import java.util.List;

import org.junit.Test;

import com.mashape.unirest.http.exceptions.UnirestException;

import si.skycoin.discovery.service.NodeSearchServicesResult;
import si.skycoin.discovery.service.SkycoinDiscoveryRest;

public class SkycoinDiscoverRestTest {
    
    @Test
    public void restTest() throws UnirestException, InterruptedException {
        SkycoinDiscoveryRest rest = new SkycoinDiscoveryRest();
        rest.setUrl("http://192.168.1.138");
        rest.setDiscoveryKey("034b1cd4ebad163e457fb805b3ba43779958bba49f2c5e1e8b062482904bacdb68");
        System.out.println(rest.token());
        List<NodeSearchServicesResult> list = rest.allNodes();
        System.out.println("list.size: " + list.size());
        for (NodeSearchServicesResult r : list) {
            System.out.println(r.getNodeKey() + " " + r.getLocation());
        }
    }
}
