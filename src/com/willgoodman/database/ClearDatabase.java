package com.willgoodman.database;

import java.sql.*;

//deletes all tables in the database
public class ClearDatabase {

	//constants
	private final static String DB_NAME = "";
	private final static String DB_USERNAME = "";
	private final static String DB_PASSWORD = "";

	public static void main(String[] args) {
		try {
			// open connection
			System.setProperty("jdbc.drivers", "org.postgresql.Driver");

			Connection dbConn = DriverManager.getConnection(DB_NAME, DB_USERNAME, DB_PASSWORD);

			try {

				// clear database
				PreparedStatement dropAll = dbConn.prepareStatement("DROP TABLE IF EXISTS Party, Entertainment, Venue, Menu");
				dropAll.executeUpdate();
				dropAll.close();

			} finally {
				// close connection
				dbConn.close();
			}
		} catch (SQLException e) {
			System.out.println("SQLException " + e.getMessage());
		}
	}
}