package com.willgoodman.database;

import java.sql.*;

//deletes all tables in the database
public class ClearDatabase {

	public static void main(String[] args) {
		try {
			// open connection
			System.setProperty("jdbc.drivers", "org.postgresql.Driver");

			Connection dbConn = DriverManager.getConnection(DatabaseKeys.DB_NAME, DatabaseKeys.DB_USERNAME, DatabaseKeys.DB_PASSWORD);

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