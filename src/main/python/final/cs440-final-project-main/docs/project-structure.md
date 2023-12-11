# 🗄️ Project Structure

## An overview of the project's folders and configuration/setup files at a high level:

`├──`[`data`](../data/) - Test data (csv files) for unit testing<br>
`├──`[`docs`](../docs/) — shared documentations for workflow<br>
`├──`[`src`](../src/) — The file [src/main.py](../src/main.py) contains all the implementations of our relational algebra operations.<br>
`├──`[`tests`](../tests) — The folder includes separate files for each of the 12 relational algebra operations, with each file containing the corresponding unit test<br>
`├──`[`.gitignore`](../.gitignore) — Specifies which files and directories should be ignored and not tracked by Git when committing changes to a repository<br>
`├──`[`Makefile`](../Makefile) — Defines rules and instructions for automating tasks in the project: `make test` - runs all the unit tests in the [tests](../tests/) folder simultaneously<br>
`├──`[`pyproject.toml`](../pyproject.toml) — Configuration file in Python project, commonly used with Poetry, that specifies project details and settings.<br>
`├──`[`pytest.ini`](../pytest.ini) — Configuration file for the pytest testing framework that allows customization of test behavior and settings within a project<br>
`├──`[`setup.py`](../setup.py) — Contains metadata and installation instructions for packages<br>
