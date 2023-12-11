import pytest
from src.main import *

class TestRelationUnion:
    @pytest.fixture(autouse=True)
    def setup(self):
        self.relation = Relation('data/COURSE.csv')
        
    print("test")
    def test_valid_union(self):
        other = Relation('data/COURSE2.csv')
        assert isinstance(other, Relation)
        unioned_data = self.relation.union(other)
        for key in unioned_data:
            assert key in self.relation.data.keys()
            assert unioned_data[key]
        
        
        