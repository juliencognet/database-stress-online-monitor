/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressserver.consolidationdatabase;

import com.cgi.databasestressserver.domain.PeriodResult;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julien
 */
public class ConsolidationDatabaseManager {
    
    private static Connection conn;
    
    public static void initConnection() throws SQLException{
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.
                    getConnection("jdbc:h2:~/test","StressDB","StressDB");        
                    //getConnection("jdbc:h2:mem:","StressDB","StressDB");                            
            System.out.println("Connection to H2 DB OK");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConsolidationDatabaseManager.class.getName()).log(Level.SEVERE, "Driver H2 non trouvé", ex);
        }
    }
    
    public static void createDatabase(){
        Statement stmt = null;
        try {
            System.out.println("About to create metrics table");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS METRICS(AGENT_INFORMATION VARCHAR2(500), "
                    + " ACTION_NAME VARCHAR2(500), "
                    + " START_INTERVAL TIMESTAMP, "
                    + " END_INTERVAL TIMESTAMP, "
                    + " NB_SUCCESS INTEGER, "
                    + " NB_ERRORS INTEGER)";
            stmt.executeUpdate(sql);
            System.out.println("Table METRICS created");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (stmt!=null){
                try{
                    stmt.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static Timestamp convertZoneDateTimeToTimestamp(ZonedDateTime datetime){
        return new Timestamp(datetime.toInstant().toEpochMilli());
    }
    
    public static void insertPeriodResult(PeriodResult period){
        PreparedStatement stmt = null;
        try {
            System.out.println("About to insert into metrics");
            String sql = "INSERT INTO METRICS(AGENT_INFORMATION,ACTION_NAME,START_INTERVAL,END_INTERVAL,"
                    + " NB_SUCCESS, NB_ERRORS) VALUES (?,?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, period.getAgentInformation());
            stmt.setString(2,period.getActionName());
            stmt.setTimestamp(3, convertZoneDateTimeToTimestamp(period.getStartInterval()));
            stmt.setTimestamp(4, convertZoneDateTimeToTimestamp(period.getEndInterval()));
            stmt.setInt(5, period.getNbExecutions());
            stmt.setInt(6, period.getNbErrors());
            stmt.executeUpdate();
            conn.commit();            
            System.out.println("Data inserted "+period.getStartInterval().toString());

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (stmt!=null){
                try{
                    stmt.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }        
    }

    public static ArrayList<PeriodResult> getConsolidatedMetricsOfLastThreeSeconds(){
        ArrayList<PeriodResult> results = new ArrayList<PeriodResult>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select PARSEDATETIME(to_char(end_interval,'yyyy-MM-dd HH24:mi:ss'),'yyyy-MM-dd HH:mm:ss','en','GMT+1') as hms"
                      + ",sum(nb_success) success, sum(nb_errors) errors " +
                        "from metrics " +
                        "where end_interval < sysdate-0.000034722 " +
                        " and end_interval > sysdate-0.05 " +                    
                        "group by PARSEDATETIME(to_char(end_interval,'yyyy-MM-dd HH24:mi:ss'),'yyyy-MM-dd HH:mm:ss','en','GMT+1') " +
                        "order by 1 desc";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            int i=0;
            while (rs.next()){
                PeriodResult period = new PeriodResult();
                //period.setActionName(rs.getString("action_name"));
                period.setStartInterval(ZonedDateTime.ofInstant(rs.getTimestamp("hms").toInstant(),ZoneId.of("GMT+1")));
                period.setNbExecutions(rs.getInt("success"));
                period.setNbErrors(rs.getInt("errors"));
                results.add(period);
                i++;
            }
            System.out.println("Got "+i+ " results from consolidated query.");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (rs!=null){
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ConsolidationDatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (stmt!=null){
                try{
                    stmt.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }        
        return results;
    }

    
    public static void closeConnection(){
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConsolidationDatabaseManager.class.getName()).log(Level.SEVERE, "Unable to close connection", ex);
        }
    }
    
}
