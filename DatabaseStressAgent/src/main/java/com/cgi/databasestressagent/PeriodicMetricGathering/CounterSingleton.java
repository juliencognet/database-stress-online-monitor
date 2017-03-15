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
public class CounterSingleton {

    private PeriodResult periodResult;
    private String AgentInformation;
    private String actionName;
    
    private static volatile CounterSingleton instance = null;    
     /**
      * Constructeur de l'objet.
      */
     private CounterSingleton() {
         // La présence d'un constructeur privé supprime le constructeur public par défaut.
         // De plus, seul le singleton peut s'instancier lui-même.
         this.periodResult = new PeriodResult();
     }    

     /**
      * Méthode permettant de renvoyer une instance de la classe Singleton
      * @return Retourne l'instance du singleton.
      */
     public final static CounterSingleton getInstance() {
         //Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet 
         //d'éviter un appel coûteux à synchronized, 
         //une fois que l'instanciation est faite.
         if (CounterSingleton.instance == null) {
            // Le mot-clé synchronized sur ce bloc empêche toute instanciation
            // multiple même par différents "threads".
            // Il est TRES important.
            synchronized(CounterSingleton.class) {
              if (CounterSingleton.instance == null) {
                CounterSingleton.instance = new CounterSingleton();
              }
            }
         }
         return CounterSingleton.instance;
     }     
     
     public synchronized void addResult(boolean result){
         if (result){
            //Execution OK
            this.periodResult.setNbExecutions(this.periodResult.getNbExecutions()+1);
         } else {
             //Exécution KO
             this.periodResult.setNbErrors(this.periodResult.getNbErrors()+1);
         }
     }
     
     public synchronized void init(String agentInformation, String actionName){
         this.periodResult = new PeriodResult();
         this.AgentInformation=agentInformation;
         this.actionName=actionName;
         this.periodResult.setActionName(actionName);
         this.periodResult.setAgentInformation(agentInformation);
     }
     
     public synchronized PeriodResult getCountAndReset(){
         this.periodResult.setEndInterval(ZonedDateTime.now());
         PeriodResult result = this.periodResult.clone();
         this.periodResult.resetCounters();
         return result;
     }
}
