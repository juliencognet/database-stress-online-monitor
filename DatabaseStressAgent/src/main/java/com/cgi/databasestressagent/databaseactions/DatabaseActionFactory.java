/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent.databaseactions;

/**
 * Cette factory retourne la bonne action en fonction de ce qui est passé en nom
 * @author Julien
 */
public class DatabaseActionFactory {
    
    public final static String CST_FAKE_ACTION = "FakeAction";
    public final static String CST_CASSANDRA_SIREN_SELECT_ACTION = "CassandraSirenSelectAction";
    
    public static AbstractDatabaseAction getDatabaseActionByName(String name){
        if (name.equals(CST_CASSANDRA_SIREN_SELECT_ACTION)){
            return new CassandraSirenSelectAction();
        } else {
            return new FakeAction();            
        }
    }
}
