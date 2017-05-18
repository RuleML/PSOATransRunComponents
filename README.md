Import Project into Eclipse using Eclipse's built-in Git plugin and Prepare for Compilation
1. Install JDK 1.8 or higher and Eclipse 4.2 or higher
2. If you want to submit your changes to this project, you need to fork this project under your own GitHub account
3. Import project into Eclipse
* Open Eclipse, choose File -> Import -> Git -> Project from Git and click "Next"
* Choose "Clone URI" and click "Next"
* Input the URL of your forked repository (if changes need to be submitted) or this GitHub repository (if no changes need to be  submitted)
* Input your GitHub credentials and click "Next"
* Unselect all branches except the "master" branch and click "Next"
* Set your local path of the project folder and click "Next"
* Click "Next" and then "Finish" to import the project into Eclipse
4. Prepare for compilation
* Expand the dropdown list besides the green run button in the Eclipse toolbar and click "Run Configurations"
* Expand "Maven Build" menu, choose any one of the "InterPrologInstall" items, and click "Run". This will install the InterProlog library to your local Maven repository.
* There should be red-cross error icons shown for projects PSOA2X, PSOACore, and tptp-parser. To fix the problem, follow the next steps.
* Expand the "PSOACore" project, open pom.xml and choose the "pom.xml" tab.
* Hover your cursor over the \<execution\> element with a red wavy underline and click the "Discover new m2e connectors" fix option.
* Click "Finish" and follow the guideline to install m2e connectors for ANTLR. The installation will show a security warning "You are installing software that contains unsigned content." Click "OK" to ignore the warning.
* Restart Eclipse to finish the preparation process
