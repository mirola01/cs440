# Contributing Documentation

## Responsibilities and Tasks of Team Members

We utilized [random.org](https://www.random.org/) to divide the tasks among team members, ensuring a fair distribution. Each team member took responsibility for their own implementation, documentation, and testing of the assigned operations.

1. Laura Miro Rodrigo:

- Single-table operations:
  - select(query)
  - sort(cols, order)
- Multi-table operations:
  - semijoin(other)
  - antijoin(other)

2. Reece Flynn:

- Single-table operations:
  - project(cols)
  - extend(name, formula)
- Multi-table operations:
  - union(other)
  - outerjoin(other)

3. Hudson Nguyen:

- Single-table operations:
  - rename(old, new)
  - groupby(cols)
- Multi-table operations:
  - product(other)
  - join(other, condition)

## Git Workflow Guide

This guide will walk you through the step-by-step process of using Git from the command line to perform common tasks such as checking out a new branch, adding files, committing changes, pushing to a new branch, and creating a pull request. By following these instructions, you will be able to collaborate effectively with your team members.

### Step 1: Checkout a New Branch

1. Change to the cloned repository's directory:

```
cd <repository-name>
```

Replace `<repository-name>` with the name of the cloned repository's directory.

2. From **main branch**, create and switch to a new branch using the following command:

```
git checkout -b <branch-name>
```

Replace `<branch-name>` with a descriptive name for your new branch.
For example: `hudson/sto-rename`

### Step 2: Add Files

1. Place all files you want to add to the repository in the appropriate directory.
2. Run the following command to stage the changes:

```
git add <file1>
git add <file2>
...
```

Replace `<file1>`, `<file2>`, etc., with the names of the files you want to add. You can also use `.` to add all modified files (not recommended)

before using `git add`, you can use `git status` to see your list of changed files, and `git diff <filename>` to see changes in each file

### Step 3: Commit Changes and Push to a new Branch

```
git commit -m "Commit message"
git push origin <branch-name>
```

### Step 4: Create a Pull Request

1. Go to the repository's web page on the Git hosting platform (e.g., GitHub, GitLab).

2. Switch to the branch you just pushed to.

3. Click on the "New Pull Request" or similar button.

4. Review the changes and add any necessary comments.

5. Submit the pull request.

After that, you can use `git checkout main` and `git pull origin main` to switch back to main branch and pull latest changes from main
