package si.skycoin.discovery.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import si.skycoin.discovery.service.NodeSearchServices;
import si.skycoin.discovery.service.NodeSearchServicesResult;

public class NodeLocationTest {
    
    private static final Logger log = LoggerFactory.getLogger(NodeLocationTest.class);
    
    private static final String LINE = "[{\"result\":[{\"node_key\":\"02f1e4c3ab5deea852ef741a8216317cdd2471694672b0a025265a149f76e94a15\",\"app_key\":\"022756b058777caad4609630e01e34e93cdddf39332f3fcc897af42724eb2edc99\",\"location\":\"Hawke's Bay, New Zealand\",\"version\":\"1.0.0\",\"node_version\":[\"0.1.0\",\"0.1.0\",\"0.1.0\"]}],\"seq\":2179,\"count\":7351}]";
    
    @Test
    public void line() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        NodeSearchServices services = mapper.readValue(LINE.substring(1, LINE.length() - 1), NodeSearchServices.class);        
        log.info("count: {}", services.getCount());
    }
    
    @Test
    public void serializeTest() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\git\\Skycoin\\node_20181106-205130.json"))) {
            String line;
            int count = 0;
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Integer> countries = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                NodeSearchServices services = mapper.readValue(line.substring(1, line.length() - 1), NodeSearchServices.class);
                for (NodeSearchServicesResult result : services.getResult()) {
                    String[] split = result.getLocation().split(",");
                    String country = split.length == 1 ? split[0].trim() : split[split.length - 1].trim();
                    if (countries.containsKey(country)) {
                        Integer c = countries.get(country) + 1;
                        countries.put(country, c);
                    } else {
                        countries.put(country, 1);
                    }
                    //log.info("country: {}", country);
                }
                count++;
            }
            log.info("count: {} countries.size: {}", count, countries.size());
            for (Entry<String, Integer> e : countries.entrySet()) {
                log.info("country: {} count: {}", e.getKey(),  e.getValue());
            }
        }
    }   
}
