package luther;

import java.sql.Time;

public class Sections {
    private final String courseString;
    private final int courseNumber;
    private final String instructor;
    private final String courseTitle;
    private final String locationString;
    private final int locationNumber;
    private final Time startHour;
    public Sections(String courseString, int courseNumber, String courseTitle, String instructor,String locationString, int locationNumber, Time startHour) {
        this.courseString = courseString;
        this.courseNumber = courseNumber;
        this.courseTitle = courseTitle;
        this.instructor = instructor;
        this.locationString = locationString;
        this.locationNumber = locationNumber;
        this.startHour = startHour;
    }
    public String getCourseString() {
        return this.courseString;
    }
    public String getCourseTitle() {
        return this.courseTitle;
    }
    /**
     * @return Team name
     */
    public int getCourseNumber() {
        return this.courseNumber;
    }
    
    /**
     * @return Team name
     */
    public String getInstructor() {
        return this.instructor;
    }
    /**
     * @return Team name
     */
    public String getLocationString() {
        return this.locationString;
    }
    
    /**
     * @return Team name
     */
    public int getLocationNumber() {
        return this.locationNumber;
    }
    /**
     * @return Team name
     */
    public Time getStartHour() {
        return this.startHour;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s\n%i\n%i\n%i\n%s\n", courseString, courseNumber, instructor, locationString, locationNumber, startHour);
    }
}
