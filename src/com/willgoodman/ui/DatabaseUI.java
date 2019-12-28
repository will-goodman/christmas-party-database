package com.willgoodman.ui;

import com.willgoodman.database.DatabaseInteraction;

import java.sql.*;
import java.util.Scanner;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;


/**
 * A basic CLI to interact with the DatabaseInteraction class
 * @author Will Goodman
 */
public class DatabaseUI {

	private final static String INPUT_MESSAGE = "What do you wish to do?\n1: Party report, 2: Menu report, 3: Insert a new party, 4: Quit";
	private final static Scanner INPUT = new Scanner(System.in);
	private final DatabaseInteraction DATABASE;
	private final static NumberFormat FORMATTER = NumberFormat.getNumberInstance(Locale.UK);

	/**
	 * Constructor for the class.
	 * @param interaction The DatabaseInteraction object to communicate with.
	 */
	public DatabaseUI(DatabaseInteraction interaction) {
		DATABASE = interaction;
	}

	/**
	 * Runs the UI.
	 */
	public void runUI() {
		// loop until the user wishes to quit
		boolean quit = false;
		while (!quit) {

			boolean validInput = false;
			String userInput;
			int intInput = 0;

			// loop until a valid user input is received
			while (!validInput) {
				// get the user's input
				System.out.println(INPUT_MESSAGE);
				userInput = INPUT.nextLine();
				/*
				 * if the user's input is not an integer, then loop again. Also, if it is not a
				 * valid input then loop again
				 */
				try {
					intInput = Integer.parseInt(userInput);
					if (intInput == 1 || intInput == 2 || intInput == 3 || intInput == 4) {
						validInput = true;
					}
				} catch (NumberFormatException e) {
					validInput = false;
				}

			}

			// do the action relating to the input
			switch (intInput) {
			case 1:
				getPartyReport();
				break;
			case 2:
				getMenuReport();
				break;
			case 3:
				insertParty();
				break;
			case 4:
				quit = true;
			}
		}
	}

	/**
	 * Retrieves a party report and displays it to the user.
	 */
	private void getPartyReport() {
		// take in the party ID
		String PARTY_ID_INPUT_MSG = "Please enter the party's ID";
		System.out.println(PARTY_ID_INPUT_MSG);

		String stringPartyID = INPUT.nextLine();

		try {

			int partyID = Integer.parseInt(stringPartyID);

			String report = DATABASE.getPartyReport(partyID);

			System.out.println(report);

		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Retrieves a menu report and displays it to the user.
	 */
	private void getMenuReport() {

		// input the mid
		String MENU_ID_INPUT_MSG = "Please enter the menu's ID";
		System.out.println(MENU_ID_INPUT_MSG);

		String stringMenuID = INPUT.nextLine();
		try {

			int menuID = Integer.parseInt(stringMenuID);

			String report = DATABASE.getMenuReport(menuID);

			System.out.println(report);

		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Inserts a new party into the database and informs the user whether the operation was successful.
	 */
	private void insertParty() {
		// as we will be parsing strings to integer, we need to catch any
		// NumberFormatExceptions
		try {
			// get the party details
			System.out.println("Insert the Party ID:");
			String stringPartyID = INPUT.nextLine();
			int partyID = Integer.parseInt(stringPartyID);

			System.out.println("Insert Party Name:");
			String name = INPUT.nextLine();

			System.out.println("Insert Menu ID:");
			String stringMenuID = INPUT.nextLine();
			int menuID = Integer.parseInt(stringMenuID);

			System.out.println("Insert Venue ID:");
			String stringVenueID = INPUT.nextLine();
			int venueID = Integer.parseInt(stringVenueID);

			System.out.println("Insert Entertainment ID:");
			String stringEnterID = INPUT.nextLine();
			int enterID = Integer.parseInt(stringEnterID);


			// the date and time, as well as the price must be in a certain format to be able to be parsed, hence
			// I must catch any errors
			try {
				
				System.out.println("Insert the quoted price:");
				String stringPrice = INPUT.nextLine();
	            int price = FORMATTER.parse(stringPrice.replaceAll("[£.]", "")).intValue();
	            if (!stringPrice.contains(".")) {
	            	price *= 100;
	            }
	            System.out.println(price);

				System.out.println("Insert the date and time:");
				String dateString = INPUT.nextLine();
				
				DateFormat dateFormat = DateFormat.getInstance();
				Date date = dateFormat.parse(dateString);
				Timestamp tStamp = new Timestamp(date.getTime());

				System.out.println("Insert the number of guests:");
				String stringNumberOfGuests = INPUT.nextLine();
				int numberOfGuests = Integer.parseInt(stringNumberOfGuests);

				String result = DATABASE.insertParty(partyID, name, menuID, venueID, enterID, price, tStamp,
						numberOfGuests);

				System.out.println(result);

			} catch (ParseException e) {
				System.out.println(e.getMessage());
			}
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
		}
	}

}
