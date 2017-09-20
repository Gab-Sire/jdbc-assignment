/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import java.sql.Connection;

/**
 *
 * @author portable
 */
public class Main {
    
	static final String DB_NAME = "coursDB";
	static final String DB_DRV = "com.mysql.jdbc.Driver";
    static final String DB_USER = "root";
    static final String DB_PASSWD = "toor";
    static final String DB_URL = "jdbc:mysql://localhost:3306/coursdb";
    
    public static void main(String[] args) {
        
        try {
            Connection connexion = ConnectionManager.connectJDBC(DB_URL, DB_USER, DB_PASSWD);
            System.out.println(connexion);
            ConnectionManager.closeSSHSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
}
