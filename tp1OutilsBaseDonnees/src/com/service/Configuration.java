package com.service;

public interface Configuration {

	//constantes pour la session de ssh (tunneling)
	public static final String SSH_HOSTNAME = "35.182.225.246";
	public static final String SSH_USERNAME = "ubuntu";
	public static final int SSH_LOCALPORT = 8890;
	public static final int SSH_REMOTEPORT = 3306;
	public static final String SSH_PATH_KEY = "C:/Users/portable/AMAZON/A17-420533-KP.pem";
	
	//constantes pour la base de données
	/*public static final String DB_NAME = "coursDB";
	public static final String DB_USER = "root";
	public static final String DB_HOSTNAME = "localhost";
	public static final String DB_PASSWD = "toor";
	public static final String DB_PORT = "8890";
	public static final String DB_DRV = "com.mysql.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://" + DB_HOSTNAME +":" + DB_PORT + "/" + DB_NAME;*/
	
	public static final String DB_NAME = "coursdb";
	public static final String DB_USER = "root";
	public static final String DB_HOSTNAME = "localhost";
	public static final String DB_PASSWD = "lenneth11";
	public static final String DB_PORT = "3306";
	public static final String DB_DRV = "com.mysql.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://" + DB_HOSTNAME +":" + DB_PORT + "/" + DB_NAME;
	
	int QUERY_MAX_RESULTS = 30;
}