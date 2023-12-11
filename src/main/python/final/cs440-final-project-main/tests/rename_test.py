import pytest
from src.main import *

class TestRelationRename:
    @pytest.fixture(autouse=True)
    def setup(self):
        self.relation = Relation('data/COURSE.csv')

    def test_rename_existing_column(self):
        old_column = 'Title'
        new_column = 'CourseTitle'

        # Verify that the old column exists before renaming
        assert old_column in self.relation.data

        # Rename the column
        self.relation.rename(old_column, new_column)

        # Verify that the new column exists after renaming
        assert old_column not in self.relation.data
        assert new_column in self.relation.data

        # Verify that the data in the renamed column is unchanged
        assert self.relation.data[new_column] == ['db systems', 'compilers', 'calculus', 'algebra', 'acting', 'elocution']

    def test_rename_nonexistent_column(self):
        old_column = 'NonexistentColumn'
        new_column = 'NewColumn'

        # Verify that renaming a nonexistent column raises a ValueError
        with pytest.raises(ValueError):
            self.relation.rename(old_column, new_column)

        # Verify that the data dictionary remains unchanged
        assert self.relation.data == {
            'CId': [12, 22, 32, 42, 52, 62],
            'Title': ['db systems', 'compilers', 'calculus', 'algebra', 'acting', 'elocution'],
            'DeptId': [10, 10, 20, 20, 30, 30]
        }