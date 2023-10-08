package mlb;

/**
 * @author Roman Yasinovskyy
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;

public class DatabaseReader {

    private Connection db_connection;
    private final String SQLITEDBPATH = "jdbc:sqlite:data/mlb/mlb.sqlite";

    public DatabaseReader() {
    }

    /**
     * Connect to a database (file)
     */
    public void connect() {
        try {
            this.db_connection = DriverManager.getConnection(SQLITEDBPATH);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseReaderGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Disconnect from a database (file)
     */
    public void disconnect() {
        try {
            this.db_connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseReaderGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Populate the list of divisions
     *
     * @param divisions
     */
    public void getDivisions(ArrayList<String> divisions) {
        Statement stat;
        ResultSet results;

        this.connect();
        try {
            stat = this.db_connection.createStatement();
            String sql = "SELECT DISTINCT division FROM team;";
            results = stat.executeQuery(sql);
            while (results.next()) {
                divisions.add(results.getString("division"));
            }
            
            results.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
    }

    /**
     * Read all teams from the database
     *
     * @param confDiv
     * @param teams
     */
    public void getTeams(String confDiv, ArrayList<String> teams) {
        Statement stat;
        ResultSet results;
        String conference = confDiv.split(" | ")[0];
        String division = confDiv.split(" | ")[2];

        this.connect();
        try {
            stat = this.db_connection.createStatement();
            String sql = "SELECT name FROM team WHERE division = ?;";
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
            statement_prepared.setString(1, division);

            results = stat.executeQuery(sql);
            while (results.next()) {
                teams.add(results.getString("name"));
            }
            results.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.disconnect();
        }
    }

    /**
     * @param teamName
     * @return Team info
     */
    public Team getTeamInfo(String teamName) {
        Statement stat;
    ResultSet results;
    Team team = null;
    ArrayList<Player> roster = new ArrayList<>();
    Address address = null;

    this.connect();
    try {
        // Retrieve team info
        stat = this.db_connection.createStatement();
        String sql = "SELECT * FROM team WHERE name = ?;";
        PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
        statement_prepared.setString(1, teamName);
        
        results = statement_prepared.executeQuery();
        
        team = new Team(results.getString("id"), results.getString("abbr"), results.getString("name"), results.getString("conference"), results.getString("division"));
        team.setLogo(results.getBytes("logo"));
        results.close();
    } catch (SQLException ex) {
        Logger.getLogger(DatabaseReader.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        this.disconnect();
    }
    
    return team;
    }
}
