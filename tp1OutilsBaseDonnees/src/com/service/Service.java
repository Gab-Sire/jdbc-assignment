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
	
	/**
	 * REQUETE 01
	 * Fonction qui permet d'afficher le département du personnel de sexe féminin et qui ne sont pas logistique.
	 * 
	 * @param connection la connexion à la base de données JDBC
	 * @return les départements valides
	 */
	public static String[] get_departmentWomenNotLogistics(Connection connection) {

		String queryColumnName = "departement";
		String query = "{CALL get_departmentWomenNotLogistics()}";
		ResultSet resultSet = executeQueryStatement(connection, query);

		return getQueryResults(resultSet, queryColumnName);
	}
	
	/**
	 * REQUETE 02
	 * Fonction qui permet d'afficher le total des salaires des docteurs qui s’occupent des patients dont l’âge varie entre 30 et 55 et qui habitent la mêmes ville
	 * 
	 * @param connection la connexion à la base de données JDBC
	 * @return salariesParsed les totaux de salaire valides par ville
	 */
	public static Float[] get_totalSalaryDoctors(Connection connection) {

		String queryColumnName = "totalSalaire";
		String query = "{CALL get_totalSalaryDoctors()}";
		ResultSet resultSet = executeQueryStatement(connection, query);
		String[] salariesToParse = getQueryResults(resultSet, queryColumnName);

		//convertit les salaires obtenus en float pour une comparaison optimale
		Float[] salariesParsed = convertStringArrayToFloatArray(salariesToParse);

		return salariesParsed;
	}
	
	/**
	 * REQUETE 03
	 * Fonction qui permet d'afficher en ordre croissant l’email du personnel technicien dont les noms commencent par une lettre de l’intervalle [d..m]
	 * 
	 * @param connection la connexion à la base de données JDBC
	 * @return les courriels valides en ordre ascendant
	 */
	public static String[] get_emailsAsc(Connection connection) {

		String queryColumnName = "email";
		String query = "{CALL get_emailsAsc()}";
		ResultSet resultSet = executeQueryStatement(connection, query);

		return getQueryResults(resultSet, queryColumnName);
	}
	
	/**
	 * REQUETE 04
	 * Fonction qui permet de supprimer tout le personnel administrative de type « secrétariat » et qui sont entrés en fonction l’année 2005
	 * 
	 * @param connection la connexion à la base de données JDBC
	 * @return numberAffectedRows le nombre d'enregistrement supprimés dans la table SQL personnel
	 */
	public static Integer delete_SecretaryStaffFrom2005(Connection connection) {

		Integer numberAffectedRows = 0;

		String query = "DELETE FROM tp01_personnel_per " + "WHERE per_emploi = 'SECRETARIAT' "
				+ "AND per_departement = 'ADMINISTRATIVE' " + "AND DATE_FORMAT(per_date_embauche, '%Y') = '2005';";

		try {
			Statement statement = connection.createStatement();
			numberAffectedRows = statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return numberAffectedRows;
	}
	
	/**
	 * REQUETE 05
	 * Fonction qui permet d'ajouter une « note » aux patients qui ont la maladie « XXX » et qui sont traités par du personnel médical 
	 * de type infirmier de sexe masculin
	 * 
	 * @param connection la connexion à la base de données JDBC
	 * @param auteurID l'identifiant de l'auteur de la note
	 * @param maladieID l'identifiant de la maladie
	 * @return numberAffectedRows le nombre de notes créées
	 */
	public static Integer add_note_to_patient_with_disease(Connection connection, String auteurID, String maladieID) {

		Integer numberInitialRows = countRowsTable(connection, "tp01_note_not");
		String queryColumnName = "";
		String query = "{CALL add_note_to_patient_with_disease(?, ?)}";
		CallableStatement callableStatement;

		try {
			callableStatement = connection.prepareCall(query);
			callableStatement.setString(1, auteurID);
			callableStatement.setString(2, maladieID);
			callableStatement.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer numberFinalRows = countRowsTable(connection, "tp01_note_not");
		Integer numberAffectedRows = calculateAffectedRows(numberInitialRows, numberFinalRows);

		return numberAffectedRows;
	}
	
	/**
	 * Fonction qui permet d'exécuter une requête
	 * 
	 * @param connection la connexion à la base de données JDBC
	 * @param query la requête SQL
	 * @return resultSet les résultats de la requête sous forme objet
	 */
	public static ResultSet executeQueryStatement(Connection connection, String query) {
		
		ResultSet resultSet = null;
		
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	/**
	 * Fonction qui permet de convertir les résultats sous forme objet en un tableau String
	 * 
	 * @param resultSet les résultats sous forme objet
	 * @param queryColumnName la colonne de la table SQL qui contient l'information de la requête
	 * @return results le tableau des résultats sous forme String
	 */
	public static String[] getQueryResults(ResultSet resultSet, String queryColumnName) {

		int i = 0;
		String[] results = new String[Configuration.QUERY_MAX_RESULTS];

		try {
			while (resultSet.next()) {
				if (queryColumnName != "") {
					results[i] = resultSet.getString(queryColumnName);
				} else {
					results[i] = resultSet.getString(i + 1);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}
	
	/**
	 * Fonction qui permet de convertir un tableau de String en tableau de Float
	 * 
	 * @param resultsToParse le tableau de String à convertir
	 * @return resultsParsed le tableau de Float
	 */
	public static Float[] convertStringArrayToFloatArray(String[] resultsToParse) {

		Float[] resultsParsed = new Float[Configuration.ARRAY_SIZE];
		
		//copie et insère les données String converties en Float
		for (int i = 0; i < resultsToParse.length; i++) {
			
			if (null != resultsToParse[i]) {
				resultsParsed[i] = Float.parseFloat(resultsToParse[i]);
			}
		}
		return resultsParsed;
	}
	
	/**
	 * Fonction qui permet de compter le nombre d'enregistrements dans un table SQL
	 * 
	 * @param connection la connexion à la base de données JDBC
	 * @param tableName le nom de la table SQL
	 * @return le nombre d'enregristrements trouvés
	 */
	public static int countRowsTable(Connection connection, String tableName) {

		int numberRows = 0;

		String query = "SELECT COUNT(*) AS rowCount " + "FROM " + tableName + ";";

		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			resultSet.next();
			numberRows = resultSet.getInt("rowCount");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return numberRows;
	}
	
	/**
	 * Fonction qui permet calculer le nombre d'enregistrements affectés par une instruction Data Manipulation Language
	 * 
	 * @param numberInitialRows le nombre d'enregistrements initial de la table SQL
	 * @param numberFinalRows le nombre d'enregistrements final de la table SQL
	 * @return numberAffectedRows le nombre d'enregistrements affectés dans la table SQL
	 */
	public static int calculateAffectedRows(int numberInitialRows, int numberFinalRows) {
		int numberAffectedRows = numberFinalRows - numberInitialRows;
		return numberAffectedRows;
	}
}
