/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent.databasesession;

/**
 *
 * @author Julien
 */
public class FakeSession implements AbstractDatabaseSession{

    @Override
    public AbstractDatabaseSession openDatabaseSession(String node, int port, String database, String user, String password) {
        return this;
    }

    @Override
    public AbstractDatabaseSession getSession() {
        return this;
    }

    @Override
    public void closeDatabaseSesssion() {
        //Rien à implémenter
    }
    
}
