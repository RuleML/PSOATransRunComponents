

package logic.is.power.tptp_parser;

import java.io.*;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Hashtable;
import java.lang.Exception;



/** This is a program to test TPTP file for being syntactically and 
 *  semantically correct.
 *    
 *   The invocation is:
 *   tptp_tester [-o output_file] [-I include_dir] input_file
 *   tptp_tester -h
 *   
 *     input_file -- an input TPTP file to be checked
 *     -h -- prints help message and exits
 *     -o output_file -- outputs the parsed tree into output_file
 *     -I include_dir -- specifies where to look for included files;
 *         if -I is ommited, all include directives will be ignored\n\n
 *
 *     
 *  @author Andrei Tchaltsev
 *  @author Alexandre Riazanov
 *  @since Mar 26, 2006
 *  @since Apr 19, 2006, parsing of includes was added by Alexandre Riazanov
 *     
 */
public class tptp_tester {
    
  public static void help_print() {
    System.out.print(
                     "This is a program to test TPTP file for being syntactically correct.\n"+
                     "The invocation is:\n"+
                     "  tptp_tester [-h] [-o output_file] [-I include_dir] input_file\n"+
                     "  tptp_tester -h\n\n"+
                     "   input_file -- an input TPTP file to be checked\n"+
                     "   -h -- tells the program to print help message and exit\n"+
                     "   -o output_file -- tells the program to output the parsed formulas/clauses into output_file\n" +
                     "   -I include_dir -- specifies where to look for included files; " +
                     "                     if -I is ommited, all include directives will be ignored\n\n"
                     );
    return;
  }
        
  public static void main(String[] args) 
  {
    String inFile = null; /* name of the input file */
    String outFile = null; /* name of the output file */
    String includeDir = null; 
    try {
             
      /* process the command line options */
      /* there is input file */
      if (args.length == 1 && !args[0].equals("-h")) {
        inFile = args[0];
      }   
      /* there are input and output files */
      else if (args.length == 3 && args[0].equals("-o")) {
        outFile = args[1];
        inFile = args[2];
      }
      /* there is an input file and an include directory */
      else if (args.length == 3 && args[0].equals("-I")) {
        inFile = args[2];
        includeDir = args[1];
      }
      /* there are input and output files, and an include directory */
      else if (args.length == 5 && args[0].equals("-o") && args[2].equals("-I")) {
        outFile = args[1];
        includeDir = args[3];
        inFile = args[4];
      }
      /* there are input and output files, and an include directory */
      else if (args.length == 5 && args[2].equals("-o") && args[0].equals("-I")) {
        outFile = args[3];
        includeDir = args[1];
        inFile = args[4];
      }
      else { /* print the help message and exit */ 
        help_print();
        return;
      }
                
      /* check the input file for existence.
         This check is done just to output nicer error messages 
      */
      {
        File file = new File(inFile);
        if (!file.isFile()) {
          System.err.print("Error: cannot find an input file \""
                           + inFile + "\"\n");
          return;
        }
        if (!file.canRead()) {
          System.err.print("Error: cannot read an input file \""
                           + inFile + "\"\n");
          return;
        }
      }

      /* the whole output of the parser */
      LinkedList<SimpleTptpParserOutput.TopLevelItem> allParsed = 
        new LinkedList<SimpleTptpParserOutput.TopLevelItem>();

      /* create a parser output object */
      SimpleTptpParserOutput outputManager = new SimpleTptpParserOutput();
             
      /* prepare the symbol table for semantic checks */

      _signature.put(new String("="),new SymbolDescriptor(true,2)); // "=" is a binary predicate 

      /* parse the input file and semantically check the results */
             
      parseAndCheck(inFile,includeDir,outputManager,allParsed,0);

             
      System.out.println("File \"" + inFile + "\" is OK");

      /* printing the parsed formulas/clauses to a file*/
      if (outFile != null) {           
        FileOutputStream out = new FileOutputStream(outFile);
        for (TptpParserOutput.TptpInput iter : allParsed) {
          out.write((iter.toString() + "\n").getBytes());
        } /* for */
        System.out.println("Parsed formulas/clauses were printed to \"" + outFile + "\"");
        out.close();
      } /* if */
    }
    // general ANTLR exception. It is enough to catch all ANTRL exceptions
    catch(antlr.ANTLRException e) {
      System.err.println("\nERROR during parsing \"" + inFile + "\": "+e);
    }
    // general exception. it is enough to catch all exceptions.
    catch(Exception e) { // general exception
      System.err.println("\nGENERAL exception: "+e);
    }
  } /* end of main function */

    
    /** Parses the file <strong> fileName </strong> and checks the results semantically.
     *  If <strong> includeDir </strong> != null, included files are parsed recursively
     *  as soon as they appear.
     */
  private 
    static 
    void parseAndCheck(String fileName,
                       String includeDir,
                       SimpleTptpParserOutput outputManager,
                       LinkedList<SimpleTptpParserOutput.TopLevelItem> results,
                       int recursionDepth) 
    throws Exception
  {
    FileInputStream in = new FileInputStream(fileName);
    TptpLexer lexer = new TptpLexer(new DataInputStream(in));
    TptpParser parser = new TptpParser(lexer);
               
    for (SimpleTptpParserOutput.TopLevelItem item = 
           (SimpleTptpParserOutput.TopLevelItem)parser.topLevelItem(outputManager);
         item != null;
         item = (SimpleTptpParserOutput.TopLevelItem)parser.topLevelItem(outputManager))
      {
//DEBUG         System.out.println(item);
        if (includeDir != null &&
            item.getKind() == TptpParserOutput.TptpInput.Kind.Include)
          {                    
            if (recursionDepth >= 1024)
              throw new Exception("Too many nested include directives (depth > 1024) " +
                                  "in " + fileName + ", line " + item.getLineNumber() + ".");

            String relativeIncludeFileName = 
              ((SimpleTptpParserOutput.IncludeDirective)item).getFileName();
                    
            // The file name has to be truncated because it is quoted
                    
            relativeIncludeFileName = 
              relativeIncludeFileName.substring(1,
                                                relativeIncludeFileName.length() - 1);

            String includeFileName =  
              includeDir + "/" + relativeIncludeFileName;

            parseAndCheck(includeFileName,
                          includeDir,
                          outputManager,
                          results,
                          recursionDepth + 1);
          }
        else
          {
            /* Minimal semantic analysis: check that no symbol is used
               both as a predicate and as a function or constant, and that 
               no symbol is used with different arities.
               checkSemantically(item) will throw 
               java.lang.Exception(<error description>) if something is wrong 
               with the item.
            */             
            checkSemantically(item,fileName);

            results.add(item);
          };
      };

    in.close();

  } // parseAndCheck(..)



