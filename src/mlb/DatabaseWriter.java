package mlb;
/**
 * @author Roman Yasinovskyy
 */
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseWriter {
    public final String SQLITEDBPATH = "jdbc:sqlite:data/mlb/";
    /**
     * @param filename (JSON file)
     * @return League
     */
    public ArrayList < Team > readTeamFromJson(String filename) {
        ArrayList < Team > league = new ArrayList < > ();
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser;

        try {
            jsonParser = jsonFactory.createParser(new File(filename));
            jsonParser.nextToken();
            while (jsonParser.nextToken() == JsonToken.START_OBJECT) {
                Team team = mapper.readValue(jsonParser, Team.class);
                league.add(team);
            }
            jsonParser.close();
        } catch (IOException ex) {
            Logger.getLogger(DatabaseReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return league;
    }
    /**
     * @param filename (TXT file)
     * @return Addresses
     */
    public ArrayList < Address > readAddressFromTxt(String filename) {
        ArrayList < Address > addressBook = new ArrayList < > ();
        try {
            Scanner fs = new Scanner(new File(filename));
            while (fs.hasNextLine()) {
                String[] parts = fs.nextLine().split("\t");
                if (parts.length >= 8) { 
                    Address address = new Address(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]);
                    addressBook.add(address);
                    System.out.println("Line read: " + address);
                    System.out.println("Address created: " + address.toString());
                } else {
                    System.out.println("Skipping line: not enough data");
                }
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(DatabaseWriter.class.getName()).log(Level.SEVERE, null, e);
        }
        return addressBook;
    }
    public ArrayList < Player > readPlayerFromCsv(String filename) throws IOException {
        ArrayList < Player > roster = new ArrayList < > ();

        try (
            FileReader fileReader = new FileReader(filename); CSVReader reader = new CSVReaderBuilder(fileReader).build()
        ) {
            String[] row;
            while ((row = reader.readNext()) != null) {
                if (row.length >= 5) {
                    Player player = new Player(row[0], row[1], row[4], row[2]);
                    roster.add(player);
                }
            }
        }

        return roster;
    }
    /**
     * Create tables cities and teams
     * @param db_filename
     * @throws SQLException 
     */
    public void createTables(String db_filename) throws SQLException {
        Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
        Statement statement = db_connection.createStatement();

        statement.executeUpdate("DROP TABLE IF EXISTS team;");
        statement.executeUpdate("CREATE TABLE team (" +
            "idpk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "id TEXT NOT NULL," +
            "abbr TEXT NOT NULL," +
            "name TEXT NOT NULL," +
            "conference TEXT NOT NULL," +
            "division TEXT NOT NULL," +
            "logo BLOB);");

        statement.execute("PRAGMA foreign_keys = ON;");

        statement.executeUpdate("DROP TABLE IF EXISTS player;");
        statement.executeUpdate("CREATE TABLE player (" +
            "idpk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "id TEXT NOT NULL," +
            "name TEXT NOT NULL," +
            "team TEXT NOT NULL," +
            "position TEXT NOT NULL," +
            "FOREIGN KEY (team) REFERENCES team(idpk));");

        statement.executeUpdate("DROP TABLE IF EXISTS address;");
        statement.executeUpdate("CREATE TABLE address (" +
            "idpk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "team TEXT NOT NULL," +
            "site TEXT NOT NULL," +
            "street TEXT NOT NULL," +
            "city TEXT NOT NULL," +
            "state TEXT NOT NULL," +
            "zip TEXT NOT NULL," +
            "phone TEXT NOT NULL," +
            "url TEXT NOT NULL," +
            "FOREIGN KEY (team) REFERENCES team(idpk));");
        db_connection.close();
    }
    /**
     * Read the file and returns the byte array
     * @param filename
     * @return the bytes of the file
     */
    private byte[] readLogoFile(String filename) {
        ByteArrayOutputStream byteArrOutStream = null;
        try {
            File fileIn = new File(filename);
            FileInputStream fileInStream = new FileInputStream(fileIn);
            byte[] buffer = new byte[1024];
            byteArrOutStream = new ByteArrayOutputStream();
            for (int len;
                (len = fileInStream.read(buffer)) != -1;) {
                byteArrOutStream.write(buffer, 0, len);
            }
            fileInStream.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        return byteArrOutStream != null ? byteArrOutStream.toByteArray() : null;
    }
    /**
     * @param db_filename
     * @param league 
     * @throws java.sql.SQLException 
     */
    public void writeTeamTable(String db_filename, ArrayList < Team > league) throws SQLException {
        Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
        Statement statement = db_connection.createStatement();
        statement.execute("PRAGMA foreign_keys = ON;");
        String sql = "INSERT INTO team(id, abbr, name, conference, division, logo) VALUES(?, ?, ?, ?, ?, ?)";
        for (Team team: league) {
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
            statement_prepared.setString(1, team.getId());
            statement_prepared.setString(2, team.getAbbreviation());
            statement_prepared.setString(3, team.getName());
            statement_prepared.setString(4, team.getConference());
            statement_prepared.setString(5, team.getDivision());
            statement_prepared.setBytes(6, team.getLogo());
            statement_prepared.executeUpdate();
        }
        db_connection.commit();
        db_connection.close();
    }
    /**
     * @param db_filename 
     * @param addressBook 
     * @throws java.sql.SQLException 
     */
    public void writeAddressTable(String dbFilename, ArrayList < Address > addressBook) throws SQLException {
        try (
            Connection dbConnection = DriverManager.getConnection(SQLITEDBPATH + dbFilename); Statement statement = dbConnection.createStatement()
        ) {
            statement.execute("PRAGMA foreign_keys = ON;");
            dbConnection.setAutoCommit(false);

            String sql = "INSERT INTO address(idpk, team, site, street, city, state, zip, phone, url) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {
                for (Address address: addressBook) {
                    ResultSet results = statement.executeQuery("SELECT idpk FROM team WHERE team.name = '" + address.getTeam() + "';");

                    if (results.next()) {
                        int idpk = results.getInt(1);
                        preparedStatement.setInt(1, idpk);
                        preparedStatement.setString(2, address.getTeam());
                        preparedStatement.setString(3, address.getSite());
                        preparedStatement.setString(4, address.getStreet());
                        preparedStatement.setString(5, address.getCity());
                        preparedStatement.setString(6, address.getState());
                        preparedStatement.setString(7, address.getZip());
                        preparedStatement.setString(8, address.getPhone());
                        preparedStatement.setString(9, address.getUrl());
                        preparedStatement.executeUpdate();
                    } else {
                        System.out.println("Error: Team " + address.getTeam() + " not found in the database.");
                    }
                }
                dbConnection.commit();
            } catch (SQLException e) {
                dbConnection.rollback();
                throw e;
            }
        }
    }
    /**
     * @param db_filename 
     * @param roster 
     * @throws java.sql.SQLException 
     */
    public void writePlayerTable(String db_filename, ArrayList < Player > roster) throws SQLException {
        try (
            Connection dbConnection = DriverManager.getConnection(SQLITEDBPATH + dbFilename); Statement statement = dbConnection.createStatement()
        ) {
            statement.execute("PRAGMA foreign_keys = ON;");
            dbConnection.setAutoCommit(false);

            String sql = "INSERT INTO player(idpk, id, name, team, position) VALUES(?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = dbConnection.prepareStatement(sql)) {
                for (Player player: roster) {
                    ResultSet results = statement.executeQuery("SELECT idpk FROM team WHERE team.name = '" + player.getTeam() + "';");

                    if (results.next()) {
                        int idpk = results.getInt(1);
                        preparedStatement.setInt(1, idpk);
                        preparedStatement.setString(2, player.getId());
                        preparedStatement.setString(3, player.getName());
                        preparedStatement.setString(4, player.getTeam());
                        preparedStatement.setString(5, player.getPosition());
                        preparedStatement.executeUpdate();
                    } else {
                        // Handle the case where the team was not found
                        System.out.println("Error: Team " + player.getTeam() + " not found in the database.");
                    }
                }

                dbConnection.commit();
            } catch (SQLException e) {
                dbConnection.rollback();
                throw e;
            }
        }
    }
    public static void main(String[] args) {
        String filename = "data/mlb/teams.json";
        DatabaseWriter instance = new DatabaseWriter();
        ArrayList < Address > result = instance.readAddressFromTxt(filename);
        System.out.println("result");
        System.out.println(result);
    }
}