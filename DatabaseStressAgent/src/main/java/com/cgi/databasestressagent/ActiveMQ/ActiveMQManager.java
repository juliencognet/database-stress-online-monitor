/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent.ActiveMQ;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author Julien
 */
public class ActiveMQManager {
    private String messageQueue= "StressTest.MetricsFromAgent";
    private Connection mqconnection;
    private Session mqsession;       
    private MessageProducer mqproducer;
    public void initMessageProducer(String ActiveMQBrokerURL) throws JMSException{
        // Create a ConnectionFactory
         ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQBrokerURL);

         // Create a Connection
         mqconnection = connectionFactory.createConnection();
         mqconnection.start();

         // Create a Session
         mqsession = mqconnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

         // Create the destination (Topic or Queue)
         Destination destination = mqsession.createQueue(messageQueue);

         // Create a MessageProducer from the Session to the Topic or Queue
         mqproducer = mqsession.createProducer(destination);
         mqproducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    }
    public void sendMessage(String textMessage) throws JMSException{
        TextMessage mqmessage = this.mqsession.createTextMessage(textMessage);
        // Tell the producer to send the message
        System.out.println("Sent message: "+ textMessage.hashCode() + " : " + Thread.currentThread().getName());
        this.mqproducer.send(mqmessage);        
    }
    public void closeMessageSessionAndConnection() throws JMSException{
        mqsession.close();
        mqconnection.close();
    }
}
