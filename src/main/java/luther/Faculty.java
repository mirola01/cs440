package luther;

public class Faculty {
    String name;
    String department;
    int office;
    public Faculty(String name, String department, int office) {
        this.name = name;
        this.department = department;
        this.office = office;
    }
    /**
     * @return Team name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * @return Team name
     */
    public String getDepartment() {
        return this.department;
    }

    /**
     * @return Team name
     */
    public int getOffice() {
        return this.office;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s\n%s\n", name, department, office);
    }
}
