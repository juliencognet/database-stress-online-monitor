/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgi.databasestressagent.databaseactions;

import com.cgi.databasestressagent.databasesession.AbstractDatabaseSession;

/**
 * Interface permettant d'implémenter des actions
 * @author Julien
 */
public interface AbstractDatabaseAction {
    //Retourne le nom de l'action
    public String getActionName();
    
    //Exécute l'action de stress - le résultat retourné est le résultat d'exécution
    // true : succès
    // false : échec
    public boolean executeAction(AbstractDatabaseSession session);
}
