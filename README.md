# christmas-party-database
Java code which creates and interacts with a database for a company which hosts Christmas parties.

Created for a university project, using Model-View-Controller.

## To Run
- Install PostgreSQL and a JDBC library on your machine
- Create a database with PostgreSQL
- Update the DatabaseKeys file with the details of the database you just created
- Compile the java package
- Run com.willgoodman.LaunchSystem with the JDBC library in the classpath

LaunchSystem will populate the database with fake data, allow the user to try the different features of the database, and then reset the database afterwards.

If you wish to interact with the database normally, then you can create a DatabaseInteraction object. The DatabaseUI class provides a simple UI for the DatabaseInteraction object.

## DatabaseInteraction Features
The DatabaseInteraction class is used to interact with the database, and contains the following methods.

### Constructor Detail
#### DatabaseInteraction
Constructs a new object which will interact with a specific database. <br>
```java
public DatabaseInteraction(java.lang.String dbName,
                           java.lang.String dbUsername,
                           java.lang.String dbPassword)
```

*Parameters:*<br>
dbName - The name of the database to connect to.<br>
dbUsername - The username for database login.<br>
dbPassword - The password for database login.<br>

### Method Detail
#### getPartyReport
Retrieves a report on a party. <br>
```java
public java.lang.String getPartyReport(int partyID)
```

*Parameters:* <br>
partyID - The ID of the party to return a report on. <br>

*Returns:* <br>
The party report.

#### getMenuReport
Retrieves a report on a menu. <br>
```java
public java.lang.String getMenuReport(int menuID)
```

*Parameters:* <br>
menuID - The ID of the menu to be reported. <br>

*Returns:* <br>
The menu report.

#### insertParty
Inserts a new party into the database. <br>
```java
public java.lang.String insertParty(int partyID,
                                    java.lang.String name,
                                    int menuID,
                                    int venueID,
                                    int enterID,
                                    int price,
                                    java.sql.Timestamp tStamp,
                                    int numberOfGuests)
```

*Parameters:* <br>
partyID - The ID of the new party. <br>
name - The name of the party. <br>
menuID - The ID of the menu for the party. <br>
venueID - The ID of the venue for the party. <br>
enterID - The ID of the entertainment for the party. <br>
price - The price of the party. <br>
tStamp - The timestamp for the Date/Time of the start of the party. <br>
numberOfGuests - The number of guests attending the party. <br>

*Returns:* <br>
Whether the new party was added or not.