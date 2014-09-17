tree grammar PSOA2PrologWalker;

options 
{
	ASTLabelType = CommonTree;
	tokenVocab = PSOAPS;
	k = 1;
	superClass = TranslatorWalker;
}

@header
{
	package org.ruleml.psoa.psoa2x.psoa2prolog;

	import static org.ruleml.psoa.psoa2x.common.PSOATranslatorUtil.*;
	import org.ruleml.psoa.psoa2x.common.*;
	import org.ruleml.psoa.psoa2x.common.ANTLRBasedTranslator.TranslatorWalker;
  import java.io.*;
  import java.util.Set;
  import java.util.HashSet;
  import java.util.Map;
  import java.util.LinkedHashMap;
}

@members
{
   private StringBuilder _output = new StringBuilder(256);
    
   private enum TermType { CONST, VAR, FUNC_INTERNAL, FUNC_EXTERNAL };
}

document
    :   ^(DOCUMENT base? prefix* importDecl* group?)
    ;

base
    :   ^(BASE IRI_REF)
    ;

prefix
    :   ^(PREFIX ID IRI_REF)
    ;

importDecl
    :   ^(IMPORT IRI_REF IRI_REF?)
    ;

group
    :   ^(GROUP group_element*)
    ;

group_element
    :   rule 
    { println(_output.toString(), "."); _output.setLength(0); }
    |   group
    ;

query returns [Map<String, String> varMap]
scope
{
  Map<String, String> freeVarMap;
}
@init
{
   $varMap = ($query::freeVarMap = new LinkedHashMap<String, String>());
}
    :   formula
    {
        print(_output.toString());
        _output.setLength(0);
    }
    ;

rule
    :   ^(FORALL VAR_ID+ clause)
    |   clause
    ;

clause
    :   ^(IMPLICATION head { _output.append(" :- "); } formula)
    |   atomic
    ;

head
    :   atomic
    |   ^(EXISTS VAR_ID+ atomic)
		    {
//		        throw new TranslatorException("Existentials must be removed before translating to Prolog");
		    }
    ;

formula
scope
{
  Set<String> existVars;
}
@init
{ 
   boolean hasConjunct = false, isQuery = $query.size() > 0;
   Set<String> existVars;
   
   if (isQuery)
     existVars = ($formula::existVars = new HashSet<String>());
   else
     existVars = null;
}
    :   ^(AND { _output.append("("); }
            (formula { _output.append(","); hasConjunct = true; } )*
         )
         {
            int len = _output.length();
            if (!hasConjunct)
              _output.replace(len - 1, len, "true");
            else
              _output.replace(len - 1, len, ")");
         }
    |   ^(OR { _output.append("("); }
            (formula { _output.append(";"); hasConjunct = true; } )*
         )
         {
            int len = _output.length();
            if (!hasConjunct)
              _output.replace(len - 1, len, "false");
            else
              _output.replace(len - 1, len, ")");
         }
    |   ^(EXISTS 
            (VAR_ID { if (isQuery) existVars.add($VAR_ID.text); })+
            formula)
    {
      if (isQuery)
        existVars.clear();
    }
    |   atomic
    |   external
    ;

atomic
    :   atom
    |   equal
    |   subclass
    ;

atom
    :   psoa
    ;

equal
    :   ^(EQUAL t1=term t2=term)
    {
        if ($t1.type == TermType.VAR && 
            $t2.type == TermType.FUNC_EXTERNAL)
        {
            append(_output, "is(", $t1.output, ",", $t2.output, ")");
        }
    }
    ;

subclass
    :   ^(SUBCLASS sub=term sup=term)
    {
       append(_output, "member(X,", $sup.output, ") :- member(X,", $sub.output, ")");
    }
    ;
    
term returns [String output, TermType type]
scope
{
  StringBuilder outputBuilder;
}
@init
{
  StringBuilder outputBuilder = ($term::outputBuilder = new StringBuilder(200));
}
@after
{
  $output = outputBuilder.toString();
}
    :   constant { $type = TermType.CONST; }
    |   VAR_ID
    {
      String varName = $VAR_ID.text, newVarName = "Q".concat(varName);
      append(outputBuilder, newVarName);
      if ($query.size() > 0 && !$formula::existVars.contains(varName))
      {
        $query::freeVarMap.put(newVarName, varName);
      }
      $type = TermType.VAR;
    }
    |   psoa { $type = TermType.FUNC_INTERNAL; }
    |   external { $type = TermType.FUNC_EXTERNAL; }
    ;

external
    :   ^(EXTERNAL psoa)
    ;
    
psoa
scope
{
  String oid;
  String func;
}
@init
{
  boolean hasTupleOrSlot = false;
}
    :   ^(PSOA (o=term { $psoa::oid = $o.output; })?
           ^(INSTANCE type=term
            {
              if ($psoa::oid == null)
              {
                $psoa::func = $type.output;
              }
            })
           ({ hasTupleOrSlot = true; } tuple)*
           ({ hasTupleOrSlot = true; } slot)*
         )
    {
        if (!hasTupleOrSlot)
        {
          // Class membership
	        if (!$type.output.equals("TOP"))
	          append(_output, "member(", $psoa::oid, ",", $type.output, ")");
	        else
	          append(_output, "true");
	      }
    }
    ;

tuple
@init
{
  String oid = $psoa::oid;
  StringBuilder b;
  if (oid == null)
  {
    if ($term.size() > 0)
      // Function application 
      b = $term::outputBuilder;
    else
      // External predicate application
      b = _output;
    append(b, $psoa::func, "(");
  }
  else
  {
    b = _output;
    append(b, "tupterm(", oid, ",");
  }
}
    :   ^(TUPLE (term { append(b, $term.output, ","); })+)
    {
        int len = b.length();
        b.replace(len - 1, len, ")");
    }
    ;
    
slot
    :   ^(SLOT s=term v=term)
    {
        String oid;
        if ((oid = $psoa::oid) != null)
          append(_output, "sloterm(", oid, ",", $s.output, ",", $v.output, ")");
    }
    ;

constant
    :   ^(LITERAL IRI) // { print("\'", $IRI.text, "\'"); }
    |   ^(SHORTCONST constshort)
    |   TOP
    ;

constshort
    :
        // TODO: Handle special datatypes, e.g. string, int, etc.
        IRI     { appendIRIConst($term::outputBuilder, $IRI.text); }
    |   LITERAL { append($term::outputBuilder, "\'\"", $LITERAL.text, "\"\'"); }
    |   NUMBER  { $term::outputBuilder.append($NUMBER.text); }
    |   LOCAL   { append($term::outputBuilder, "\'_", $LOCAL.text, "\'"); }
    ;