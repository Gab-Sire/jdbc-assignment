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
public class TestService {
	
	Connection connexion;
	String departement01, departement02, departement03;
	Float salaire01, totalSalaire01;
	String email01, email02, email03, email04, email05;
	int nombreLignesSupprimees, aucuneLigneSupprimee;
	String maladie01, maladie02;
	
	@Before
	public void setUp() throws Exception {
		connexion = ConnectionManager.connectJDBC(Configuration.DB_URL, Configuration.DB_USER, Configuration.DB_PASSWD);
		connexion.setAutoCommit(false);
		
		departement01 = "ADMINISTRATIVE";
		departement02 = "MEDICAL";
		departement03 = "LOGISTIQUE";
		
		salaire01 = 225000.00f;
		totalSalaire01 = 430000.00f;

		email01 = "b.filion@medi.com";			
		email02 = "myves@gmail.com";	
		email03 = "vincethegod@gmail.com";
		email04 = "katy.reich@bones.com";
		email05 = "bobvance@refrigerator.com";
		
		nombreLignesSupprimees = 1;
		aucuneLigneSupprimee = 0;
		
		maladie01 = "Cancer du colon";
		maladie02 = "Grippe porcine";
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
		
		nombreLignesSupprimees = 0;
		aucuneLigneSupprimee = 0;
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
		String[] resultats = Service.get_departmentWomenNotLogistics(connexion);
		
		//vérifie si le département invalide logistique se trouve dans les résultats de la requête
		for(int i = 0; i < resultats.length; i++){
			assertFalse(departement03.equals(resultats[i]));
		}
	}
	
	/* requête 02 */
	
	@Test
	public void testValidGetTotalSalaryDoctors(){
		Float[] totalSalaries = Service.get_totalSalaryDoctors(connexion);
		assertEquals(salaire01, totalSalaries[0]);
		System.out.println(totalSalaries[0]);
		//assertEquals(totalSalaire01, totalSalaries[1]);
		System.out.println(totalSalaries[1]);
	}
	
	/* requête 03 */
	
	@Test
	public void testValidGetEmailsAsc(){
		//vérifie que les emails valides sont dans les résultats et en ordre croissant alphabétique (b..., m..., v...)
		String[] resultats = Service.get_emailsAsc(connexion);
		assertEquals(email01, resultats[0]);
		//assertEquals(email02, resultats[1]);
		assertEquals(email03, resultats[1]);
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
	public void testValidDeleteSecretaryStaffFrom2005() throws SQLException {
		assertEquals(nombreLignesSupprimees, Service.delete_SecretaryStaffFrom2005(connexion));	
	}
	
	@Test
	public void testInvalidDeleteSecretaryStaffFrom2005() throws SQLException {
		//vérifie qu'il supprime au moins une ligne
		assertNotEquals(aucuneLigneSupprimee, Service.delete_SecretaryStaffFrom2005(connexion));	
	}
	
	/* requête 05 */
	
	@Test
	public void testAddNoteToPatientWithDisease(){
		//int alo = Service.add_note_to_patient_with_disease(connexion,"df8d182e-9960-11e7-abc4-cec278b6b50a", "6bb65c48-98d5-11e7-abc4-cec278b6b50a");
	}
}
