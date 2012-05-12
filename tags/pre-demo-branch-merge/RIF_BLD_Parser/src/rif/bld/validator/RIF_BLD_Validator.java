/* 
 * This software is a part of a parser for the BLD dialect 
 * of the RIF language. 
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

package rif.bld.validator;


import java.util.*;
import java.io.*;

import java.net.URI;

import gnu.getopt.LongOpt;
import gnu.getopt.Getopt;    

import rif.bld.parser.*;
import rif.bld.*;

/** Simple command-line validator for RIF BLD. */
public class RIF_BLD_Validator {


    public static void main(String[] args) {

	boolean importClosure = false;
	String[] ruleBaseFileNames = null;

	LongOpt[] longOpts = new LongOpt[256];
	
	// Reserved short option names: i ?
    	longOpts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, '?');
	longOpts[1] = new LongOpt("import_closure", LongOpt.NO_ARGUMENT, null, 'i');

	Getopt optionsParse = new Getopt("", args, "?i", longOpts);
	
	for (int opt = optionsParse.getopt();
	     opt != -1;
	     opt = optionsParse.getopt())
	    {
		switch (opt)
		    {
		    case '?': 
			printUsage();
			System.exit(1);

		    case 'i':
			importClosure = true;
			break;

		    default:
			assert false;

		    }; // switch (opt)


	    }; // for (int nextOpt = optionsParse.getopt();
	
	int optInd = optionsParse.getOptind();

	if (args.length > optInd)
	    {
		ruleBaseFileNames = new String[args.length - optInd];
		for (int i = optInd; i < args.length; ++i)
		    ruleBaseFileNames[i - optInd] = args[i];
	    }
	else
	    {
		System.out.println("No rule base file specified.");
		printUsage();
		System.exit(1);
	    };
	

	try 
	    {	
		DefaultAbstractSyntax absSynFactory = 
		    new DefaultAbstractSyntax();
		
		Parser parser = new Parser();
	       
		for (int i = 0; i < ruleBaseFileNames.length; ++i)
		    {
			System.err.println("\n\n% Processing rule base " + ruleBaseFileNames[i]);
			File ruleBaseFile = new File(ruleBaseFileNames[i]);
			
			AbstractSyntax.Document doc = 
			    parser.parse(ruleBaseFile,absSynFactory);
			
			
			System.out.println(doc.toStringInPresSyntax("   "));
			

			System.err.println("% End of rule base " + ruleBaseFileNames[i]);

		    }; // for (int i = 0; i < ruleBaseFileNames.length; ++i)

	    }
	catch (Exception ex)
	    {
		System.out.println("Error: " + ex);
		ex.printStackTrace();
		System.exit(1);
	    };


    } // main(String[] args)



    private static void printUsage() {

	System.out.println("Usage: RIF_BLD_Validator [OPTIONS] <rule base file>+");
	System.out.println("Options:");
	System.out.println("\t--help -? \n\t\t Print this message.");
	System.out.println("\t--import_closure -i \n\t\tProcess the whole import closures of the rule bases.");
    } // printUsage() 


} // class RIF_BLD_Validator