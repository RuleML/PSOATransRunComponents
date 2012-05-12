/*
 * This software is a converter from the BLD dialect of the RIF language
 * to the TPTP format for the first-order predicate logic. 
 *
 * Copyright (C) 2009 Alexandre Riazanov
 *
 * @author Alexandre Riazanov <alexandre.riazanov@gmail.com>
 *
 * @date 17/10/2009
 * 
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Also add information on how to contact you by electronic and paper mail.
 */

package rif.bld.totptp;

import java.util.*;
import java.io.*;

import rif.bld.*;
import tptp_parser.*;

import gnu.getopt.LongOpt;
import gnu.getopt.Getopt;   


/** Command line application for the converter of RIF BLD documents 
 *  into the TPTP format.
 */
public class RIF_BLD_to_TPTP {



    public static void main(String[] args) {

	boolean addCommonAxioms = false;
	boolean importClosure = false;
	boolean generateCNF = false;
	boolean convertConjecture = false;
	String[] documentFileNames = null;
	LinkedList<String> inputParameterFiles = new LinkedList<String>();
	String accumulatedParameterFile = null;

	LongOpt[] longOpts = new LongOpt[256];
	
	// Reserved short option names: a c i j n p ?

	longOpts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, '?');
	longOpts[1] = new LongOpt("common_axioms", LongOpt.NO_ARGUMENT, null, 'c');
	longOpts[2] = new LongOpt("import_closure", LongOpt.NO_ARGUMENT, null, 'i');
	longOpts[3] = new LongOpt("cnf", LongOpt.NO_ARGUMENT, null, 'n');
	longOpts[4] = new LongOpt("conjecture", LongOpt.NO_ARGUMENT, null, 'j');
	longOpts[5] = new LongOpt("parameters", LongOpt.REQUIRED_ARGUMENT, null, 'p');
	longOpts[6] = new LongOpt("accumulated_parameters", LongOpt.REQUIRED_ARGUMENT, null, 'a');
	
	Getopt optionsParse = new Getopt("", args, "?a:cinjp:", longOpts);
	

	for (int opt = optionsParse.getopt();
	     opt != -1;
	     opt = optionsParse.getopt())
	    {
		switch (opt)
		    {
		    case '?': 
			printUsage();
			System.exit(1);
			
		    case 'c':
			addCommonAxioms = true;
			break;

		    case 'i':
			importClosure = true;
			break;

		    case 'n':
			generateCNF = true;
			break;

		    case 'j':
			convertConjecture = true;
			break;

		    case 'p':
			inputParameterFiles.addLast(optionsParse.getOptarg());
			break;

		    case 'a':
			accumulatedParameterFile = optionsParse.getOptarg();
			break;

		    default:
			assert false;

		    }; // switch (opt)


	    }; // for (int nextOpt = optionsParse.getopt();
	

	int optInd = optionsParse.getOptind();

	if (args.length > optInd)
	    {
		documentFileNames = new String[args.length - optInd];
		for (int i = optInd; i < args.length; ++i)
		    documentFileNames[i - optInd] = args[i];
	    }
	else
	    {
		System.out.println("No document URL specified.");
		printUsage();
		System.exit(1);
	    };

	Converter.Parameters parameters = new Converter.Parameters();
	    
	try 
	    {
		
		//             Parse the specified parameter files:
		
		rif.bld.totptp.parameters.Parser parametersParser = 
		    new rif.bld.totptp.parameters.Parser();

		
		for (String parametersFileName : inputParameterFiles) 
		    {
			System.err.println("\n\n% Loading parameters from " + parametersFileName);
			
			File parametersFile = new File(parametersFileName);
			
			if (!parametersFile.isFile() || !parametersFile.canRead())
			    {
				System.err.println("% Error: cannot read parameters from " + parametersFileName);
				System.exit(1);
			    }; 
			
			parametersParser.parse(parametersFile,parameters);
			
			System.err.println("% End of parameters file " + parametersFileName);
			
		    }; // for (String parameterFileName : inputParameterFiles)
	    }
	catch (Throwable ex)
	    {
		System.err.println("% Error while reading parameters: " + ex);
		ex.printStackTrace();
		System.exit(1);

	    }; // catch (Throwable ex)


	
	//               Process the RIF BLD documents:

