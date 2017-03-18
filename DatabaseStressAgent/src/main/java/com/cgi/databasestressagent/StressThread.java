/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent;

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
public class StressThread extends Thread{
    
    DatabaseStressParameters params;
    DatabaseStressLoopManager dbStressLoopManager = null;
    
    @Override
    public void interrupt() {
        dbStressLoopManager.interrupt();
        super.interrupt();
    }

    public void initThread(DatabaseStressParameters params){
        this.params = params;
    };
    
    @Override
    public void run() {
        
       try{
        //Démarrage du thread de stress de la database
        dbStressLoopManager = new DatabaseStressLoopManager();
        dbStressLoopManager.init(params);
        dbStressLoopManager.start();
           
       } catch (JMSException ex) {
           Logger.getLogger(StressIt.class.getName()).log(Level.SEVERE, "Erreur de communication JMS", ex);
       } catch (Exception ex) {
           Logger.getLogger(StressIt.class.getName()).log(Level.SEVERE, "Erreur globale", ex);
       }    
    }
    
}
