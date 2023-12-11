import pytest

from src.main import *

class TestRelationProject:
    @pytest.fixture(autouse=True)
    def setup(self):
        self.relation = Relation('data/COURSE.csv')
    
    def test_project_column(self):
        field_names = ['CId','Title']
        #Verify that columns exist
        for name in field_names:
            assert name in self.relation.data.keys()
        project_data = self.relation.project(field_names)
        #verify proper columns were projected
        for key in project_data.keys():
            assert key in field_names
        #verify that data is the same
        for key in project_data.keys():
            assert project_data[key] == self.relation.data[key]

    def test_project_not_a_column(self):
        field_names = ['Car']
        # verify error is raised
        with pytest.raises(ValueError):
            project_data1 = self.relation.project(field_names)
