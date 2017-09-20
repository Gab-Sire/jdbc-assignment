package test;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.service.ConnectionManager;
import com.service.Service;

public class TestService {
	
	Connection connexion;
	
	@Before
	public void setUp() throws Exception {
		connexion = ConnectionManager.connectJDBC(Service.DB_URL, Service.DB_USER, Service.DB_PASSWD);
	}

	@After
	public void tearDown() throws Exception {
		connexion.close();
		ConnectionManager.closeSSHSession();
	}

	

}
