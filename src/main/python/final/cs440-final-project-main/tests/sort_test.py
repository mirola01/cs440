import pytest
from src.main import Relation

class TestRelationSort:
    @pytest.fixture(autouse=True)
    def setup(self):
        # Initialize Relation with a sample CSV for testing
        self.relation = Relation("data/COURSE.csv")  # Assuming sorting is tested on COURSE.csv

    def test_sort_single_column_ascending(self):
        self.relation.sort(['Title'], 'ascending')
        titles = self.relation.data['Title']
        assert titles == sorted(titles)

    def test_sort_single_column_descending(self):
        self.relation.sort(['Title'], 'descending')
        titles = self.relation.data['Title']
        assert titles == sorted(titles, reverse=True)

    def test_sort_multiple_columns(self):
        # Assuming another column, for example, 'CId' exists and is relevant for sorting
        self.relation.sort(['DeptId', 'Title'], 'ascending')
        # Extract the sorted columns
        dept_ids = self.relation.data['DeptId']
        titles = self.relation.data['Title']
        # Create pairs of (DeptId, Title) to assert sorting order
        sorted_pairs = list(zip(dept_ids, titles))
        # Assert that pairs are sorted first by DeptId, then by Title
        assert sorted_pairs == sorted(sorted_pairs, key=lambda x: (x[0], x[1]))

    def test_sort_invalid_column(self):
        with pytest.raises(ValueError):
            self.relation.sort(['NonExistentColumn'], 'ascending')

    def test_sort_invalid_order(self):
        with pytest.raises(ValueError):
            self.relation.sort(['Title'], 'invalid_order')

if __name__ == '__main__':
    pytest.main()
