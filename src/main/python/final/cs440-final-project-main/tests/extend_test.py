import pytest

from src.main import *

class TestRelationExtend:
    @pytest.fixture(autouse=True)
    def setup(self):
        self.relation = Relation('data/COURSE.csv')
    
    def test_extend_valid_data(self):
        new_col_name = 'year'
        new_col_data = [2016,2017,2018,2019,2020,2021]
        self.relation.extend(new_col_name, new_col_data)
        assert new_col_name in self.relation.data.keys()
        for data in self.relation.data[new_col_name]:
            assert data in new_col_data


    def test_extend_invaldi_data(self):
        new_col_name = 'year'
        new_col_data = [2016,2017,2018,2019,2020,2021,2022,2023]
        with pytest.raises(IndexError):
            self.relation.extend(new_col_name, new_col_data)
