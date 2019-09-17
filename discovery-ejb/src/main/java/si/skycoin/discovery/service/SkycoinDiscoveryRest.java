package si.skycoin.discovery.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SkycoinDiscoveryRest {
    
    private static final Logger log = LoggerFactory.getLogger(SkycoinDiscoveryRest.class);
    
    private String portNode = "6001";
    private String portManager = "8000";
    private String url = "http://127.0.0.1";
    private String token;
    private String discoveryKey;
    
    public SkycoinDiscoveryRest() {
        // Only one time
        Unirest.setObjectMapper(new ObjectMapper() {
            
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            
            @Override
            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            
            @Override
            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    
    public String token() throws UnirestException {
        token = Unirest.get(url + ":" + portManager + "/getToken").asString().getBody();
        return token;
    }
    
    public int searchServices(int page, int limit, String discoveryKey) throws UnirestException {
        HttpResponse<String> response = Unirest.get(url + ":" + portNode + "/node/run/searchServices")
                .queryString("token", token)
                .queryString("key", "sockss")
                .queryString("pages", page)
                .queryString("limit", limit)
                .queryString("discoveryKey", discoveryKey)
                .asString();
        return response.getStatus();
    }
    
    public NodeSearchServices[] getSearchServicesResult() throws UnirestException {
        return Unirest.get(url + ":" + portNode + "/node/run/getSearchServicesResult")
                .queryString("token", token)
                .asObject(NodeSearchServices[].class)
                .getBody();
    }
    
    
    public List<NodeSearchServicesResult> allNodes() throws UnirestException, InterruptedException {
        int perPage = 5;
        int page = 0;
        int count = 1;
        Map<String, NodeSearchServicesResult> nodes = new HashMap<>();
        while ((page - 1) * perPage < count) {
            if (page % 10 == 0) {
                log.debug("count: {} page: {}", count, page);
            }
            searchServices(page, perPage, discoveryKey);
            NodeSearchServices[] nArray = null;
            int sleep = 150;
            int max = 12;
            while (nArray == null) {
                Thread.sleep(sleep);
                nArray = getSearchServicesResult();
                sleep += 200;
                max--;
                if (max == 0) {
                    log.debug("max reached");
                    break;
                }
            }
            if (nArray == null) {
                continue;
            }
            NodeSearchServices n = nArray[0];
            for (NodeSearchServicesResult r : n.getResult()) {
                nodes.put(r.getNodeKey(), r);
            }
            count = n.getCount();
            if (max != 0) {
                page++;
            }
        }
        return new ArrayList<>(nodes.values());
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getDiscoveryKey() {
        return discoveryKey;
    }
    
    public void setDiscoveryKey(String discoveryKey) {
        this.discoveryKey = discoveryKey;
    }
}
