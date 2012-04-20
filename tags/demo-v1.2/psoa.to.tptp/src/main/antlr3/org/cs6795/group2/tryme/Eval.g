tree grammar Eval;

options {
    tokenVocab=Expr;
    ASTLabelType=CommonTree;
}

// START:members
@header {
package org.cs6795.group2.tryme;
import java.util.HashMap;
}

@members {
/** Map variable name to Integer object holding value */
HashMap memory = new HashMap();
}
// END:members

// START:stat
prog:   stat+ ;

stat:   expr
        {System.out.println($expr.value);}
    |   ^('=' ID expr)
        {memory.put($ID.text, new Integer($expr.value));}
    ;
// END:stat

// START:expr
expr returns [int value]
    :   ^('+' a=expr b=expr) {$value = a+b;}
    |   ^('-' a=expr b=expr) {$value = a-b;}   
    |   ^('*' a=expr b=expr) {$value = a*b;}
    |   ID 
        {
        Integer v = (Integer)memory.get($ID.text);
        if ( v!=null ) $value = v.intValue();
        else System.err.println("undefined variable "+$ID.text);
        }
    |   INT                  {$value = Integer.parseInt($INT.text);}
    ;
// END:expr