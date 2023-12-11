# Final Project CS440

## Table of Contents:

- Setup
- Features & Usage
- Project Structure
- Contributor
- License

## Setup

This guide will walk you through the steps to set up a project from cloning the repository to turning on a virtual environment and installing the required dependencies. Please ensure that you have Python 3.10 or 3.11 (or above) installed on your system before proceeding.

### Step 1: Clone the Repository

1. Open your terminal or command prompt.
2. Navigate to the directory where you want to clone the repository.
3. Run the following command to clone the repository:

```
git clone <https://github.com/Hudson-Pufferfish/cs440-final-project>
```

### Step 2: Create and Activate a Virtual Environment

1. Change your current directory to the project's root directory:

```
cd <project_directory>
```

Replace `<project_directory>` with the name of the cloned repository's directory.

2. Create a new virtual environment by running the following command:

```
python3 -m venv venv
```

3. Activate the virtual environment:

- For Windows:

```
.\venv\Scripts\activate
```

- For macOS and Linux:

```
source ./venv/bin/activate
```

Once activated, you will see (venv) prefix in your command prompt, indicating that you are now working within the virtual environment.

### Step 3: Install Dependencies

1. Make sure you are in the project's root directory and your virtual environment is active.
2. Install the project dependencies by running the following commands

```
pip install -r requirements.txt
```

This command will read the [`requirements.txt`](./requirements.txt) file and install all the required dependencies.

Note: If `requirements.txt` is not available, make sure you have a file listing all the project dependencies and their versions.

3. To install pytest, run the following command

```
pip install -e ".[dev]"
```

To run unit tests for a specific test file, use the command `pytest <test_file_name>`. For instance, you can run `pytest tests/rename_test.py` to execute the unit tests in the rename_test.py file.

If you wish to run all the unit tests located in the tests folder, you can simply run `make test`. This command is defined in the Makefile and will automatically execute pytest for each test file in the specified folder.

![Successful tests](https://github.com/Hudson-Pufferfish/cs440-final-project/assets/96578906/d53a08eb-b07e-4d10-9c92-5ef4afee0dcf)

## Features & Usage

- Single-table operations:

  - project(cols)
  - rename(old, new)
  - extend(name, formula)
  - select(query)
  - sort(cols, order)
  - groupby(cols)

- Multi-table operations:
  - product(other)
  - union(other)
  - join(other, condition)
  - semijoin(other)
  - antijoin(other)
  - outerjoin(other)

For detailed documentation on each feature and usage, please refer to the [Feature Documentation](./docs/feature.md).

## Project Structure

Please consult the document named [project-structure.md](./docs/project-structure.md) for information on the project's structure.

## Contributor:

- Hudson Nguyen
- Laura Miro Rodrigo
- Reece Flynn

To access specific information about the tasks and responsibilities assigned to each team member, please consult [the contributing guide](./docs/contributing.md). This guide provides detailed documentation regarding the roles and responsibilities of each individual within the project.

Additionally, it also includes a Git workflow guide that provides instructions and best practices for version control and collaboration using Git.

## License

    Copyright [2023]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
