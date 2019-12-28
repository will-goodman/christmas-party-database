package com.willgoodman.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


/**
 * Class used to interact with the database.
 * @author Will Goodman
 */
public class DatabaseInteraction {

	private final static String CURRENCY_SYMBOL = "£";
	private final String DB_USERNAME;
	private final String DB_PASSWORD;
	private final String DB_NAME;

	/**
	 * Constructs a new object which will interact with a specific database.
	 * @param dbName The name of the database to connect to.
	 * @param dbUsername The username for database login.
	 * @param dbPassword The password for database login.
	 */
	public DatabaseInteraction(String dbName, String dbUsername, String dbPassword) {
		DB_USERNAME = dbUsername;
		DB_PASSWORD = dbPassword;
		DB_NAME = dbName;
	}

	/**
	 * Retrieves a report on a party.
	 * @param partyID The ID of the party to return a report on.
	 * @return The party report.
	 */
	public String getPartyReport(int partyID) {

		String report = "";

		try {
			// Connect to the database
			Connection dbConn = DriverManager.getConnection(DB_NAME, DB_USERNAME, DB_PASSWORD);

			try {
				// form the SQL query
				//@formatter:off
				PreparedStatement selectAll = 	dbConn.prepareStatement("SELECT Party.pid, Party.name, Party.numberofguests, Party.price, Venue.name, Venue.venuecost, Menu.description, Menu.costprice, Entertainment.description, Entertainment.costprice " + 
																		"FROM Party " +
																			"INNER JOIN Venue " + 			
																				"ON Venue.vid = Party.vid " +
																			"INNER JOIN Menu " +
																				"ON Menu.mid = Party.mid " +
																			"INNER JOIN Entertainment " +
																				"ON Entertainment.eid = Party.mid " +
																		"WHERE Party.pid = ?");
				//@formatter:on

				// set the query to have the correct pid
				selectAll.setInt(1, partyID);

				// get the results of the query
				ResultSet allRS = selectAll.executeQuery();

				String partyName = "";
				int numberOfGuests = 0;
				int price = 0;
				String venueName = "";
				int venueCost = 0;
				String menuDesc = "";
				int menuCost = 0;
				String enterDesc = "";
				int enterCost = 0;

				// this boolean stores whether or not any results were returned by the query
				boolean foundParty = false;
				while (allRS.next()) {
					foundParty = true;
					partyName = allRS.getString(2);
					numberOfGuests = allRS.getInt(3);
					price = allRS.getInt(4);
					venueName = allRS.getString(5);
					venueCost = allRS.getInt(6);
					menuDesc = allRS.getString(7);
					menuCost = allRS.getInt(8);
					enterDesc = allRS.getString(9);
					enterCost = allRS.getInt(10);
				}
				
				int partyExpenses = venueCost + (menuCost * numberOfGuests) + enterCost;
				int profit = price - partyExpenses;
				
				// close the prepared statement
				selectAll.close();

				// if a result was found, we can print it
				if (foundParty) {
					
					String formattedPrice = formatCurrency(price);
					String formattedExpenses = formatCurrency(partyExpenses);
					String formattedProfit = formatCurrency(profit);
					
					//@formatter:off
					report += 	"Party ID: " + partyID + "\n"
								+ "Party Name: " + partyName + "\n"
								+ "Venue Name: " + venueName + "\n"
								+ "Menu Description: " + menuDesc + "\n"
								+ "Entertainment Description: " + enterDesc + "\n"
								+ "Number of Guests: " + numberOfGuests + "\n"
								+ "Price: " + formattedPrice + "\n"
								+ "Total cost: " + formattedExpenses + "\n"
								+ "Net Profit: " + formattedProfit;
					//@formatter:on

				} else {
					report += "Party doesn't exist";
				}
			} finally {
				dbConn.close();
			}

		} catch (SQLException e) {
			report += "SQLException " + e.getMessage();
		}

		return report;
	}

