/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressserver.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 *
 * @author Julien
 */
public class MetricPoint {
    
    private ZonedDateTime pointDate;
    private int metricValue;

    public ZonedDateTime getPointDate() {
        return pointDate;
    }

    public void setPointDate(ZonedDateTime pointDate) {
        this.pointDate = pointDate;
    }

    public int getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(int metricValue) {
        this.metricValue = metricValue;
    }
    
    
    
    
}
