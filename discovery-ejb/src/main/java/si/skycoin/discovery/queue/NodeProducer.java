package si.skycoin.discovery.queue;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;

import org.slf4j.Logger;

import si.skycoin.discovery.model.DiscoveryFile;
import si.skycoin.discovery.service.Discovery;
import si.skycoin.discovery.service.DiscoveryNodeResult;
import si.skycoin.discovery.service.DiscoveryNode;

@Stateless
public class NodeProducer {
    
    @Inject
    private Logger log;
    
    @Inject
    private JMSContext jmsContext;
    
    @Resource(mappedName = "java:/jms/queue/NodeQueue")
    private Queue nodeQueue;
    
    public void send(DiscoveryFile discoveryFile, Discovery discovery) {
        try {
            NodeMessage nodeMessage = new NodeMessage();
            nodeMessage.setMessageType(ENodeMessageType.UPDATE);
            nodeMessage.setDiscoveryFile(discoveryFile);
            nodeMessage.setDiscovery(discovery);
            javax.jms.Message jmsMessage = jmsContext.createObjectMessage(nodeMessage);
            jmsMessage.setStringProperty("JMSXGroupID", discovery.getKey());
            jmsContext.createProducer().send(nodeQueue, jmsMessage);
        } catch (JMSRuntimeException | JMSException ex) {
            log.error("JMSRuntimeException ex: {}", ex.getMessage(), ex);
        }
    }

    public void send(DiscoveryNode discoveryNode) {
        try {
            NodeMessage nodeMessage = new NodeMessage();
            nodeMessage.setMessageType(ENodeMessageType.SAVE);
            nodeMessage.setDiscoveryFile(discoveryNode.getDiscoveryFile());
            nodeMessage.setDiscoveryNode(discoveryNode);
            javax.jms.Message jmsMessage = jmsContext.createObjectMessage(nodeMessage);
            jmsMessage.setStringProperty("JMSXGroupID", discoveryNode.getKey());
            jmsContext.createProducer().send(nodeQueue, jmsMessage);
        } catch (JMSRuntimeException | JMSException ex) {
            log.error("JMSRuntimeException ex: {}", ex.getMessage(), ex);
        }
    }
    
    public void location(DiscoveryNodeResult discoveryNodeResult) {
        try {
            NodeMessage nodeMessage = new NodeMessage();
            nodeMessage.setMessageType(ENodeMessageType.LOCATION);
            nodeMessage.setDiscoveryNodeLocation(discoveryNodeResult);
            javax.jms.Message jmsMessage = jmsContext.createObjectMessage(nodeMessage);
            jmsMessage.setStringProperty("JMSXGroupID", discoveryNodeResult.getNodeKey());
            jmsContext.createProducer().send(nodeQueue, jmsMessage);
        } catch (JMSRuntimeException | JMSException ex) {
            log.error("JMSRuntimeException ex: {}", ex.getMessage(), ex);
        }
    }
}
