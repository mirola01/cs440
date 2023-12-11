import pytest
from src.main import Relation

class TestRelationAntiJoin:
    @pytest.fixture(autouse=True)
    def setup(self):
        self.student_relation = Relation('data/STUDENT.csv')
        self.dept_relation = Relation('data/DEPT.csv')
        self.enroll_relation = Relation('data/ENROLL.csv')
        self.section_relation = Relation('data/SECTION.csv')
        self.course_relation = Relation('data/COURSE.csv')

    # Valid Input Tests
    def test_antijoin_students_depts(self):
        # Antijoin between STUDENT and DEPT on MajorId and DId
        self.student_relation.antijoin(self.dept_relation, ('MajorId', 'DId'))
        assert len(self.student_relation.data['SId']) == 0

    def test_antijoin_enroll_students(self):
        # Antijoin between ENROLL and STUDENT on StudentId and SId
        self.enroll_relation.antijoin(self.student_relation, ('StudentId', 'SId'))
        assert len(self.enroll_relation.data['EId']) == 0

    def test_antijoin_section_course(self):
        # Antijoin between SECTION and COURSE on CourseId and CId
        self.section_relation.antijoin(self.course_relation, ('CourseId', 'CId'))
        assert len(self.section_relation.data['SectId']) == 0

    def test_antijoin_enroll_section(self):
        # Antijoin between SECTION and ENROLL on CourseId and CId
        self.section_relation.antijoin(self.enroll_relation, ('SectId', 'SectionId'))
        expected_sect_id = 23
        assert expected_sect_id in self.section_relation.data['SectId'], "Antijoin on SECTION and ENROLL should include SectId '23'"
        sect_id_23_index = self.section_relation.data['SectId'].index(expected_sect_id)
        assert self.section_relation.data['CourseId'][sect_id_23_index] == 12, "CourseId for SectId 23 does not match expected"

if __name__ == '__main__':
    pytest.main()