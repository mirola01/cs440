# Relational Algebra Operations & Usages

This documentation provides an overview of the features implemented in a Python library for performing relational algebra operations using the standard library and built-in collections. The library supports both single-table and multi-table operations.

## Single-Table Operations

#### Project

- The `project(cols)` operation selects specific columns from a table and returns a new table with only those columns.
```python
from relation import Relation

# Create an instance of the Relation class and load data from the 'STUDENT.csv' file
courses = Relation("path_to_csv/COURSE.csv")

# Projects selected field names eg. 'CId' and 'Title'
title_and_id = courses.project(['CId','Title'])
```

#### Rename

- The `rename(old, new)` operation renames a column in a table. It takes two parameters: old (the current column name) and new (the new column name).

```py
from relation import Relation

# Create a Relation object and load data from a CSV file
relation = Relation('path_to_csv/COURSE.csv')

# Rename the 'Title' column to 'CourseTitle'
relation.rename('Title', 'CourseTitle')

```

#### Extend

- The `extend(name, formula)` operation adds a new column to a table based on a formula. It takes two parameters: name (the name of the new column) and formula (a formula or expression used to calculate the values of the new column).
```python
from relation import Relation

# Create an instance of the Relation class and load data from the 'STUDENT.csv' file
courses = Relation("path_to_csv/COURSE.csv")

# Adds a new field with given name and list of values eg. field 'year' with values [2016,2017,2018,2019,2020,2021]
courses_and_year = courses.extend(['year',[2016,2017,2018,2019,2020,2021]])
```

#### Select

- The `select(query)` operation filters rows from a table based on a given query. It takes a dictionary query as a parameter, where keys are column names and values are the corresponding values to match.

```python
from relation import Relation

# Create an instance of the Relation class and load data from the 'STUDENT.csv' file
student = Relation("path_to_csv/STUDENT.csv")

# Select rows where 'MajorId' is 10 (assuming 10 represents a specific major, e.g., Computer Science)
cs_students = student.select({'MajorId': 10})
```

#### Sort

- The `sort(cols, order)` operation sorts the rows of a table based on specified columns. It takes a list of column names cols and an optional order parameter (defaulting to 'ascending').

```python
from relation import Relation

# Create a Relation object and load data from 'SECTION.csv' file
section = Relation('path_to_csv/SECTION.csv')

# Sort the sections by 'YearOffered' in descending order
sorted_sections = section.sort(['YearOffered'], order='descending')
```

#### GroupBy

- The `groupby(cols)` group rows in a table based on one or more columns. The groupby method can be applied to a Relation object, which represents a table loaded from a CSV file. After grouping, you can use aggregate functions like `count`, `sum`, `min`, `max`, and `mean` on the groups

```python
from relation import Relation

# Create an instance of the Relation class and load data from a CSV file
relation = Relation("path_to_csv/SECTION.csv")

# Group by a single column
result = relation.groupby(['CourseId', 'YearOffered']).sum('SectId')

'''
result = {
    (12, 2016): 23,
    (12, 2018): 13,
    (32, 2017): 33,
    (32, 2018): 43,
    (62, 2017): 53
}
'''

relation2 = Relation('path_to_csv/STUDENT.csv')

# Group by multiple columns
result2 = relation2.groupby('MajorId').mean('GradYear')

'''
result2 = {
    (10,): 2021.3333333333333,
    (20,): 2020.25,
    (30,): 2020.5
}
'''
```

## Multi-Table Operations

#### Product

- The `product(other)` operation performs a Cartesian product between two tables and returns a new table with all possible combinations of rows from both tables.

```python
from relation import Relation

section_relation = Relation('path_to_csv/SECTION.csv')
student_relation = Relation('path_to_csv/STUDENT.csv')

section_relation.product(student_relation)
```

#### Union

- The `union(other)` operation combines two tables and returns a new table with all unique rows from both tables.
```python
from relation import Relation

# Create an instance of the Relation class and load data from a CSV file
r1 = Relation('path_to_csv/SECTION.csv')
r2 = Relation('path_to_csv/COURSE.csv')

# Create union of relations r1 and r2 that removes duplicate values form r2
r1_and_r2 = r1.union(r2)

# Create union of relations r1 and r2 that removes duplicate values form r1
r1_and_r2 = r2.union(r1)
```

#### Join

- The `join(other, condition)` operation combines two tables based on a join condition and returns a new table with matching rows from both tables. It takes two parameters: other (the other table to join) and condition (the join condition). Join = Product + Select

```python
from relation import Relation

section_relation = Relation('path_to_csv/SECTION.csv')
course_relation = Relation('path_to_csv/COURSE.csv')

section_relation.join(course_relation, ('CourseId', 'CId'))
```

If we join relations by nonexistent columns, raise `ValueError`

```python
from relation import Relation

course_relation = Relation('path_to_csv/COURSE.csv')
section_relation.join(course_relation, ('CourseId', 'InvalidColumn'))

# ValueError: Column 'InvalidColumn' does not exist in the current relation
```

#### Semijoin

- The `semijoin(other, on)` operation performs a semijoin with another relation. It takes another Relation object other and a tuple on specifying the columns to join on.

```python
from relation import Relation

# Create Relation objects and load data from 'COURSE.csv' and 'ENROLL.csv' files
course = Relation('path_to_csv/COURSE.csv')
enroll = Relation('path_to_csv/ENROLL.csv')

# Perform a semijoin on 'CourseId' between 'COURSE.csv' and 'ENROLL.csv'
courses_with_enrollment = course.semijoin(enroll, ('CId', 'CourseId'))
```

#### Antijoin

- The `antijoin(other, on)` operation performs an antijoin with another relation. Similar to semijoin, it takes another Relation object other and a tuple on.

```python
from relation import Relation

# Create Relation objects and load data from 'DEPT.csv' and 'COURSE.csv' files
dept = Relation('path_to_csv/DEPT.csv')
course = Relation('path_to_csv/COURSE.csv')

# Perform an antijoin on 'DeptId' to find departments without courses
depts_without_courses = dept.antijoin(course, ('DId', 'DeptId'))
```

#### Outer Join

- The `outerjoin(other)` operation combines two tables based on a shared field  and returns a new table with all rows from both tables, including unmatched rows, repalicing missing valuse with `None` It takes one parameter: other (the other table to perform the outer join with).
```python
from relation import Relation

# Create Relation objects with data from csv files 'COURSE.csv' and 'SECTION.csv'
course = Relation('path_to_csv/COURSE.csv')
section = Relation('path_to_csv/SECTION.csv')


# Preform Outer Join on course to inclued section information when avalable
coures_with_section = course.outerjoin(section)
```

