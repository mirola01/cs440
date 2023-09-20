package mlb;
/**
 * @author Roman Yasinovskyy
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Team {
    @JsonProperty("id")
    private final String id;
    @JsonProperty("abbreviation")
    private final String abbreviation;
    @JsonProperty("name")
    private final String name;
    @JsonProperty("conference")
    private final String conference;
    @JsonProperty("division")
    private final String division;
    private ArrayList<Player> roster;
    private Address address;
    @JsonProperty("logo")
    private byte[] logo;
    /**
     * Default class constructor.
     * Needed to load json properly.
     */
    public Team() {
        id = "";
        abbreviation = "";
        name = "";
        conference = "";
        division = "";
        address = null;
    }
    /**
     * Class constructor
     * @param _id
     * @param _abbr
     * @param _name
     * @param _conf
     * @param _div
     */
    public Team(String _id, String _abbr, String _name, String _conf, String _div) {
        this.id = _id;
        this.abbreviation = _abbr;
        this.name = _name;
        this.conference = _conf;
        this.division = _div;
    }
    /**
     * @return Team id
     */
    public String getId() {
        return this.id;
    }
    /**
     * @return Team abbreviation
     */
    public String getAbbreviation() {
        return this.abbreviation;
    }
    /**
     * @return Team name
     */
    public String getName() {
        return this.name;
    }
    /**
     * @return Team conference
     */
    public String getConference() {
        return this.conference;
    }
    /**
     * @return Team division
     */
    public String getDivision() {
        return this.division;
    }
    /**
     * @return Team roster
     */
    public ArrayList<Player> getRoster() {
        return this.roster;
    }
    /**
     * Set team's roster
     * @param _roster 
     */
    public void setRoster(ArrayList<Player> _roster) {
        this.roster = _roster;
    }
    /**
     * @return Team address
     */
    public Address getAddress() {
        return this.address;
    }
    /**
     * Set team address
     * @param _address
     */
    public void setAddress(Address _address) {
        this.address = _address;
    }
    /**
     * @return Team logo
     */
    public byte[] getLogo() {
        return this.logo;
    }
    /**
     * Set team logo
     * @param _logo 
     */
    public void setLogo(byte[] _logo) {
        this.logo = _logo;
    }
    /**
     * @return Team as a String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append(conference).append(" | ").append(division).append("\n");
        if (address != null) {
            sb.append(address.getSite()).append("\n");
            sb.append(address.getStreet()).append("\n");
            sb.append(address.getCity()).append(", ").append(address.getState()).append(" ").append(address.getZip()).append("\n");
            sb.append(address.getPhone()).append("\n");
            sb.append(address.getUrl()).append("\n");
        }
        if (roster != null) {
            sb.append("Roster size: ").append(roster.size());
        }
        return sb.toString();
    }
}