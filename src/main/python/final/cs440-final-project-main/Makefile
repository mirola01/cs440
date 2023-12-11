# Makefile

# Set the source directory and test directory
SRC_DIR := src
TEST_DIR := tests

# Find all Python test files in the test directory
TEST_FILES := $(wildcard $(TEST_DIR)/*.py)

# Define the default target
.DEFAULT_GOAL := test

# Target to run pytest for all test files
test:
	pytest $(TEST_FILES)