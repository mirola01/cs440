package aba;

import nba.Team;

/**
 * @author Roman Yasinovskyy
 * This is a basic example of using Java to access SQLite DB.
 * This application creates a new DB with two tables, cities and teams.
 * city(id, name, state)
 * team(id, name, conference, division, site, city_id)
 * Both tables are populated with some initial data.
 * Print statements are commented out for clarity.
 * http://www.sqlitetutorial.net/sqlite-java/
 * https://www.javatpoint.com/sqlite-tutorial
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteExampleMain {
    /**
     * main function
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SQLiteExample sqlite = new SQLiteExample();
        Connection db_connection = null;
        Statement statement = null;
        ResultSet results = null;

        /*
         * Create a new database or create a new one.
         */
        try {
            sqlite.createDB("test.sqlite");
        } catch (SQLException sqle) {
            System.err.printf("createDB() failed%n");
            System.err.printf("%s%n", sqle.getMessage());
        }
        /*
         * Connect to the database or create a new one.
         */
        try {
            db_connection = sqlite.connect("test.sqlite");
        } catch (SQLException sqle) {
            System.err.printf("connect() failed%n");
            System.err.printf("%s%n", sqle.getMessage());
        }
        /*
         * Add cities.
         */
        try {
            sqlite.insertCity(db_connection, "New York", "NY");
            sqlite.insertCity(db_connection, "Louisville", "KY");
            sqlite.insertCity(db_connection, "Pittsburgh", "PA");
        } catch (SQLException sqle) {
            System.err.printf("insertCity() failed%n");
            System.err.printf("%s%n", sqle.getMessage());
        }
        /*
         * Query the database and retrieve cities.
         */
        System.out.printf("Records in the *cities* table%n");
        System.out.printf("%-5s%-15s%5s%n", "ID", "City", "State");
        try {
            statement = db_connection.createStatement();
            results = statement.executeQuery("SELECT * FROM cities;");
            while (results.next()) {
                System.out.printf("%-5d%-15s%5s%n", results.getInt("city_id_pk"), results.getString("city_name"),
                        results.getString("city_state"));
            }
        } catch (SQLException sqle) {
            System.err.printf("queryCities() failed%n");
            System.err.printf("%s%n", sqle.getMessage());
        }
        /*
         * Add teams.
         */
        Team nya = new Team("New York Americans", "East", "Atlantic", "Long Island", "New York", "NY");
        Team pbp = new Team("Pittsburgh Pipers", "East", "Atlantic", "Pittsburgh Civic Arena", "Pittsburgh", "PA");
        try {
            sqlite.insertTeam(db_connection, nya);
            sqlite.insertTeam(db_connection, pbp);
        } catch (SQLException sqle) {
            System.err.printf("insertTeam() failed%n");
            System.err.printf("%s%n", sqle.getMessage());
        }
        /*
         * Query the database and retrieve teams.
         */
        System.out.printf("Records in the *teams* table%n");
        System.out.printf("%-5s%-25s%-15s%-15s%-15s%-20s%n", "ID", "Name", "Conference", "Division", "City", "Arena");
        try {
            statement = db_connection.createStatement();
            results = statement
                    .executeQuery("SELECT * FROM cities INNER JOIN teams ON cities.city_id_pk = teams.city_id;");
            while (results.next()) {
                System.out.printf("%-5d%-25s%-15s%-15s%-15s%-20s%n", results.getInt("team_id_pk"),
                        results.getString("team_name"), results.getString("team_conference"),
                        results.getString("team_division"), results.getString("city_name"),
                        results.getString("team_arena"));
            }
        } catch (SQLException sqle) {
            System.err.printf("queryTeams() failed%n");
            System.err.printf("%s%n", sqle.getMessage());
        }
        /*
         * Close the database connection.
         */
        try {
            db_connection.close();
            // System.out.printf("Connection to the database has been closed.%n");
        } catch (SQLException sqle) {
            System.err.printf("%s%n", sqle.getMessage());
        }
    }

}
