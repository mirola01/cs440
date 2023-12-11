import pytest
from src.main import Relation

class TestRelationProduct:
    @classmethod
    def setup_class(cls):
        # Load SECTION and STUDENT data for testing
        cls.section_relation = Relation('data/SECTION.csv')
        cls.student_relation = Relation('data/STUDENT.csv')
        cls.enroll_relation = Relation('data/ENROLL.csv')

    def test_product_section_student(self):
        # Test when both relations are non-empty
        relation1 = self.section_relation
        original_column1 = relation1.data.keys()

        relation2 = self.student_relation
        original_column2 = relation2.data.keys()
        relation1.product(relation2)

        after_product_column1 = relation1.data.keys()

        assert len(original_column1) * len(original_column2) == len(after_product_column1)

    def test_product_enroll_student(self):
        # Test when both relations are non-empty
        relation1 = self.enroll_relation
        original_column1 = relation1.data.keys()

        relation2 = self.student_relation
        original_column2 = relation2.data.keys()
        relation1.product(relation2)

        after_product_column1 = relation1.data.keys()

        assert len(original_column1) * len(original_column2) == len(after_product_column1)

    def test_product_enroll_section(self):
        # Test when both relations are non-empty
        relation1 = self.enroll_relation
        original_column1 = relation1.data.keys()

        relation2 = self.section_relation
        original_column2 = relation2.data.keys()
        relation1.product(relation2)

        after_product_column1 = relation1.data.keys()

        assert len(original_column1) * len(original_column2) == len(after_product_column1)

    def test_product_with_itself(self):
        # Test when product is performed with the same relation
        relation = self.section_relation
        
        original_column = relation.data.keys()
        relation.product(relation)
        
        # Validate number of columns after using product operation
        after_product_column = relation.data.keys()
        assert len(original_column) ** 2 == len(after_product_column)
