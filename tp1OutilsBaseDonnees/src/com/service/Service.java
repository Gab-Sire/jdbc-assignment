package com.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Service {
	
	public static final int MAX_RESULTS = 30;
	
	//constantes pour la base de données
	public static final String DB_NAME = "coursDB";
    public static final String DB_USER = "root";
    public static final String DB_HOSTNAME = "localhost";
    public static final String DB_PASSWD = "toor";
    public static final String DB_PORT = "8890";
	public static final String DB_DRV = "com.mysql.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://" + DB_HOSTNAME +":" + DB_PORT + "/" + DB_NAME;
    
    private static String[] results;
    
	public static String[] executeStatement(Connection connection, String query) {

		int i = 0;
		results = new String[MAX_RESULTS];
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			while (resultSet.next()) {
				results[i] = resultSet.getString(i+1);
				i++;
			}
			System.out.println("Requête effectuée avec succès");
		} catch (Exception e) {
			System.out.println("Problème avec la requête: ");
			e.printStackTrace();
		}
		
		return results;
	}
	
	public static String[] displayDepartmentWomenNotLogistics(Connection connection){
		
		String query = "use tp01; " +
						"SELECT per_nom" + 
						"FROM tp01_personnel_per " + 
						"WHERE per_sexe = 'F' " + 
						  "AND per_emploi != 'LOGISTIQUE';";
		
		results = executeStatement(connection, query);
		return results;
	}
	 
	public static String[] displayTotalSalaryDoctors(Connection connection){
		
		String query = "use tp01;" + 
						"SELECT SUM(per_salaire), per_adresse " +
						"FROM tp01_personnel_per " + 
						"INNER JOIN tp01_note_not ON not_id_auteur = per_id " + 
						"WHERE per_emploi = 'DOCTEUR' " +
						"AND not_discriminant = 'A' " + 
						"AND 0 < 	(SELECT COUNT(pat_id) " + 
									"FROM tp01_patient_pat " + 
						            "WHERE not_id_patient = pat_id " + 
						            "AND (DATE_FORMAT(NOW(), '%Y') - " + 
						            "DATE_FORMAT(pat_date_naissance, '%Y') " + 
						            "BETWEEN 35 AND 55)) " + 
						"GROUP BY per_adresse";
		
		results = executeStatement(connection, query);
		return results;
	}
}