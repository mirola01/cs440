import pytest
from src.main import Relation

class TestRelationSemiJoin:
    @pytest.fixture(autouse=True)
    def setup(self):
        self.student_relation = Relation('data/STUDENT.csv')
        self.dept_relation = Relation('data/DEPT.csv')
        self.enroll_relation = Relation('data/ENROLL.csv')
        self.section_relation = Relation('data/SECTION.csv')
        self.course_relation = Relation('data/COURSE.csv')

    # Valid Input Tests
    def test_semijoin_students_depts(self):
        # Semijoin between STUDENT and DEPT on MajorId and DId
        self.student_relation.semijoin(self.dept_relation, ('MajorId', 'DId'))
        assert len(self.student_relation.data['SId']) == 9  

    def test_semijoin_enroll_students(self):
        # Semijoin between ENROLL and STUDENT on StudentId and SId
        self.enroll_relation.semijoin(self.student_relation, ('StudentId', 'SId'))
        assert len(self.enroll_relation.data['EId']) == 6  

    def test_semijoin_section_course(self):
        # Semijoin between SECTION and COURSE on CourseId and CId
        self.section_relation.semijoin(self.course_relation, ('CourseId', 'CId'))
        assert len(self.section_relation.data['SectId']) == 5 
        
if __name__ == '__main__':
    pytest.main()