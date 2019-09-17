package si.skycoin.discovery.queue;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;

@MessageDriven(name = "DiscoveryQueue", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/DiscoveryQueue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "1") })
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class DiscoveryQueue implements MessageListener {
    
    @Inject
    private Logger log;
    
    @EJB
    private DiscoveryWorker discoveryWorker;
    
    @Override
    public void onMessage(Message message) {
        try {
            //log.info("message.JMSMessageID: {}", message.getJMSMessageID());
            DiscoveryMessage discoveryMessage = message.getBody(DiscoveryMessage.class);
            log.info("message.JMSMessageID: {} discoveryMessage.file.name: {}", message.getJMSMessageID(), discoveryMessage.getFile().getName());
            discoveryWorker.work(discoveryMessage.getFile());
        } catch (JMSException ex) {
            log.error("JMSException ex: {}", ex.getMessage(), ex);
        }
    }
}
