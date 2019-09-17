package si.skycoin.discovery.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import si.skycoin.discovery.service.Discovery;

public class JsonTest {
    
    private static final Logger log = LoggerFactory.getLogger(JsonTest.class);

    private static final String test1 = "[{\"key\":\"0305bb8f0699557f29ab18c764d0aab97a376ef6a50348402aecff02ea09f3d4ba\",\"type\":\"TCP\",\"send_bytes\":48721,\"recv_bytes\":49088,\"last_ack_time\":8,\"start_time\":322747}]";
    private static final String test2 = "[{\"key\":\"0305bb8f0699557f29ab18c764d0aab97a376ef6a50348402aecff02ea09f3d4ba\",\"type\":\"TCP\",\"send_bytes\":48721,\"recv_bytes\":49088,\"last_ack_time\":8,\"start_time\":322747},{\"key\":\"022d40fe392f1512a6fa0d67cf57432017d61d7a0f79e966270e9d7da3e4e48375\",\"type\":\"TCP\",\"send_bytes\":895,\"recv_bytes\":1257,\"last_ack_time\":2,\"start_time\":3902},{\"key\":\"03894fa517fd96378b4c2f88185979eea659b2d7a79bc4b850d55b0ecdf13c7565\",\"type\":\"TCP\",\"send_bytes\":66423,\"recv_bytes\":66794,\"last_ack_time\":28,\"start_time\":440768}]";
    
    @Test
    public void ParseJsonTest() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Discovery> list1 = mapper.readValue(test1, new TypeReference<List<Discovery>>() {});
            log.info("list1.size: {}", list1.size());
            List<Discovery> list2 = mapper.readValue(test2, mapper.getTypeFactory().constructCollectionType(List.class, Discovery.class));
            log.info("list2.size: {}", list2.size());
            List<Discovery> list3 = mapper.readValue(new File("C:\\Users\\mitja\\Desktop\\20180604\\discovery_20180604000101.json"), new TypeReference<List<Discovery>>() {});
            log.info("list3.size: {}", list3.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Done");
    }
}
