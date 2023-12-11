import pytest
from src.main import Relation

@pytest.fixture(scope='module')
def section_relation():
    return Relation('data/SECTION.csv')

@pytest.fixture(scope='module')
def course_relation():
    return Relation('data/COURSE.csv')

class TestRelationJoin:
    def test_join(self, section_relation, course_relation):
        original_column_count = len(section_relation.data.keys()) + len(course_relation.data.keys())
        section_relation.join(course_relation, ('CourseId', 'CId'))

        expected_data = {
            'SectId': [13, 23, 33, 43, 53],
            'self.CourseId': [12, 12, 32, 32, 62],
            'Prof': ['turing', 'turing', 'newton', 'einstein', 'brando'],
            'YearOffered': [2018, 2016, 2017, 2018, 2017],
            'other.CId': [12, 12, 32, 32, 62],
            'Title': ['db systems', 'db systems', 'calculus', 'calculus', 'elocution'],
            'DeptId': [10, 10, 20, 20, 30]
        }
        assert section_relation.data == expected_data

        # Test if the number of columns after using join operation 
        # is equal to the sum of the number of columns of the two original relations
        joined_column_count = len(section_relation.data.keys())
        assert joined_column_count == original_column_count


    def test_join_with_nonexistent_column(self, section_relation, course_relation):
        with pytest.raises(ValueError):
            section_relation.join(course_relation, ('CourseId', 'InvalidColumn'))
