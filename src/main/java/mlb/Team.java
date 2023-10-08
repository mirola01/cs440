package mlb;

/**
 * @author Roman Yasinovskyy
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

import org.apache.commons.lang3.NotImplementedException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
     * Default class constructor. Needed to load json properly.
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
     *
     * @param id
     * @param abbr
     * @param name
     * @param conference
     * @param division
     */
    public Team(String id, String abbr, String name, String conference, String division) {
        this.id = id;
        this.abbreviation = abbr;
        this.name = name;
        this.conference = conference;
        this.division = division;
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
     *
     * @param roster
     */
    public void setRoster(ArrayList<Player> roster) {
        this.roster = roster;
    }

    /**
     * @return Team address
     */
    public Address getAddress() {
        return this.address;
    }

    /**
     * Set team address
     *
     * @param new_value
     */
    public void setAddress(Address new_value) {
        this.address = new_value;
    }

    /**
     * @return Team logo
     */
    public byte[] getLogo() {
        return this.logo;
    }

    /**
     * Set team logo
     *
     * @param new_value
     */
    public void setLogo(byte[] new_value) {
        this.logo = new_value;
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
