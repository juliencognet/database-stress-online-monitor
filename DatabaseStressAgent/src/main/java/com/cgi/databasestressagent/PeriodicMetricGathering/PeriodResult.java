/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent.PeriodicMetricGathering;

import java.time.ZonedDateTime;

/**
 *
 * @author Julien
 */
public class PeriodResult {

    public PeriodResult() {
        this.resetCounters();
    }
    private String agentInformation;
    private String actionName;
    private int nbStressThreads=1;

    public int getNbStressThreads() {
        return nbStressThreads;
    }

    public void setNbStressThreads(int nbStressThreads) {
        this.nbStressThreads = nbStressThreads;
    }

    public String getAgentInformation() {
        return agentInformation;
    }

    public void setAgentInformation(String agentInformation) {
        this.agentInformation = agentInformation;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    private int nbExecutions = 0;
    private int nbErrors = 0;
    private ZonedDateTime startInterval;
    private ZonedDateTime endInterval;    

    public int getNbExecutions() {
        return nbExecutions;
    }

    public void setNbExecutions(int nbExecutions) {
        this.nbExecutions = nbExecutions;
    }

    public int getNbErrors() {
        return nbErrors;
    }

    public void setNbErrors(int nbErrors) {
        this.nbErrors = nbErrors;
    }

    public ZonedDateTime getStartInterval() {
        return startInterval;
    }

    public void setStartInterval(ZonedDateTime startInterval) {
        this.startInterval = startInterval;
    }

    public ZonedDateTime getEndInterval() {
        return endInterval;
    }

    public void setEndInterval(ZonedDateTime endInterval) {
        this.endInterval = endInterval;
    }
    
    public void resetCounters(){
        this.nbErrors=0;
        this.nbExecutions=0;
        this.startInterval=ZonedDateTime.now();
        this.endInterval=null;
    }
    
    public PeriodResult clone(){
        PeriodResult periodCloned = new PeriodResult();
        periodCloned.setStartInterval(this.startInterval);
        periodCloned.setEndInterval(this.endInterval);
        periodCloned.setNbErrors(this.nbErrors);
        periodCloned.setNbExecutions(this.nbExecutions);
        periodCloned.setAgentInformation(this.agentInformation);
        periodCloned.setActionName(this.actionName);
        periodCloned.setNbStressThreads(this.nbStressThreads);
        return periodCloned;
    }
}
