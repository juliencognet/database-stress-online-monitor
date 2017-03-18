/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent;

import com.cgi.databasestressagent.PeriodicMetricGathering.CounterSingleton;
import com.cgi.databasestressagent.PeriodicMetricGathering.PeriodicMetricGatheringThread;
import com.cgi.databasestressagent.databasestress.DatabaseStressLoopManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;

/**
 *
 * @author Julien
 */
public class MultiStressIt {
    
   private static final Logger logger = Logger.getLogger(MainTest.class.getName());
   private static DatabaseStressParameters params;
   private static ArrayList<StressThread> threads = new ArrayList<StressThread>();

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
       PeriodicMetricGatheringThread gatheringManager = null;
       try {
           //Gestion des arguments
           manageArguments(args);
           
           //Autres paramètres: nombre d'agents
           int nbAgents = Integer.parseInt(args[9]);
           int delaySecondsBetweenAgents = Integer.parseInt(args[10]);
           int currentNbAgents = 0;
           
            //Démarrage du thread de stress de la database
            //Démarrage du thread de relève périodique
           gatheringManager = new PeriodicMetricGatheringThread();
           gatheringManager.init(params);
           gatheringManager.start();                   
           
           //
           while (currentNbAgents < nbAgents){
               
               StressThread stressThread = new StressThread();
               stressThread.initThread(params);
               stressThread.start();
               threads.add(stressThread);
               currentNbAgents++;               
               CounterSingleton.getInstance().setNbThreads(currentNbAgents);
               Thread.sleep(delaySecondsBetweenAgents*1000L);
           }
           
            //Attente arrêt
            //Attente saisie touche entrée pour arrêter
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Appuyer sur Entree pour arreter le multistress.");
            reader.readLine();           
           
       } catch (Exception ex) {
           Logger.getLogger(MultiStressIt.class.getName()).log(Level.SEVERE, "Erreur globale", ex);
       } finally{
           gatheringManager.interrupt();
           for (Thread thread:threads){
               thread.interrupt();
           }
       }


    }
    
}
