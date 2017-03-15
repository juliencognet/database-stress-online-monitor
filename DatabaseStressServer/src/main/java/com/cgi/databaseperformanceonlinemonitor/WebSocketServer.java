/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databaseperformanceonlinemonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.DeploymentException;
import org.glassfish.tyrus.server.Server;

/**
 *
 * @author Julien
 */
public class WebSocketServer {
    
    private static final Logger LOGGER =
    Logger.getLogger(WebSocketServer.class.getName());

    public static void main(String[] args)  {
        Server server = new Server("localhost", 8098, "/websockets", MetricsEndPoint.class);
        try {
            LOGGER.log(Level.INFO, "Lancement du serveur");
            server.start();
            //Démarage du broker embarqué ActiveMQ
            Thread activeMqBrokerThread = new ActiveMQEmbeddedBroker();
            activeMqBrokerThread.start();
            
            //Attente 1 seconde avant de démarrer le consommateur / émetteur 
            Thread.sleep(1000);

            //Démarrage du thread de surveillance d'arrivée
            Thread metricsLoadingThread = (new MetricsLoadingThread());
            metricsLoadingThread.start();

            //Attente saisie touche entrée pour arrêter
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Appuyer sur Entree pour arreter le serveur.");
            reader.readLine();

            //Arrêt des sous-threads
            activeMqBrokerThread.interrupt();
            metricsLoadingThread.interrupt();
            
            System.out.print("Les sous-threads sont arrêtés.");
            
        } catch (IOException | DeploymentException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            LOGGER.log(Level.INFO, "Arret du serveur");
            server.stop();
            LOGGER.log(Level.INFO, "Serveur arrêté");
        }
    }    
}
