package com.willgoodman.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Locale;

//actual program which works on the database
public class DatabaseInteraction {

	// constants
	private final static String CURRENCY_SYMBOL = "£";
	private final String DB_USERNAME;
	private final String DB_PASSWORD;
	private final String DB_NAME;

	public DatabaseInteraction(String dbName, String dbUsername, String dbPassword) {
		DB_USERNAME = dbUsername;
		DB_PASSWORD = dbPassword;
		DB_NAME = dbName;
	}

	// gets a on a party, given its pid
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
					report += 	"Party ID: " + Integer.toString(partyID) + "\n"
								+ "Party Name: " + partyName + "\n"
								+ "Venue Name: " + venueName + "\n"
								+ "Menu Description: " + menuDesc + "\n"
								+ "Entertainment Description: " + enterDesc + "\n"
								+ "Number of Guests: " + Integer.toString(numberOfGuests) + "\n"
								+ "Price: " + formattedPrice + "\n"
								+ "Total cost: " + formattedExpenses + "\n"
								+ "Net Profit: " + formattedProfit;
					//@formatter:on

				} else {
					report += "Party doesn't exist";
				}
			} finally {
				// close the database connection
				dbConn.close();

			}

		} catch (SQLException e) {
			report += "SQLException " + e.getMessage();
		}

		return report;
	}

	// gets a report on a menu, given its mid
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
					report += 	"MenuID: " + Integer.toString(menuID) + "\n"
								+ "Description: " + menuDesc + "\n"
								+ "Costprice: " + formattedCost + "\n"
								+ "Total number of guests: " + Integer.toString(totalGuests) + "\n"
								+ "Number of Parties: " + Integer.toString(numParties); 
					//@formatter:on
				} else {
					report += "Menu doesn't exist";
				}

			} finally {
				// close the database connection
				dbConn.close();
			}

		} catch (SQLException e) {
			report += "SQLException: " + e.getMessage();
		}

		return report;
	}

	// inserts a new party into the database, given its information
	public String insertParty(int partyID, String name, int menuID, int venueID, int enterID, int price,
			Timestamp tStamp, int numberOfGuests) {

		String result = "";

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
				// close the database connection
				dbConn.close();
			}

		} catch (SQLException e) {
			result = "SQLException: " + e.getMessage();
		}

		return result;
	}
	
	//takes the integer value stored in the database and reconverts it into a string in the correct currency format
	private String formatCurrency(int value) {
		String stringValue = Integer.toString(value);
		int decimalLocation = stringValue.length() - 2;
		String formattedValue = CURRENCY_SYMBOL + stringValue.substring(0,decimalLocation) + "." + stringValue.substring(decimalLocation);
		
		return formattedValue;
	}

}