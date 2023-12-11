import pytest
from src.main import *  

class TestRelationOuterJoin:
    @pytest.fixture(autouse=True)
    def setup(self):
        self.relation = Relation('data/COURSE.csv')

    def test_outerjoin(self):
        
        outerJoined_data = self.relation.outerjoin(Relation('data/SECTION2.csv'))
        
        cols_that_should_be_present = ['CId','Title','DeptId','SectId','Prof','YearOffered']
        column_names = outerJoined_data.keys()
        for col in column_names:
            assert col in cols_that_should_be_present

        assert outerJoined_data['CId'] == [12, 22, 32, 42, 52, 62]
        assert outerJoined_data['Title'] == ['db systems', 'compilers', 'calculus', 'algebra', 'acting', 'elocution']
        assert outerJoined_data['DeptId'] == [10, 10, 20, 20, 30, 30]
        assert outerJoined_data['SectId'] == [23, 33, 53, None, None, None]
        assert outerJoined_data['Prof'] == ['turing', 'newton', 'brando', None, None, None]
        assert outerJoined_data['YearOffered'] == [2016, 2017, 2017, None, None, None]
        