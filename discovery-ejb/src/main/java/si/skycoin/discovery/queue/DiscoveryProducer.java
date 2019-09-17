package si.skycoin.discovery.queue;

import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;

import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;

@Stateless
public class DiscoveryProducer {
    
    @Inject
    private Logger log;
    
    @Inject
    private JMSContext jmsContext;
    
    @Resource(mappedName = "java:/jms/queue/DiscoveryQueue")
    private Queue discoveryQueue;
    
    public void send(DiscoveryMessage message) {
        try {
            javax.jms.Message jmsMessage = jmsContext.createObjectMessage(message);
            //jmsMessage.setStringProperty("JMSXGroupID", "key");
            jmsContext.createProducer().send(discoveryQueue, jmsMessage);
            log.info("Send jmsMessage.JMSMessageID: {}", jmsMessage.getJMSMessageID());
        } catch (JMSRuntimeException | JMSException ex) {
            log.error("JMSRuntimeException ex: {}", ex.getMessage(), ex);
        }
    }
}
