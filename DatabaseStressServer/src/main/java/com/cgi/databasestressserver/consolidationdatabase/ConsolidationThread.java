/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressserver.consolidationdatabase;

import com.cgi.databasestressserver.StopperSingleton;
import com.cgi.databasestressserver.domain.MetricPoint;
import com.cgi.databasestressserver.domain.PeriodResult;
import com.cgi.databasestressserver.websocketserver.MetricsEndPoint;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julien
 */
public class ConsolidationThread extends Thread {
    public void run() {
        ArrayList<PeriodResult> results= null;
        MetricPoint point = null;
        while (StopperSingleton.getInstance().shouldRun()){
            
            results = ConsolidationDatabaseManager.getConsolidatedMetricsOfLastThreeSeconds();
            if (!results.isEmpty()){
                point = new MetricPoint();
                point.setPointDate(results.get(0).getStartInterval());
                point.setMetricValue(results.get(0).getNbExecutions());
                point.setNbAgents(results.get(0).getNbStressThreads());
            } else {
                point = new MetricPoint();
                point.setPointDate(ZonedDateTime.now());
                point.setMetricValue(0);
                point.setNbAgents(0);
            }
            MetricsEndPoint.send(point);
            //System.out.println("Sent "+point.getPointDate().toString()+" : "+point.getMetricValue());
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConsolidationThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    
}
