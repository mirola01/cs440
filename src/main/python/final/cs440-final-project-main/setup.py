from setuptools import setup, find_packages

setup(
    name="cs-440-final-project",
    version="0.1.0",
    description="relational algebra operations",
    packages=find_packages(),
    extras_require={
        "dev": [
            "pytest",
        ]
    },
)