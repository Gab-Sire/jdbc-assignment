package com.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Classe représentant les fonctions de service (requêtes)
 * 
 * @author Gabriel Cyr et Marc-Antoine Béchard
 *
 */
public class Service {

	private static String[] results;
	private static ResultSet resultSet;
	private static String queryColumnName; 

	/**
	 * Fonction qui permet d'afficher le département du personnel de sexe féminin et qui ne sont pas logistique (requête 01)
	 * 
	 * @param connection la connexion JDBC à la base de données
	 * @return results le tableau des résultats sous forme String
	 */
	public static String[] get_departmentWomenNotLogistics(Connection connection) {

		queryColumnName = "departement";
		String query = "{CALL get_departmentWomenNotLogistics()}";
		resultSet = executeQueryStatement(connection, query);

		return getQueryResults(queryColumnName);
	}
	
	/**
	 * Fonction qui permet d'afficher le total des salaires des docteurs qui s’occupent des patients 
	 * dont l’âge varie entre 30 et 55 et qui habitent la mêmes ville (requête 02)
	 * 
	 * @param connection la connexion JDBC à la base de données
	 * @return results le tableau des résultats sous forme String
	 */
	public static Float get_totalSalaryDoctors(Connection connection) {

		queryColumnName = "totalSalaire";
		String query = "{CALL get_totalSalaryDoctors()}";
		resultSet = executeQueryStatement(connection, query);
		String[] resultsToParse = getQueryResults(queryColumnName);
		
		return Float.parseFloat(resultsToParse[0]); 
	}
	
	/**
	 * Fonction qui permet d'afficher en ordre croissant l’email du personnel technicien 
	 * dont les noms commencent par une lettre de l’intervalle [d..m] (requête 03)
	 * 
	 * @param connection la connexion JDBC à la base de données
	 * @return results le tableau des résultats sous forme String
	 */
	public static String[] get_emailsAsc(Connection connection) {

		queryColumnName = "email";
		String query = "{CALL get_emailsAsc()}";
		resultSet = executeQueryStatement(connection, query);

		return getQueryResults(queryColumnName);
	}
	
	/**
	 * Fonction qui permet de supprimer tout le personnel administrative de type « secrétariat » et qui sont entrés 
	 * en fonction l’année 2005 (requête 04)
	 * 
	 * @param connection la connexion JDBC à la base de données
	 * @return results le tableau des résultats sous forme String
	 */
	public static int delete_SecretaryStaffFrom2005(Connection connection) {

		int numberAffectedRows = 0;

		String query = "DELETE FROM tp01_personnel_per " +
						"WHERE per_emploi = 'SECRETARIAT' " + 
						"AND per_departement = 'ADMINISTRATIVE' " + 
						"AND DATE_FORMAT(per_date_embauche, '%Y') = '2005';";
		
		try {
			Statement statement = connection.createStatement();
			numberAffectedRows = statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return numberAffectedRows;
	}

	/**
	 * Fonction qui permet d'ajouter une « note » aux patients qui ont la maladie « XXX » 
	 * et qui sont traités par du personnel médical de type infirmier de sexe masculin (requête 05)
	 * 
	 * @param connection la connexion JDBC à la base de données
	 * @param maladieID l'identifiant de la maladie 
	 * @return results le tableau des résultats sous forme String
	 */
	public static String[] add_note_to_patient_with_disease(Connection connection, String nomMaladie) {

		queryColumnName = "";
		CallableStatement callableStatement;
		String[] insertResults = new String[Configuration.QUERY_MAX_RESULTS];
		String[] totalInsertResults = new String[Configuration.QUERY_MAX_RESULTS];
		int j = 0;
		
		//trouver l'id de la maladie de par son nom
		
		String query = "SELECT mal_id as maladieIDs "
					 + "FROM tp01_maladie_mal "
					 + "WHERE mal_nom = '" + nomMaladie + "';";

		resultSet = executeQueryStatement(connection, query);
		String[] maladieIDs = getQueryResults("maladieIDs");
		
		//insérer une note pour chaque id de maladie correspond au nom
		for(String maladieID: maladieIDs){
			
			query = "{CALL add_note_to_patient_with_disease(?, ?)}";
			
			try {
				callableStatement = connection.prepareCall(query);
				callableStatement.setString(1, "laifheiuh7");
				callableStatement.setString(2, maladieID);
				callableStatement.execute();
				resultSet = callableStatement.getResultSet();
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			insertResults = getQueryResults(queryColumnName);		
			totalInsertResults = ArrayUtils.addAll(totalInsertResults, insertResults);
		}
		return totalInsertResults;
	}
	
	/**
	 * Fonction qui permet d'exécuter une requête en JDBC
	 * 
	 * @param connection la connexion JDBC à la base de données
	 * @param query la requête (sous forme de procédure stockée)
	 * @return resultSet le resultat à interpréter
	 */
	public static ResultSet executeQueryStatement(Connection connection, String query) {
		
		resultSet = null;

		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			
			System.out.println("Requête effectuée avec succès");
		} catch (Exception e) {
			System.out.println("Problème avec la requête: ");
			e.printStackTrace();
		}
		return resultSet;
	}
	
	/**
	 * Fonction qui permet d'extraire les résultats d'une requête JDBC
	 * 
	 * @param queryColumnName nom de la colonne recherché dans la requête 
	 * @return results le tableau des résultats sous forme String
	 */
	public static String[] getQueryResults(String queryColumnName) {
		
		int i = 0;
		initialiseResultsArray();
		
		try {
			while (resultSet.next()) {
				if(queryColumnName != ""){
					results[i] = resultSet.getString(queryColumnName);
					System.out.println(results[i]);
				}
				else{
					results[i] = resultSet.getString(i + 1);
					System.out.println(results[i]);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}
	
	/**
	 * Fonction qui permet d'initialiser le tableau des résultats
	 */
	private static void initialiseResultsArray() {
		results = new String[Configuration.QUERY_MAX_RESULTS];
	}
}
