package com.willgoodman.main;

import com.willgoodman.database.*;
import com.willgoodman.ui.DatabaseUI;

//main program which launches all other classes
public class LaunchSystem {
	
	
	private final static String DB_USERNAME = "";
	private final static String DB_PASSWORD = "";
	private final static String DB_NAME = "";
	private final static DatabaseInteraction INTERACTION = new DatabaseInteraction(DB_NAME, DB_USERNAME, DB_PASSWORD);
	private final static DatabaseUI INTERFACE = new DatabaseUI(INTERACTION);
	
	
	
	public static void main(String[] args) {
		
		GenerateDatabase.main(new String[0]);
		
		INTERFACE.runUI();
		
		ClearDatabase.main(new String[0]);
		
		
	}
	
	
}