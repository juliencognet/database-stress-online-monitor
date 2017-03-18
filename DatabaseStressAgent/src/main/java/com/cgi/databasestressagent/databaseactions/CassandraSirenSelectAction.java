/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent.databaseactions;

import com.cgi.databasestressagent.databaseactions.Cassandra.SiretRow;
import com.cgi.databasestressagent.databasesession.AbstractDatabaseSession;
import com.cgi.databasestressagent.databasesession.CassandraDatabaseSession;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julien
 */
public class CassandraSirenSelectAction implements AbstractDatabaseAction {

    private static final Logger logger = Logger.getLogger(CassandraSirenSelectAction.class.getName());
    
    @Override
    public String getActionName() {
        return "Sélection Cassandra dans le fichier SIREN de toutes les entreprises françaises de l'établissement 5420120,23";
    }

    @Override
    public boolean executeAction(AbstractDatabaseSession session) {
        try{
          CassandraDatabaseSession cassandraSession = (CassandraDatabaseSession)session;
          
          ResultSet resultSet = cassandraSession.getCassandraSession().execute("select siren,nic,l1_normalisee,l2_normalisee,l3_normalisee,l4_normalisee, "
                    + "l5_normalisee, l6_normalisee, l7_normalisee,apet700 from cgi_demo.siren where siren=? and nic=?",5420120,23);
            Row siretRow = resultSet.one();
            SiretRow row = new SiretRow();
            row.setNom1(siretRow.getString("l1_normalisee"));
            row.setNom2(siretRow.getString("l2_normalisee"));
            row.setAddr3(siretRow.getString("l3_normalisee"));
            row.setAddr4(siretRow.getString("l4_normalisee"));
            row.setAddr5(siretRow.getString("l5_normalisee"));
            row.setVille(siretRow.getString("l6_normalisee"));
            row.setPays(siretRow.getString("l7_normalisee"));
            row.setCodeNat(siretRow.getString("apet700"));          
          
            return true;
            
        } catch (Exception e){
            if (Thread.interrupted()){
                System.out.println("Thread de sollicitation DB interrompu");
            } else {
                logger.log(Level.SEVERE, "Impossible de sélectionner une entreprise dans le fichier SIREN : "+e.getMessage());
            }
            return false;            
        } 
    }
    
    
}
