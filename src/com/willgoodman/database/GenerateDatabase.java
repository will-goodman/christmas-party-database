package com.willgoodman.database;

import java.sql.*;
import java.util.Random;
import java.util.Date;
import java.util.Calendar;

//loads made-up data into the database
public class GenerateDatabase {

	//constants
	private final static String[] VENUE_NAME = { "Cinema", "Bowling Alley", "Theatre", "Park", "Town hall", "Pub", "Restaurant",
			"Beach", "Canal", "Hotel" };
	private final static int[] VENUE_COST = { 100000, 50000, 200000, 10000, 50000, 10000, 10000, 0, 2000, 500000 };
	private final static int NUM_VENUES = VENUE_NAME.length;
	private final static String[] MENU_DESC = { "Italian", "Chinese", "French", "Sushi", "Thai", "British", "American", "Mexican",
			"Indian", "Spicy food medley" };
	private final static int[] MENU_COST = { 2000, 3000, 3000, 2500, 2000, 1500, 1000, 3000, 2000, 5000 };
	private final static int NUM_MENUS = MENU_DESC.length;
	private final static String[] ENTER_DESC = { "Circus", "Silent Disco", "Stand-up comedy", "Ballroom Dancing", "Band/Singer",
			"Poetry", "Comedy Sketch", "Stage-show", "Talk", "Film" };
	private final static int[] ENTER_COST = { 500000, 100000, 200000, 150000, 300000, 10000, 100000, 500000, 50000, 10000 };
	private final static int NUM_ENTER = ENTER_DESC.length;
	private final static int[] PARTY_PROFITS = { 40000, 50000, 100000, 75000, 60000, 20000, 0, 57000, 150000, 130000 };
	private final static long[] PARTY_MILLIS = { 1543438800000L, 1543525200000L, 1543611600000L, 1543672800000L, 1543759200000L,
			1543845600000L, 1543856400000L, 1543942800000L, 1544029200000L, 1544115600000L };
	private final static int[] PARTY_GUESTS = { 30, 35, 50, 45, 40, 20, 30, 60, 100, 85 };
	private final static int NUM_PARTIES = PARTY_PROFITS.length;