	try
	    {  

		rif.bld.parser.Parser documentParser = 
		    new rif.bld.parser.Parser();
		
		DefaultAbstractSyntax absSynFactory = 
		    new DefaultAbstractSyntax();

		Converter converter = 
		    new Converter(parameters,generateCNF);
						    

		for (int i = 0; i < documentFileNames.length; ++i)
		    {
			System.err.println("\n\n% Processing document " + documentFileNames[i]);			
			File documentFile = new File(documentFileNames[i]);
			
			if (!documentFile.isFile() || !documentFile.canRead())
			    {
				System.err.println("% Error: cannot read from " + documentFileNames[i]);
				System.exit(1);
			    }; 

			Collection<? extends SimpleTptpParserOutput.TopLevelItem> convertedDoc;

			if (convertConjecture)
			    {
				AbstractSyntax.Formula formula = 
				    documentParser.parseFormula(documentFile,absSynFactory);

				convertedDoc = converter.convertConjecture(formula);
			    }
			else // !convertConjecture
			    {
				AbstractSyntax.Document document =
				    documentParser.parse(documentFile,absSynFactory);
				
				convertedDoc = converter.convert(document);
			    }
				
			if (addCommonAxioms)
			    {
				System.out.println("%  Common axioms:\n\n");
				
				for (SimpleTptpParserOutput.TopLevelItem item : 
					 converter.commonAxioms())
				    System.out.println("\n" + item.toString());
				
				System.out.println("%  End of common axioms.\n\n");
			    };
			
			
			for (SimpleTptpParserOutput.TopLevelItem item : convertedDoc)
			    System.out.println("\n" + item.toString());
			
			System.err.println("% End of document " + documentFileNames[i]);

		    }; // for (int i = 0; i < documentFileNames.length; ++i)
	       
	    }
	catch (Exception ex)
	    {
		System.out.println("Internal error: " + ex);
		ex.printStackTrace();
		System.exit(1);
	    };
	
	

	//               Dump the accumulated parameters:

	
	try
	    {
		if (accumulatedParameterFile != null)
		    {
			FileOutputStream stream = 
			    new FileOutputStream(accumulatedParameterFile);
			
			rif.bld.totptp.parameters.Renderer.render(parameters,stream);
			
			stream.close();
			
		    }; // if (accumulatedParameterFile != null)
		
	    }
	catch (FileNotFoundException ex)
	    {
		System.err.println("% Error while writing accumulated parameters: " + ex);
		System.exit(1);
	    }
	catch (Exception ex)
	    {
		System.out.println("Unclassified error: " + ex);
		ex.printStackTrace();
		System.exit(1);
	    };
	
    } // main(String[] args)


    private static void printUsage() {

	System.out.println("Usage: RIF_BLD_to_TPTP [OPTIONS] <file>+");
	System.out.println("Options:");
	System.out.println("\t--help -? \n\t\t Print this message.");
	System.out.println("\t--common_axioms -c \n\t\tInclude axioms that are common for all documents.");
	System.out.println("\t--import_closure -i \n\t\tProcess the whole import closures of documents.");
	System.out.println("\t--cnf -n \n\t\tClausify the results of conversion.");
	System.out.println("\t--conjecture -j \n\t\tParse a separate formula (in each file) and convert it as a TPTP conjecture.");
	System.out.println("\t--parameters=file -p \n\t\tRead input parameters from the given XML file.");
	System.out.println("\t--accumulated_parameters=file -a \n\t\tSave the parameters in this XML file after all work is done, so that they can be used in later calls.");

    } // printUsage() 

    

} // class RIF_BLD_to_TPTP