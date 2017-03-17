/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressserver.websocketserver;

import com.cgi.databasestressserver.StopperSingleton;
import com.cgi.databasestressserver.domain.MetricPoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
/**
 *
 * @author Julien
 */
@ServerEndpoint("/valeurs")
public class MetricsEndPoint {
    private static final Logger logger = Logger.getLogger(MetricsEndPoint.class.getName());
    static Queue<Session> queue = new ConcurrentLinkedQueue<>();
    
    public static void send(MetricPoint point) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JSR310Module());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            String message = mapper.writeValueAsString(point);
            
            //logger.log(Level.INFO, "Should Send: {0} ", message + " to " + queue.size()+" sessions.");
            try {
                for (Session session : queue) {
                    session.getBasicRemote().sendText(message);
                    //logger.log(Level.INFO, "Send: {0} ", message + " to " + session.getId());
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, e.toString());
            }
        } catch (JsonProcessingException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @OnOpen
    public void open(Session session) {
        queue.add(session);
    }

    @OnMessage
    public void receivedMessage(String message){
        if (message.equals("STOP")){
           StopperSingleton.getInstance().stopExecution();
        } else if (message.equals("START")){
           StopperSingleton.getInstance().restartExecution();
        }
    }
            
    @OnClose
    public void close(Session session) {
        queue.remove(session);
    }

    @OnError
    public void error(Session session, Throwable t) {
        queue.remove(session);
    }    
}
