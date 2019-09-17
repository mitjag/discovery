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

@MessageDriven(name = "NodeQueue", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/NodeQueue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "maxSession", propertyValue = "2") })
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class NodeQueue implements MessageListener {
    
    @Inject
    private Logger log;
    
    @EJB
    private NodeWorker nodeWorker;
    
    @Override
    public void onMessage(Message message) {
        try {
            //log.info("message.JMSMessageID: {}", message.getJMSMessageID());
            NodeMessage nodeMessage = message.getBody(NodeMessage.class);
            //log.info("message.JMSMessageID: {} discoveryMessage.file.name: {}", message.getJMSMessageID(), discoveryMessage.getFile().getName());
            switch (nodeMessage.getMessageType()) {
            case UPDATE:
                //if (nodeMessage.getDiscovery() != null)
                nodeWorker.work(nodeMessage.getDiscoveryFile(), nodeMessage.getDiscovery());
                break;
            case SAVE:
                // else if (nodeMessage.getDiscoveryNode() != null)
                nodeWorker.work(nodeMessage.getDiscoveryNode());
                break;
            case LOCATION:
                nodeWorker.location(nodeMessage.getDiscoveryNodeResult());
                break;
            default:
                log.error("Default case nodeMessage.messageType: {}", nodeMessage.getMessageType());
            }                
        } catch (JMSException ex) {
            log.error("JMSException ex: {}", ex.getMessage(), ex);
        }
    }
}
