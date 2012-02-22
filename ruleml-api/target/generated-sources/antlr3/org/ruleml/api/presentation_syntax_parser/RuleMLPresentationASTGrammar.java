// $ANTLR 3.4 org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g 2012-02-21 17:29:59

	package org.ruleml.api.presentation_syntax_parser;
	import org.ruleml.api.*;
    import org.ruleml.api.AbstractSyntax.*;


import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class RuleMLPresentationASTGrammar extends TreeParser {
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


    public RuleMLPresentationASTGrammar(TreeNodeStream input) {
        this(input, new RecognizerSharedState());
    }
    public RuleMLPresentationASTGrammar(TreeNodeStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return RuleMLPresentationASTGrammar.tokenNames; }
    public String getGrammarFileName() { return "org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g"; }


        private AbstractSyntax factory;
        
        public RuleMLPresentationASTGrammar(TreeNodeStream input, AbstractSyntax factory) {
            this(input, new RecognizerSharedState());
            this.factory = factory;
        }



    // $ANTLR start "document"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:27:1: document returns [Document result] : ^( DOCUMENT (b= base )? (prf= prefix )* (imp= importDecl )* (g= group )? ) ;
    public final Document document() throws RecognitionException {
        Document result = null;


        Base b =null;

        Prefix prf =null;

        Import imp =null;

        Group g =null;



            List<Prefix> prefixes = new ArrayList<Prefix>();
            List<Import> imports = new ArrayList<Import>();

        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:33:5: ( ^( DOCUMENT (b= base )? (prf= prefix )* (imp= importDecl )* (g= group )? ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:33:7: ^( DOCUMENT (b= base )? (prf= prefix )* (imp= importDecl )* (g= group )? )
            {
            match(input,DOCUMENT,FOLLOW_DOCUMENT_in_document69); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:33:19: (b= base )?
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
                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:33:19: b= base
                        {
                        pushFollow(FOLLOW_base_in_document73);
                        b=base();

                        state._fsp--;


                        }
                        break;

                }


                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:34:9: (prf= prefix )*
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
                	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:34:10: prf= prefix
                	    {
                	    pushFollow(FOLLOW_prefix_in_document87);
                	    prf=prefix();

                	    state._fsp--;


                	     prefixes.add(prf); 

                	    }
                	    break;

                	default :
                	    break loop2;
                    }
                } while (true);


                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:35:9: (imp= importDecl )*
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
                	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:35:10: imp= importDecl
                	    {
                	    pushFollow(FOLLOW_importDecl_in_document105);
                	    imp=importDecl();

                	    state._fsp--;


                	     imports.add(imp); 

                	    }
                	    break;

                	default :
                	    break loop3;
                    }
                } while (true);


                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:36:10: (g= group )?
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
                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:36:10: g= group
                        {
                        pushFollow(FOLLOW_group_in_document122);
                        g=group();

                        state._fsp--;


                        }
                        break;

                }


                match(input, Token.UP, null); 
            }



                        result = factory.createDocument(b, prefixes, imports, g);
                    

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
    // $ANTLR end "document"



    // $ANTLR start "base"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:42:1: base returns [Base result] : ^( BASE IRI_REF ) ;
    public final Base base() throws RecognitionException {
        Base result = null;


        CommonTree IRI_REF1=null;

        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:43:5: ( ^( BASE IRI_REF ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:43:9: ^( BASE IRI_REF )
            {
            match(input,BASE,FOLLOW_BASE_in_base158); 

            match(input, Token.DOWN, null); 
            IRI_REF1=(CommonTree)match(input,IRI_REF,FOLLOW_IRI_REF_in_base160); 

            match(input, Token.UP, null); 



            		    result = factory.createBase((IRI_REF1!=null?IRI_REF1.getText():null));
            		

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
    // $ANTLR end "base"



    // $ANTLR start "prefix"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:49:1: prefix returns [Prefix result] : ^( PREFIX ID IRI_REF ) ;
    public final Prefix prefix() throws RecognitionException {
        Prefix result = null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:50:5: ( ^( PREFIX ID IRI_REF ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:50:9: ^( PREFIX ID IRI_REF )
            {
            match(input,PREFIX,FOLLOW_PREFIX_in_prefix190); 

            match(input, Token.DOWN, null); 
            match(input,ID,FOLLOW_ID_in_prefix192); 

            match(input,IRI_REF,FOLLOW_IRI_REF_in_prefix194); 

            match(input, Token.UP, null); 



            		    System.out.println("prefix");
            		    result = null;
            		

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
    // $ANTLR end "prefix"



    // $ANTLR start "importDecl"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:57:1: importDecl returns [Import result] : ^( IMPORT IRI_REF ( IRI_REF )? ) ;
    public final Import importDecl() throws RecognitionException {
        Import result = null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:58:5: ( ^( IMPORT IRI_REF ( IRI_REF )? ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:58:9: ^( IMPORT IRI_REF ( IRI_REF )? )
            {
            match(input,IMPORT,FOLLOW_IMPORT_in_importDecl224); 

            match(input, Token.DOWN, null); 
            match(input,IRI_REF,FOLLOW_IRI_REF_in_importDecl226); 

            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:58:26: ( IRI_REF )?
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
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:58:26: IRI_REF
                    {
                    match(input,IRI_REF,FOLLOW_IRI_REF_in_importDecl228); 

                    }
                    break;

            }


            match(input, Token.UP, null); 



            		    System.out.println("import");
            		    result = null;
            		

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
    // $ANTLR end "importDecl"



    // $ANTLR start "group"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:65:1: group returns [Group result] : ^( GROUP ( group_element )* ) ;
    public final Group group() throws RecognitionException {
        Group result = null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:66:5: ( ^( GROUP ( group_element )* ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:66:9: ^( GROUP ( group_element )* )
            {
            match(input,GROUP,FOLLOW_GROUP_in_group258); 

            if ( input.LA(1)==Token.DOWN ) {
                match(input, Token.DOWN, null); 
                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:66:17: ( group_element )*
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
                	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:66:17: group_element
                	    {
                	    pushFollow(FOLLOW_group_element_in_group260);
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



            		    System.out.println("group");
            		    result = null;
            		

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
    // $ANTLR end "group"



    // $ANTLR start "group_element"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:73:1: group_element returns [GroupElement result] : ( rule | group );
    public final GroupElement group_element() throws RecognitionException {
        GroupElement result = null;


        Rule rule2 =null;

        Group group3 =null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:74:5: ( rule | group )
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
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:74:9: rule
                    {
                    pushFollow(FOLLOW_rule_in_group_element289);
                    rule2=rule();

                    state._fsp--;


                     result = rule2; 

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:75:9: group
                    {
                    pushFollow(FOLLOW_group_in_group_element301);
                    group3=group();

                    state._fsp--;


                     result = group3; 

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
    // $ANTLR end "group_element"



    // $ANTLR start "rule"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:79:1: rule returns [Rule result] : ( ^( FORALL ^( VAR_LIST ( VAR_ID )+ ) clause ) | clause );
    public final Rule rule() throws RecognitionException {
        Rule result = null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:80:5: ( ^( FORALL ^( VAR_LIST ( VAR_ID )+ ) clause ) | clause )
            int alt9=2;
            switch ( input.LA(1) ) {
            case FORALL:
                {
                alt9=1;
                }
                break;
            case EQUAL:
            case IMPLICATION:
            case PSOA:
            case SUBCLASS:
                {
                alt9=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }

            switch (alt9) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:80:9: ^( FORALL ^( VAR_LIST ( VAR_ID )+ ) clause )
                    {
                    match(input,FORALL,FOLLOW_FORALL_in_rule328); 

                    match(input, Token.DOWN, null); 
                    match(input,VAR_LIST,FOLLOW_VAR_LIST_in_rule331); 

                    match(input, Token.DOWN, null); 
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:80:29: ( VAR_ID )+
                    int cnt8=0;
                    loop8:
                    do {
                        int alt8=2;
                        switch ( input.LA(1) ) {
                        case VAR_ID:
                            {
                            alt8=1;
                            }
                            break;

                        }

                        switch (alt8) {
                    	case 1 :
                    	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:80:29: VAR_ID
                    	    {
                    	    match(input,VAR_ID,FOLLOW_VAR_ID_in_rule333); 

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


                    match(input, Token.UP, null); 


                    pushFollow(FOLLOW_clause_in_rule337);
                    clause();

                    state._fsp--;


                    match(input, Token.UP, null); 



                    		    System.out.println("rule");
                    		    result = null;
                    		

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:85:6: clause
                    {
                    pushFollow(FOLLOW_clause_in_rule349);
                    clause();

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
    // $ANTLR end "rule"



    // $ANTLR start "clause"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:88:1: clause returns [Clause result] : ( ^( IMPLICATION head formula ) | atomic );
    public final Clause clause() throws RecognitionException {
        Clause result = null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:89:5: ( ^( IMPLICATION head formula ) | atomic )
            int alt10=2;
            switch ( input.LA(1) ) {
            case IMPLICATION:
                {
                alt10=1;
                }
                break;
            case EQUAL:
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
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:89:9: ^( IMPLICATION head formula )
                    {
                    match(input,IMPLICATION,FOLLOW_IMPLICATION_in_clause373); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_head_in_clause375);
                    head();

                    state._fsp--;


                    pushFollow(FOLLOW_formula_in_clause377);
                    formula();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:90:9: atomic
                    {
                    pushFollow(FOLLOW_atomic_in_clause388);
                    atomic();

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
    // $ANTLR end "clause"



    // $ANTLR start "head"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:93:1: head returns [Head result] : ( atomic | ^( EXISTS ^( VAR_LIST ( VAR_ID )+ ) atomic ) );
    public final Head head() throws RecognitionException {
        Head result = null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:94:5: ( atomic | ^( EXISTS ^( VAR_LIST ( VAR_ID )+ ) atomic ) )
            int alt12=2;
            switch ( input.LA(1) ) {
            case EQUAL:
            case PSOA:
            case SUBCLASS:
                {
                alt12=1;
                }
                break;
            case EXISTS:
                {
                alt12=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;

            }

            switch (alt12) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:94:9: atomic
                    {
                    pushFollow(FOLLOW_atomic_in_head415);
                    atomic();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:95:9: ^( EXISTS ^( VAR_LIST ( VAR_ID )+ ) atomic )
                    {
                    match(input,EXISTS,FOLLOW_EXISTS_in_head426); 

                    match(input, Token.DOWN, null); 
                    match(input,VAR_LIST,FOLLOW_VAR_LIST_in_head429); 

                    match(input, Token.DOWN, null); 
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:95:29: ( VAR_ID )+
                    int cnt11=0;
                    loop11:
                    do {
                        int alt11=2;
                        switch ( input.LA(1) ) {
                        case VAR_ID:
                            {
                            alt11=1;
                            }
                            break;

                        }

                        switch (alt11) {
                    	case 1 :
                    	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:95:29: VAR_ID
                    	    {
                    	    match(input,VAR_ID,FOLLOW_VAR_ID_in_head431); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt11 >= 1 ) break loop11;
                                EarlyExitException eee =
                                    new EarlyExitException(11, input);
                                throw eee;
                        }
                        cnt11++;
                    } while (true);


                    match(input, Token.UP, null); 


                    pushFollow(FOLLOW_atomic_in_head435);
                    atomic();

                    state._fsp--;


                    match(input, Token.UP, null); 


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
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:98:1: formula returns [Formula result] : ( ^( AND ( formula )+ ) | ^( OR ( formula )+ ) | ^( EXISTS ^( VAR_LIST ( VAR_ID )+ ) formula ) | atomic | external );
    public final Formula formula() throws RecognitionException {
        Formula result = null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:99:5: ( ^( AND ( formula )+ ) | ^( OR ( formula )+ ) | ^( EXISTS ^( VAR_LIST ( VAR_ID )+ ) formula ) | atomic | external )
            int alt16=5;
            switch ( input.LA(1) ) {
            case AND:
                {
                alt16=1;
                }
                break;
            case OR:
                {
                alt16=2;
                }
                break;
            case EXISTS:
                {
                alt16=3;
                }
                break;
            case EQUAL:
            case PSOA:
            case SUBCLASS:
                {
                alt16=4;
                }
                break;
            case EXTERNAL:
                {
                alt16=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;

            }

            switch (alt16) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:99:9: ^( AND ( formula )+ )
                    {
                    match(input,AND,FOLLOW_AND_in_formula460); 

                    match(input, Token.DOWN, null); 
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:99:15: ( formula )+
                    int cnt13=0;
                    loop13:
                    do {
                        int alt13=2;
                        switch ( input.LA(1) ) {
                        case AND:
                        case EQUAL:
                        case EXISTS:
                        case EXTERNAL:
                        case OR:
                        case PSOA:
                        case SUBCLASS:
                            {
                            alt13=1;
                            }
                            break;

                        }

                        switch (alt13) {
                    	case 1 :
                    	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:99:15: formula
                    	    {
                    	    pushFollow(FOLLOW_formula_in_formula462);
                    	    formula();

                    	    state._fsp--;


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt13 >= 1 ) break loop13;
                                EarlyExitException eee =
                                    new EarlyExitException(13, input);
                                throw eee;
                        }
                        cnt13++;
                    } while (true);


                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:100:9: ^( OR ( formula )+ )
                    {
                    match(input,OR,FOLLOW_OR_in_formula475); 

                    match(input, Token.DOWN, null); 
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:100:14: ( formula )+
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
                    	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:100:14: formula
                    	    {
                    	    pushFollow(FOLLOW_formula_in_formula477);
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
                case 3 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:101:9: ^( EXISTS ^( VAR_LIST ( VAR_ID )+ ) formula )
                    {
                    match(input,EXISTS,FOLLOW_EXISTS_in_formula490); 

                    match(input, Token.DOWN, null); 
                    match(input,VAR_LIST,FOLLOW_VAR_LIST_in_formula493); 

                    match(input, Token.DOWN, null); 
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:101:29: ( VAR_ID )+
                    int cnt15=0;
                    loop15:
                    do {
                        int alt15=2;
                        switch ( input.LA(1) ) {
                        case VAR_ID:
                            {
                            alt15=1;
                            }
                            break;

                        }

                        switch (alt15) {
                    	case 1 :
                    	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:101:29: VAR_ID
                    	    {
                    	    match(input,VAR_ID,FOLLOW_VAR_ID_in_formula495); 

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


                    pushFollow(FOLLOW_formula_in_formula499);
                    formula();

                    state._fsp--;


                    match(input, Token.UP, null); 


                    }
                    break;
                case 4 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:102:9: atomic
                    {
                    pushFollow(FOLLOW_atomic_in_formula510);
                    atomic();

                    state._fsp--;


                    }
                    break;
                case 5 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:103:9: external
                    {
                    pushFollow(FOLLOW_external_in_formula520);
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
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:106:1: atomic returns [Atomic result] : ( atom | equal | subclass );
    public final Atomic atomic() throws RecognitionException {
        Atomic result = null;


        Atom atom4 =null;

        Equal equal5 =null;

        Subclass subclass6 =null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:107:5: ( atom | equal | subclass )
            int alt17=3;
            switch ( input.LA(1) ) {
            case PSOA:
                {
                alt17=1;
                }
                break;
            case EQUAL:
                {
                alt17=2;
                }
                break;
            case SUBCLASS:
                {
                alt17=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;

            }

            switch (alt17) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:107:9: atom
                    {
                    pushFollow(FOLLOW_atom_in_atomic543);
                    atom4=atom();

                    state._fsp--;


                     result = atom4; 

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:108:9: equal
                    {
                    pushFollow(FOLLOW_equal_in_atomic555);
                    equal5=equal();

                    state._fsp--;


                     result = equal5; 

                    }
                    break;
                case 3 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:109:9: subclass
                    {
                    pushFollow(FOLLOW_subclass_in_atomic567);
                    subclass6=subclass();

                    state._fsp--;


                     result = subclass6; 

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
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:112:1: atom returns [Atom result] : psoa ;
    public final Atom atom() throws RecognitionException {
        Atom result = null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:113:5: ( psoa )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:113:9: psoa
            {
            pushFollow(FOLLOW_psoa_in_atom592);
            psoa();

            state._fsp--;


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
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:116:1: equal returns [Equal result] : ^( EQUAL term term ) ;
    public final Equal equal() throws RecognitionException {
        Equal result = null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:117:5: ( ^( EQUAL term term ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:117:9: ^( EQUAL term term )
            {
            match(input,EQUAL,FOLLOW_EQUAL_in_equal616); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_term_in_equal618);
            term();

            state._fsp--;


            pushFollow(FOLLOW_term_in_equal620);
            term();

            state._fsp--;


            match(input, Token.UP, null); 


             result = null; 

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
    // $ANTLR end "equal"



    // $ANTLR start "subclass"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:120:1: subclass returns [Subclass result] : ^( SUBCLASS term term ) ;
    public final Subclass subclass() throws RecognitionException {
        Subclass result = null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:121:5: ( ^( SUBCLASS term term ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:121:9: ^( SUBCLASS term term )
            {
            match(input,SUBCLASS,FOLLOW_SUBCLASS_in_subclass647); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_term_in_subclass649);
            term();

            state._fsp--;


            pushFollow(FOLLOW_term_in_subclass651);
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
        return result;
    }
    // $ANTLR end "subclass"



    // $ANTLR start "term"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:124:1: term returns [Term result] : ( constant | VAR_ID | psoa | external );
    public final Term term() throws RecognitionException {
        Term result = null;


        Psoa psoa7 =null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:125:5: ( constant | VAR_ID | psoa | external )
            int alt18=4;
            switch ( input.LA(1) ) {
            case LITERAL:
            case SHORTCONST:
                {
                alt18=1;
                }
                break;
            case VAR_ID:
                {
                alt18=2;
                }
                break;
            case PSOA:
                {
                alt18=3;
                }
                break;
            case EXTERNAL:
                {
                alt18=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;

            }

            switch (alt18) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:125:9: constant
                    {
                    pushFollow(FOLLOW_constant_in_term679);
                    constant();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:126:9: VAR_ID
                    {
                    match(input,VAR_ID,FOLLOW_VAR_ID_in_term689); 

                    }
                    break;
                case 3 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:127:9: psoa
                    {
                    pushFollow(FOLLOW_psoa_in_term700);
                    psoa7=psoa();

                    state._fsp--;


                     result = psoa7; 

                    }
                    break;
                case 4 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:128:9: external
                    {
                    pushFollow(FOLLOW_external_in_term712);
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
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:131:1: external returns [Psoa result] : ^( EXTERNAL psoa ) ;
    public final Psoa external() throws RecognitionException {
        Psoa result = null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:132:5: ( ^( EXTERNAL psoa ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:132:9: ^( EXTERNAL psoa )
            {
            match(input,EXTERNAL,FOLLOW_EXTERNAL_in_external736); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_psoa_in_external738);
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
        return result;
    }
    // $ANTLR end "external"



    // $ANTLR start "psoa"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:135:1: psoa returns [Psoa result] : ^( PSOA ( term )? ^( INSTANCE term ) ( tuple )* ( slot )* ) ;
    public final Psoa psoa() throws RecognitionException {
        Psoa result = null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:136:5: ( ^( PSOA ( term )? ^( INSTANCE term ) ( tuple )* ( slot )* ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:136:9: ^( PSOA ( term )? ^( INSTANCE term ) ( tuple )* ( slot )* )
            {
            match(input,PSOA,FOLLOW_PSOA_in_psoa767); 

            match(input, Token.DOWN, null); 
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:136:16: ( term )?
            int alt19=2;
            switch ( input.LA(1) ) {
                case EXTERNAL:
                case LITERAL:
                case PSOA:
                case SHORTCONST:
                case VAR_ID:
                    {
                    alt19=1;
                    }
                    break;
            }

            switch (alt19) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:136:16: term
                    {
                    pushFollow(FOLLOW_term_in_psoa769);
                    term();

                    state._fsp--;


                    }
                    break;

            }


            match(input,INSTANCE,FOLLOW_INSTANCE_in_psoa773); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_term_in_psoa775);
            term();

            state._fsp--;


            match(input, Token.UP, null); 


            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:136:39: ( tuple )*
            loop20:
            do {
                int alt20=2;
                switch ( input.LA(1) ) {
                case TUPLE:
                    {
                    alt20=1;
                    }
                    break;

                }

                switch (alt20) {
            	case 1 :
            	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:136:39: tuple
            	    {
            	    pushFollow(FOLLOW_tuple_in_psoa778);
            	    tuple();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);


            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:136:46: ( slot )*
            loop21:
            do {
                int alt21=2;
                switch ( input.LA(1) ) {
                case SLOT:
                    {
                    alt21=1;
                    }
                    break;

                }

                switch (alt21) {
            	case 1 :
            	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:136:46: slot
            	    {
            	    pushFollow(FOLLOW_slot_in_psoa781);
            	    slot();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);


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
        return result;
    }
    // $ANTLR end "psoa"



    // $ANTLR start "tuple"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:139:1: tuple returns [Tuple result] : ^( TUPLE ( term )+ ) ;
    public final Tuple tuple() throws RecognitionException {
        Tuple result = null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:140:5: ( ^( TUPLE ( term )+ ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:140:9: ^( TUPLE ( term )+ )
            {
            match(input,TUPLE,FOLLOW_TUPLE_in_tuple807); 

            match(input, Token.DOWN, null); 
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:140:17: ( term )+
            int cnt22=0;
            loop22:
            do {
                int alt22=2;
                switch ( input.LA(1) ) {
                case EXTERNAL:
                case LITERAL:
                case PSOA:
                case SHORTCONST:
                case VAR_ID:
                    {
                    alt22=1;
                    }
                    break;

                }

                switch (alt22) {
            	case 1 :
            	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:140:17: term
            	    {
            	    pushFollow(FOLLOW_term_in_tuple809);
            	    term();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    if ( cnt22 >= 1 ) break loop22;
                        EarlyExitException eee =
                            new EarlyExitException(22, input);
                        throw eee;
                }
                cnt22++;
            } while (true);


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
        return result;
    }
    // $ANTLR end "tuple"



    // $ANTLR start "slot"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:143:1: slot returns [Slot result] : ^( SLOT term term ) ;
    public final Slot slot() throws RecognitionException {
        Slot result = null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:144:5: ( ^( SLOT term term ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:144:9: ^( SLOT term term )
            {
            match(input,SLOT,FOLLOW_SLOT_in_slot839); 

            match(input, Token.DOWN, null); 
            pushFollow(FOLLOW_term_in_slot841);
            term();

            state._fsp--;


            pushFollow(FOLLOW_term_in_slot843);
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
        return result;
    }
    // $ANTLR end "slot"



    // $ANTLR start "constant"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:147:1: constant returns [Const result] : ( ^( LITERAL IRI ) | ^( SHORTCONST constshort ) );
    public final Const constant() throws RecognitionException {
        Const result = null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:148:5: ( ^( LITERAL IRI ) | ^( SHORTCONST constshort ) )
            int alt23=2;
            switch ( input.LA(1) ) {
            case LITERAL:
                {
                alt23=1;
                }
                break;
            case SHORTCONST:
                {
                alt23=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 23, 0, input);

                throw nvae;

            }

            switch (alt23) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:148:9: ^( LITERAL IRI )
                    {
                    match(input,LITERAL,FOLLOW_LITERAL_in_constant868); 

                    match(input, Token.DOWN, null); 
                    match(input,IRI,FOLLOW_IRI_in_constant870); 

                    match(input, Token.UP, null); 


                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:149:9: ^( SHORTCONST constshort )
                    {
                    match(input,SHORTCONST,FOLLOW_SHORTCONST_in_constant882); 

                    match(input, Token.DOWN, null); 
                    pushFollow(FOLLOW_constshort_in_constant884);
                    constshort();

                    state._fsp--;


                    match(input, Token.UP, null); 


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
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:152:1: constshort returns [Const_Constshort result] : ( IRI | LITERAL | NUMBER | LOCAL );
    public final Const_Constshort constshort() throws RecognitionException {
        Const_Constshort result = null;


        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:153:5: ( IRI | LITERAL | NUMBER | LOCAL )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationASTGrammar.g:
            {
            if ( input.LA(1)==IRI||(input.LA(1) >= LITERAL && input.LA(1) <= LOCAL)||input.LA(1)==NUMBER ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
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
        return result;
    }
    // $ANTLR end "constshort"

    // Delegated rules


 

    public static final BitSet FOLLOW_DOCUMENT_in_document69 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_base_in_document73 = new BitSet(new long[]{0x0000001000420008L});
    public static final BitSet FOLLOW_prefix_in_document87 = new BitSet(new long[]{0x0000001000420008L});
    public static final BitSet FOLLOW_importDecl_in_document105 = new BitSet(new long[]{0x0000000000420008L});
    public static final BitSet FOLLOW_group_in_document122 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_BASE_in_base158 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IRI_REF_in_base160 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PREFIX_in_prefix190 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ID_in_prefix192 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_IRI_REF_in_prefix194 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IMPORT_in_importDecl224 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IRI_REF_in_importDecl226 = new BitSet(new long[]{0x0000000004000008L});
    public static final BitSet FOLLOW_IRI_REF_in_importDecl228 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GROUP_in_group258 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_group_element_in_group260 = new BitSet(new long[]{0x0000102000229008L});
    public static final BitSet FOLLOW_rule_in_group_element289 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_group_in_group_element301 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FORALL_in_rule328 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VAR_LIST_in_rule331 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VAR_ID_in_rule333 = new BitSet(new long[]{0x0000800000000008L});
    public static final BitSet FOLLOW_clause_in_rule337 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_clause_in_rule349 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPLICATION_in_clause373 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_head_in_clause375 = new BitSet(new long[]{0x0000102800007020L});
    public static final BitSet FOLLOW_formula_in_clause377 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_atomic_in_clause388 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atomic_in_head415 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXISTS_in_head426 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VAR_LIST_in_head429 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VAR_ID_in_head431 = new BitSet(new long[]{0x0000800000000008L});
    public static final BitSet FOLLOW_atomic_in_head435 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AND_in_formula460 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_formula_in_formula462 = new BitSet(new long[]{0x0000102800007028L});
    public static final BitSet FOLLOW_OR_in_formula475 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_formula_in_formula477 = new BitSet(new long[]{0x0000102800007028L});
    public static final BitSet FOLLOW_EXISTS_in_formula490 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VAR_LIST_in_formula493 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_VAR_ID_in_formula495 = new BitSet(new long[]{0x0000800000000008L});
    public static final BitSet FOLLOW_formula_in_formula499 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_atomic_in_formula510 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_external_in_formula520 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atom_in_atomic543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_equal_in_atomic555 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subclass_in_atomic567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_psoa_in_atom592 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQUAL_in_equal616 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_term_in_equal618 = new BitSet(new long[]{0x0000812020004000L});
    public static final BitSet FOLLOW_term_in_equal620 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SUBCLASS_in_subclass647 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_term_in_subclass649 = new BitSet(new long[]{0x0000812020004000L});
    public static final BitSet FOLLOW_term_in_subclass651 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_constant_in_term679 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_ID_in_term689 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_psoa_in_term700 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_external_in_term712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXTERNAL_in_external736 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_psoa_in_external738 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PSOA_in_psoa767 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_term_in_psoa769 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_INSTANCE_in_psoa773 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_term_in_psoa775 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_tuple_in_psoa778 = new BitSet(new long[]{0x0000420000000008L});
    public static final BitSet FOLLOW_slot_in_psoa781 = new BitSet(new long[]{0x0000020000000008L});
    public static final BitSet FOLLOW_TUPLE_in_tuple807 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_term_in_tuple809 = new BitSet(new long[]{0x0000812020004008L});
    public static final BitSet FOLLOW_SLOT_in_slot839 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_term_in_slot841 = new BitSet(new long[]{0x0000812020004000L});
    public static final BitSet FOLLOW_term_in_slot843 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LITERAL_in_constant868 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IRI_in_constant870 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SHORTCONST_in_constant882 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_constshort_in_constant884 = new BitSet(new long[]{0x0000000000000008L});

}