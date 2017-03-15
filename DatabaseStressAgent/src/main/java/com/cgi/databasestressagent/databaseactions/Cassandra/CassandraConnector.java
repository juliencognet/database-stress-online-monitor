/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent.databaseactions.Cassandra;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import java.util.logging.Logger;

/**
 *
 * @author Julien
 */
public class CassandraConnector
{
   private static final Logger logger = Logger.getLogger(CassandraConnector.class.getName());
    
   /** Cassandra Cluster. */
   private Cluster cluster;
   /** Cassandra Session. */
   private Session session;
   /**
    * Connect to Cassandra Cluster specified by provided node IP
    * address and port number.
    *
    * @param node Cluster node IP address.
    * @param port Port of cluster host.
    */
   public void connect(final String node, final int port)
   {
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
      session = cluster.connect();
   }
   /**
    * Provide my Session.
    *
    * @return My session.
    */
   public Session getSession()
   {
      return this.session;
   }
   /** Close cluster. */
   public void close()
   {
      cluster.close();
   }
}
