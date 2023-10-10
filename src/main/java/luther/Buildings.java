package luther;

public class Buildings {
    private final String building;

    public Buildings(String building) {
        this.building = building;
    }

    /**
     * @return Team name
     */
    public String getBuilding() {
        return this.building;
    }

    @Override
    public String toString() {
        return String.format("%s", building);
    }
}
