package test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.service.Configuration;
import com.service.ConnectionManager;
import com.service.Service;

/**
 * Classe de tests pour les fonctions de service (requêtes)
 * 
 * @author Gabriel Cyr et Marc-Antoine Béchard
 *
 */
public class TestServiceRequetes {
	
	Connection connexion;
	String departement01, departement02, departement03;
	Float salaire01, salaire02, totalSalaire01;
	String email01, email02, email03, email04, email05;
	Integer nombreLignesSupprimees;
	String auteurID, maladieID;
	Integer nombreLignesInserees;
	
	@Before
	public void setUp() throws Exception {
		connexion = ConnectionManager.connectJDBC(Configuration.DB_URL, Configuration.DB_USER, Configuration.DB_PASSWD);
		connexion.setAutoCommit(false);
		
		departement01 = "ADMINISTRATIVE";
		departement02 = "MEDICAL";
		departement03 = "LOGISTIQUE";
		
		salaire01 = 225000.00f;									//salaire de Milenne Breton
		salaire02 = 10000.00f;									//salaire invalide de la secrétaire Minawa Suzuki
		totalSalaire01 = 430000.00f;							//total des salaires valides de Saint-Eustache

		email01 = "b.filion@medi.com";							//valide, F_ilion
		email02 = "myves@gmail.com";							//valide, G_uillemette
		email03 = "vincethegod@gmail.com";  					//valide, L_anglois
		email04 = "katy.reich@bones.com";						//invalide, docteur mais D_eschannel
		email05 = "bobvance@refrigerator.com";					//invalide, technicien mais V_ance
		
		nombreLignesSupprimees = 1;								//le regretté secrétaire Pepito Rodriguez
		
		auteurID = "df8d1cb6-9960-11e7-abc4-cec278b6b50a";		//le docteur George Wallace (pas de restriction sur l'auteur)
		maladieID = "6bb65c48-98d5-11e7-abc4-cec278b6b50a";		//cancer de l'estomac
		nombreLignesInserees = 1;								//note pour la patiente Julie Gauthier traitée par l'infirmier Yves Guillemette
	}

	@After
	public void tearDown() throws Exception {
		connexion.rollback();
		connexion.close();
		ConnectionManager.closeSSHSession();
		
		departement01 = null;
		departement02 = null;
		departement03 = null;
		
		salaire01 = null;
		totalSalaire01 = null;
		
		email01 = null;		
		email02 = null;
		email03 = null;		
		email04 = null;
		email05 = null;
		
		nombreLignesSupprimees = null;
		
		auteurID = null;
		maladieID = null;
		nombreLignesInserees = null;
	}
	
	/* requête 01 */
	
	@Test
	public void testValidGetDepartmentWomenNotLogistics(){
		String[] resultats = Service.get_departmentWomenNotLogistics(connexion);
		assertEquals(departement01, resultats[0]);
		assertEquals(departement02, resultats[1]);
	}
	
	@Test
	public void testInvalidGetDepartmentWomenNotLogistics(){
		String[] departements = Service.get_departmentWomenNotLogistics(connexion);
		
		//vérifie si le département invalide logistique se trouve dans les résultats de la requête
		for(int i = 0; i < departements.length; i++){
			assertFalse(departement03.equals(departements[i]));
		}
	}
	
	/* requête 02 */
	
	@Test
	public void testValidGetTotalSalaryDoctors(){
		Float[] totalSalaries = Service.get_totalSalaryDoctors(connexion);
		assertEquals(salaire01, totalSalaries[0]);
		assertEquals(totalSalaire01, totalSalaries[1]);
	}
	
	@Test
	public void testInvalidGetTotalSalaryDoctors(){
		Float[] totalSalaries = Service.get_totalSalaryDoctors(connexion);
		
		//vérifie si le salaire invalide 10 000 est présent dans la liste des résultats
		for(int i = 0; i < totalSalaries.length; i++){
			assertFalse(salaire02.equals(totalSalaries[i]));
		}
	}
	
	/* requête 03 */
	
	@Test
	public void testValidGetEmailsAsc(){
		//vérifie que les emails valides sont dans les résultats et en ordre croissant alphabétique (b..., m..., v...)
		String[] emails = Service.get_emailsAsc(connexion);
		assertEquals(email01, emails[0]);
		assertEquals(email02, emails[1]);
		assertEquals(email03, emails[2]);
	}
	
	@Test
	public void testInvalidGetEmailsAsc(){
		String[] resultats = Service.get_emailsAsc(connexion);
		
		for(int i = 0; i < resultats.length; i++){
			/*vérifie qu'une personne non technicienne (docteur) ayant un nom dont la première lettre 
			 * se situe entre d et m ne se trouve pas dans les résultats*/ 
			assertFalse(email04.equals(resultats[i]));
			/*vérifie qu'une personne technicienne ayant un nom dont la première lettre ne 
			 * se situe pas entre d et m ne se trouve pas dans les résultats (Vance)*/
			assertFalse(email05.equals(resultats[i]));
		}
	}
	
	/* requête 04 */
	
	@Test
	public void testDeleteSecretaryStaffFrom2005() throws SQLException {
		assertEquals(nombreLignesSupprimees, Service.delete_SecretaryStaffFrom2005(connexion));	
	}

	
	/* requête 05 */
	
	@Test
	public void testAddNoteToPatientWithDisease(){
		assertEquals(nombreLignesInserees, Service.add_note_to_patient_with_disease(connexion, auteurID, maladieID));
	}
}
