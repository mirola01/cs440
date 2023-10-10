package luther;

public class Courses {
    private final String abbreviation;
    private final int number;
    private final String title;
    private final String credits;
    private final String instructor;
    public Courses(String abbreviation, int number, String title,String instructor, String credits) {
        this.abbreviation = abbreviation;
        this.number = number;
        this.title = title;
        this.instructor = instructor;
        this.credits = credits;
    }
    /**
     * @return Team name
     */
    public String getAbbreviation() {
        return this.abbreviation;
    }
    public String getInstructor() {
        return this.instructor;
    }
    /**
     * @return Team name
     */
    public int getNumber() {
        return this.number;
    }
    
    /**
     * @return Team name
     */
    public String getTitle() {
        return this.title;
    }
    /**
     * @return Team name
     */
    public String getCredits() {
        return this.credits;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s\n%s\n%s\n%s\n", abbreviation, number, title, credits);
    }
}
