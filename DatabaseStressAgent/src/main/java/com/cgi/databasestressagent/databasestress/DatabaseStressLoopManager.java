/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent.databasestress;

import com.cgi.databasestressagent.DatabaseStressParameters;
import com.cgi.databasestressagent.PeriodicMetricGathering.CounterSingleton;
import com.cgi.databasestressagent.databaseactions.AbstractDatabaseAction;
import com.cgi.databasestressagent.databaseactions.DatabaseActionFactory;
import com.cgi.databasestressagent.databasesession.AbstractDatabaseSession;
import com.cgi.databasestressagent.databasesession.DatabaseSessionFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julien
 */
public class DatabaseStressLoopManager extends Thread{
    
    private AbstractDatabaseSession session;
    private boolean shouldRun = true;
    private int delay;
    private String databaseAction;
    
    public void init(DatabaseStressParameters params) throws Exception{
        session = DatabaseSessionFactory.getDatabaseSessionByName(params.getDatabaseType());
        session.openDatabaseSession(params.getDatabaseNode(), params.getDatabasePort(), params.getDatabaseId(), params.getDatabaseUser(), params.getDatabasePassword());
        this.delay = params.getDelay();
        this.databaseAction=params.getStressAgentClass();
    }
    
    @Override
    public void run(){
        AbstractDatabaseAction action = DatabaseActionFactory.getDatabaseActionByName(this.databaseAction);
        try {       
            boolean result;
            while (shouldRun){
                Thread.sleep(this.delay);
                try{
                    result = action.executeAction(this.session);
                    CounterSingleton.getInstance().addResult(result);
                } catch (Exception ex){
                    CounterSingleton.getInstance().addResult(false);
                    Logger.getLogger(DatabaseStressLoopManager.class.getName()).log(Level.SEVERE, "Erreur execution", ex);                    
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(DatabaseStressLoopManager.class.getName()).log(Level.SEVERE, "Erreur globale d'exécution", ex);
        }
    }
    
    @Override
    public void interrupt(){
        session.closeDatabaseSesssion();
        super.interrupt();
    }
    
}
