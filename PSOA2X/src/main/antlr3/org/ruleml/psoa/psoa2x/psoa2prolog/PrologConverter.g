/**
 * This grammar file is used to generate the converter from a normalized PSOA input to Prolog
*/

tree grammar PrologConverter;

options 
{
	ASTLabelType = CommonTree;
	tokenVocab = PSOAPS;
	k = 1;
	superClass = AbstractPrologConverter;
}

@header
{
	package org.ruleml.psoa.psoa2x.psoa2prolog;

	import org.ruleml.psoa.psoa2x.common.*;
    import java.io.*;
    import java.util.Set;
    import java.util.HashSet;
    import java.util.Map;
    import java.util.LinkedHashMap;
}

@members
{
    private enum TermType { CONST, VAR, FUNC_INTERNAL, FUNC_EXTERNAL };
    private PSOA2PrologConfig m_config;
    
       
    public PrologConverter(TreeNodeStream input, PSOA2PrologConfig config) {
        this(input);
        m_config = config;
    }
}

document
    :   ^(DOCUMENT base? prefix* importDecl* group?)
    ;

base
    :   ^(BASE IRI_REF)
    ;

prefix
    :   ^(PREFIX NAMESPACE IRI_REF)
    ;

importDecl
    :   ^(IMPORT IRI_REF IRI_REF?)
    ;

group
    :   ^(GROUP group_element*)
    ;

group_element
    :   rule
    // output translated clause
    {
       append(".");
       flushln();
    }
    |   group
    ;

query returns [Map<String, String> varMap]
scope
{
  Map<String, String> freeVarMap;
  Set<String> existVars;
}
@init
{
   $varMap = ($query::freeVarMap = new LinkedHashMap<String, String>());
   $query::existVars = new HashSet<String>();
}
    :   formula
    // output translated query
    {
       append(".");
       flush();
    }
    ;

rule
    :   ^(FORALL VAR_ID+ clause)
    |   clause
    ;

clause
    :   ^(IMPLICATION head { append(" :- "); } formula)
    |   head
    ;

head
    :   atomic   // head can only be atomic in the LP-normalized PSOA input
    ;

formula
@init
{ 
   int numSubformulas = 0;
   boolean isQuery = $query.size() > 0;
   Set<String> existVars;
   
   if (isQuery)
     existVars = $query::existVars;
   else
     existVars = null;
}
    :   ^(AND
             ({ append(numSubformulas++ == 0? "(" : ", "); } formula )*
         )
         {
             append(numSubformulas > 0? ")" : "true");  // And() is translated to true    
         }
    |   ^(OR
            ({ append(numSubformulas++ == 0? "(" : "; "); } formula )*
         )
         {
            append(numSubformulas > 0? ")" : "false");  // Or() is translated to false
         }
    |    FALSITY { append("false"); }
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
    ;

atom
    :   psoa
    ;

equal
@init
{
	BufferIndex startIdx = getBufferIndex();
}
@after
{
    startIdx.dispose();
}
    :   ^(EQUAL   
          {
             // The two spaces are the placeholder of the Prolog primitive 
             append("  (");  
          } 
          t1=term { append(","); }
          t2=term { append(")"); })
    {
        // Insert Prolog primitive "is" or '=' based on the type of 
        // the right hand side term    
        replace(startIdx, 2, $t2.type == TermType.FUNC_EXTERNAL? "is" : "\'=\'");
    }
    ;
    
term returns [TermType type, boolean isTop]
    :   constant { $type = TermType.CONST; $isTop = $constant.isTop; }
    |   VAR_ID
    {
      String varName = $VAR_ID.text, newVarName = "Q".concat(varName);
      append(newVarName);
      // Keep record of free query variables and their translation output
      if ($query.size() > 0 && !$query::existVars.contains(varName))
      {
        $query::freeVarMap.put(newVarName, varName);
      }
      $type = TermType.VAR;
      $isTop = false;
    }
    |   psoa { $type = TermType.FUNC_INTERNAL; $isTop = false; }
    |   external { $type = TermType.FUNC_EXTERNAL; $isTop = false; }
    ;

external
    :   ^(EXTERNAL psoa)
    ;
    
psoa
@init
{
   BufferIndex startIdx = null;
   boolean isOidful = false;
   
}
@after
{
   if (startIdx != null)
   {
      startIdx.dispose();
   }
}
    :  ^(PSOA
           (
                  (
                     // Oidful psoa terms
                     {
                        isOidful = true;
                        startIdx = getBufferIndex();
                        // The seven spaces are the placeholder of the reserved Prolog predicate
                        append("       (");
                     }
                     oid=term { append(","); }  // OID
                     ^(INSTANCE pred=term)     // predicate/function
                     {
						if (!$pred.isTop)
						{
							append(",");
						}
					 }
                  )
              |  // Oidless psoa terms
                 ^(INSTANCE t=term) { append("("); }
           )
           (
              tuple  
              {
                 if (isOidful)
                 {
                    if ($tuple.isDependent)
    	               replace(startIdx, 7, "prdtupterm");
                    else
                       replace(startIdx, 7, "tupterm");
                 }
                 append(")");
              }
           |  slot
              {
                 if (isOidful)
                 {
                    if ($slot.isDependent)
    	               replace(startIdx, 7, "prdsloterm");
                    else
                       replace(startIdx, 7, "sloterm");
                 }
                 append(")");
              }
           |  // No slot or tuples  
		      {
			      if ($pred.isTop)
			      {
				     // Tautology, o#Top
			         replace(startIdx, "true");
		          }
			      else
			      {
			         if (isOidful)
			         {
		               // Class membership
		               trimEnd(1);
		               append(")");
		               replace(startIdx, 7, "memterm");
		             }
		             else
		             {
		               // Nullary predicate/function
		               append(")");	
		             }
		          }
			  }
           )
        )
    ;

tuple returns [boolean isDependent]
    :   ^(TUPLE
          DEPSIGN  { $isDependent = $DEPSIGN.text.equals("+"); }
          (term { append(","); })+)
    {
       trimEnd(1);
    }
    ;
    
slot returns [boolean isDependent]
    :   ^(SLOT
          DEPSIGN  { $isDependent = $DEPSIGN.text.equals("+"); }
          term { append(","); }
          term)
    ;

constant returns [boolean isTop]
    :   ^(LITERAL IRI) 
        { convertGeneralConst($LITERAL.text, $IRI.text); $isTop = false; }
    |   ^(SHORTCONST constshort) { $isTop = false; }
    |   TOP { $isTop = true; }
    ;

constshort
    :
        IRI     { convertIRIConst($IRI.text); }
    |   LITERAL { convertStringConst($LITERAL.text); }
    |   NUMBER  { append($NUMBER.text); }
    |   LOCAL   { convertLocalConst($LOCAL.text); }
    ;