package com.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public abstract class ConnectionManager {

	public static String SSH_HOSTNAME = "35.182.225.246";
	public static String SSH_USERNAME = "ubuntu";
	public static int SSH_LOCALPORT = 8890;
	public static int SSH_REMOTEPORT = 3306;
	public static String SSH_PATH_KEY = "C:/Users/portable/AMAZON/A17-420533-KP.pem";
	
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
			sshSession = jsch.getSession(SSH_USERNAME, SSH_HOSTNAME, 22);
			jsch.addIdentity(SSH_PATH_KEY);
			
			configureSSH(sshSession);
			sshSession.connect();
			sshSession.setPortForwardingL(SSH_LOCALPORT, "127.0.0.1", SSH_REMOTEPORT);
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
