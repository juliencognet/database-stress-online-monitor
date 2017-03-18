/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent.PeriodicMetricGathering;

import com.cgi.databasestressagent.ActiveMQ.ActiveMQManager;
import com.cgi.databasestressagent.DatabaseStressParameters;
import com.cgi.databasestressagent.databaseactions.DatabaseActionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;

/**
 *
 * @author Julien
 */
public class PeriodicMetricGatheringThread extends Thread{
    
    private ActiveMQManager mqmanager;
    private boolean shouldRun = true;
    private int DELAY_MS_BETWEEN_PROBE = 500;
    private ObjectMapper mapper;
    private String actionName;
    DatabaseStressParameters savedParams;
    
    public void init(DatabaseStressParameters parameters) throws JMSException{
        this.savedParams=parameters;
        mqmanager = new ActiveMQManager();
        mqmanager.initMessageProducer(parameters.getActiveMQBrokerURL());
        mapper = new ObjectMapper();
        mapper.registerModule(new JSR310Module());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.actionName=DatabaseActionFactory.getDatabaseActionByName(parameters.getStressAgentClass()).getActionName();
    }
     
    @Override
    public void run(){

        try {
            CounterSingleton.getInstance().init("Host:"+InetAddress.getLocalHost()
                    +"/Thread:"+ManagementFactory.getRuntimeMXBean().getName()
                    ,this.actionName
            );
        } catch (UnknownHostException ex) {
            Logger.getLogger(PeriodicMetricGatheringThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while (shouldRun) {

                //Attente
                Thread.sleep(DELAY_MS_BETWEEN_PROBE);

                //Relève des compteurs
                PeriodResult periodResult = CounterSingleton.getInstance().getCountAndReset();
                
                //Conversion du message
                String message = mapper.writeValueAsString(periodResult);

                mqmanager.sendMessage(message);
            }
        } catch (JMSException | JsonProcessingException ex) {
            Logger.getLogger(PeriodicMetricGatheringThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException e){
            System.out.println("Thread de récolte périodique des résultats arrêté.");
        }
    }
        
    @Override
    public void interrupt(){
        try {
            mqmanager.closeMessageSessionAndConnection();
        } catch (JMSException ex) {
            Logger.getLogger(PeriodicMetricGatheringThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.interrupt();
    }
    
 
}
