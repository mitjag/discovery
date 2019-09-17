package si.skycoin.discovery.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegexTest {
    
    private static final Logger log = LoggerFactory.getLogger(JsonTest.class);

    private static final String test1 = "discovery_20180604000301.json";
    
    @Test
    public void RegexTimestampTest() {
        Pattern pattern = Pattern.compile(".+_(\\d+)\\.json");
        Matcher matcher = pattern.matcher(test1);
        if (matcher.find()) {
            String group1 =  matcher.group(1);
            log.info("Group1: {}", group1);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
            try {
                Date date = format.parse(group1);
                log.info("date: {}", date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