	/**
	 * Retrieves a report on a menu.
	 * @param menuID The ID of the menu to be reported.
	 * @return The menu report.
	 */
	public String getMenuReport(int menuID) {

		String report = "";

		try {
			// connect to the database
			Connection dbConn = DriverManager.getConnection(DB_NAME, DB_USERNAME, DB_PASSWORD);

			try {

				// prepare the query
				//@formatter:off
				PreparedStatement selectBoth = dbConn.prepareStatement("SELECT Menu.mid, Menu.description, Menu.costprice, SUM(Party.numberofguests), COUNT(Party.pid) " +
																		"FROM Menu " + 
																			"INNER JOIN Party " + 
																				"ON Party.mid = Menu.mid " +
																		"WHERE Menu.mid = ? " + 
																		"GROUP BY Menu.mid");
				//@formatter:on

				// add the mid to the query
				selectBoth.setInt(1, menuID);

				// get the results from the query
				ResultSet bothRS = selectBoth.executeQuery();

				String menuDesc = "";
				int menuCost = 0;
				int totalGuests = 0;
				int numParties = 0;
				boolean foundMenu = false;
				while (bothRS.next()) {
					foundMenu = true;
					menuDesc = bothRS.getString("description");
					menuCost = bothRS.getInt("costprice");
					totalGuests = bothRS.getInt("sum");
					numParties = bothRS.getInt("count");
				}
				
				// if the query returned a result, then we can print it
				if (foundMenu) {
					String formattedCost = formatCurrency(menuCost);
					//@formatter:off
					report += 	"MenuID: " + menuID + "\n"
								+ "Description: " + menuDesc + "\n"
								+ "Costprice: " + formattedCost + "\n"
								+ "Total number of guests: " + totalGuests + "\n"
								+ "Number of Parties: " + numParties;
					//@formatter:on
				} else {
					report += "Menu doesn't exist";
				}

			} finally {
				dbConn.close();
			}

		} catch (SQLException e) {
			report += "SQLException: " + e.getMessage();
		}

		return report;
	}

	/**
	 * Inserts a new party into the database.
	 * @param partyID The ID of the new party.
	 * @param name The name of the party.
	 * @param menuID The ID of the menu for the party.
	 * @param venueID The ID of the venue for the party.
	 * @param enterID The ID of the entertainment for the party.
	 * @param price The price of the party.
	 * @param tStamp The timestamp for the Date/Time of the start of the party.
	 * @param numberOfGuests The number of guests attending the party.
	 * @return Whether the new party was added or not.
	 */
	public String insertParty(int partyID, String name, int menuID, int venueID, int enterID, int price,
			Timestamp tStamp, int numberOfGuests) {

		String result;

		try {
			// connect to the database
			Connection dbConn = DriverManager.getConnection(DB_NAME, DB_USERNAME, DB_PASSWORD);

			try {

				// create the statement
				PreparedStatement insertParty = dbConn
						.prepareStatement("INSERT INTO Party VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

				// add the party details to the statement
				insertParty.setInt(1, partyID);
				insertParty.setString(2, name);
				insertParty.setInt(3, menuID);
				insertParty.setInt(4, venueID);
				insertParty.setInt(5, enterID);
				insertParty.setInt(6, price);
				insertParty.setTimestamp(7, tStamp);
				insertParty.setInt(8, numberOfGuests);

				// update the database
				insertParty.executeUpdate();

				// close the prepared statement
				insertParty.close();

				result = "Success!";

			} finally {
				dbConn.close();
			}

		} catch (SQLException e) {
			result = "SQLException: " + e.getMessage();
		}

		return result;
	}

	/**
	 * Converts an integer value from the database back into a string in the correct currency format.
	 * @param value The integer value from the database.
	 * @return The value converted into the correct currency format as a string.
	 */
	private String formatCurrency(int value) {
		String stringValue = Integer.toString(value);
		int decimalLocation = stringValue.length() - 2;
		String formattedValue = CURRENCY_SYMBOL + stringValue.substring(0,decimalLocation)
								+ "." + stringValue.substring(decimalLocation);
		
		return formattedValue;
	}

}