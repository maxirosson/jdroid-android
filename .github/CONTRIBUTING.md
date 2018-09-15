How to contribute
======

Looking to contribute something to jdroid? **Here's how you can help.**

Please take a moment to review this document in order to make the contribution
process easy and effective for everyone involved.

Following these guidelines helps to communicate that you respect the time of
the developers managing and developing this open source project. In return,
they should reciprocate that respect in addressing your issue or assessing
patches and features.


## Using the issue tracker

The Github issue tracker is the preferred channel for [bug reports](#bug-reports) and [features or improvement requests](#feature-or-improvement-requests), but please respect the following
restrictions:

* Please **do not** use the issue tracker for personal support requests.
* Assign the **Backlog** milestone to the issue
* Assign the proper **labels** to the issue

## Bug reports

A bug is a _demonstrable problem_ that is caused by the code in the repository.
Good bug reports are extremely helpful, so thanks!

Guidelines for bug reports:

1. **Use the GitHub issue search** &mdash; check if the issue has already been
   reported.

2. **Check if the issue has been fixed** &mdash; try to reproduce it using the `master` branch in the repository.

3. **Isolate the problem** &mdash; ideally create a reduced test
   case and a live example.


A good bug report shouldn't leave others needing to chase you up for more
information. Please try to be as detailed as possible in your report. What is
your environment? What steps will reproduce the issue? What OS
experience the problem? What
would you expect to be the outcome? All these details will help people to fix
any potential bugs.

Example:

> Short and descriptive example bug report title
>
> A summary of the issue and the OS environment in which it occurs. If
> suitable, include the steps required to reproduce the bug.
>
> 1. This is the first step
> 2. This is the second step
> 3. Further steps, etc.
>
> Any other information you want to share that is relevant to the issue being
> reported. This might include the lines of code that you have identified as
> causing the bug, and potential solutions (and your opinions on their
> merits).


## Feature or improvement requests

Feature or improvement requests are welcome. But take a moment to find out whether your idea
fits with the scope and aims of the project. It's up to *you* to make a strong
case to convince the project's developers of the merits of this feature. Please
provide as much detail and context as possible.


## Pull requests

Good pull requests—patches, improvements, new features—are a fantastic
help. They should remain focused in scope and avoid containing unrelated
commits.

**Please ask first** before embarking on any significant pull request (e.g.
implementing features, refactoring code),
otherwise you risk spending a lot of time working on something that the
project's developers might not want to merge into the project.

Please adhere to the [coding guidelines](#code-guidelines) used throughout the
project (indentation, accurate comments, etc.) and any other requirements
(such as test coverage).

Adhering to the following process is the best way to get your work
included in the project:

1. [Fork](http://help.github.com/fork-a-repo/) the project, clone your fork,
   and configure the remotes:

   ```bash
   # Clone your fork of the repo into the current directory
   git clone git@github.com:<your-username>/<repo-name>.git
   # Navigate to the newly cloned directory
   cd <repo-name>
   # Assign the original repo to a remote called "upstream"
   git remote add upstream git@github.com:maxirosson/<repo-name>.git
   ```

2. If you cloned a while ago, get the latest changes from upstream:

   ```bash
git pull --all
git co {BRANCH}
git pull upstream {BRANCH}
   ```

3. Create a new topic branch (off the main project development branch) to
   contain your feature, change, or fix:

   ```bash
   git checkout -b <topic-branch-name>
   ```

4. Please adhere to the [commit guidelines](#commit-guidelines)

5. Locally merge (or rebase) the upstream development branch into your topic branch:

   ```bash
   git pull [--rebase] upstream master
   ```

6. Push your topic branch up to your fork:

   ```bash
   git push origin <topic-branch-name>
   ```

7. [Open a Pull Request](https://help.github.com/articles/using-pull-requests/)
    with a clear title and description against the `master` branch.


## Commit guidelines

* Before sending a code review, you should check all items in the list below:
   * The code compile
   * There aren't warnings
   * The code is appropriately commented
   * The code is appropriately tested
   * All the tests are running
   * Your local code is updated to the latest version
* Commit your changes in logical chunks. 
* Use Git's [interactive rebase](https://help.github.com/articles/interactive-rebase)
feature to tidy up your commits before making them public.
* Please adhere to these [git commit message guidelines](http://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html)
or your code is unlikely be merged into the main project. 
* If your commit closes an issue, add `Close #{ISSUE NUMBER}` to your commit message.

## Code guidelines

[Follow these code guidelines](https://github.com/maxirosson/jdroid/wiki/Code-guidelines)

