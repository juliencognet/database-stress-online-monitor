/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databaseperformanceonlinemonitor;

/**
 *
 * @author Julien
 */
public final class StopperSingleton {
    boolean shouldRun = true;
    private static volatile StopperSingleton instance = null;    
     /**
      * Constructeur de l'objet.
      */
     private StopperSingleton() {
         // La présence d'un constructeur privé supprime le constructeur public par défaut.
         // De plus, seul le singleton peut s'instancier lui-même.
         super();
     }    

     /**
      * Méthode permettant de renvoyer une instance de la classe Singleton
      * @return Retourne l'instance du singleton.
      */
     public final static StopperSingleton getInstance() {
         //Le "Double-Checked Singleton"/"Singleton doublement vérifié" permet 
         //d'éviter un appel coûteux à synchronized, 
         //une fois que l'instanciation est faite.
         if (StopperSingleton.instance == null) {
            // Le mot-clé synchronized sur ce bloc empêche toute instanciation
            // multiple même par différents "threads".
            // Il est TRES important.
            synchronized(StopperSingleton.class) {
              if (StopperSingleton.instance == null) {
                StopperSingleton.instance = new StopperSingleton();
              }
            }
         }
         return StopperSingleton.instance;
     }     
     
     public void stopExecution(){
         this.shouldRun=false;
     }
     public void restartExecution(){
         this.shouldRun=true;
     }     
     public boolean shouldRun(){
         return this.shouldRun;
     }
     
}
