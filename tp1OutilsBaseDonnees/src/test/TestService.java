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
	String departement01;
	String departement02;
	Float totalSalaire;
	String email01;
	String email02;
	String email03;
	String email04;
	String email05;
	String email06;
	String email07;
	int nombreLignesSupprimees;
	
	@Before
	public void setUp() throws Exception {
		connexion = ConnectionManager.connectJDBC(Configuration.DB_URL, Configuration.DB_USER, Configuration.DB_PASSWD);
		connexion.setAutoCommit(false);
		
		departement01 = "ADMINISTRATIVE";
		departement02 = "MEDICAL";
		
		totalSalaire = 120000.00f;
		
		email01 = "b.filion@medi.com";
		email02 = "bennedict.flanderson@moo.com";
		email03 = "brunohoude@gmail.com";
		email04 = "cave.johnson@aperture.com";
		email05 = "guilac@gmail.com";
		email06 = "katy.reich@bones.com";
		email07 = "manon@gmail.com";
		
		nombreLignesSupprimees = 1;
		
	}

	@After
	public void tearDown() throws Exception {
		
		connexion.rollback();
		connexion.close();
		ConnectionManager.closeSSHSession();
		
		departement01 = null;
		departement02 = null;
		
		totalSalaire = null;
		
		email01 = null;
		email02 = null;
		email03 = null;
		email04 = null;
		email05 = null;
		email06 = null;
		email07 = null;
		
		nombreLignesSupprimees = 0;
	}
	
	/* requête 01 */
	
	@Test
	public void testGetDepartmentWomenNotLogistics(){
		String[] resultats = Service.get_departmentWomenNotLogistics(connexion);
		assertEquals(departement01, resultats[0]);
		assertEquals(departement02, resultats[1]);
	}
	
	/* requête 02 */
	
	@Test
	public void testGetTotalSalaryDoctors(){
		assertEquals(totalSalaire, (Object) Service.get_totalSalaryDoctors(connexion));
	}
	
	/* requête 03 */
	
	@Test
	public void testGetEmailsAsc(){
		String[] resultats = Service.get_emailsAsc(connexion);
		assertEquals(email01, resultats[0]);
		assertEquals(email02, resultats[1]);
		assertEquals(email03, resultats[2]);
		assertEquals(email04, resultats[3]);
		assertEquals(email05, resultats[4]);
		assertEquals(email06, resultats[5]);
		assertEquals(email07, resultats[6]);
	}
	
	/* requête 04 */
	
	@Test
	public void testDeleteSecretaryStaffFrom2005() throws SQLException {
		assertEquals(nombreLignesSupprimees, Service.delete_SecretaryStaffFrom2005(connexion));	
	}
	
	/* requête 05 */
	
	@Test
	public void testAddNoteToPatientWithDisease(){
		//String[] tableau = Service.get_totalSalaryDoctors(connexion);
	}
}
