/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent.databaseactions;

import com.cgi.databasestressagent.databasesession.AbstractDatabaseSession;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julien
 */
public class FakeAction implements AbstractDatabaseAction{

    @Override
    public String getActionName() {
        return "Fake Action";
    }

    @Override
    public boolean executeAction(AbstractDatabaseSession session) {
        try {
            Random randomGenerator = new Random();
            int actionDelay = randomGenerator.nextInt(50);
            Thread.sleep(actionDelay);
            if (actionDelay<10){
                return false;
            } else {
                return true;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(FakeAction.class.getName()).log(Level.SEVERE, "Imposible d'attendre", ex);
            return false;
        }
    }
    
    
}
