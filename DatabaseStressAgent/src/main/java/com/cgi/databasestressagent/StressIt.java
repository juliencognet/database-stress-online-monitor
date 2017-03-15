/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent;

import com.cgi.databasestressagent.PeriodicMetricGathering.PeriodicMetricGatheringThread;
import com.cgi.databasestressagent.databasestress.DatabaseStressLoopManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author Julien
 */
public class StressIt {
    
   private static final Logger logger = Logger.getLogger(MainTest.class.getName());

   private static DatabaseStressParameters params;

   private static void manageArguments(String[] args){
        //Adresse du Broker MQ (par exemple: tcp://localhost:61616)
        params = new DatabaseStressParameters();
        params.setActiveMQBrokerURL(args[0]);
        params.setStressAgentClass(args[1]);
        params.setDelay(Integer.parseInt(args[2]));
        params.setDatabaseType(args[3]);
        params.setDatabaseNode(args[4]);
        params.setDatabasePort(Integer.parseInt(args[5]));
        params.setDatabaseId(args[6]);
        params.setDatabaseUser(args[7]);
        params.setDatabasePassword(args[8]);
   }

   
    public static void main(String[] args) {
       try {
           //Gestion des arguments
           manageArguments(args);
           
           //Démarrage du thread de relève périodique
           PeriodicMetricGatheringThread gatheringManager = new PeriodicMetricGatheringThread();
           gatheringManager.init(params);
           gatheringManager.start();
           
           //Démarrage du thread de stress de la database
           DatabaseStressLoopManager dbStressLoopManager = new DatabaseStressLoopManager();
           dbStressLoopManager.init(params);
           dbStressLoopManager.start();
           
       } catch (JMSException ex) {
           Logger.getLogger(StressIt.class.getName()).log(Level.SEVERE, "Erreur de communication JMS", ex);
       } catch (Exception ex) {
           Logger.getLogger(StressIt.class.getName()).log(Level.SEVERE, "Erreur globale", ex);
       }


    }
    
}
