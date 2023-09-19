package mlb;
/**
 * @author Roman Yasinovskyy
 */
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseWriter {
    public final String SQLITEDBPATH = "jdbc:sqlite:data/mlb/";
    /**
     * @param filename (JSON file)
     * @return League
     */
    public ArrayList<Team> readTeamFromJson(String filename) {
        ArrayList<Team> league = new ArrayList<>();
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
    public ArrayList<Address> readAddressFromTxt(String filename) {
        ArrayList<Address> addressBook = new ArrayList<>();
        try {
            Scanner fs = new Scanner(new File(filename));
            while (fs.hasNextLine()) {
                String team = fs.next();
                String arena = fs.next();
                String street = fs.next();
                String city = fs.next();
                String state = fs.next();
                String zip = fs.next();
                String phone = fs.next();
                String url = fs.next();
                Address address = new Address(team, arena, street, city, state, zip, phone, url);
                addressBook.add(address);
            }
        } catch (IOException ex) {
            Logger.getLogger(DatabaseReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return addressBook;
    }
    public ArrayList<Player> readPlayerFromCsv(String filename) throws IOException {
        ArrayList<Player> roster = new ArrayList<>();
        CSVReader reader = new CSVReader(new FileReader(filename));

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine.length >= 4){
                Player player = new Player(nextLine[0], nextLine[1], nextLine[2], nextLine[3]);
                roster.add(player);
        }}
        reader.close();
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
        statement.executeUpdate("CREATE TABLE team ("
                            + "idpk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                            + "id TEXT NOT NULL,"
                            + "abbr TEXT NOT NULL,"
                            + "name TEXT NOT NULL,"
                            + "conference TEXT NOT NULL,"
                            + "division TEXT NOT NULL,"
                            + "logo BLOB);");
        
        statement.execute("PRAGMA foreign_keys = ON;");
        
        statement.executeUpdate("DROP TABLE IF EXISTS player;");
        statement.executeUpdate("CREATE TABLE player ("
                            + "idpk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                            + "id TEXT NOT NULL,"
                            + "name TEXT NOT NULL,"
                            + "team TEXT NOT NULL,"
                            + "position TEXT NOT NULL,"
                            + "FOREIGN KEY (team) REFERENCES team(idpk));");

        statement.executeUpdate("DROP TABLE IF EXISTS address;");
        statement.executeUpdate("CREATE TABLE address ("
                            + "idpk INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                            + "team TEXT NOT NULL,"
                            + "site TEXT NOT NULL,"
                            + "street TEXT NOT NULL,"
                            + "city TEXT NOT NULL,"
                            + "state TEXT NOT NULL,"
                            + "zip TEXT NOT NULL,"
                            + "phone TEXT NOT NULL,"
                            + "url TEXT NOT NULL,"
                            + "FOREIGN KEY (team) REFERENCES team(idpk));");
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
            for (int len; (len = fileInStream.read(buffer)) != -1;) {
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
    public void writeTeamTable(String db_filename, ArrayList<Team> league) throws SQLException {
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
    public void writeAddressTable(String db_filename, ArrayList<Address> addressBook) throws SQLException {
        Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
        String sql = "INSERT INTO address(team, site, street, city, state, zip, phone, url) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        for (Address address: addressBook) {
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
            statement_prepared.setString(1, address.getTeam());
            statement_prepared.setString(2, address.getSite());
            statement_prepared.setString(3, address.getStreet());
            statement_prepared.setString(4, address.getCity());
            statement_prepared.setString(5, address.getState());
            statement_prepared.setString(6, address.getZip());
            statement_prepared.setString(7, address.getPhone());
            statement_prepared.setString(8, address.getUrl());
            statement_prepared.executeUpdate();
        }
        db_connection.commit();
        db_connection.close();
    }
    /**
     * @param db_filename 
     * @param roster 
     * @throws java.sql.SQLException 
     */
    public void writePlayerTable(String db_filename, ArrayList<Player> roster) throws SQLException {
        Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
        String sql = "INSERT INTO player(id, name, team, position) VALUES(?, ?, ?, ?)";
        for (Player player: roster) {
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
            statement_prepared.setString(1, player.getId());
            statement_prepared.setString(2, player.getName());
            statement_prepared.setString(3, player.getPosition());
            statement_prepared.setString(4, player.getTeam());
            statement_prepared.executeUpdate();
        }
        
        db_connection.close();
    }
}