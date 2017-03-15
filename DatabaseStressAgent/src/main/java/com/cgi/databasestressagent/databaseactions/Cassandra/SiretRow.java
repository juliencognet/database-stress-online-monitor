/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent.databaseactions.Cassandra;

/**
 *
 * @author Julien
 */
public class SiretRow {
    private int siren;
    private int nic;
    private String nom1;
    private String nom2;
    private String addr3;
    private String addr4;
    private String addr5;
    private String ville;
    private String pays;
    private String codeNat;

    public int getSiren() {
        return siren;
    }

    public void setSiren(int siren) {
        this.siren = siren;
    }

    public int getNic() {
        return nic;
    }

    public void setNic(int nic) {
        this.nic = nic;
    }

    public String getNom1() {
        return nom1;
    }

    public void setNom1(String nom1) {
        this.nom1 = nom1;
    }

    public String getNom2() {
        return nom2;
    }

    public void setNom2(String nom2) {
        this.nom2 = nom2;
    }

    public String getAddr3() {
        return addr3;
    }

    public void setAddr3(String addr3) {
        this.addr3 = addr3;
    }

    public String getAddr4() {
        return addr4;
    }

    public void setAddr4(String addr4) {
        this.addr4 = addr4;
    }

    public String getAddr5() {
        return addr5;
    }

    public void setAddr5(String addr5) {
        this.addr5 = addr5;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getCodeNat() {
        return codeNat;
    }

    public void setCodeNat(String codeNat) {
        this.codeNat = codeNat;
    }
    
    public String toString(){
        return this.nom1+" "+this.nom2+" / Adresse: "+this.addr3+" "+this.addr4+" "+this.addr5+" "+this.ville+" "+this.pays+" / Code NAT: "+this.codeNat;
    }
}
