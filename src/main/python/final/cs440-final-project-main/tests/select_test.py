import pytest
from src.main import *

class TestRelationSelect:
    @pytest.fixture(autouse=True)
    def setup(self):
        # Initialize Relation with SECTION.csv for testing
        self.relation = Relation("data/SECTION.csv")

    def test_select_valid_query(self):
        query = {"YearOffered": 2018}
        selected_data = self.relation.select(query)
        for col, data in selected_data.items():
            for value in data:
                assert value == 2018 if col == "YearOffered" else True

    def test_select_valid_query_multiple_conditions(self):
        query = {"YearOffered": 2017, "Prof": "newton"}
        selected_data = self.relation.select(query)
        for col, data in selected_data.items():
            for value in data:
                assert (value == 2017 if col == "YearOffered" else (value == "newton" if col == "Prof" else True))

    def test_select_no_match(self):
        query = {"YearOffered": 1999}  # Year not in data
        selected_data = self.relation.select(query)
        assert all(len(data) == 0 for data in selected_data.values())


if __name__ == '__main__':
    pytest.main()
