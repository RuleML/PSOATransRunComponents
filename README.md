The package PSOATransRunComponents, while listed as one of five siblings (along with PSOA2X, PSOACore, PSOATransRun, PSOATransRunWebService), acts as their root, creating a top-level tree structure.

## Eclipse Import and Preparation

Import Project into Eclipse using Eclipse's Built-in Git Plugin EGit (see https://eclipsesource.com/blogs/tutorials/egit-tutorial) and Prepare for Building:
1. Install JDK 8 or higher and Eclipse 4.2 or higher; configure Eclipse so it uses JDK (see http://techiedan.com/2009/10/19/set-up-jdk-in-eclipse/)
2. If you want to submit your changes to this project as a non-collaborator, you need to fork this project under your own GitHub account
3. Import project into Eclipse
   1. Open Eclipse, choose File -> Import -> Git -> Projects from Git and click "Next"
   2. Choose "Clone URI" and click "Next"
   3. Input the URL of your forked repository (if changes need to be submitted) or this GitHub repository (if no changes need to be  submitted)
   4. Input your GitHub credentials and click "Next"
   5. Unselect all branches except the "master" branch and click "Next"
   6. Set your local path of the project folder and click "Next"
   7. The "Import existing Eclipse projects" radio button should be selected if it is not already. Click "Next" and then "Finish" to import the project into Eclipse (pop-up about missing "natures" can be ignored, if no edits on PSOATransRunWebService, currently for TPTP, will be done)
4. Preparation for building project
   1. Go to Eclipse's "Project" menu item and uncheck "Build Automatically"
   2. Expand the dropdown list besides the green run button in the Eclipse toolbar and click "Run Configurations..."
   3. Expand the "Maven Build" menu, choose any one of the "InterPrologInstall" items, and click "Run". This will install the InterProlog library to your local Maven repository.
   4. There should be red-cross error icons shown for projects PSOA2X and PSOACore. If not, try building the project once as explained in the Section "Building Project in Eclipse" (DO NOT perform "Maven -> Update Project" after this build). To fix these errors, follow the next steps.
   5. Expand the "PSOACore" project, open pom.xml and navigate to the "pom.xml" tab.
   6. Hover your cursor over the \<execution\> element with a red wavy underline and click the "Discover new m2e connectors" fix option.
   7. Click "Finish" and follow the guideline to install m2e connectors for ANTLR. The installation will show a security warning "You are installing software that contains unsigned content". Click "OK" to ignore the warning.
   8. Restart Eclipse to finish the preparation process

## Building Project in Eclipse

* Right-click the PSOATransRunComponents project in the left panel, choose "Run As -> Maven install" to build the project. This does an incremental build of PSOATransRunComponents and all of its subprojects. The path of the output PSOATransRun jar file is as follows, where \<local repo dir\> typically ends in the segment \PSOATransRunComponents:

  \<local repo dir\>\PSOATransRun\target\PSOATransRunLocal.jar
  
  In case Eclipse still shows errors for the projects (as tiny red crosses in the project icons) after a successful building process, right-click the PSOATransRunComponents project and choose "Maven -> Update Project".
* For a clean (not just incremental) build, which is recommended for the last round of testing and release, click "Run As -> Maven clean" before doing the above "Maven install".

## Running Tests

* To test PSOATransRun with provided test directory, execute the following command line from \<local repo dir\>\PSOATransRun

  java -jar target\PSOATransRunLocal.jar --test -i test
  
* To test PSOATransRun with your own test directory, execute the following command line from \<local repo dir\>\PSOATransRun, where \<testDir\> is the path to the test directory:

  java -jar target\PSOATransRunLocal.jar --test -i \<testDir\>

* For authoring test cases refer to the README of https://github.com/RuleML/PSOATransRunComponents/tree/master/PSOATransRun/test

## Release Procedure

* On Github (possibly before "On Eclipse")
    * In the PSOATransRunComponents repo
        * Follow the instruction at https://help.github.com/articles/creating-releases, with the following entries:
            * In "Tag version" field, enter name of new release (also for the co-created new tag), e.g. PSOATransRun-v1.3.2-a
                * Use Semantic Versioning https://semver.org together with a specific prefix, here PSOATransRun-v1.3.2-a
            * Leave "Target:master" unchanged
            * Leave title and description blank
            * Add no additional binaries
            * In general, do not check "pre-release", even though we use "-" notation of Semantic Versioning
            * Click "Publish release"
* On Eclipse (possibly before "On Github")
    * In the psoa-ruleml repo
        * Duplicate (as sibling) the most recent subdirectory of the directory /transrun in the psoa-ruleml repository, e.g. 1.3.1, and rename it, e.g. to 1.3.2-a
        * Delete the jar file in this new directory
        * Copy the jar PSOATransRunLocal.jar from the PSOATransRun/target directory in the PSOATransRunComponents repository to the psoa-ruleml repository
        * Modify the README.txt
        * Commit the three files .jar, README.txt, and README.html (do not commit .project) and push to Github
* On the RuleML Server
    * The cron job running on the RuleML webhost automatically updates from the psoa-ruleml Github repository to http://psoa.ruleml.org/transrun . Verify that the new directory has been uploaded and the README.html (redirect) is functioning properly
* On the RuleML Wiki
    * Update wrt version number (at two places) at http://wiki.ruleml.org/index.php/PSOA_RuleML#PSOATransRun
