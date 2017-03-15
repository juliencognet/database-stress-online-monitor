/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent.databasesession;

import com.cgi.databasestressagent.databaseactions.Cassandra.CassandraConnector;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import java.util.logging.Logger;

/**
 *
 * @author Julien
 */
public class CassandraDatabaseSession implements AbstractDatabaseSession{

   /** Cassandra Cluster. */
   private Cluster cluster;
   /** Cassandra Session. */
   private Session session;    
   /** Logger */
   private static final Logger logger = Logger.getLogger(CassandraDatabaseSession.class.getName());   
    
    @Override
   /**
    * Connect to Cassandra Cluster specified by provided node IP
    * address and port number.
    */
    public AbstractDatabaseSession openDatabaseSession(String node, int port, String databaseId, String user, String password) {
      this.cluster = Cluster.builder()
                            .addContactPoint(node)
                            .withPort(port)
                            .build();
      final Metadata metadata = cluster.getMetadata();
      logger.info("Connected to cluster: "+ metadata.getClusterName());
      for (final Host host : metadata.getAllHosts())
      {
         logger.info("Datacenter: "+host.getDatacenter()
                 + " Host: "+host.getAddress()
                 + " Rack: "+host.getRack());
      }
      this.session = cluster.connect();
      
      return this;
    }

    @Override
    public void closeDatabaseSesssion() {
        cluster.close();
    }

    @Override
    public AbstractDatabaseSession getSession() {
        return this;
    }
    
    public Session getCassandraSession(){
        return this.session;
    }
    
}
