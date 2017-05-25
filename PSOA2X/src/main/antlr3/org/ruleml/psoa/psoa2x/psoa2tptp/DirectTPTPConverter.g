/**
 * This grammar file is used to generate the converter from a normalized PSOA input to TPTP
*/

tree grammar DirectTPTPConverter;

options 
{
	ASTLabelType = CommonTree;
	tokenVocab = PSOAPS;
	k = 1;
    superClass = PrologTermLangConverter;
}

@header
{
	package org.ruleml.psoa.psoa2x.psoa2tptp;

	import org.ruleml.psoa.psoa2x.common.*;
    import java.io.*;
    import java.util.Set;
    import java.util.HashSet;
    import java.util.Map;
    import java.util.LinkedHashMap;
    
    import org.ruleml.psoa.psoa2x.psoa2prolog.PrologTermLangConverter;
}

@members
{
    private enum TermType { CONST, VAR, FUNC_INTERNAL, FUNC_EXTERNAL };
    private PSOA2TPTPConfig m_config;
       
    public DirectTPTPConverter(TreeNodeStream input, PSOA2TPTPConfig config) {
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
@init
{
    int numClauses = 0;
}
    :   ^(GROUP 
           ({ append("fof(ax", String.valueOf(++numClauses), ",axiom,");  }
            group_element
            { append(")."); flushln(); })*)
    ;

group_element
    :   rule
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
    :   { print("fof(query,theorem,"); }   
    formula
    {
        if ($varMap.isEmpty())
        {
           print("(");
           flush();
           println(") => ans ).");
        }
        else
        {
            int i = 0;
	        for (String newVar : $varMap.keySet())
	        {
	           print(i++ == 0? "![" : ",");
	           print(newVar);
	        }
        	print("]: (");
        	flush();
        	print(" => ans");
        	
        	i = 0;
	        for (Map.Entry<String,String> varPair : $varMap.entrySet())
	        {
	            print(i++ == 0? "(\"?" : ", \"?");
	            print(varPair.getValue(), "= \", ", varPair.getKey());
	        }
	        print("))).");
        }
    }
    ;

rule
    :   ^(FORALL { append("!["); } 
          (VAR_ID { convertVar($VAR_ID.text); append(","); } )+ { trimEnd(1); append("]: ("); }
           clause { append(")"); } )
    |   clause
    ;

clause
    :   ^(IMPLICATION head { append(" <= "); } formula)
    |   head
    ;

head
@init
{ 
   int numSubformulas = 0;
}
    :   atomic
    |   ^(AND ({ append(numSubformulas++ == 0? "(" : " & "); } head)+)
        {
            append(numSubformulas > 0? ")" : "\$true");  // And() is translated to true    
        }
    |   ^(EXISTS { append("(?["); }
            (VAR_ID { convertVar($VAR_ID.text); append(","); } )+
            { trimEnd(1); append("]: "); }
            head { append(")"); } )
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
             ({ append(numSubformulas++ == 0? "(" : " & "); } formula )*
         )
         {
             append(numSubformulas > 0? ")" : "\$true");  // And() is translated to true    
         }
    |   ^(OR
            ({ append(numSubformulas++ == 0? "(" : " | "); } formula )*
         )
         {
             append(numSubformulas > 0? ")" : "\$false");  // Or() is translated to false
         }
    |    FALSITY { append("\$false"); }
    |   ^(EXISTS { append("(?["); }
            (VAR_ID 
             {
                convertVar($VAR_ID.text);
                if (isQuery)
					existVars.add($VAR_ID.text);
             })+
            { append("]: "); }
            formula { append(")"); } )
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
    :   ^(EQUAL   { append("("); }
          t1=term { append(" = "); }
          t2=term { append(")"); })
    ;
    
term returns [boolean isTop]
    :   constant { $isTop = $constant.isTop; }
    |   VAR_ID
    {
      String varName = $VAR_ID.text, newVarName = "Q".concat(varName);
      append(newVarName);
      // Keep record of free query variables and their translation output
      if ($query.size() > 0 && !$query::existVars.contains(varName))
      {
         $query::freeVarMap.put(newVarName, varName);
      }
      $isTop = false;
    }
    |   psoa { $isTop = false; }
    |   external
    ;
    
external
	:   ^(EXTERNAL psoa)
    { throw new TranslatorException("External is not supported by PSOA2TPTP"); }
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
			         replace(startIdx, "\$true");
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
    :   IRI     { convertIRIConst($IRI.text); }
    |   LITERAL { convertStringConst($LITERAL.text); }
    |   NUMBER  { append($NUMBER.text); }
    |   LOCAL   { convertLocalConst($LOCAL.text); }
    ;