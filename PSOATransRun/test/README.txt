The directory stores a test suite used for the automated testing of 
PSOA implementations, e.g. by our Java-based PSOATransRun 
testing module. Each test case is stored under one 
subdirectory, which we will call <testCaseName>. Under this 
subdirectory, there exists one main KB file <testCaseName>-KB.psoa, 
zero or more imported KB files, e.g. written as 
<testCaseName>-KB-importedI.psoa, I=1,2,... (the testing 
module will ignore all files whose names contain anything 
between "KB" and ".psoa"), one or more query file 
<testCaseName>-queryJ.psoa, J=1,2,..., and one answer file 
<testCaseName>-answerJ.psoa for each query 
<testCaseName>-queryJ.psoa.

Query and answer files should be authored such that answer bindings 
use only constants drawn from the KB ("certain answers") instead of
system-generated ones (e.g., _oidcons, Skolem functions), which can 
vary under different command-line options and implementations.

The following command is used to manually execute
<testCaseName>-queryJ.psoa on <testCaseName>-KB.psoa
(the answers will go to the standard output):
java -jar PSOATransRunLocal.jar -i <testCaseName>-KB.psoa -q <testCaseName>-queryJ.psoa