	public static void main(String[] args) {
		try {
			// open connection
			System.setProperty("jdbc.drivers", "org.postgresql.Driver");

			Connection dbConn = DriverManager.getConnection(DatabaseKeys.DB_NAME, DatabaseKeys.DB_USERNAME, DatabaseKeys.DB_PASSWORD);

			try {

				//@formatter:off
            
                //Venue
                PreparedStatement createVenue = dbConn.prepareStatement("CREATE TABLE Venue " +
                														" (vid     SERIAL, " +
                														" name    CHAR(30) NOT NULL, " +
                														" venuecost   INTEGER NOT NULL CHECK(venuecost >= 0), " +
                														" CONSTRAINT PK_Venue PRIMARY KEY (vid))");
                createVenue.executeUpdate();
                createVenue.close();
               
                //Menu
               	PreparedStatement createMenu = dbConn.prepareStatement("CREATE TABLE Menu " +
               														" (mid     SERIAL, " +
               														" description     CHAR(500) NOT NULL, " +
               														" costprice       INTEGER NOT NULL CHECK(costprice >= 0), " +
               														" CONSTRAINT PK_Menu PRIMARY KEY (mid))");
                createMenu.executeUpdate();
                createMenu.close();
                //Entertainment
                PreparedStatement createEnter = dbConn.prepareStatement("CREATE TABLE Entertainment " +
                														" (eid     SERIAL, " +
                														" description     CHAR(500) NOT NULL, " +
                														" costprice       INTEGER NOT NULL CHECK(costprice >= 0), " +
                														" CONSTRAINT PK_Entertainment PRIMARY KEY (eid))");
                createEnter.executeUpdate();
                createEnter.close();
                
                
                //Party
                PreparedStatement createParty = dbConn.prepareStatement("CREATE TABLE Party " +
        																"(pid     SERIAL, " +
        																" name    CHAR(10) NOT NULL, " +
        																" mid     INTEGER NOT NULL, " +
        																" vid     INTEGER NOT NULL, " +
        																" eid     INTEGER NOT NULL, " +
        																" price   INTEGER NOT NULL CHECK(price >= 0), " +
        																" timing  TIMESTAMP NOT NULL, " +
        																" numberofguests  INTEGER NOT NULL CHECK(numberofguests >= 0), " +
        																" CONSTRAINT PK_Party PRIMARY KEY (pid), " +
        																" CONSTRAINT FK_PartyMenu FOREIGN KEY (mid) REFERENCES Menu(mid), " +
        																" CONSTRAINT FK_PartyVenue FOREIGN KEY (vid) REFERENCES Venue(vid), " +
        																" CONSTRAINT FK_PartyEntertainment FOREIGN KEY (eid) REFERENCES Entertainment(eid))");
                createParty.executeUpdate();
                createParty.close();
                
                
                
            	//@formatter:on

				// add data
				// random object to create random costs
				Random randomPrice = new Random();

				// venue
				PreparedStatement venueInsert = dbConn
						.prepareStatement("INSERT INTO Venue (name, venuecost) VALUES (?, ?)");

				// explicitly created data
				for (int vid = 1; vid <= NUM_VENUES; vid++) {
					venueInsert.setString(1, VENUE_NAME[vid - 1]);
					venueInsert.setInt(2, VENUE_COST[vid - 1]);
					venueInsert.addBatch();
				}

				venueInsert.executeBatch();

				// made up data
				int[] countVenues = new int[NUM_VENUES];

				for (int vid = NUM_VENUES + 1; vid <= 100; vid++) {
					int n = randomPrice.nextInt(NUM_VENUES - 1);
					countVenues[n]++;
					venueInsert.setString(1, VENUE_NAME[n] + countVenues[n]);
					venueInsert.setInt(2, randomPrice.nextInt(10000));
					venueInsert.addBatch();
				}

				venueInsert.executeBatch();
				venueInsert.close();

				// menu
				PreparedStatement menuInsert = dbConn
						.prepareStatement("INSERT INTO Menu (description, costprice) VALUES (?, ?)");
				// explicit data
				for (int mid = 1; mid <= NUM_MENUS; mid++) {
					menuInsert.setString(1, MENU_DESC[mid - 1]);
					menuInsert.setInt(2, MENU_COST[mid - 1]);
					menuInsert.addBatch();
				}

				menuInsert.executeBatch();

				// made up data
				int[] countMenus = new int[NUM_MENUS];

				for (int mid = NUM_MENUS + 1; mid <= 100; mid++) {
					int n = randomPrice.nextInt(NUM_MENUS - 1);
					countMenus[n]++;
					menuInsert.setString(1, MENU_DESC[n] + countMenus[n]);
					menuInsert.setInt(2, randomPrice.nextInt(1000));
					menuInsert.addBatch();
				}

				menuInsert.executeBatch();
				menuInsert.close();

				// entertainment
				PreparedStatement enterInsert = dbConn
						.prepareStatement("INSERT INTO Entertainment (description, costprice) VALUES (?, ?)");
				// explicit data
				for (int eid = 1; eid <= NUM_ENTER; eid++) {
					enterInsert.setString(1, ENTER_DESC[eid - 1]);
					enterInsert.setInt(2, ENTER_COST[eid - 1]);
					enterInsert.addBatch();
				}

				enterInsert.executeBatch();

				// made up data
				int[] countEnter = new int[NUM_ENTER];

				for (int eid = NUM_ENTER + 1; eid <= 100; eid++) {
					int n = randomPrice.nextInt(NUM_ENTER - 1);
					countEnter[n]++;
					enterInsert.setString(1, ENTER_DESC[n] + countEnter[n]);
					enterInsert.setInt(2, randomPrice.nextInt(10000));
					enterInsert.addBatch();
				}

				enterInsert.executeBatch();
				enterInsert.close();

				// party
				PreparedStatement partyInsert = dbConn.prepareStatement(
						"INSERT INTO Party (name, mid, vid, eid, price, timing, numberofguests) VALUES (?, ?, ?, ?, ?, ?, ?)");
				// explicit data
				for (int pid = 1; pid <= NUM_PARTIES; pid++) {
					partyInsert.setString(1, "Party" + pid);
					partyInsert.setInt(2, pid);
					partyInsert.setInt(3, pid);
					partyInsert.setInt(4, pid);
					int partyExpenses = (MENU_COST[pid - 1] * PARTY_GUESTS[pid - 1]) + VENUE_COST[pid - 1]
							+ ENTER_COST[pid - 1];
					int partyPrice = partyExpenses + PARTY_PROFITS[pid - 1];
					partyInsert.setInt(5, partyPrice);
					java.sql.Timestamp tStamp = new java.sql.Timestamp(PARTY_MILLIS[pid - 1]);
					partyInsert.setTimestamp(6, tStamp);
					partyInsert.setInt(7, PARTY_GUESTS[pid - 1]);
					partyInsert.addBatch();
				}

				partyInsert.executeBatch();

				// made-up data
				for (int pid = NUM_PARTIES + 1; pid <= 1000; pid++) {
					int vid = randomPrice.nextInt(100) + 1;
					int mid = randomPrice.nextInt(100) + 1;
					int eid = randomPrice.nextInt(100) + 1;

					// partyInsert.setInt(1, pid);
					partyInsert.setString(1, "Party" + pid);
					partyInsert.setInt(2, mid);
					partyInsert.setInt(3, vid);
					partyInsert.setInt(4, eid);

					// get venue cost
					PreparedStatement selectPrice = dbConn
							.prepareStatement("SELECT venuecost FROM Venue WHERE vid = ?");
					selectPrice.setInt(1, vid);
					ResultSet vRS = selectPrice.executeQuery();
					int venueCost = 0;
					while (vRS.next()) {
						venueCost = vRS.getInt("venuecost");
					}

					// get menu cost
					selectPrice = dbConn.prepareStatement("SELECT costprice FROM Menu WHERE mid = ?");
					selectPrice.setInt(1, mid);
					ResultSet mRS = selectPrice.executeQuery();
					int menuCost = 0;
					while (mRS.next()) {
						menuCost = mRS.getInt("costprice");
					}

					// get entertainment cost
					selectPrice = dbConn.prepareStatement("SELECT costprice FROM Entertainment WHERE eid = ?");
					selectPrice.setInt(1, eid);
					ResultSet eRS = selectPrice.executeQuery();
					int enterCost = 0;
					while (eRS.next()) {
						enterCost = eRS.getInt("costprice");
					}

					// total cost
					partyInsert.setInt(5, venueCost + menuCost + enterCost);

					java.sql.Timestamp partyDate = randomDate();
					partyInsert.setTimestamp(6, partyDate);
					int numberOfGuests = randomPrice.nextInt(100);
					partyInsert.setInt(7, numberOfGuests);
					partyInsert.addBatch();
				}

				partyInsert.executeBatch();
				partyInsert.close();

			} finally {
				dbConn.close();
			}
		} catch (SQLException e) {
			System.out.println("SQLException" + e.getMessage());
		} 
	}

	//makes a timestamp object for parties
	private static java.sql.Timestamp randomDate() {
		Random generateRandomNum = new Random();

		int day = generateRandomNum.nextInt(28);
		int month = generateRandomNum.nextInt(12);
		int min = 2018;
		int max = 2020;
		int year = generateRandomNum.nextInt((max - min) + 1) + min;
		int hour = generateRandomNum.nextInt(24);
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, hour, 0);
		Date date = calendar.getTime();
		return new java.sql.Timestamp(date.getTime());
	}
}
