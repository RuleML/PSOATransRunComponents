// $ANTLR 3.4 org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g 2012-02-21 17:29:59

	package org.ruleml.api.presentation_syntax_parser;

	import static cs6795.group2.PSOATranslatorUtil.*;
    import java.io.*;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class BasicRuleMLPresentationASTGrammar extends TreeParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ALPHA", "AND", "BASE", "CURIE", "DIGIT", "DOCUMENT", "ECHAR", "EOL", "EQUAL", "EXISTS", "EXTERNAL", "FORALL", "GREATER", "GROUP", "ID", "ID_CHAR", "ID_START_CHAR", "IMPLICATION", "IMPORT", "INSTANCE", "IRI", "IRI_CHAR", "IRI_REF", "IRI_START_CHAR", "LESS", "LITERAL", "LOCAL", "LPAR", "LSQBR", "MULTI_LINE_COMMENT", "NUMBER", "OR", "PREFIX", "PSOA", "RPAR", "RSQBR", "SHORTCONST", "SLOT", "SLOT_ARROW", "STRING", "SUBCLASS", "SYMSPACE_OPER", "TUPLE", "VAR_ID", "VAR_LIST", "WHITESPACE", "'@'"
    };

    public static final int EOF=-1;
    public static final int T__50=50;
    public static final int ALPHA=4;
    public static final int AND=5;
    public static final int BASE=6;
    public static final int CURIE=7;
    public static final int DIGIT=8;
    public static final int DOCUMENT=9;
    public static final int ECHAR=10;
    public static final int EOL=11;
    public static final int EQUAL=12;
    public static final int EXISTS=13;
    public static final int EXTERNAL=14;
    public static final int FORALL=15;
    public static final int GREATER=16;
    public static final int GROUP=17;
    public static final int ID=18;
    public static final int ID_CHAR=19;
    public static final int ID_START_CHAR=20;
    public static final int IMPLICATION=21;
    public static final int IMPORT=22;
    public static final int INSTANCE=23;
    public static final int IRI=24;
    public static final int IRI_CHAR=25;
    public static final int IRI_REF=26;
    public static final int IRI_START_CHAR=27;
    public static final int LESS=28;
    public static final int LITERAL=29;
    public static final int LOCAL=30;
    public static final int LPAR=31;
    public static final int LSQBR=32;
    public static final int MULTI_LINE_COMMENT=33;
    public static final int NUMBER=34;
    public static final int OR=35;
    public static final int PREFIX=36;
    public static final int PSOA=37;
    public static final int RPAR=38;
    public static final int RSQBR=39;
    public static final int SHORTCONST=40;
    public static final int SLOT=41;
    public static final int SLOT_ARROW=42;
    public static final int STRING=43;
    public static final int SUBCLASS=44;
    public static final int SYMSPACE_OPER=45;
    public static final int TUPLE=46;
    public static final int VAR_ID=47;
    public static final int VAR_LIST=48;
    public static final int WHITESPACE=49;

    // delegates
    public TreeParser[] getDelegates() {
        return new TreeParser[] {};
    }

    // delegators


    public BasicRuleMLPresentationASTGrammar(TreeNodeStream input) {
        this(input, new RecognizerSharedState());
    }
    public BasicRuleMLPresentationASTGrammar(TreeNodeStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return BasicRuleMLPresentationASTGrammar.tokenNames; }
    public String getGrammarFileName() { return "org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g"; }


        private PrintStream m_outStream = System.out;
        
        public void setOutStream(OutputStream out)
        {
            m_outStream = new PrintStream(out);
        }
        
        private void writeln(StringBuilder b)
        {
            m_outStream.println(b);
        }
        
        private void writeln()
        {
            m_outStream.println();
        }



    // $ANTLR start "document"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:38:1: document : ^( DOCUMENT ( base )? ( prefix )* ( importDecl )* ( group )? ) ;
    public final void document() throws RecognitionException {
        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:39:5: ( ^( DOCUMENT ( base )? ( prefix )* ( importDecl )* ( group )? ) )
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:39:7: ^( DOCUMENT ( base )? ( prefix )* ( importDecl )* ( group )? )
            {
            match(input,DOCUMENT,FOLLOW_DOCUMENT_in_document59); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:39:18: ( base )?
                int alt1=2;
                switch ( input.LA(1) ) {
                    case BASE:
                        {
                        alt1=1;
                        }
                        break;
                }

                switch (alt1) {
                    case 1 :
                        // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:39:18: base
                        {
                        pushFollow(FOLLOW_base_in_document61);
                        base();

                        state._fsp--;


                        }
                        break;

                }


                // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:39:24: ( prefix )*
                loop2:
                do {
                    int alt2=2;
                    switch ( input.LA(1) ) {
                    case PREFIX:
                        {
                        alt2=1;
                        }
                        break;

                    }

                    switch (alt2) {
                	case 1 :
                	    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:39:24: prefix
                	    {
                	    pushFollow(FOLLOW_prefix_in_document64);
                	    prefix();

                	    state._fsp--;


                	    }
                	    break;

                	default :
                	    break loop2;
                    }
                } while (true);


                // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:39:32: ( importDecl )*
                loop3:
                do {
                    int alt3=2;
                    switch ( input.LA(1) ) {
                    case IMPORT:
                        {
                        alt3=1;
                        }
                        break;

                    }

                    switch (alt3) {
                	case 1 :
                	    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:39:32: importDecl
                	    {
                	    pushFollow(FOLLOW_importDecl_in_document67);
                	    importDecl();

                	    state._fsp--;


                	    }
                	    break;

                	default :
                	    break loop3;
                    }
                } while (true);


                // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:39:44: ( group )?
                int alt4=2;
                switch ( input.LA(1) ) {
                    case GROUP:
                        {
                        alt4=1;
                        }
                        break;
                }

                switch (alt4) {
                    case 1 :
                        // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:39:44: group
                        {
                        pushFollow(FOLLOW_group_in_document70);
                        group();

                        state._fsp--;


                        }
                        break;

                }


                match(input, Token.UP, null); 
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "document"



    // $ANTLR start "base"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:42:1: base : ^( BASE IRI_REF ) ;
    public final void base() throws RecognitionException {
        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:43:5: ( ^( BASE IRI_REF ) )
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:43:9: ^( BASE IRI_REF )
            {
            match(input,BASE,FOLLOW_BASE_in_base92); 

            match(input, Token.DOWN, null); 
            match(input,IRI_REF,FOLLOW_IRI_REF_in_base94); 

            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "base"



    // $ANTLR start "prefix"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:46:1: prefix : ^( PREFIX ID IRI_REF ) ;
    public final void prefix() throws RecognitionException {
        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:47:5: ( ^( PREFIX ID IRI_REF ) )
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:47:9: ^( PREFIX ID IRI_REF )
            {
            match(input,PREFIX,FOLLOW_PREFIX_in_prefix115); 

            match(input, Token.DOWN, null); 
            match(input,ID,FOLLOW_ID_in_prefix117); 

            match(input,IRI_REF,FOLLOW_IRI_REF_in_prefix119); 

            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "prefix"



    // $ANTLR start "importDecl"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:50:1: importDecl : ^( IMPORT IRI_REF ( IRI_REF )? ) ;
    public final void importDecl() throws RecognitionException {
        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:51:5: ( ^( IMPORT IRI_REF ( IRI_REF )? ) )
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:51:9: ^( IMPORT IRI_REF ( IRI_REF )? )
            {
            match(input,IMPORT,FOLLOW_IMPORT_in_importDecl140); 

            match(input, Token.DOWN, null); 
            match(input,IRI_REF,FOLLOW_IRI_REF_in_importDecl142); 

            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:51:26: ( IRI_REF )?
            int alt5=2;
            switch ( input.LA(1) ) {
                case IRI_REF:
                    {
                    alt5=1;
                    }
                    break;
            }

            switch (alt5) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:51:26: IRI_REF
                    {
                    match(input,IRI_REF,FOLLOW_IRI_REF_in_importDecl144); 

                    }
                    break;

            }


            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "importDecl"



    // $ANTLR start "group"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:54:1: group : ^( GROUP ( group_element )* ) ;
    public final void group() throws RecognitionException {
        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:55:5: ( ^( GROUP ( group_element )* ) )
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:55:9: ^( GROUP ( group_element )* )
            {
            match(input,GROUP,FOLLOW_GROUP_in_group166); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:55:17: ( group_element )*
                loop6:
                do {
                    int alt6=2;
                    switch ( input.LA(1) ) {
                    case EQUAL:
                    case FORALL:
                    case GROUP:
                    case IMPLICATION:
                    case PSOA:
                    case SUBCLASS:
                        {
                        alt6=1;
                        }
                        break;

                    }

                    switch (alt6) {
                	case 1 :
                	    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:55:17: group_element
                	    {
                	    pushFollow(FOLLOW_group_element_in_group168);
                	    group_element();

                	    state._fsp--;


                	    }
                	    break;

                	default :
                	    break loop6;
                    }
                } while (true);


                match(input, Token.UP, null); 
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "group"



    // $ANTLR start "group_element"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:58:1: group_element : (r= rule | group );
    public final void group_element() throws RecognitionException {
        StringBuilder r =null;


        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:59:5: (r= rule | group )
            int alt7=2;
            switch ( input.LA(1) ) {
            case EQUAL:
            case FORALL:
            case IMPLICATION:
            case PSOA:
            case SUBCLASS:
                {
                alt7=1;
                }
                break;
            case GROUP:
                {
                alt7=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;

            }

            switch (alt7) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:59:9: r= rule
                    {
                    pushFollow(FOLLOW_rule_in_group_element191);
                    r=rule();

                    state._fsp--;


                     writeln(fofFactSentence(r)); 

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:60:9: group
                    {
                    pushFollow(FOLLOW_group_in_group_element203);
                    group();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "group_element"



    // $ANTLR start "queries"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:63:1: queries : (r= rule )+ ;
    public final void queries() throws RecognitionException {
        StringBuilder r =null;


        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:64:5: ( (r= rule )+ )
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:64:9: (r= rule )+
            {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:64:9: (r= rule )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                switch ( input.LA(1) ) {
                case EQUAL:
                case FORALL:
                case IMPLICATION:
                case PSOA:
                case SUBCLASS:
                    {
                    alt8=1;
                    }
                    break;

                }

                switch (alt8) {
            	case 1 :
            	    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:64:10: r= rule
            	    {
            	    pushFollow(FOLLOW_rule_in_queries225);
            	    r=rule();

            	    state._fsp--;


            	     writeln(fofConjSentence(r)); 

            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "queries"



    // $ANTLR start "rule"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:67:1: rule returns [StringBuilder result] : ( ^( FORALL ^( VAR_LIST ( VAR_ID )+ ) clause ) |c= clause );
    public final StringBuilder rule() throws RecognitionException {
        StringBuilder result = null;


        StringBuilder c =null;


        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:68:5: ( ^( FORALL ^( VAR_LIST ( VAR_ID )+ ) clause ) |c= clause )
            int alt10=2;
            switch ( input.LA(1) ) {
            case FORALL:
                {
                alt10=1;
                }
                break;
            case EQUAL:
            case IMPLICATION:
            case PSOA:
            case SUBCLASS:
                {
                alt10=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }

            switch (alt10) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:68:9: ^( FORALL ^( VAR_LIST ( VAR_ID )+ ) clause )
                    {
                    match(input,FORALL,FOLLOW_FORALL_in_rule254); 

                    match(input, Token.DOWN, null); 
                    match(input,VAR_LIST,FOLLOW_VAR_LIST_in_rule257); 

                    match(input, Token.DOWN, null); 
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:68:29: ( VAR_ID )+
                    int cnt9=0;
                    loop9:
                    do {
                        int alt9=2;
                        switch ( input.LA(1) ) {
                        case VAR_ID:
                            {
                            alt9=1;
                            }
                            break;

                        }

                        switch (alt9) {
                    	case 1 :
                    	    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:68:29: VAR_ID
                    	    {
                    	    match(input,VAR_ID,FOLLOW_VAR_ID_in_rule259); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt9 >= 1 ) break loop9;
                                EarlyExitException eee =
                                    new EarlyExitException(9, input);
                                throw eee;
                        }
                        cnt9++;
                    } while (true);


                    match(input, Token.UP, null); 


                    pushFollow(FOLLOW_clause_in_rule263);
                    clause();

                    state._fsp--;


                    match(input, Token.UP, null); 


                      

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:69:6: c= clause
                    {
                    pushFollow(FOLLOW_clause_in_rule275);
                    c=clause();

                    state._fsp--;


                     result = c; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "rule"



    // $ANTLR start "clause"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:72:1: clause returns [StringBuilder result] : ( ^( IMPLICATION h= head f= formula ) |atomicFormula= atomic );
    public final StringBuilder clause() throws RecognitionException {
        StringBuilder result = null;


        StringBuilder h =null;

        StringBuilder f =null;

        StringBuilder atomicFormula =null;


        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:73:5: ( ^( IMPLICATION h= head f= formula ) |atomicFormula= atomic )
            int alt11=2;
            switch ( input.LA(1) ) {
            case IMPLICATION:
                {
                alt11=1;
                }
                break;
            case EQUAL:
            case PSOA:
            case SUBCLASS:
                {
                alt11=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }

            switch (alt11) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:73:9: ^( IMPLICATION h= head f= formula )
                    {
                    match(input,IMPLICATION,FOLLOW_IMPLICATION_in_clause301); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_head_in_clause305);
                    h=head();

                    state._fsp--;


                    pushFollow(FOLLOW_formula_in_clause309);
                    f=formula();

                    state._fsp--;


                    match(input, Token.UP, null); 


                     ruleStr(h, f); 

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:74:9: atomicFormula= atomic
                    {
                    pushFollow(FOLLOW_atomic_in_clause326);
                    atomicFormula=atomic();

                    state._fsp--;


                     result = atomicFormula; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "clause"



    // $ANTLR start "head"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:77:1: head returns [StringBuilder result] : (atomicHead= atomic | ^( EXISTS ^( VAR_LIST ( VAR_ID )+ ) f= atomic ) );
    public final StringBuilder head() throws RecognitionException {
        StringBuilder result = null;


        CommonTree VAR_ID1=null;
        StringBuilder atomicHead =null;

        StringBuilder f =null;



            StringBuilder b = builder();

        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:81:5: (atomicHead= atomic | ^( EXISTS ^( VAR_LIST ( VAR_ID )+ ) f= atomic ) )
            int alt13=2;
            switch ( input.LA(1) ) {
            case EQUAL:
            case PSOA:
            case SUBCLASS:
                {
                alt13=1;
                }
                break;
            case EXISTS:
                {
                alt13=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;

            }

            switch (alt13) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:81:9: atomicHead= atomic
                    {
                    pushFollow(FOLLOW_atomic_in_head362);
                    atomicHead=atomic();

                    state._fsp--;


                     result = atomicHead; 

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:82:9: ^( EXISTS ^( VAR_LIST ( VAR_ID )+ ) f= atomic )
                    {
                    match(input,EXISTS,FOLLOW_EXISTS_in_head375); 

                    match(input, Token.DOWN, null); 
                    match(input,VAR_LIST,FOLLOW_VAR_LIST_in_head388); 

                    match(input, Token.DOWN, null); 
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:83:22: ( VAR_ID )+
                    int cnt12=0;
                    loop12:
                    do {
                        int alt12=2;
                        switch ( input.LA(1) ) {
                        case VAR_ID:
                            {
                            alt12=1;
                            }
                            break;

                        }

                        switch (alt12) {
                    	case 1 :
                    	    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:83:23: VAR_ID
                    	    {
                    	    VAR_ID1=(CommonTree)match(input,VAR_ID,FOLLOW_VAR_ID_in_head391); 

                    	     collectTerm(b, getVarName((VAR_ID1!=null?VAR_ID1.getText():null))); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt12 >= 1 ) break loop12;
                                EarlyExitException eee =
                                    new EarlyExitException(12, input);
                                throw eee;
                        }
                        cnt12++;
                    } while (true);


                    match(input, Token.UP, null); 


                    pushFollow(FOLLOW_atomic_in_head410);
                    f=atomic();

                    state._fsp--;


                    match(input, Token.UP, null); 



                    	        result = existStr(b, f);
                    	    

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "head"



    // $ANTLR start "formula"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:90:1: formula returns [StringBuilder result] : ( ^( AND ( formula )+ ) | ^( OR ( formula )+ ) | ^( EXISTS ^( VAR_LIST ( VAR_ID )+ ) formula ) |f= atomic | external );
    public final StringBuilder formula() throws RecognitionException {
        StringBuilder result = null;


        StringBuilder f =null;


        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:91:5: ( ^( AND ( formula )+ ) | ^( OR ( formula )+ ) | ^( EXISTS ^( VAR_LIST ( VAR_ID )+ ) formula ) |f= atomic | external )
            int alt17=5;
            switch ( input.LA(1) ) {
            case AND:
                {
                alt17=1;
                }
                break;
            case OR:
                {
                alt17=2;
                }
                break;
            case EXISTS:
                {
                alt17=3;
                }
                break;
            case EQUAL:
            case PSOA:
            case SUBCLASS:
                {
                alt17=4;
                }
                break;
            case EXTERNAL:
                {
                alt17=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;

            }

            switch (alt17) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:91:9: ^( AND ( formula )+ )
                    {
                    match(input,AND,FOLLOW_AND_in_formula442); 

                    match(input, Token.DOWN, null); 
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:91:15: ( formula )+
                    int cnt14=0;
                    loop14:
                    do {
                        int alt14=2;
                        switch ( input.LA(1) ) {
                        case AND:
                        case EQUAL:
                        case EXISTS:
                        case EXTERNAL:
                        case OR:
                        case PSOA:
                        case SUBCLASS:
                            {
                            alt14=1;
                            }
                            break;

                        }

                        switch (alt14) {
                    	case 1 :
                    	    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:91:15: formula
                    	    {
                    	    pushFollow(FOLLOW_formula_in_formula444);
                    	    formula();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt14 >= 1 ) break loop14;
                                EarlyExitException eee =
                                    new EarlyExitException(14, input);
                                throw eee;
                        }
                        cnt14++;
                    } while (true);


                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:92:9: ^( OR ( formula )+ )
                    {
                    match(input,OR,FOLLOW_OR_in_formula457); 

                    match(input, Token.DOWN, null); 
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:92:14: ( formula )+
                    int cnt15=0;
                    loop15:
                    do {
                        int alt15=2;
                        switch ( input.LA(1) ) {
                        case AND:
                        case EQUAL:
                        case EXISTS:
                        case EXTERNAL:
                        case OR:
                        case PSOA:
                        case SUBCLASS:
                            {
                            alt15=1;
                            }
                            break;

                        }

                        switch (alt15) {
                    	case 1 :
                    	    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:92:14: formula
                    	    {
                    	    pushFollow(FOLLOW_formula_in_formula459);
                    	    formula();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt15 >= 1 ) break loop15;
                                EarlyExitException eee =
                                    new EarlyExitException(15, input);
                                throw eee;
                        }
                        cnt15++;
                    } while (true);


                    match(input, Token.UP, null); 


                    }
                    break;
                case 3 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:93:9: ^( EXISTS ^( VAR_LIST ( VAR_ID )+ ) formula )
                    {
                    match(input,EXISTS,FOLLOW_EXISTS_in_formula472); 

                    match(input, Token.DOWN, null); 
                    match(input,VAR_LIST,FOLLOW_VAR_LIST_in_formula475); 

                    match(input, Token.DOWN, null); 
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:93:29: ( VAR_ID )+
                    int cnt16=0;
                    loop16:
                    do {
                        int alt16=2;
                        switch ( input.LA(1) ) {
                        case VAR_ID:
                            {
                            alt16=1;
                            }
                            break;

                        }

                        switch (alt16) {
                    	case 1 :
                    	    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:93:29: VAR_ID
                    	    {
                    	    match(input,VAR_ID,FOLLOW_VAR_ID_in_formula477); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt16 >= 1 ) break loop16;
                                EarlyExitException eee =
                                    new EarlyExitException(16, input);
                                throw eee;
                        }
                        cnt16++;
                    } while (true);


                    match(input, Token.UP, null); 


                    pushFollow(FOLLOW_formula_in_formula481);
                    formula();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    }
                    break;
                case 4 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:94:9: f= atomic
                    {
                    pushFollow(FOLLOW_atomic_in_formula494);
                    f=atomic();

                    state._fsp--;


                     result = f; 

                    }
                    break;
                case 5 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:95:9: external
                    {
                    pushFollow(FOLLOW_external_in_formula506);
                    external();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "formula"



    // $ANTLR start "atomic"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:98:1: atomic returns [StringBuilder result] : (at= atom | equal | subclass );
    public final StringBuilder atomic() throws RecognitionException {
        StringBuilder result = null;


        StringBuilder at =null;


        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:99:5: (at= atom | equal | subclass )
            int alt18=3;
            switch ( input.LA(1) ) {
            case PSOA:
                {
                alt18=1;
                }
                break;
            case EQUAL:
                {
                alt18=2;
                }
                break;
            case SUBCLASS:
                {
                alt18=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;

            }

            switch (alt18) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:99:9: at= atom
                    {
                    pushFollow(FOLLOW_atom_in_atomic531);
                    at=atom();

                    state._fsp--;


                     result = at; 

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:100:9: equal
                    {
                    pushFollow(FOLLOW_equal_in_atomic543);
                    equal();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:101:9: subclass
                    {
                    pushFollow(FOLLOW_subclass_in_atomic553);
                    subclass();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "atomic"



    // $ANTLR start "atom"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:104:1: atom returns [StringBuilder result] : p= psoa ;
    public final StringBuilder atom() throws RecognitionException {
        StringBuilder result = null;


        StringBuilder p =null;


        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:105:5: (p= psoa )
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:105:9: p= psoa
            {
            pushFollow(FOLLOW_psoa_in_atom578);
            p=psoa();

            state._fsp--;


             result = p; 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "atom"



    // $ANTLR start "equal"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:108:1: equal : ^( EQUAL term term ) ;
    public final void equal() throws RecognitionException {
        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:109:5: ( ^( EQUAL term term ) )
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:109:9: ^( EQUAL term term )
            {
            match(input,EQUAL,FOLLOW_EQUAL_in_equal600); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_term_in_equal602);
            term();

            state._fsp--;


            pushFollow(FOLLOW_term_in_equal604);
            term();

            state._fsp--;


            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "equal"



    // $ANTLR start "subclass"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:112:1: subclass : ^( SUBCLASS term term ) ;
    public final void subclass() throws RecognitionException {
        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:113:5: ( ^( SUBCLASS term term ) )
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:113:9: ^( SUBCLASS term term )
            {
            match(input,SUBCLASS,FOLLOW_SUBCLASS_in_subclass625); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_term_in_subclass627);
            term();

            state._fsp--;


            pushFollow(FOLLOW_term_in_subclass629);
            term();

            state._fsp--;


            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "subclass"



    // $ANTLR start "term"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:116:1: term returns [String result] : (c= constant | VAR_ID |p= psoa | external );
    public final String term() throws RecognitionException {
        String result = null;


        CommonTree VAR_ID2=null;
        String c =null;

        StringBuilder p =null;


        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:117:5: (c= constant | VAR_ID |p= psoa | external )
            int alt19=4;
            switch ( input.LA(1) ) {
            case LITERAL:
            case SHORTCONST:
                {
                alt19=1;
                }
                break;
            case VAR_ID:
                {
                alt19=2;
                }
                break;
            case PSOA:
                {
                alt19=3;
                }
                break;
            case EXTERNAL:
                {
                alt19=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;

            }

            switch (alt19) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:117:9: c= constant
                    {
                    pushFollow(FOLLOW_constant_in_term659);
                    c=constant();

                    state._fsp--;


                     result = c; 

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:118:9: VAR_ID
                    {
                    VAR_ID2=(CommonTree)match(input,VAR_ID,FOLLOW_VAR_ID_in_term671); 

                     result = getVarName((VAR_ID2!=null?VAR_ID2.getText():null)); 

                    }
                    break;
                case 3 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:119:9: p= psoa
                    {
                    pushFollow(FOLLOW_psoa_in_term685);
                    p=psoa();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:120:9: external
                    {
                    pushFollow(FOLLOW_external_in_term695);
                    external();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "term"



    // $ANTLR start "external"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:123:1: external : ^( EXTERNAL psoa ) ;
    public final void external() throws RecognitionException {
        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:124:5: ( ^( EXTERNAL psoa ) )
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:124:9: ^( EXTERNAL psoa )
            {
            match(input,EXTERNAL,FOLLOW_EXTERNAL_in_external715); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_psoa_in_external717);
            psoa();

            state._fsp--;


            match(input, Token.UP, null); 


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "external"



    // $ANTLR start "psoa"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:127:1: psoa returns [StringBuilder result] : ^( PSOA (oid= term )? ^( INSTANCE type= term ) (t= tuple )* (s= slot )* ) ;
    public final StringBuilder psoa() throws RecognitionException {
        StringBuilder result = null;


        String oid =null;

        String type =null;

        String t =null;

        String s =null;



        	List<String> tuples = list();
        	List<String> slots = list();

        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:132:5: ( ^( PSOA (oid= term )? ^( INSTANCE type= term ) (t= tuple )* (s= slot )* ) )
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:132:9: ^( PSOA (oid= term )? ^( INSTANCE type= term ) (t= tuple )* (s= slot )* )
            {
            match(input,PSOA,FOLLOW_PSOA_in_psoa751); 

            match(input, Token.DOWN, null); 
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:132:19: (oid= term )?
            int alt20=2;
            switch ( input.LA(1) ) {
                case EXTERNAL:
                case LITERAL:
                case PSOA:
                case SHORTCONST:
                case VAR_ID:
                    {
                    alt20=1;
                    }
                    break;
            }

            switch (alt20) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:132:19: oid= term
                    {
                    pushFollow(FOLLOW_term_in_psoa755);
                    oid=term();

                    state._fsp--;


                    }
                    break;

            }


            match(input,INSTANCE,FOLLOW_INSTANCE_in_psoa759); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_term_in_psoa763);
            type=term();

            state._fsp--;


            match(input, Token.UP, null); 


            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:132:48: (t= tuple )*
            loop21:
            do {
                int alt21=2;
                switch ( input.LA(1) ) {
                case TUPLE:
                    {
                    alt21=1;
                    }
                    break;

                }

                switch (alt21) {
            	case 1 :
            	    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:132:49: t= tuple
            	    {
            	    pushFollow(FOLLOW_tuple_in_psoa769);
            	    t=tuple();

            	    state._fsp--;


            	    tuples.add(t); 

            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);


            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:132:77: (s= slot )*
            loop22:
            do {
                int alt22=2;
                switch ( input.LA(1) ) {
                case SLOT:
                    {
                    alt22=1;
                    }
                    break;

                }

                switch (alt22) {
            	case 1 :
            	    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:132:78: s= slot
            	    {
            	    pushFollow(FOLLOW_slot_in_psoa778);
            	    s=slot();

            	    state._fsp--;


            	    slots.add(s); 

            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);


            match(input, Token.UP, null); 



            		result = psoaStr(oid, type, tuples, slots);
                

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "psoa"



    // $ANTLR start "tuple"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:138:1: tuple returns [String result] : ^( TUPLE (t= term )+ ) ;
    public final String tuple() throws RecognitionException {
        String result = null;


        String t =null;



            StringBuilder b = builder();

        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:142:5: ( ^( TUPLE (t= term )+ ) )
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:142:9: ^( TUPLE (t= term )+ )
            {
            match(input,TUPLE,FOLLOW_TUPLE_in_tuple818); 

            match(input, Token.DOWN, null); 
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:142:17: (t= term )+
            int cnt23=0;
            loop23:
            do {
                int alt23=2;
                switch ( input.LA(1) ) {
                case EXTERNAL:
                case LITERAL:
                case PSOA:
                case SHORTCONST:
                case VAR_ID:
                    {
                    alt23=1;
                    }
                    break;

                }

                switch (alt23) {
            	case 1 :
            	    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:142:18: t= term
            	    {
            	    pushFollow(FOLLOW_term_in_tuple823);
            	    t=term();

            	    state._fsp--;


            	     collectTerm(b, t); 

            	    }
            	    break;

            	default :
            	    if ( cnt23 >= 1 ) break loop23;
                        EarlyExitException eee =
                            new EarlyExitException(23, input);
                        throw eee;
                }
                cnt23++;
            } while (true);


            match(input, Token.UP, null); 



                  result = b.toString(); 
                

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "tuple"



    // $ANTLR start "slot"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:147:1: slot returns [String result] : ^( SLOT s= term t= term ) ;
    public final String slot() throws RecognitionException {
        String result = null;


        String s =null;

        String t =null;



          StringBuilder b = builder();

        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:151:5: ( ^( SLOT s= term t= term ) )
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:151:9: ^( SLOT s= term t= term )
            {
            match(input,SLOT,FOLLOW_SLOT_in_slot863); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_term_in_slot867);
            s=term();

            state._fsp--;


            pushFollow(FOLLOW_term_in_slot871);
            t=term();

            state._fsp--;


            match(input, Token.UP, null); 



                  collectTerms(b, s, t);
                  result = b.toString(); 
                

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "slot"



    // $ANTLR start "constant"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:157:1: constant returns [String result] : ( ^( LITERAL IRI ) | ^( SHORTCONST c= constshort ) );
    public final String constant() throws RecognitionException {
        String result = null;


        String c =null;


        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:158:5: ( ^( LITERAL IRI ) | ^( SHORTCONST c= constshort ) )
            int alt24=2;
            switch ( input.LA(1) ) {
            case LITERAL:
                {
                alt24=1;
                }
                break;
            case SHORTCONST:
                {
                alt24=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;

            }

            switch (alt24) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:158:9: ^( LITERAL IRI )
                    {
                    match(input,LITERAL,FOLLOW_LITERAL_in_constant898); 

                    match(input, Token.DOWN, null); 
                    match(input,IRI,FOLLOW_IRI_in_constant900); 

                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:159:9: ^( SHORTCONST c= constshort )
                    {
                    match(input,SHORTCONST,FOLLOW_SHORTCONST_in_constant912); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_constshort_in_constant916);
                    c=constshort();

                    state._fsp--;


                    match(input, Token.UP, null); 


                     result = c; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "constant"



    // $ANTLR start "constshort"
    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:162:1: constshort returns [String result] : ( IRI | LITERAL | NUMBER | LOCAL );
    public final String constshort() throws RecognitionException {
        String result = null;


        CommonTree NUMBER3=null;
        CommonTree LOCAL4=null;

        try {
            // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:163:5: ( IRI | LITERAL | NUMBER | LOCAL )
            int alt25=4;
            switch ( input.LA(1) ) {
            case IRI:
                {
                alt25=1;
                }
                break;
            case LITERAL:
                {
                alt25=2;
                }
                break;
            case NUMBER:
                {
                alt25=3;
                }
                break;
            case LOCAL:
                {
                alt25=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;

            }

            switch (alt25) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:163:9: IRI
                    {
                    match(input,IRI,FOLLOW_IRI_in_constshort946); 

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:164:9: LITERAL
                    {
                    match(input,LITERAL,FOLLOW_LITERAL_in_constshort956); 

                    }
                    break;
                case 3 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:165:9: NUMBER
                    {
                    NUMBER3=(CommonTree)match(input,NUMBER,FOLLOW_NUMBER_in_constshort966); 

                     result = (NUMBER3!=null?NUMBER3.getText():null); 

                    }
                    break;
                case 4 :
                    // org/ruleml/api/presentation_syntax_parser/BasicRuleMLPresentationASTGrammar.g:166:9: LOCAL
                    {
                    LOCAL4=(CommonTree)match(input,LOCAL,FOLLOW_LOCAL_in_constshort978); 

                     result = getConstName((LOCAL4!=null?LOCAL4.getText():null)); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return result;
    }
    // $ANTLR end "constshort"

    // Delegated rules


 

    public static final BitSet FOLLOW_DOCUMENT_in_document59 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_base_in_document61 = new BitSet(new long[]{0x0000001000420008L});
    public static final BitSet FOLLOW_prefix_in_document64 = new BitSet(new long[]{0x0000001000420008L});
    public static final BitSet FOLLOW_importDecl_in_document67 = new BitSet(new long[]{0x0000000000420008L});
    public static final BitSet FOLLOW_group_in_document70 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_BASE_in_base92 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IRI_REF_in_base94 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PREFIX_in_prefix115 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_prefix117 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_IRI_REF_in_prefix119 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IMPORT_in_importDecl140 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IRI_REF_in_importDecl142 = new BitSet(new long[]{0x0000000004000008L});
    public static final BitSet FOLLOW_IRI_REF_in_importDecl144 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GROUP_in_group166 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_group_element_in_group168 = new BitSet(new long[]{0x0000102000229008L});
    public static final BitSet FOLLOW_rule_in_group_element191 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_group_in_group_element203 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule_in_queries225 = new BitSet(new long[]{0x0000102000209002L});
    public static final BitSet FOLLOW_FORALL_in_rule254 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VAR_LIST_in_rule257 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VAR_ID_in_rule259 = new BitSet(new long[]{0x0000800000000008L});
    public static final BitSet FOLLOW_clause_in_rule263 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_clause_in_rule275 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPLICATION_in_clause301 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_head_in_clause305 = new BitSet(new long[]{0x0000102800007020L});
    public static final BitSet FOLLOW_formula_in_clause309 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_atomic_in_clause326 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atomic_in_head362 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXISTS_in_head375 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VAR_LIST_in_head388 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VAR_ID_in_head391 = new BitSet(new long[]{0x0000800000000008L});
    public static final BitSet FOLLOW_atomic_in_head410 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AND_in_formula442 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_formula_in_formula444 = new BitSet(new long[]{0x0000102800007028L});
    public static final BitSet FOLLOW_OR_in_formula457 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_formula_in_formula459 = new BitSet(new long[]{0x0000102800007028L});
    public static final BitSet FOLLOW_EXISTS_in_formula472 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VAR_LIST_in_formula475 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VAR_ID_in_formula477 = new BitSet(new long[]{0x0000800000000008L});
    public static final BitSet FOLLOW_formula_in_formula481 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_atomic_in_formula494 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_external_in_formula506 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_atomic531 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_equal_in_atomic543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subclass_in_atomic553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_psoa_in_atom578 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQUAL_in_equal600 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_term_in_equal602 = new BitSet(new long[]{0x0000812020004000L});
    public static final BitSet FOLLOW_term_in_equal604 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SUBCLASS_in_subclass625 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_term_in_subclass627 = new BitSet(new long[]{0x0000812020004000L});
    public static final BitSet FOLLOW_term_in_subclass629 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_constant_in_term659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_ID_in_term671 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_psoa_in_term685 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_external_in_term695 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXTERNAL_in_external715 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_psoa_in_external717 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PSOA_in_psoa751 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_term_in_psoa755 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_INSTANCE_in_psoa759 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_term_in_psoa763 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_tuple_in_psoa769 = new BitSet(new long[]{0x0000420000000008L});
    public static final BitSet FOLLOW_slot_in_psoa778 = new BitSet(new long[]{0x0000020000000008L});
    public static final BitSet FOLLOW_TUPLE_in_tuple818 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_term_in_tuple823 = new BitSet(new long[]{0x0000812020004008L});
    public static final BitSet FOLLOW_SLOT_in_slot863 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_term_in_slot867 = new BitSet(new long[]{0x0000812020004000L});
    public static final BitSet FOLLOW_term_in_slot871 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LITERAL_in_constant898 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IRI_in_constant900 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SHORTCONST_in_constant912 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_constshort_in_constant916 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IRI_in_constshort946 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LITERAL_in_constshort956 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_constshort966 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LOCAL_in_constshort978 = new BitSet(new long[]{0x0000000000000002L});

}