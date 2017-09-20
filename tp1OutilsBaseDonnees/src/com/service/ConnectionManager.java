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

	private static Session sshSession;
	private static String sshHostName = "35.182.225.246";
	private static String sshUserName = "ubuntu";
	private static int localPort = 8890;
	private static int remotePort = 3306;
	private static String pathKey = "C:/Users/portable/AMAZON/A17-420533-KP.pem";

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
			sshSession = jsch.getSession(sshUserName, sshHostName, 22);
			jsch.addIdentity(pathKey);
			
			configureSSH(sshSession);
			sshSession.connect();
			sshSession.setPortForwardingL(localPort, "127.0.0.1", remotePort);
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
