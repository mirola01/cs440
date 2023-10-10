package luther;

public class Departments {
    private final String name;
    private final String building;
    public Departments(String name, String building) {
        this.name = name;
        this.building = building;
    }
    /**
     * @return Team name
     */
    public String getDepartment() {
        return this.name;
    }
    
    /**
     * @return Team name
     */
    public String getBuilding() {
        return this.building;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s", name, building);
    }
}
