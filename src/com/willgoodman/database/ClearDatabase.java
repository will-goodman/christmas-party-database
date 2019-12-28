package com.willgoodman.database;

import java.sql.*;

/**
 * Deletes all Christmas Party tables in the database to reset for a fresh run.
 * @author Will Goodman
 */
public class ClearDatabase {

	/**
	 * The main method which deletes all the Christmas Party tables in the database.
	 * @param args Required parameter for Java main methods, no data actually passed here.
	 */
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
				dbConn.close();
			}
		} catch (SQLException e) {
			System.out.println("SQLException " + e.getMessage());
		}
	}
}