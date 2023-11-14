#!/usr/bin/env python3

# Import necessary libraries
import csv
from tinydb import TinyDB, Query

def insert_data_from_csv(db, file_name, table_name):
    """
    Insert data from a CSV file into the specified TinyDB table.

    Parameters:
    db: TinyDB database instance.
    file_name: Path to the CSV file.
    table_name: Name of the table in the TinyDB database.

    The function checks if the table already exists in the database.
    If it does, the table is dropped and recreated.
    Data is then read from the CSV file and inserted row-by-row into the TinyDB table.
    """
    with open(file_name, newline='', encoding='utf-8') as csvfile:
        if db.table(table_name).all():  # Check if table exists
            db.drop_table(table_name)  # Drop the table if it exists
        reader = csv.DictReader(csvfile)
        table = db.table(table_name)
        for row in reader:  # Insert each row into the table
            table.insert(row)

def execute_queries(db):
    """
    Execute a series of predefined queries on the TinyDB database.

    Parameters:
    db: TinyDB database instance.

    The function demonstrates various query operations on different tables.
    It includes queries to fetch and process data from tables such as courses,
    departments, enrollments, sections, and students.
    """
    # Define query objects for different tables
    Course = Query()
    Department = Query()
    Enrollment = Query()
    Section = Query()
    Student = Query()
    
    
    # Query 1: Find all courses in the computer science department
    comp_sci_dept_id = db.table('departments').get(Department.DName == 'compsci')['DId']
    courses_in_comp_sci = db.table('courses').search(Course.DeptId == comp_sci_dept_id)
    print("Courses in Computer Science Department:", courses_in_comp_sci)

    # Query 2: Search grades for section 43
    enrollments_in_section_43 = db.table('enrollments').search(Enrollment.SectionId == '43')
    grades_in_section_43 = [(enrollment['StudentId'], enrollment['Grade']) for enrollment in enrollments_in_section_43]
    print("Grades in Section 43:", grades_in_section_43)

    # Query 3: Count the number of sections for a specific course ID
    section_count_course_32 = db.table('sections').count(Section.CourseId == '32')
    print("Number of Sections for Course 32:", section_count_course_32)

    # Query 4: List all departments
    all_departments = db.table('departments').all()
    print("All Departments:", all_departments)

    # Query 5: Find a student by student ID
    student_with_id_1 = db.table('students').get(Student.SId == '1')
    print("Student with ID 1:", student_with_id_1)

    # Query 6: For each course, find the department name
    for course in db.table('courses').all():
        dept_name = db.table('departments').get(Department.DId == course['DeptId'])['DName']
        print(f"Course ID {course['CId']} is in Department: {dept_name}")

    # Query 7: Find all students enrolled in a specific section
    enrolled_students_section_43 = db.table('enrollments').search(Enrollment.SectionId == '43')
    students_in_section_43 = [db.table('students').get(Student.SId == e['StudentId']) for e in enrolled_students_section_43]
    print("Students in Section 43:", students_in_section_43)

    # Query 8: For each department, count the number of students
    for dept in db.table('departments').all():
        students_in_dept = db.table('students').search(Student.MajorId == dept['DId'])
        print(f"Department: {dept['DName']}, Number of Students: {len(students_in_dept)}")

    # Query 9: Search sections of a course offered in a specific year
    sections_course_32_2018 = db.table('sections').search((Section.CourseId == '32') & (Section.YearOffered == '2018'))
    print("Sections of Course 32 offered in 2018:", sections_course_32_2018)

    # Query 10: Find students graduating in a 2021 and their departments:
    grads_2021 = db.table('students').search(Query().GradYear == '2021')
    for student in grads_2021:
        dept = db.table('departments').get(Query().DId == student['MajorId'])
        print("Students graduating in a 2021 and their departments:", student['SName'], dept['DName'])


def main():
    """Main"""
    # Initialize TinyDB
    db = TinyDB('college_data.json')

    # Insert data from CSV files into TinyDB
    insert_data_from_csv(db, 'data/college/COURSE.csv', 'courses')
    insert_data_from_csv(db, 'data/college/DEPT.csv', 'departments')
    insert_data_from_csv(db, 'data/college/ENROLL.csv', 'enrollments')
    insert_data_from_csv(db, 'data/college/SECTION.csv', 'sections')
    insert_data_from_csv(db, 'data/college/STUDENT.csv', 'students')

    # Execute queries
    execute_queries(db)

if __name__ == "__main__":
    main()