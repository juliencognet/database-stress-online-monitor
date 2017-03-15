/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent;

/**
 *
 * @author Julien
 */
public class DatabaseStressParameters {
   //Adresse du Broker MQ (par exemple: tcp://localhost:61616)
   private String ActiveMQBrokerURL;

   //Classe à exécuter
   private String stressAgentClass;

   //Délai entre deux exécutions
   private int delay;

   //Database Type 
   private String databaseType;

   //Database Host
   private String databaseNode;

   //Database Port
   private int databasePort;

   private String databaseId;

   private String databaseUser;

   private String databasePassword;

    public String getActiveMQBrokerURL() {
        return ActiveMQBrokerURL;
    }

    public void setActiveMQBrokerURL(String ActiveMQBrokerURL) {
        this.ActiveMQBrokerURL = ActiveMQBrokerURL;
    }

    public String getStressAgentClass() {
        return stressAgentClass;
    }

    public void setStressAgentClass(String stressAgentClass) {
        this.stressAgentClass = stressAgentClass;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getDatabaseNode() {
        return databaseNode;
    }

    public void setDatabaseNode(String databaseNode) {
        this.databaseNode = databaseNode;
    }

    public int getDatabasePort() {
        return databasePort;
    }

    public void setDatabasePort(int port) {
        this.databasePort = port;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public void setDatabaseUser(String databaseUser) {
        this.databaseUser = databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
       
}