    /** Checks whether <strong> item </strong> is semantically well-formed 
     *  wrt the signature <strong> _signature </strong>. 
     *  Throws java.lang.Exception(<error description>) if the use of some symbol
     *  in <strong> item </strong> is inconsistent with its declaration in  
     *  <strong> _signature </strong>. Side effect: new symbols are declared in 
     *  <strong> _signature </strong>.
     *  @param item != null
     *  @param fileName file name to be used in error messages
     */
  private 
    static void checkSemantically(SimpleTptpParserOutput.TopLevelItem item,
                                  String fileName) 
    throws Exception
  {
    assert item != null;

    _semanticCheckFileName = fileName;
    _semanticCheckLineNumber = item.getLineNumber();

    switch (item.getKind()) {
    case Formula: 
      checkSemantically(((SimpleTptpParserOutput.AnnotatedFormula)item).getFormula()); 
      break;
    case Clause: 
      checkSemantically(((SimpleTptpParserOutput.AnnotatedClause)item).getClause()); 
      break;
    case Include: break; // nothing to check
    } 
  } // checkSemantically(SimpleTptpParserOutput.TopLevelItem item)



    /** Auxilliary for 
     *  checkSemantically(SimpleTptpParserOutput.TopLevelItem item,String fileName):
     *  checks whether <strong> formula </strong> is semantically well-formed 
     *  wrt the signature <strong> _signature </strong>. Side effect: new symbols are 
     *  declared in <strong> _signature </strong>.
     */
  private 
    static void checkSemantically(SimpleTptpParserOutput.Formula formula)
    throws Exception
  {

    switch (formula.getKind()) {            
    case Atomic: 
      checkSemantically((SimpleTptpParserOutput.Formula.Atomic)formula);
      break;
    case Negation: 
      checkSemantically(((SimpleTptpParserOutput.Formula.Negation)formula).getArgument());
      break;
    case Binary: 
      checkSemantically(((SimpleTptpParserOutput.Formula.Binary)formula).getLhs());
      checkSemantically(((SimpleTptpParserOutput.Formula.Binary)formula).getRhs());
      break;
    case Quantified: 
      checkSemantically(((SimpleTptpParserOutput.Formula.Quantified)formula).getMatrix());
      break;
    } 
  } // checkSemantically(SimpleTptpParserOutput.Formula formula)

    
    /** Auxilliary for 
     *  checkSemantically(SimpleTptpParserOutput.TopLevelItem item,String fileName):
     *  checks whether <strong> formula </strong> is semantically well-formed 
     *  wrt the signature <strong> _signature </strong>. Side effect: new symbols are 
     *  declared in <strong> _signature </strong>.
     */
  private 
    static void checkSemantically(SimpleTptpParserOutput.Clause clause) 
    throws Exception
  {
        
    if (clause.getLiterals() != null) {
          
      for (SimpleTptpParserOutput.Literal lit : clause.getLiterals()) 
        checkSemantically(lit.getAtom());

    };
  } // checkSemantically(SimpleTptpParserOutput.Clause clause)


