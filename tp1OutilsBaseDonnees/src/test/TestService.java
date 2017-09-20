package test;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.service.Configuration;
import com.service.ConnectionManager;
import com.service.Service;

public class TestService {
	
	Connection connexion;
	
	@Before
	public void setUp() throws Exception {
		connexion = ConnectionManager.connectJDBC(Configuration.DB_URL, Configuration.DB_USER, Configuration.DB_PASSWD);
	}

	@After
	public void tearDown() throws Exception {
		connexion.close();
		ConnectionManager.closeSSHSession();
	}
	
	@Test
	public void testDisplayDepartmentWomenNotLogistics(){
		String[] tableau = Service.displayDepartmentWomenNotLogistics(connexion);
		System.out.println(tableau[0]);
		//assertEquals(true, Service.displayDepartmentWomenNotLogistics(connexion));
	}
	
	@Test
	public void testConnection() {
		assertEquals(connexion, ConnectionManager.connectJDBC(Configuration.DB_URL, Configuration.DB_USER, Configuration.DB_PASSWD));
	}
	
	

}
