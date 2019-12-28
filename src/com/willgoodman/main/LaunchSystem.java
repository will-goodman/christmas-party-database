package com.willgoodman.main;

import com.willgoodman.database.*;
import com.willgoodman.ui.DatabaseUI;
import com.willgoodman.database.DatabaseKeys;


/**
 * Class to test the database.
 * @author Will Goodman
 */
public class LaunchSystem {

	private final static DatabaseInteraction INTERACTION = new DatabaseInteraction(DatabaseKeys.DB_NAME, DatabaseKeys.DB_USERNAME, DatabaseKeys.DB_PASSWORD);
	private final static DatabaseUI INTERFACE = new DatabaseUI(INTERACTION);

	/**
	 * Main method which generates, tests, and then destroys the database.
	 * @param args Required parameter for Java main methods, no data actually passed here.
	 */
	public static void main(String[] args) {
		
		GenerateDatabase.main(new String[0]);
		
		INTERFACE.runUI();
		
		ClearDatabase.main(new String[0]);
		
		
	}
	
	
}