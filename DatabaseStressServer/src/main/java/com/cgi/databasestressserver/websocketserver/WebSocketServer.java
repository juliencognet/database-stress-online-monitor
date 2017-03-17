/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressserver.websocketserver;

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

    private static Server server;
    
    public static void startServer()  {
        server = new Server("localhost", 8098, "/websockets", MetricsEndPoint.class);
        try {
            LOGGER.log(Level.INFO, "Lancement du serveur websockets");
            server.start();
            LOGGER.log(Level.INFO, "Serveur websockets démarré.");
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        } 
    }
    
    public static void stopServer(){
        LOGGER.log(Level.INFO, "Arret du serveur websockets");
        server.stop();
        LOGGER.log(Level.INFO, "Serveur websockets arrêté");
    }
}
