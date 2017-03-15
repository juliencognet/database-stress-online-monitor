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
public class DatabaseSessionFactory {
    
    public static String DATABASE_CASSANDRA="Cassandra";
    public static String DATABASE_FAKE="FakeDatabase";
    
    public static AbstractDatabaseSession getDatabaseSessionByName(String databaseType) throws Exception{
        if (databaseType.equals(DatabaseSessionFactory.DATABASE_CASSANDRA)){
            return new CassandraDatabaseSession();
        } else if (databaseType.equals(DatabaseSessionFactory.DATABASE_FAKE)){
            return new FakeSession();
        } else {
            throw new Exception("Bad database name");
        }
    }
}