    /** Auxilliary for 
     *  checkSemantically(SimpleTptpParserOutput.TopLevelItem item,String fileName):
     *  checks whether <strong> formula </strong> is semantically well-formed 
     *  wrt the signature <strong> _signature </strong>. Side effect: new symbols are 
     *  declared in <strong> _signature </strong>.
     */
  private 
    static void checkSemantically(SimpleTptpParserOutput.Formula.Atomic atom)
    throws Exception
  {
        
    String predicate = atom.getPredicate();

    int arity = atom.getNumberOfArguments();
        
    // Look up this symbol in the signature:
    SymbolDescriptor predicateDescriptor = _signature.get(predicate);
        
    if (predicateDescriptor == null) {
      // New symbol, has to be added to _signature
      _signature.put(predicate,new SymbolDescriptor(true,arity));
    }
    else
      {
        if (!predicateDescriptor.isPredicate) {
          throw new Exception("Semantic error in " + _semanticCheckFileName +
                              ", line " + _semanticCheckLineNumber + ":" +
                              "in atom " + atom + ": predicate " + 
                              predicate + " was used as a function elsewhere.");
        };

        if (predicateDescriptor.arity != arity) {
          throw new Exception("Semantic error in " + _semanticCheckFileName +
                              ", line " + _semanticCheckLineNumber + ":" +
                              "in atom " + atom + ": predicate " + 
                              predicate + " was used with a different arity elsewhere.");
        };
      };

    // Check the arguments
        
    if (atom.getArguments() != null)
      for (SimpleTptpParserOutput.Term arg : atom.getArguments())
        checkSemantically(arg);
       
  } // checkSemantically(SimpleTptpParserOutput.Formula.Atomic atom)
    

    /** Auxilliary for 
     *  checkSemantically(SimpleTptpParserOutput.TopLevelItem item,String fileName):
     *  checks whether <strong> formula </strong> is semantically well-formed 
     *  wrt the signature <strong> _signature </strong>. Side effect: new symbols are 
     *  declared in <strong> _signature </strong>.
     */
  private 
    static void checkSemantically(SimpleTptpParserOutput.Term term) 
    throws Exception
  {
        
    if (!term.getTopSymbol().isVariable()) {

      String function = term.getTopSymbol().getText();

      int arity = term.getNumberOfArguments();

      // Look up the function symbol in the signature
      SymbolDescriptor functionDescriptor = _signature.get(function);

      if (functionDescriptor == null) {
        // New symbol, has to be added to _signature
        _signature.put(function,new SymbolDescriptor(false,arity));
      }
      else
        {
          if (functionDescriptor.isPredicate)  {
            throw new Exception("Semantic error in " + _semanticCheckFileName +
                                ", line " + _semanticCheckLineNumber + ":" +
                                "in term " + term + ": function " + 
                                function + " was used as a predicate elsewhere.");
          };

          if (functionDescriptor.arity != arity) {
            throw new Exception("Semantic error in " + _semanticCheckFileName +
                                ", line " + _semanticCheckLineNumber + ":" +
                                "in term " + term + ": function " + 
                                function + 
                                " was used with a different arity elsewhere.");
          };
        };
                        

      // Check the arguments
        
      if (term.getArguments() != null)
        for (SimpleTptpParserOutput.Term arg : term.getArguments())
          checkSemantically(arg);

    }; // if (!term.getTopSymbol().isVariable())
  } // checkSemantically(SimpleTptpParserOutput.Term term)

    /** Keeps information about the usage of a symbol: the category (predicate or function)
     *  and the arity.
     */
  private static class SymbolDescriptor {
        
    public SymbolDescriptor(boolean isPred,int ar) {
      isPredicate = isPred;
      arity = ar;
    }

    public boolean isPredicate;

    public int arity;

  } // class SymbolDescriptor

    /** Keeps descriptors of all symbols registered so far. */
  private static Hashtable<String,SymbolDescriptor> _signature = 
        new Hashtable<String,SymbolDescriptor>();

    /** This is here to avoid threading this information through different
     *  variants of checkSemantically(_).
     */
    private static String _semanticCheckFileName;

    /** This is here to avoid threading this information through different
     *  variants of checkSemantically(_).
     */
    private static int _semanticCheckLineNumber;

} /* end of the class */

