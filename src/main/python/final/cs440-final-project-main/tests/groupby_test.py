import pytest
from src.main import Relation, GroupBy

class TestRelationGroupBy:
    @pytest.fixture(scope='module')
    def section_relation(self):
        return Relation('data/SECTION.csv')

    @pytest.fixture(scope='module')
    def student_relation(self):
        return Relation('data/STUDENT.csv')

    def test_groupby_count(self, section_relation):
        grouped = section_relation.groupby('Prof')
        result = grouped.count('SectId')

        assert result == {
            ('turing',): 2,
            ('newton',): 1,
            ('einstein',): 1,
            ('brando',): 1
        }

    def test_groupby_sum(self, student_relation):
        grouped = student_relation.groupby('GradYear')
        result = grouped.sum('MajorId')

        assert result == {
            (2021,): 50,
            (2020,): 70,
            (2022,): 30,
            (2019,): 20
        }

    def test_groupby_min(self, section_relation):
        grouped = section_relation.groupby('CourseId')
        result = grouped.min('YearOffered')

        assert result == {
            (12,): 2016,
            (32,): 2017,
            (62,): 2017
        }

    def test_groupby_max(self, section_relation):
        grouped = section_relation.groupby('CourseId')
        result = grouped.max('YearOffered')

        assert result == {
            (12,): 2018,
            (32,): 2018,
            (62,): 2017
        }

    def test_groupby_mean(self, student_relation):
        # grouped = student_relation.groupby('MajorId')
        # result = grouped.mean('GradYear')

        result = student_relation.groupby('MajorId').mean('GradYear')

        assert result == {
            (10,): 2021.3333333333333,
            (20,): 2020.25,
            (30,): 2020.5
        }

    def test_groupby_multiple_columns_count(self, student_relation):
        # Group by 'GradYear' and 'MajorId' and count occurrences of 'SId'
        result = student_relation.groupby(['GradYear', 'MajorId']).count('SId')

        expected_result = {
            (2021, 10): 2,
            (2020, 20): 2,
            (2022, 10): 1,
            (2022, 20): 1,
            (2020, 30): 1,
            (2021, 30): 1,
            (2019, 20): 1
        }

        assert result == expected_result

    def test_groupby_multiple_columns_sum(self, section_relation):
        # Group by 'CourseId' and 'YearOffered' and calculate the sum of 'SectId'
        result = section_relation.groupby(['CourseId', 'YearOffered']).sum('SectId')

        expected_result = {
            (12, 2016): 23,
            (12, 2018): 13,
            (32, 2017): 33,
            (32, 2018): 43,
            (62, 2017): 53
        }

        assert result == expected_result