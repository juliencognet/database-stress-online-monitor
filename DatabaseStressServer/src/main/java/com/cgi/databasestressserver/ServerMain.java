/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressserver;

import com.cgi.databasestressserver.activemqbroker.ActiveMQEmbeddedBroker;
import com.cgi.databasestressserver.activemqconsumer.MetricsMessageLoadingThread;
import com.cgi.databasestressserver.consolidationdatabase.ConsolidationDatabaseManager;
import com.cgi.databasestressserver.consolidationdatabase.ConsolidationThread;
import com.cgi.databasestressserver.websocketserver.MetricsEndPoint;
import com.cgi.databasestressserver.websocketserver.WebSocketServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.DeploymentException;
import org.glassfish.tyrus.server.Server;

/**
 *
 * @author Julien
 */
public class ServerMain {
    
   private static final Logger LOGGER =
    Logger.getLogger(WebSocketServer.class.getName());

    public static void main(String[] args)  {
        
        Thread activeMqBrokerThread = null;
        Thread metricsLoadingThread = null;
        Thread consolidationThread = null;
        try{
            //1. Création DB H2 + schéma associé
            ConsolidationDatabaseManager.initConnection();            
            ConsolidationDatabaseManager.createDatabase();

            //2. Démarrage du broker
            activeMqBrokerThread = new ActiveMQEmbeddedBroker();
            activeMqBrokerThread.start();

            //3. Démarrage du serveur Web Socket
            WebSocketServer.startServer();

            //4. Attente du démarrage des processus démarrés            
            Thread.sleep(1000);            
            
            //5. Démarrage du thread d'écoute sur bus
            //Démarrage du thread de surveillance d'arrivée
            metricsLoadingThread = (new MetricsMessageLoadingThread());
            metricsLoadingThread.start();

            //6. Démarrage du thread de consolidation des points avant envoi web socket
            consolidationThread = (new ConsolidationThread());
            consolidationThread.start();
            
            //7. Attente arrêt
            //Attente saisie touche entrée pour arrêter
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Appuyer sur Entree pour arreter le serveur.");
            reader.readLine();
            
        } catch (IOException e){
            //Affichage de la stack trace complète d'erreur
            e.printStackTrace();
        } catch (SQLException ex) {
           Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
       } catch (InterruptedException ex) {
           Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
       } finally {
            //Arrêt
            //8. Kill de tous les sous-threads            
            try{
                activeMqBrokerThread.interrupt();
            } catch (Exception ex){
                Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, "Could not interrupt activeMQ Broker", ex);
            }
            try{            
                metricsLoadingThread.interrupt();   
            } catch (Exception ex){
                Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, "Could not interrupt Metrics Loading Thread", ex);
            }
            try{            
                consolidationThread.interrupt();   
            } catch (Exception ex){
                Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, "Could not interrupt Consolidation Thread", ex);
            }            
            System.out.print("Les sous-threads sont arrêtés.");                        
            
            WebSocketServer.stopServer();
            ConsolidationDatabaseManager.closeConnection();
        }
    }        
    
}
