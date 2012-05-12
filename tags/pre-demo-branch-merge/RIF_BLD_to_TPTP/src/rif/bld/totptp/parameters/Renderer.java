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


/** Renders rif.bld.totptp.Converter parameters into XML. */
public class Renderer {


    /** Writes the XML representation of <code>parameters</code>
     *  into the stream.
     */
    public static void render(Converter.Parameters parameters,
			      OutputStream stream) 
	throws java.lang.Exception {
	
	RIFBLDToTPTPParameters jaxbRepresentation = convert(parameters);

	JAXBContext jc = 
	    JAXBContext.newInstance("rif.bld.totptp.parameters.jaxb");
	
	Marshaller marshaller = jc.createMarshaller();
	
	marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
			       Boolean.TRUE);
	
	marshaller.marshal(jaxbRepresentation,stream);


    } // render(Converter.Parameters parameters,..)


    
    
    private static RIFBLDToTPTPParameters convert(Converter.Parameters parameters) {

	RIFBLDToTPTPParameters result = new RIFBLDToTPTPParameters();
	

	for (Map.Entry<String,Set<Converter.TPTPSymbolDescriptor>> entry :
		 parameters.IRIsToTPTPSymbols().entrySet())
	    {
		IRIToSymbols IRISymbols = new IRIToSymbols();
	
		IRISymbols.setIRI(entry.getKey());
		
		for (Converter.TPTPSymbolDescriptor desc : entry.getValue())
		    IRISymbols.getFunctor().add(convert(desc));

		result.getTopLevelOption().add(IRISymbols);

	    }; // for (Map.Entry<String,Set<Converter.TPTPSymbolDescriptor>> entry :



	OtherReservedSymbols reserved = new OtherReservedSymbols();
	    
	for (Converter.TPTPSymbolDescriptor desc : 
		 parameters.otherReservedSymbols())
	    reserved.getFunctor().add(convert(desc));
	    
	if (!reserved.getFunctor().isEmpty())
	    result.getTopLevelOption().add(reserved);



	if (parameters.dataDomainPredicate() != null)
	    {
		RIFBLDToTPTPParameters.DataDomainPredicate dataDomainPred = 
		    new RIFBLDToTPTPParameters.DataDomainPredicate();
	
		dataDomainPred.setName(parameters.dataDomainPredicate());

		result.getTopLevelOption().add(dataDomainPred);
	    };
	

	if (parameters.equalityPredicate() != null)
	    {
		RIFBLDToTPTPParameters.EqualityPredicate equalityPred = 
		    new RIFBLDToTPTPParameters.EqualityPredicate();
	
		equalityPred.setName(parameters.equalityPredicate());

		result.getTopLevelOption().add(equalityPred);
	    };
	
	if (parameters.memberPredicate() != null)
	    {
		RIFBLDToTPTPParameters.MemberPredicate memberPred = 
		    new RIFBLDToTPTPParameters.MemberPredicate();
	
		memberPred.setName(parameters.memberPredicate());

		result.getTopLevelOption().add(memberPred);
	    };
	
	if (parameters.subclassPredicate() != null)
	    {
		RIFBLDToTPTPParameters.SubclassPredicate subclassPred = 
		    new RIFBLDToTPTPParameters.SubclassPredicate();
	
		subclassPred.setName(parameters.subclassPredicate());

		result.getTopLevelOption().add(subclassPred);
	    };
	
	if (parameters.methodApplicationPredicate() != null)
	    {
		RIFBLDToTPTPParameters.MethodApplicationPredicate methodApplicationPred = 
		    new RIFBLDToTPTPParameters.MethodApplicationPredicate();
	
		methodApplicationPred.setName(parameters.methodApplicationPredicate());

		result.getTopLevelOption().add(methodApplicationPred);
	    };
	
	if (parameters.intLitFunc() != null)
	    {
		RIFBLDToTPTPParameters.IntLitFunc func = 
		    new RIFBLDToTPTPParameters.IntLitFunc();
	
		func.setName(parameters.intLitFunc());

		result.getTopLevelOption().add(func);
	    };

	if (parameters.stringLitNoLangFunc() != null)
	    {
		RIFBLDToTPTPParameters.StringLitNoLangFunc func = 
		    new RIFBLDToTPTPParameters.StringLitNoLangFunc();
	
		func.setName(parameters.stringLitNoLangFunc());

		result.getTopLevelOption().add(func);
	    };
	
	if (parameters.stringLitWithLangFunc() != null)
	    {
		RIFBLDToTPTPParameters.StringLitWithLangFunc func = 
		    new RIFBLDToTPTPParameters.StringLitWithLangFunc();
	
		func.setName(parameters.stringLitWithLangFunc());

		result.getTopLevelOption().add(func);
	    };
	
	if (parameters.typedLitFunc() != null)
	    {
		RIFBLDToTPTPParameters.TypedLitFunc func = 
		    new RIFBLDToTPTPParameters.TypedLitFunc();
	
		func.setName(parameters.typedLitFunc());

		result.getTopLevelOption().add(func);
	    };
	
	if (parameters.emptyListConstant() != null)
	    {
		RIFBLDToTPTPParameters.EmptyListConstant func = 
		    new RIFBLDToTPTPParameters.EmptyListConstant();
	
		func.setName(parameters.emptyListConstant());

		result.getTopLevelOption().add(func);
	    };
	
	if (parameters.listConsFunc() != null)
	    {
		RIFBLDToTPTPParameters.ListConsFunc func = 
		    new RIFBLDToTPTPParameters.ListConsFunc();
	
		func.setName(parameters.listConsFunc());

		result.getTopLevelOption().add(func);
	    };
	

	return result;

    } // convert(Converter.Parameters parameters)


    private static Object convert(Converter.TPTPSymbolDescriptor desc) {

	if (desc instanceof Converter.TPTPConstantSymbolDescriptor)
	    {
		Constant result = new Constant();
		result.setName(((Converter.TPTPConstantSymbolDescriptor)desc).name());
		return result;
	    }
	else if (desc instanceof Converter.TPTPPositionalSymbolDescriptor)
	    {
		PositionalFunctor result = new PositionalFunctor();
		result.setName(((Converter.TPTPPositionalSymbolDescriptor)desc).name());
		result.setPredicate(((Converter.TPTPPositionalSymbolDescriptor)desc).isPredicate());
		result.setArity(BigInteger.valueOf(((Converter.TPTPPositionalSymbolDescriptor)desc).arity()));
		return result;
	    }
	else if (desc instanceof Converter.TPTPSlottedSymbolDescriptor)
	    {
		SlottedFunctor result = new SlottedFunctor();
		result.setName(((Converter.TPTPSlottedSymbolDescriptor)desc).name());
		result.setPredicate(((Converter.TPTPSlottedSymbolDescriptor)desc).isPredicate());
		for (String slot : 
			 ((Converter.TPTPSlottedSymbolDescriptor)desc).slots())
		    result.getSlot().add(slot);

		return result;
	    }
	else 
	    throw new Error("Bad kind of rif.bld.totptp.Converter.TPTPSymbolDescriptor .");

    } // convert(Converter.TPTPSymbolDescriptor desc)



} // class Renderer 