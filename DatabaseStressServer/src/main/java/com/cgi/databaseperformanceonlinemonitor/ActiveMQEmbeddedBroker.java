/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databaseperformanceonlinemonitor;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.activemq.broker.BrokerService;

/**
 *
 * @author Julien
 */
public class ActiveMQEmbeddedBroker extends Thread {

    public void run() {
        BrokerService broker = new BrokerService();
        try {
            // configure the broker
            broker.addConnector("tcp://localhost:61616");
            broker.start();
        } catch (Exception ex) {
            Logger.getLogger(ActiveMQEmbeddedBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
