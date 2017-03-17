/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressserver.activemqconsumer;

import com.cgi.databasestressserver.StopperSingleton;
import com.cgi.databasestressserver.consolidationdatabase.ConsolidationDatabaseManager;
import com.cgi.databasestressserver.websocketserver.MetricsEndPoint;
import com.cgi.databasestressserver.domain.MetricPoint;
import com.cgi.databasestressserver.domain.PeriodResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.Connection;

/**
 *
 * @author Julien
 */
public class MetricsMessageLoadingThread extends Thread {

    public void run() {
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

            // Create a Connection
            javax.jms.Connection connection = connectionFactory.createConnection();
            connection.start();
            //connection.setExceptionListener((ExceptionListener) this);

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("StressTest.MetricsFromAgent");

            // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(destination);        
            while (StopperSingleton.getInstance().shouldRun()) {


                    //Random randomGenerator = new Random();
                    //int randomInt = randomGenerator.nextInt(100);

                    //A cet endroit là, on va écouter les messages sur le bus
                    //int randomInt = 0;
                    Message message = consumer.receive();
                    TextMessage textMessage = null ;
                    if (message instanceof TextMessage) {
                        textMessage = (TextMessage) message;
                        System.out.println("Received: " + textMessage.getText());
                    } else {
                        System.out.println("Received: " + message);
                    }                    

                    //Puis l'enregistrer en BDD
                    try{
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.registerModule(new JSR310Module());
                        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
                        PeriodResult periodResult = (PeriodResult)mapper.readValue(textMessage.getText(),PeriodResult.class);
                        ConsolidationDatabaseManager.insertPeriodResult(periodResult);
                        
                        /*randomInt = periodResult.getNbExecutions();
                    
                        MetricPoint point = new MetricPoint();
                        point.setMetricValue(randomInt);
                        point.setPointDate(periodResult.getStartInterval());
                        MetricsEndPoint.send(point);*/
                    } catch (Exception e){
                        System.out.println("Invalid message"+e.getMessage());
                    }
                    
                    //Puis on attend à nouveau
                    try{
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MetricsMessageLoadingThread.class.getName()).log(Level.SEVERE, "Impossible d'attendre", ex);
                    }
            }
            consumer.close();
            session.close();
            connection.close();
            
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }
}
