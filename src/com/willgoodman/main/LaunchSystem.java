package com.willgoodman.main;

import com.willgoodman.database.*;
import com.willgoodman.ui.DatabaseUI;
import com.willgoodman.database.DatabaseKeys;

//main program which launches all other classes
public class LaunchSystem {

	private final static DatabaseInteraction INTERACTION = new DatabaseInteraction(DatabaseKeys.DB_NAME, DatabaseKeys.DB_USERNAME, DatabaseKeys.DB_PASSWORD);
	private final static DatabaseUI INTERFACE = new DatabaseUI(INTERACTION);


	public static void main(String[] args) {
		
		GenerateDatabase.main(new String[0]);
		
		INTERFACE.runUI();
		
		ClearDatabase.main(new String[0]);
		
		
	}
	
	
}