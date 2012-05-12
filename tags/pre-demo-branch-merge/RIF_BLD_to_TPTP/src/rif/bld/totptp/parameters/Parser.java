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

package rif.bld.totptp.parameters;

import java.io.*;
import java.math.*;
import java.util.*;
import java.net.*;
import javax.xml.bind.*;
import javax.xml.bind.util.*;
import javax.xml.validation.*;

import rif.bld.totptp.parameters.jaxb.*;
import rif.bld.totptp.Converter;

/** Reads rif.bld.totptp.Converter parameters from XML files. */
public class Parser {

    public Parser() throws java.lang.Exception {

	JAXBContext jc = 
	    JAXBContext.newInstance("rif.bld.totptp.parameters.jaxb");
	_unmarshaller = jc.createUnmarshaller();
	
	SchemaFactory schemaFactory = 
	    SchemaFactory.
	    newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

	URL schemaURL = 
	    ClassLoader.
	    getSystemResource("resources/config_syntax.xsd");
	
	Schema schema = schemaFactory.newSchema(schemaURL);

	_unmarshaller.setSchema(schema);

    } // Parser()



    /** Tries to parse the parameters file and augments 
     *  the values in <code>parameters</code> correspondingly.
     *  @throws java.lang.Exception if the parameters file does not
     *          comply with the XML Schema for rif.bld.totptp.Converter parameter
     *          files, or the contents of the file clash with some previous
     *          values in <code>parameters</code>.
     */
    public 
	void 
	parse(File file,Converter.Parameters parameters)
	throws java.lang.Exception {

	RIFBLDToTPTPParameters param;

	try
	    {
		param = 
		    (RIFBLDToTPTPParameters)_unmarshaller.unmarshal(file);
	    }
	catch (javax.xml.bind.UnmarshalException ex)
	    {
		throw 
		    new java.lang.Exception("Parameters file cannot be read: " +
					    ex);
	    };

	for (Object topLevelOption : param.getTopLevelOption())
	    {
		if (topLevelOption instanceof IRIToSymbols)
		    {
			IRIToSymbols map = (IRIToSymbols)topLevelOption;
			
			String iri = map.getIRI();

			// Functors that have been already assigned to this IRI:
			Set<Converter.TPTPSymbolDescriptor> existingDescriptors = 
			    parameters.IRIsToTPTPSymbols().get(iri);
			
			if (existingDescriptors == null)
			    {
				existingDescriptors = 
				    new HashSet<Converter.TPTPSymbolDescriptor>();
				parameters.IRIsToTPTPSymbols().put(iri,
								   existingDescriptors);
			    };


			for (Object f : map.getFunctor())
			    {
				Converter.TPTPSymbolDescriptor desc;

				if (f instanceof SlottedFunctor)
				    {
					desc = 
					    new Converter.TPTPSlottedSymbolDescriptor(((SlottedFunctor)f).isPredicate(),
										      ((SlottedFunctor)f).getName(),
										      ((SlottedFunctor)f).getSlot());

				    }
				else if (f instanceof PositionalFunctor) 
				    {
					desc = 
					    new Converter.TPTPPositionalSymbolDescriptor(((PositionalFunctor)f).isPredicate(),
											 ((PositionalFunctor)f).getName(),
											 ((PositionalFunctor)f).getArity().intValue());
				    }
				else if (f instanceof Constant)
				    {
					desc = 
					    new Converter.TPTPConstantSymbolDescriptor(((Constant)f).getName());
				    }
				else
				    throw new Error("Bad kind of object in rif.bld.totptp.parameters.jaxb.IRIToSymbols.getFunctor() .");


				// Check if this descriptor conflicts with the previous mapping:

				for (Converter.TPTPSymbolDescriptor d : existingDescriptors)
				    {
					if (d.isPredicate() == desc.isPredicate() &&
					    d.isSlotted() == desc.isSlotted() &&
					    !d.name().equals(desc.name()) &&
					    d.arity() == desc.arity())
					    throw new java.lang.Exception("Error: IRI " + iri + 
									  " has conflicting mappings: " +
									  d + " and " + desc);
				    };
				
				existingDescriptors.add(desc);

			    }; // for (Object f : map.getFunctor())
		    }
		else if (topLevelOption instanceof OtherReservedSymbols) 
		    {
			OtherReservedSymbols set = (OtherReservedSymbols)topLevelOption;
			for (Object f : set.getFunctor())
			    {
				Converter.TPTPSymbolDescriptor desc;

				if (f instanceof SlottedFunctor)
				    {
					desc = 
					    new Converter.TPTPSlottedSymbolDescriptor(((SlottedFunctor)f).isPredicate(),
											 ((SlottedFunctor)f).getName(),
											 ((SlottedFunctor)f).getSlot());

				    }
				else if (f instanceof PositionalFunctor) 
				    {
					desc = 
					    new Converter.TPTPPositionalSymbolDescriptor(((PositionalFunctor)f).isPredicate(),
											 ((PositionalFunctor)f).getName(),
											 ((PositionalFunctor)f).getArity().intValue());
				    }
				else if (f instanceof Constant)
				    {
					desc = 
					    new Converter.TPTPConstantSymbolDescriptor(((Constant)f).getName());
				    }
				else
				    throw new Error("Bad kind of object in rif.bld.totptp.parameters.jaxb.IRIToSymbols.getFunctor() .");

				parameters.otherReservedSymbols().add(desc);

			    }; // for (Object f : map.getFunctor())

		    }
		else if (topLevelOption instanceof RIFBLDToTPTPParameters.DataDomainPredicate) 
		    {
			if (parameters.dataDomainPredicate() == null)
			    {
				parameters.
				    setDataDomainPredicate(((RIFBLDToTPTPParameters.DataDomainPredicate)
							    topLevelOption).
							   getName());
			    }
			else
			    {
				// Check if this is consistent with the previous value:
				if (!((RIFBLDToTPTPParameters.DataDomainPredicate)
				      topLevelOption).
				    getName().equals(parameters.dataDomainPredicate()))
				    throw new java.lang.Exception("Error: data domain predicate id " + 
								  ((RIFBLDToTPTPParameters.DataDomainPredicate)
								   topLevelOption).
								  getName() +
								  " conflicts with the previous value " +
								  parameters.dataDomainPredicate());
			    };
		    }
		else if (topLevelOption instanceof RIFBLDToTPTPParameters.EqualityPredicate) 
		    {
			if (parameters.equalityPredicate() == null)
			    {
				parameters.
				    setEqualityPredicate(((RIFBLDToTPTPParameters.EqualityPredicate)
							    topLevelOption).
							   getName());
			    }
			else
			    {
				// Check if this is consistent with the previous value:
				if (!((RIFBLDToTPTPParameters.EqualityPredicate)
				      topLevelOption).
				    getName().equals(parameters.equalityPredicate()))
				    throw new java.lang.Exception("Error: equality predicate id " + 
								  ((RIFBLDToTPTPParameters.EqualityPredicate)
								   topLevelOption).
								  getName() +
								  " conflicts with the previous value " +
								  parameters.equalityPredicate());
			    };
		    }
		else if (topLevelOption instanceof RIFBLDToTPTPParameters.MemberPredicate) 
		    {
			if (parameters.memberPredicate() == null)
			    {
				parameters.
				    setMemberPredicate(((RIFBLDToTPTPParameters.MemberPredicate)
							    topLevelOption).
							   getName());
			    }
			else
			    {
				// Check if this is consistent with the previous value:
				if (!((RIFBLDToTPTPParameters.MemberPredicate)
				      topLevelOption).
				    getName().equals(parameters.memberPredicate()))
				    throw new java.lang.Exception("Error: member predicate id " + 
								  ((RIFBLDToTPTPParameters.MemberPredicate)
								   topLevelOption).
								  getName() +
								  " conflicts with the previous value " +
								  parameters.memberPredicate());
			    };
		    }
		else if (topLevelOption instanceof RIFBLDToTPTPParameters.SubclassPredicate) 
		    {
			if (parameters.subclassPredicate() == null)
			    {
				parameters.
				    setSubclassPredicate(((RIFBLDToTPTPParameters.SubclassPredicate)
							    topLevelOption).
							   getName());
			    }
			else
			    {
				// Check if this is consistent with the previous value:
				if (!((RIFBLDToTPTPParameters.SubclassPredicate)
				      topLevelOption).
				    getName().equals(parameters.subclassPredicate()))
				    throw new java.lang.Exception("Error: subclass predicate id " + 
								  ((RIFBLDToTPTPParameters.SubclassPredicate)
								   topLevelOption).
								  getName() +
								  " conflicts with the previous value " +
								  parameters.subclassPredicate());
			    };
		    }
		else if (topLevelOption instanceof RIFBLDToTPTPParameters.MethodApplicationPredicate) 
		    {
			if (parameters.methodApplicationPredicate() == null)
			    {
				parameters.
				    setMethodApplicationPredicate(((RIFBLDToTPTPParameters.MethodApplicationPredicate)
							    topLevelOption).
							   getName());
			    }
			else
			    {
				// Check if this is consistent with the previous value:
				if (!((RIFBLDToTPTPParameters.MethodApplicationPredicate)
				      topLevelOption).
				    getName().equals(parameters.methodApplicationPredicate()))
				    throw new java.lang.Exception("Error: methodApplication predicate id " + 
								  ((RIFBLDToTPTPParameters.MethodApplicationPredicate)
								   topLevelOption).
								  getName() +
								  " conflicts with the previous value " +
								  parameters.methodApplicationPredicate());
			    };
		    }
		else if (topLevelOption instanceof RIFBLDToTPTPParameters.IntLitFunc) 
		    {
			if (parameters.intLitFunc() == null)
			    {
				parameters.
				    setIntLitFunc(((RIFBLDToTPTPParameters.IntLitFunc)
						   topLevelOption).
						  getName());
			    }
			else
			    {
				// Check if this is consistent with the previous value:
				if (!((RIFBLDToTPTPParameters.IntLitFunc)
				      topLevelOption).
				    getName().equals(parameters.intLitFunc()))
				    throw new java.lang.Exception("Error: integer-literal function " + 
								  ((RIFBLDToTPTPParameters.IntLitFunc)
								   topLevelOption).
								  getName() +
								  " conflicts with the previous value " +
								  parameters.intLitFunc());
			    };
		    }
		else if (topLevelOption instanceof RIFBLDToTPTPParameters.StringLitNoLangFunc) 
		    {
			if (parameters.stringLitNoLangFunc() == null)
			    {
				parameters.
				    setStringLitNoLangFunc(((RIFBLDToTPTPParameters.StringLitNoLangFunc)
							    topLevelOption).
							   getName());
			    }
			else
			    {
				// Check if this is consistent with the previous value:
				if (!((RIFBLDToTPTPParameters.StringLitNoLangFunc)
				      topLevelOption).
				    getName().equals(parameters.stringLitNoLangFunc()))
				    throw new java.lang.Exception("Error: string-literal-without-language function " + 
								  ((RIFBLDToTPTPParameters.StringLitNoLangFunc)
								   topLevelOption).
								  getName() +
								  " conflicts with the previous value " +
								  parameters.stringLitNoLangFunc());
			    };
		    }
		else if (topLevelOption instanceof RIFBLDToTPTPParameters.StringLitWithLangFunc) 
		    {
			if (parameters.stringLitWithLangFunc() == null)
			    {
				parameters.
				    setStringLitWithLangFunc(((RIFBLDToTPTPParameters.StringLitWithLangFunc)
							    topLevelOption).
							   getName());
			    }
			else
			    {
				// Check if this is consistent with the previous value:
				if (!((RIFBLDToTPTPParameters.StringLitWithLangFunc)
				      topLevelOption).
				    getName().equals(parameters.stringLitWithLangFunc()))
				    throw new java.lang.Exception("Error: string-literal-with-language function " + 
								  ((RIFBLDToTPTPParameters.StringLitWithLangFunc)
								   topLevelOption).
								  getName() +
								  " conflicts with the previous value " +
								  parameters.stringLitWithLangFunc());
			    };
		    }
		else if (topLevelOption instanceof RIFBLDToTPTPParameters.TypedLitFunc) 
		    {
			if (parameters.typedLitFunc() == null)
			    {
				parameters.
				    setTypedLitFunc(((RIFBLDToTPTPParameters.TypedLitFunc)
							    topLevelOption).
							   getName());
			    }
			else
			    {
				// Check if this is consistent with the previous value:
				if (!((RIFBLDToTPTPParameters.TypedLitFunc)
				      topLevelOption).
				    getName().equals(parameters.typedLitFunc()))
				    throw new java.lang.Exception("Error: typed-literal function " + 
								  ((RIFBLDToTPTPParameters.TypedLitFunc)
								   topLevelOption).
								  getName() +
								  " conflicts with the previous value " +
								  parameters.typedLitFunc());
			    };
		    }
		else if (topLevelOption instanceof RIFBLDToTPTPParameters.EmptyListConstant) 
		    {
			if (parameters.emptyListConstant() == null)
			    {
				parameters.
				    setEmptyListConstant(((RIFBLDToTPTPParameters.EmptyListConstant)
							    topLevelOption).
							   getName());
			    }
			else
			    {
				// Check if this is consistent with the previous value:
				if (!((RIFBLDToTPTPParameters.EmptyListConstant)
				      topLevelOption).
				    getName().equals(parameters.emptyListConstant()))
				    throw new java.lang.Exception("Error: typed-literal function " + 
								  ((RIFBLDToTPTPParameters.EmptyListConstant)
								   topLevelOption).
								  getName() +
								  " conflicts with the previous value " +
								  parameters.emptyListConstant());
			    };
		    }
		else if (topLevelOption instanceof RIFBLDToTPTPParameters.ListConsFunc) 
		    {
			if (parameters.listConsFunc() == null)
			    {
				parameters.
				    setListConsFunc(((RIFBLDToTPTPParameters.ListConsFunc)
							    topLevelOption).
							   getName());
			    }
			else
			    {
				// Check if this is consistent with the previous value:
				if (!((RIFBLDToTPTPParameters.ListConsFunc)
				      topLevelOption).
				    getName().equals(parameters.listConsFunc()))
				    throw new java.lang.Exception("Error: typed-literal function " + 
								  ((RIFBLDToTPTPParameters.ListConsFunc)
								   topLevelOption).
								  getName() +
								  " conflicts with the previous value " +
								  parameters.listConsFunc());
			    };
		    }
		else
		    {
			assert false;
			return;
		    };
	    }; // for (Object topLevelOption : param.getTopLevelOption())

    } // parse(File file,Converter.Parameters parameter)
	
    

    //                   Data:


    private Unmarshaller _unmarshaller;

} // class Parser