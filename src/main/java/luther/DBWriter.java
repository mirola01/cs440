package luther;
import com.github.javafaker.Faker;
import com.mifmif.common.regex.Main;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mlb.DatabaseWriter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DBWriter {
  public final static String SQLITEDBPATH = "jdbc:sqlite:data/luther/";
  /**
   * @param filename (TXT file)
   * @return Addresses
   */
  public ArrayList < Buildings > readBuildingsFromTxt(String filename) {
    ArrayList < Buildings > buildingsBook = new ArrayList < > ();
    try {
      Scanner fs = new Scanner(new File(filename));
      while (fs.hasNextLine()) {
        String building = fs.nextLine();
        Buildings address = new Buildings(building);
        buildingsBook.add(address);
      }
    } catch (FileNotFoundException e) {
      Logger.getLogger(DatabaseWriter.class.getName()).log(Level.SEVERE, null, e);
    }
    return buildingsBook;
  }
  public ArrayList < Programs > readProgramFromTxt(String filename) {
    ArrayList < Programs > programsBook = new ArrayList < > ();
    try {
      Scanner fs = new Scanner(new File(filename));
      while (fs.hasNextLine()) {
        String program = fs.nextLine();
        Programs address = new Programs(program);
        programsBook.add(address);
      }
    } catch (FileNotFoundException e) {
      Logger.getLogger(DBWriter.class.getName()).log(Level.SEVERE, null, e);
    }
    return programsBook;
  }
  public ArrayList<Faculty> readFacultyFromWeb() {
    ArrayList<Faculty> facultyList = new ArrayList<>();

    try {
        URL url = new URL("https://www2.luther.edu/directory/assets/2023_24_Faculty_Directory_2.pdf");
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();

        byte[] pdfBytes = in.readAllBytes();
        PDDocument document = Loader.loadPDF(pdfBytes);

        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        String[] lines = text.split("\n");
        String currentDepartment = "";
        Pattern departmentPattern = Pattern.compile("^([a-zA-Z ]+)$");
        Pattern facultyPattern = Pattern.compile("^(\\w+),\\s+(\\w+),?\\s+([a-zA-Z0-9 ]+)$");

        for (String line : lines) {
            Matcher departmentMatcher = departmentPattern.matcher(line);
            Matcher facultyMatcher = facultyPattern.matcher(line);

            if (departmentMatcher.find()) {
                currentDepartment = departmentMatcher.group(1).trim();
            } else if (facultyMatcher.find()) {
                String name = facultyMatcher.group(1).trim();
                
                String officeStr = facultyMatcher.group(3).trim();
                String officeNumStr = officeStr.replaceAll("[^0-9]", "");
                int office = Integer.parseInt(officeNumStr);

                Faculty faculty = new Faculty(name, currentDepartment, office);
                facultyList.add(faculty);
            }
        }

        document.close();

    } catch (IOException e) {
        e.printStackTrace();
    }

    return facultyList;
}

  public ArrayList < Departments > readDepartmentsFromTxt(String filename) {
    ArrayList < Departments > departmentsList = new ArrayList < > ();
    String departmentName = "";
    String building = "";

    try {
      Scanner fs = new Scanner(new File(filename));
      while (fs.hasNextLine()) {
        String line = fs.nextLine();
        if (line.matches("^[a-zA-Z\\s,]+$") && !line.endsWith(", Head") && !line.isEmpty()) {
          departmentName = line;
        } else if (line.matches(".*\\d+.*") && !line.contains("@") && !line.contains("-")) {
          building = line;

          Departments department = new Departments(departmentName, building);
          departmentsList.add(department);
        }
      }
    } catch (FileNotFoundException e) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
    }

    return departmentsList;
  }
  public static ArrayList<Courses> readCoursesFromWeb() throws IOException, SQLException {
    ArrayList<Courses> coursesList = new ArrayList<>();

    Document doc = Jsoup.connect("http://www.faculty.luther.edu/~bernatzr/Registrar-Public/Course%20Enrollments/enrollments_FA2023.htm").get();
    Elements rows = doc.select("tr[align=center]");
    for (Element row : rows) {
        String courseString = row.select("td").get(0).text();
        String courseName = row.select("td").get(1).text();
        String instructorString = row.select("td").get(2).text();
        String[] instructors = instructorString.split(",");
      String firstInstructor = instructors[0];
      String[] nameParts = firstInstructor.split("\\.");
      String lastName = nameParts[nameParts.length - 1];
        String courseAbr = courseString.split("-")[0];
        int courseNumber = Integer.parseInt(courseString.replaceAll("[^0-9]", ""));
        Courses course = new Courses(courseAbr, courseNumber, courseName, lastName,"4");
        coursesList.add(course);
        }

    return coursesList;
}

  public static ArrayList < Sections > readSectionsFromWeb() throws IOException {
    ArrayList < Sections > sectionsList = new ArrayList < > ();

    Document doc = Jsoup.connect("http://www.faculty.luther.edu/~bernatzr/Registrar-Public/Course%20Enrollments/enrollments_FA2023.htm").get();
    Elements rows = doc.select("tr[align=center]");
    for (Element row: rows) {
      String courseString = row.select("td").get(0).text();
      String courseTitle = row.select("td").get(1).text();
      //System.out.println("Course: " + courseTitle);  
      String instructorString = row.select("td").get(2).text();
      String locationString = row.select("td").get(14).text();
      String startHourString = row.select("td").get(6).text();

      SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
      Time startHour = null;
      if (startHourString != null && !startHourString.isEmpty()) {
        try {
            long ms = sdf.parse(startHourString).getTime();
            startHour = new Time(ms);
        } catch (ParseException e) {
            try {
                startHour = new Time(sdf.parse("12:15 PM").getTime());
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    } else {
        try {
            startHour = new Time(sdf.parse("12:15 PM").getTime());
        } catch (ParseException ex) {
        }
    }
      String courseAbr = courseString.replaceAll("[^a-zA-Z]", "");
      int courseNumber = Integer.parseInt(courseString.replaceAll("[^0-9]", ""));
      
      String[] instructors = instructorString.split(",");
      String firstInstructor = instructors[0];
      String[] nameParts = firstInstructor.split("\\.");
      String lastName = nameParts[nameParts.length - 1];


      int locationNumber;
String locationAbr;

if (locationString != null && !locationString.isEmpty()) {
    try {
        locationNumber = Integer.parseInt(locationString.replaceAll("[^0-9]", ""));
        locationAbr = locationString.replaceAll("[^a-zA-Z]", "");
    } catch (NumberFormatException e) {
        locationNumber = 175;
        locationAbr = "SAMP";
    }
} else {
    locationNumber = 175;
    locationAbr = "SAMP";
}


      Sections section = new Sections(courseAbr, courseNumber, courseTitle, lastName, locationAbr, locationNumber, startHour);
      sectionsList.add(section);
    }

    return sectionsList;
  }
  public void createTables(String db_filename) throws SQLException {
    Connection conn = DriverManager.getConnection(SQLITEDBPATH + db_filename);
    Statement statement = conn.createStatement();

    statement.executeUpdate("DROP TABLE IF EXISTS DEPARTMENT;");
    String departmentTable = "CREATE TABLE IF NOT EXISTS DEPARTMENT (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT," +
            "building TEXT" +
            ");";
    statement.execute(departmentTable);

    statement.executeUpdate("DROP TABLE IF EXISTS MAJOR;");
    String majorTable = "CREATE TABLE IF NOT EXISTS MAJOR (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "department INTEGER," +
            "name TEXT," +
            "FOREIGN KEY(department) REFERENCES DEPARTMENT(id)" +
            ");";
    statement.execute(majorTable);

    statement.executeUpdate("DROP TABLE IF EXISTS FACULTY;");
    String facultyTable = "CREATE TABLE IF NOT EXISTS FACULTY (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT," +
            "department INTEGER," +
            "startDate INTEGER," +
            "endDate INTEGER," +  
            "office INTEGER," +
            "FOREIGN KEY(department) REFERENCES DEPARTMENT(id)" +
            ");";
    statement.execute(facultyTable);

    statement.executeUpdate("DROP TABLE IF EXISTS STUDENT;");
    String studentTable = "CREATE TABLE IF NOT EXISTS STUDENT (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT," +
            "graduationDate INTEGER," +
            "major INTEGER," +
            "adviser INTEGER," +
            "FOREIGN KEY(major) REFERENCES MAJOR(id)," +
            "FOREIGN KEY(adviser) REFERENCES FACULTY(id)" +
            ");";
    statement.execute(studentTable);

    statement.executeUpdate("DROP TABLE IF EXISTS SEMESTER;");
    String semesterTable = "CREATE TABLE IF NOT EXISTS SEMESTER (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "year INTEGER," +
            "season TEXT" +
            ");";
    statement.execute(semesterTable);

    statement.executeUpdate("DROP TABLE IF EXISTS SECTION;");
    String sectionTable = "CREATE TABLE IF NOT EXISTS SECTION (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "course INTEGER," +
            "instructor INTEGER," +
            "location INTEGER," +
            "startDate INTEGER," +
            "endDate INTEGER," +
            "startHour INTEGER" +
            ");";
    statement.execute(sectionTable);

    statement.executeUpdate("DROP TABLE IF EXISTS LOCATION;");
    String locationTable = "CREATE TABLE IF NOT EXISTS LOCATION (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "building TEXT," +
            "room INTEGER," +
            "purpose TEXT" +
            ");";
    statement.execute(locationTable);

    statement.executeUpdate("DROP TABLE IF EXISTS COURSE;");
    String courseTable = "CREATE TABLE IF NOT EXISTS COURSE (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "department INTEGER," +
            "abbreviation TEXT," +
            "number INTEGER," +
            "title TEXT," +
            "credits INTEGER" +
            ");";
    statement.execute(courseTable);

    statement.executeUpdate("DROP TABLE IF EXISTS ENROLLMENT;");
    String enrollmentTable = "CREATE TABLE IF NOT EXISTS ENROLLMENT (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "student INTEGER," +
            "section INTEGER," +
            "grade TEXT DEFAULT NULL," +
            "FOREIGN KEY(student) REFERENCES STUDENT(id)," +
            "FOREIGN KEY(section) REFERENCES SECTION(id)" +
            ");";
    statement.execute(enrollmentTable);

statement.execute("CREATE TRIGGER IF NOT EXISTS set_future_grade_to_null " +
                  "BEFORE INSERT ON enrollment " +
                  "FOR EACH ROW BEGIN " +
                  "  UPDATE enrollment SET grade = NULL WHERE NEW.section IN (SELECT id FROM SECTION WHERE startDate > CURRENT_DATE); " +
                  "END;");


                  statement.execute("CREATE TRIGGER IF NOT EXISTS set_endDate_to_null " +
                  "BEFORE INSERT ON faculty " +
                  "BEGIN " +
                  "  UPDATE faculty SET endDate = NULL WHERE NEW.id = id; " +
                  "END;");

    statement.close();
    conn.close();
}

  /**
   * @param db_filename
   * @param league
   * @throws java.sql.SQLException
   */
  public void writeStudentTable(String db_filename) throws SQLException {
    Faker faker = new Faker();
    Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
    db_connection.setAutoCommit(false);
    Statement statement = db_connection.createStatement();

    // Get the maximum ID from the MAJOR table
    ResultSet rsMajor = statement.executeQuery("SELECT COUNT(*) FROM MAJOR");
    int maxMajorID = rsMajor.getInt(1);

    // Get the maximum ID from the FACULTY table
    ResultSet rsFaculty = statement.executeQuery("SELECT COUNT(*) FROM FACULTY");
    int maxFacultyID = rsFaculty.getInt(1);

    statement.execute("PRAGMA foreign_keys = ON;");

    String sql = "INSERT INTO STUDENT (name, graduationDate, major, adviser) VALUES (?, ?, ?, ?)";
    PreparedStatement statement_prepared = db_connection.prepareStatement(sql);

    for (int i = 0; i < 100; i++) {
      statement_prepared.setString(1, faker.name().fullName()); 
      statement_prepared.setInt(2, faker.number().numberBetween(2022, 2030)); 
      statement_prepared.setInt(3, faker.number().numberBetween(1, maxMajorID + 1));
      statement_prepared.setInt(4, faker.number().numberBetween(1, maxFacultyID + 1)); 

      statement_prepared.executeUpdate();
    }
    db_connection.commit();
    db_connection.close();
  }
  public void writeCourseTable(String db_filename, ArrayList<Courses> courseList) throws SQLException {
    try (
      Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
      Statement statement = db_connection.createStatement()
    ) {
      statement.execute("PRAGMA foreign_keys = ON;");
      db_connection.setAutoCommit(false);
  
      String sql = "INSERT INTO COURSE(department, abbreviation, number, title, credits) VALUES(?, ?, ?, ?, ?)";
  
      try (PreparedStatement preparedStatement = db_connection.prepareStatement(sql)) {
        for (Courses course : courseList) {
        
          PreparedStatement statement_prepared = db_connection.prepareStatement(
            "SELECT department " +
            "FROM FACULTY " +
            "WHERE name = ?"
          );
          statement_prepared.setString(1, course.getInstructor());  
          ResultSet rs = statement_prepared.executeQuery();
          if (rs.next()) {
            int departmentId = rs.getInt("department");
            preparedStatement.setInt(1, departmentId);
          }
          rs.close();
  
          preparedStatement.setString(2, course.getAbbreviation());
          preparedStatement.setInt(3, course.getNumber());
          preparedStatement.setString(4, course.getTitle());
          preparedStatement.setInt(5, Integer.parseInt(course.getCredits()));
          preparedStatement.executeUpdate();
        }
  
        db_connection.commit();
      }
    } catch (SQLException e) {

      throw e;
    }
}

  public void writeSemesterTable(String db_filename) throws SQLException {
    Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
    db_connection.setAutoCommit(false);
    Statement statement = db_connection.createStatement();
    statement.execute("PRAGMA foreign_keys = ON;");

    String sql = "INSERT INTO SEMESTER (year, season) VALUES (?, ?)";
    String[] terms = {"Fall", "Spring", "Summer"};
    int year = 2023;

    for (String term : terms) {
        PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
        statement_prepared.setInt(1, year);
        statement_prepared.setString(2, term);
        statement_prepared.executeUpdate();
    }
    year++;

    for (String term : terms) {
        PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
        statement_prepared.setInt(1, year);
        statement_prepared.setString(2, term);
        statement_prepared.executeUpdate();
    }

    db_connection.commit();
    db_connection.close();
}

  
public void writeMajorTable(String db_filename, ArrayList<Programs> programs) throws SQLException {
    Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
    db_connection.setAutoCommit(false);
    Statement statement = db_connection.createStatement();
    statement.execute("PRAGMA foreign_keys = ON;");

    String sql = "INSERT INTO MAJOR (name, department) VALUES (?, ?)";
    
    for (Programs program : programs) {
        PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
        statement_prepared.setString(1, program.getProgram());

        ResultSet rs = statement.executeQuery("SELECT id FROM DEPARTMENT WHERE name = '" + program.getProgram() + "'");
        if (rs.next()) {
            int departmentId = rs.getInt("id");
            statement_prepared.setInt(2, departmentId);
        }
        rs.close();

        statement_prepared.executeUpdate();
    }

    db_connection.commit();
    db_connection.close();
}

  
  public void writeFacultyTable(String db_filename) throws SQLException {
    ArrayList<Faculty> facultyList = readFacultyFromWeb();
    Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
    db_connection.setAutoCommit(false);
    String sql = "INSERT INTO FACULTY (name, department, startDate, endDate, office) VALUES (?, ?, ?, ?, ?)";
    PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
    for (Faculty faculty : facultyList) {
      Statement statement = db_connection.createStatement();
      ResultSet rs = statement.executeQuery("SELECT id FROM DEPARTMENT WHERE name = '" + faculty.getDepartment() + "';");
      int departmentId = -1; 
      if (rs.next()) {
        departmentId = rs.getInt("id");
      }
      rs.close();
      statement.close();
  
      if (departmentId != -1) {
        statement_prepared.setString(1, faculty.getName());
        statement_prepared.setInt(2, departmentId);
        statement_prepared.setInt(3, 2023); 
        statement_prepared.setInt(4, 2024);
        statement_prepared.setInt(5, faculty.getOffice());
  
        statement_prepared.executeUpdate();
      }
    }
    db_connection.commit();
    db_connection.close();
  }
  
  
  public void writeDepartmentTable(String db_filename, ArrayList<Departments> departments) throws SQLException {
    Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
    db_connection.setAutoCommit(false);
    Statement statement = db_connection.createStatement();
    statement.execute("PRAGMA foreign_keys = ON;");
    String sql = "INSERT INTO DEPARTMENT (name, building) VALUES (?, ?)";
  
    for (Departments department : departments) {
      PreparedStatement statement_prepared = db_connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      statement_prepared.setString(1, department.getDepartment());
      statement_prepared.setString(2, department.getBuilding());
      statement_prepared.executeUpdate();
      
    }
  
    db_connection.commit();
    db_connection.close();
  }
  
  public void writeSectionsTable(String db_filename, ArrayList<Sections> sectionsList) throws SQLException {
    Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
    db_connection.setAutoCommit(false);
    Statement statement = db_connection.createStatement();
    statement.execute("PRAGMA foreign_keys = ON;");

    String sql = "INSERT INTO SECTION (course, instructor, location, startDate, endDate, startHour) VALUES (?, ?, ?, ?, ?, ?)";

    for (Sections section : sectionsList) {
        int courseNumber = section.getCourseNumber();
        int numSections = 0;

        if (courseNumber >= 100 && courseNumber < 200) {
            numSections = 4;
        } else if (courseNumber >= 200 && courseNumber < 400) {
            numSections = 2;
        } else if (courseNumber >= 400) {
            numSections = 1;
        }

        for (int i = 0; i < numSections; i++) {
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);

PreparedStatement psCourse = db_connection.prepareStatement("SELECT id FROM COURSE WHERE title = ?");
psCourse.setString(1, section.getCourseTitle());
ResultSet rsCourse = psCourse.executeQuery();
if (rsCourse.next()) {
    int courseId = rsCourse.getInt("id");
    statement_prepared.setInt(1, courseId);
}
rsCourse.close();

PreparedStatement psFaculty = db_connection.prepareStatement("SELECT id FROM FACULTY WHERE name = ?");
psFaculty.setString(1, section.getInstructor());
ResultSet rsFaculty = psFaculty.executeQuery();
if (rsFaculty.next()) {
    int instructorId = rsFaculty.getInt("id");
    statement_prepared.setInt(2, instructorId);
}
rsFaculty.close();


PreparedStatement psLocation = db_connection.prepareStatement("SELECT id FROM LOCATION WHERE building = ?");
psLocation.setString(1, section.getLocationString());
ResultSet rsLocation = psLocation.executeQuery();
if (rsLocation.next()) {
    int locationId = rsLocation.getInt("id");
    statement_prepared.setInt(3, locationId);
}
rsLocation.close();

            statement_prepared.setInt(4, 2023); 
            statement_prepared.setInt(5, 2024);

            statement_prepared.setTime(6, section.getStartHour());

            statement_prepared.executeUpdate();
        }
    }

    db_connection.commit();
    db_connection.close();
}

public void writeLocationsTable(String db_filename, ArrayList<Sections> sectionsList) throws SQLException {
    Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
    db_connection.setAutoCommit(false);
    Statement statement = db_connection.createStatement();
    statement.execute("PRAGMA foreign_keys = ON;");

    String sql = "INSERT INTO LOCATION (building, room, purpose) VALUES (?, ?, ?)";
    Faker faker = new Faker();

    for (Sections section : sectionsList) {
        PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
        statement_prepared.setString(1, section.getLocationString());
        statement_prepared.setInt(2, section.getLocationNumber());
        statement_prepared.setString(3, faker.lorem().sentence());

        statement_prepared.executeUpdate();
    }

    db_connection.commit();
    db_connection.close();
}
public void writeEnrollmentTable(String db_filename) throws SQLException {
    Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
    db_connection.setAutoCommit(false);
    Statement statement = db_connection.createStatement();
    statement.execute("PRAGMA foreign_keys = ON;");

    String sql = "INSERT INTO ENROLLMENT (student, section, grade) VALUES (?, ?, ?)";


    List<Integer> studentIds = new ArrayList<>();
    ResultSet rsStudent = statement.executeQuery("SELECT id FROM STUDENT");
    while (rsStudent.next()) {
        studentIds.add(rsStudent.getInt("id"));
    }
    rsStudent.close();

    List<Integer> courseIds = new ArrayList<>();
    ResultSet rsCourse = statement.executeQuery("SELECT id FROM SECTION");
    while (rsCourse.next()) {
        courseIds.add(rsCourse.getInt("id"));
    }
    rsCourse.close();

    int courseCount = courseIds.size();

    for (int studentId : studentIds) {
        PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
        statement_prepared.setInt(1, studentId);

        int randomIndex = new Random().nextInt(courseCount); 
        int randomCourseId = courseIds.get(randomIndex);
        statement_prepared.setInt(2, randomCourseId);

        Faker faker = new Faker();
        String[] possibleGrades = {"A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "F"};
        String randomGrade = faker.options().option(possibleGrades);
        statement_prepared.setString(3, randomGrade);

        statement_prepared.executeUpdate();
    }

    db_connection.commit();
    db_connection.close();
}



public static void main(String[] args) {
    DBWriter dbWriter = new DBWriter();
    String db_filename = "luther.sqlite"; 

    try {
        dbWriter.createTables(db_filename);
        testTriggers();

        ArrayList<Departments> departmentsList = dbWriter.readDepartmentsFromTxt("data/luther/departments.txt");
        dbWriter.writeDepartmentTable(db_filename, departmentsList);

        ArrayList<Programs> programsList = dbWriter.readProgramFromTxt("data/luther/programs.txt");
        dbWriter.writeMajorTable(db_filename, programsList);


        dbWriter.writeFacultyTable(db_filename);

        dbWriter.writeStudentTable(db_filename);

        dbWriter.writeSemesterTable(db_filename);

        ArrayList<Courses> coursesList = DBWriter.readCoursesFromWeb(); 
        dbWriter.writeCourseTable(db_filename, coursesList);

        ArrayList<Sections> sectionsList = DBWriter.readSectionsFromWeb();
        dbWriter.writeLocationsTable(db_filename, sectionsList);


        dbWriter.writeSectionsTable(db_filename, sectionsList);
        dbWriter.writeEnrollmentTable(db_filename);
    

    } catch (SQLException | IOException e) {
        e.printStackTrace();
    }
}
public static void testTriggers() throws SQLException {
    String db_filename = "luther.sqlite"; 
    Connection db_connection = DriverManager.getConnection(SQLITEDBPATH + db_filename);
Statement statement = db_connection.createStatement();
statement.execute("INSERT INTO enrollment (student, section, grade) VALUES (1, 1, 'A')");
ResultSet rs = statement.executeQuery("SELECT * FROM enrollment WHERE student = 1 AND section = 1");
if (rs.next()) {
    String grade = rs.getString("grade");
    System.out.println("Grade for future course: " + grade);  
}

statement.execute("INSERT INTO faculty (name, endDate) VALUES ('John Doe', '2023-12-31')");
rs = statement.executeQuery("SELECT * FROM faculty WHERE name = 'John Doe'");
if (rs.next()) {
    String endDate = rs.getString("endDate");
    System.out.println("End date for new faculty: " + endDate);  
}
    db_connection.close();

}



}