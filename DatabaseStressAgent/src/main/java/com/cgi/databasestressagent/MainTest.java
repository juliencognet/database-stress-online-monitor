/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent;

import com.cgi.databasestressagent.databaseactions.Cassandra.SiretId;
import com.cgi.databasestressagent.databaseactions.Cassandra.SiretRow;
import com.cgi.databasestressagent.databaseactions.Cassandra.CassandraConnector;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 *
 * @author Julien
 */
public class MainTest {
    
   private static final Logger logger = Logger.getLogger(MainTest.class.getName());

   public static SiretRow findOneSiretRow(Session session, SiretId siretId){
        ResultSet resultSet = session.execute("select siren,nic,l1_normalisee,l2_normalisee,l3_normalisee,l4_normalisee, "
                + "l5_normalisee, l6_normalisee, l7_normalisee,apet700 from cgi_demo.siren where siren=? and nic=?",siretId.getSiren(),siretId.getNic());
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
        
        return row;
   }
   
    public static void main(String[] args){
        CassandraConnector client = new CassandraConnector();
        client.connect("localhost", 9042);
        
        ArrayList<SiretId> siretIdList = new ArrayList<SiretId>();
        
        ResultSet resultSet = 
              client.getSession().execute("select siren,nic from cgi_demo.siren_par_ville where l6_normalisee=?","80100 ABBEVILLE");
        List<Row> siretRows = resultSet.all();
        
        for (Row siretRow : siretRows){
            SiretId siretId = new SiretId();
            siretId.setSiren(siretRow.getInt("siren"));
            siretId.setNic(siretRow.getInt("nic"));
            siretIdList.add(siretId);
        }
        
        int limit = 100;
        List<CompletableFuture<SiretRow>> futureList = new ArrayList<>(limit);
        ArrayList<SiretRow> resultList = new ArrayList<SiretRow>();
        for (SiretId id:siretIdList){
            boolean add;
            add = futureList.add(CompletableFuture.supplyAsync(() -> {
                return MainTest.findOneSiretRow(client.getSession(), id);
            }));
        }
        futureList.stream().map(CompletableFuture::join).forEach(resultList::add);
        
        client.close();
        int i=0;
        for (SiretRow row2: resultList){
            logger.info(row2.toString());
            i++;
        }
        logger.info("Nb results "+i);
        
    }
}
