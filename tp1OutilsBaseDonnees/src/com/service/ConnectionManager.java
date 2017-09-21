package com.service;

import java.sql.Connection;
import java.sql.DriverManager;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Classe représentant le gestionnaire de connexion
 * 
 * @author Gabriel Cyr et Marc-Antoine Béchard
 *
 */
public abstract class ConnectionManager {
	
	private static Session sshSession;

	public static Connection connectJDBC(String url, String user, String password) {

		openSSHSession();
		Connection connection = null;

		try {
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("Connexion réussie !");
		} catch (Exception e) {
			System.out.println("Problème de connexion: ");
			e.printStackTrace();
		}
		return connection;
	}

	public static void openSSHSession() {
		
		try {
			JSch jsch = new JSch();
			sshSession = jsch.getSession(Configuration.SSH_USERNAME, Configuration.SSH_HOSTNAME, 22);
			jsch.addIdentity(Configuration.SSH_PATH_KEY);
			
			configureSSH(sshSession);
			sshSession.connect();
			sshSession.setPortForwardingL(Configuration.SSH_LOCALPORT, Configuration.SSH_LOCALADRESS, Configuration.SSH_REMOTEPORT);
			System.out.println("Session créée");
		} catch (Exception e) {
			System.out.println("Problème de création de session: ");
			e.printStackTrace();
		}
		
	}
	
	public static void configureSSH(Session session) {
		java.util.Properties configurations = new java.util.Properties();
		configurations.put("StrictHostKeyChecking", "no");
		session.setConfig(configurations);
	}
	
	public static void closeSSHSession(){
		sshSession.disconnect();
	}
}
