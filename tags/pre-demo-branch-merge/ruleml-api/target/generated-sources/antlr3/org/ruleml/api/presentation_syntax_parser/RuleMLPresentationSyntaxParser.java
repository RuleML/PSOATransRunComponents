// $ANTLR 3.4 org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g 2012-02-21 17:29:58

	package org.ruleml.api.presentation_syntax_parser;
    import org.ruleml.api.*;
    import org.ruleml.api.AbstractSyntax.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class RuleMLPresentationSyntaxParser extends Parser {
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
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public RuleMLPresentationSyntaxParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public RuleMLPresentationSyntaxParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return RuleMLPresentationSyntaxParser.tokenNames; }
    public String getGrammarFileName() { return "org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g"; }


        private DefaultAbstractSyntax factory = new DefaultAbstractSyntax();
        
        private CommonTree getTupleTree(List list_terms, int length)
        {
            CommonTree root = (CommonTree)adaptor.nil();
            for (int i = 0; i < length; i++)
                adaptor.addChild(root, list_terms.get(i));
            return root;
        }
        
        private String getStrValue(String str)
        {
            return str.substring(1, str.length() - 1);
        }


    public static class top_level_item_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "top_level_item"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:65:1: top_level_item : ( document )? EOF ;
    public final RuleMLPresentationSyntaxParser.top_level_item_return top_level_item() throws RecognitionException {
        RuleMLPresentationSyntaxParser.top_level_item_return retval = new RuleMLPresentationSyntaxParser.top_level_item_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token EOF2=null;
        RuleMLPresentationSyntaxParser.document_return document1 =null;


        CommonTree EOF2_tree=null;

        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:65:16: ( ( document )? EOF )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:65:18: ( document )? EOF
            {
            root_0 = (CommonTree)adaptor.nil();


            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:65:18: ( document )?
            int alt1=2;
            switch ( input.LA(1) ) {
                case DOCUMENT:
                    {
                    alt1=1;
                    }
                    break;
            }

            switch (alt1) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:65:18: document
                    {
                    pushFollow(FOLLOW_document_in_top_level_item166);
                    document1=document();

                    state._fsp--;

                    adaptor.addChild(root_0, document1.getTree());

                    }
                    break;

            }


            EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_top_level_item169); 
            EOF2_tree = 
            (CommonTree)adaptor.create(EOF2)
            ;
            adaptor.addChild(root_0, EOF2_tree);


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "top_level_item"


    public static class queries_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "queries"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:67:1: queries : ( rule )+ ;
    public final RuleMLPresentationSyntaxParser.queries_return queries() throws RecognitionException {
        RuleMLPresentationSyntaxParser.queries_return retval = new RuleMLPresentationSyntaxParser.queries_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        RuleMLPresentationSyntaxParser.rule_return rule3 =null;



        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:68:5: ( ( rule )+ )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:68:9: ( rule )+
            {
            root_0 = (CommonTree)adaptor.nil();


            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:68:9: ( rule )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                switch ( input.LA(1) ) {
                case AND:
                case CURIE:
                case EXISTS:
                case EXTERNAL:
                case FORALL:
                case ID:
                case IRI_REF:
                case NUMBER:
                case OR:
                case STRING:
                case VAR_ID:
                    {
                    alt2=1;
                    }
                    break;

                }

                switch (alt2) {
            	case 1 :
            	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:68:9: rule
            	    {
            	    pushFollow(FOLLOW_rule_in_queries183);
            	    rule3=rule();

            	    state._fsp--;

            	    adaptor.addChild(root_0, rule3.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "queries"


    public static class document_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "document"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:70:1: document : DOCUMENT LPAR ( base )? ( prefix )* ( importDecl )* ( group )? RPAR -> ^( DOCUMENT ( base )? ( prefix )* ( importDecl )* ( group )? ) ;
    public final RuleMLPresentationSyntaxParser.document_return document() throws RecognitionException {
        RuleMLPresentationSyntaxParser.document_return retval = new RuleMLPresentationSyntaxParser.document_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token DOCUMENT4=null;
        Token LPAR5=null;
        Token RPAR10=null;
        RuleMLPresentationSyntaxParser.base_return base6 =null;

        RuleMLPresentationSyntaxParser.prefix_return prefix7 =null;

        RuleMLPresentationSyntaxParser.importDecl_return importDecl8 =null;

        RuleMLPresentationSyntaxParser.group_return group9 =null;


        CommonTree DOCUMENT4_tree=null;
        CommonTree LPAR5_tree=null;
        CommonTree RPAR10_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_DOCUMENT=new RewriteRuleTokenStream(adaptor,"token DOCUMENT");
        RewriteRuleSubtreeStream stream_importDecl=new RewriteRuleSubtreeStream(adaptor,"rule importDecl");
        RewriteRuleSubtreeStream stream_prefix=new RewriteRuleSubtreeStream(adaptor,"rule prefix");
        RewriteRuleSubtreeStream stream_base=new RewriteRuleSubtreeStream(adaptor,"rule base");
        RewriteRuleSubtreeStream stream_group=new RewriteRuleSubtreeStream(adaptor,"rule group");
        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:71:5: ( DOCUMENT LPAR ( base )? ( prefix )* ( importDecl )* ( group )? RPAR -> ^( DOCUMENT ( base )? ( prefix )* ( importDecl )* ( group )? ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:71:9: DOCUMENT LPAR ( base )? ( prefix )* ( importDecl )* ( group )? RPAR
            {
            DOCUMENT4=(Token)match(input,DOCUMENT,FOLLOW_DOCUMENT_in_document198);  
            stream_DOCUMENT.add(DOCUMENT4);


            LPAR5=(Token)match(input,LPAR,FOLLOW_LPAR_in_document200);  
            stream_LPAR.add(LPAR5);


            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:71:23: ( base )?
            int alt3=2;
            switch ( input.LA(1) ) {
                case BASE:
                    {
                    alt3=1;
                    }
                    break;
            }

            switch (alt3) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:71:23: base
                    {
                    pushFollow(FOLLOW_base_in_document202);
                    base6=base();

                    state._fsp--;

                    stream_base.add(base6.getTree());

                    }
                    break;

            }


            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:71:29: ( prefix )*
            loop4:
            do {
                int alt4=2;
                switch ( input.LA(1) ) {
                case PREFIX:
                    {
                    alt4=1;
                    }
                    break;

                }

                switch (alt4) {
            	case 1 :
            	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:71:29: prefix
            	    {
            	    pushFollow(FOLLOW_prefix_in_document205);
            	    prefix7=prefix();

            	    state._fsp--;

            	    stream_prefix.add(prefix7.getTree());

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:71:37: ( importDecl )*
            loop5:
            do {
                int alt5=2;
                switch ( input.LA(1) ) {
                case IMPORT:
                    {
                    alt5=1;
                    }
                    break;

                }

                switch (alt5) {
            	case 1 :
            	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:71:37: importDecl
            	    {
            	    pushFollow(FOLLOW_importDecl_in_document208);
            	    importDecl8=importDecl();

            	    state._fsp--;

            	    stream_importDecl.add(importDecl8.getTree());

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:71:49: ( group )?
            int alt6=2;
            switch ( input.LA(1) ) {
                case GROUP:
                    {
                    alt6=1;
                    }
                    break;
            }

            switch (alt6) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:71:49: group
                    {
                    pushFollow(FOLLOW_group_in_document211);
                    group9=group();

                    state._fsp--;

                    stream_group.add(group9.getTree());

                    }
                    break;

            }


            RPAR10=(Token)match(input,RPAR,FOLLOW_RPAR_in_document214);  
            stream_RPAR.add(RPAR10);


            // AST REWRITE
            // elements: prefix, group, DOCUMENT, importDecl, base
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 72:9: -> ^( DOCUMENT ( base )? ( prefix )* ( importDecl )* ( group )? )
            {
                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:72:12: ^( DOCUMENT ( base )? ( prefix )* ( importDecl )* ( group )? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_DOCUMENT.nextNode()
                , root_1);

                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:72:23: ( base )?
                if ( stream_base.hasNext() ) {
                    adaptor.addChild(root_1, stream_base.nextTree());

                }
                stream_base.reset();

                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:72:29: ( prefix )*
                while ( stream_prefix.hasNext() ) {
                    adaptor.addChild(root_1, stream_prefix.nextTree());

                }
                stream_prefix.reset();

                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:72:37: ( importDecl )*
                while ( stream_importDecl.hasNext() ) {
                    adaptor.addChild(root_1, stream_importDecl.nextTree());

                }
                stream_importDecl.reset();

                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:72:49: ( group )?
                if ( stream_group.hasNext() ) {
                    adaptor.addChild(root_1, stream_group.nextTree());

                }
                stream_group.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "document"


    public static class base_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "base"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:75:1: base : BASE LPAR IRI_REF RPAR -> ^( BASE IRI_REF ) ;
    public final RuleMLPresentationSyntaxParser.base_return base() throws RecognitionException {
        RuleMLPresentationSyntaxParser.base_return retval = new RuleMLPresentationSyntaxParser.base_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token BASE11=null;
        Token LPAR12=null;
        Token IRI_REF13=null;
        Token RPAR14=null;

        CommonTree BASE11_tree=null;
        CommonTree LPAR12_tree=null;
        CommonTree IRI_REF13_tree=null;
        CommonTree RPAR14_tree=null;
        RewriteRuleTokenStream stream_BASE=new RewriteRuleTokenStream(adaptor,"token BASE");
        RewriteRuleTokenStream stream_IRI_REF=new RewriteRuleTokenStream(adaptor,"token IRI_REF");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");

        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:76:5: ( BASE LPAR IRI_REF RPAR -> ^( BASE IRI_REF ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:76:9: BASE LPAR IRI_REF RPAR
            {
            BASE11=(Token)match(input,BASE,FOLLOW_BASE_in_base259);  
            stream_BASE.add(BASE11);


            LPAR12=(Token)match(input,LPAR,FOLLOW_LPAR_in_base261);  
            stream_LPAR.add(LPAR12);


            IRI_REF13=(Token)match(input,IRI_REF,FOLLOW_IRI_REF_in_base263);  
            stream_IRI_REF.add(IRI_REF13);


            RPAR14=(Token)match(input,RPAR,FOLLOW_RPAR_in_base265);  
            stream_RPAR.add(RPAR14);


            // AST REWRITE
            // elements: IRI_REF, BASE
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 76:32: -> ^( BASE IRI_REF )
            {
                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:76:35: ^( BASE IRI_REF )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_BASE.nextNode()
                , root_1);

                adaptor.addChild(root_1, 
                stream_IRI_REF.nextNode()
                );

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "base"


    public static class prefix_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "prefix"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:79:1: prefix : PREFIX LPAR ID IRI_REF RPAR -> ^( PREFIX ID IRI_REF ) ;
    public final RuleMLPresentationSyntaxParser.prefix_return prefix() throws RecognitionException {
        RuleMLPresentationSyntaxParser.prefix_return retval = new RuleMLPresentationSyntaxParser.prefix_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token PREFIX15=null;
        Token LPAR16=null;
        Token ID17=null;
        Token IRI_REF18=null;
        Token RPAR19=null;

        CommonTree PREFIX15_tree=null;
        CommonTree LPAR16_tree=null;
        CommonTree ID17_tree=null;
        CommonTree IRI_REF18_tree=null;
        CommonTree RPAR19_tree=null;
        RewriteRuleTokenStream stream_PREFIX=new RewriteRuleTokenStream(adaptor,"token PREFIX");
        RewriteRuleTokenStream stream_IRI_REF=new RewriteRuleTokenStream(adaptor,"token IRI_REF");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");

        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:80:5: ( PREFIX LPAR ID IRI_REF RPAR -> ^( PREFIX ID IRI_REF ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:80:9: PREFIX LPAR ID IRI_REF RPAR
            {
            PREFIX15=(Token)match(input,PREFIX,FOLLOW_PREFIX_in_prefix292);  
            stream_PREFIX.add(PREFIX15);


            LPAR16=(Token)match(input,LPAR,FOLLOW_LPAR_in_prefix294);  
            stream_LPAR.add(LPAR16);


            ID17=(Token)match(input,ID,FOLLOW_ID_in_prefix296);  
            stream_ID.add(ID17);


            IRI_REF18=(Token)match(input,IRI_REF,FOLLOW_IRI_REF_in_prefix298);  
            stream_IRI_REF.add(IRI_REF18);


            RPAR19=(Token)match(input,RPAR,FOLLOW_RPAR_in_prefix300);  
            stream_RPAR.add(RPAR19);


            // AST REWRITE
            // elements: PREFIX, IRI_REF, ID
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 80:37: -> ^( PREFIX ID IRI_REF )
            {
                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:80:40: ^( PREFIX ID IRI_REF )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_PREFIX.nextNode()
                , root_1);

                adaptor.addChild(root_1, 
                stream_ID.nextNode()
                );

                adaptor.addChild(root_1, 
                stream_IRI_REF.nextNode()
                );

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "prefix"


    public static class importDecl_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "importDecl"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:83:1: importDecl : IMPORT LPAR kb= IRI_REF (pf= IRI_REF )? RPAR -> ^( IMPORT $kb ( $pf)? ) ;
    public final RuleMLPresentationSyntaxParser.importDecl_return importDecl() throws RecognitionException {
        RuleMLPresentationSyntaxParser.importDecl_return retval = new RuleMLPresentationSyntaxParser.importDecl_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token kb=null;
        Token pf=null;
        Token IMPORT20=null;
        Token LPAR21=null;
        Token RPAR22=null;

        CommonTree kb_tree=null;
        CommonTree pf_tree=null;
        CommonTree IMPORT20_tree=null;
        CommonTree LPAR21_tree=null;
        CommonTree RPAR22_tree=null;
        RewriteRuleTokenStream stream_IRI_REF=new RewriteRuleTokenStream(adaptor,"token IRI_REF");
        RewriteRuleTokenStream stream_IMPORT=new RewriteRuleTokenStream(adaptor,"token IMPORT");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");

        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:84:5: ( IMPORT LPAR kb= IRI_REF (pf= IRI_REF )? RPAR -> ^( IMPORT $kb ( $pf)? ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:84:9: IMPORT LPAR kb= IRI_REF (pf= IRI_REF )? RPAR
            {
            IMPORT20=(Token)match(input,IMPORT,FOLLOW_IMPORT_in_importDecl329);  
            stream_IMPORT.add(IMPORT20);


            LPAR21=(Token)match(input,LPAR,FOLLOW_LPAR_in_importDecl331);  
            stream_LPAR.add(LPAR21);


            kb=(Token)match(input,IRI_REF,FOLLOW_IRI_REF_in_importDecl335);  
            stream_IRI_REF.add(kb);


            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:84:32: (pf= IRI_REF )?
            int alt7=2;
            switch ( input.LA(1) ) {
                case IRI_REF:
                    {
                    alt7=1;
                    }
                    break;
            }

            switch (alt7) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:84:33: pf= IRI_REF
                    {
                    pf=(Token)match(input,IRI_REF,FOLLOW_IRI_REF_in_importDecl340);  
                    stream_IRI_REF.add(pf);


                    }
                    break;

            }


            RPAR22=(Token)match(input,RPAR,FOLLOW_RPAR_in_importDecl344);  
            stream_RPAR.add(RPAR22);


            // AST REWRITE
            // elements: kb, pf, IMPORT
            // token labels: pf, kb
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleTokenStream stream_pf=new RewriteRuleTokenStream(adaptor,"token pf",pf);
            RewriteRuleTokenStream stream_kb=new RewriteRuleTokenStream(adaptor,"token kb",kb);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 84:51: -> ^( IMPORT $kb ( $pf)? )
            {
                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:84:54: ^( IMPORT $kb ( $pf)? )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_IMPORT.nextNode()
                , root_1);

                adaptor.addChild(root_1, stream_kb.nextNode());

                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:84:68: ( $pf)?
                if ( stream_pf.hasNext() ) {
                    adaptor.addChild(root_1, stream_pf.nextNode());

                }
                stream_pf.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "importDecl"


    public static class group_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "group"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:87:1: group : GROUP LPAR ( group_element )* RPAR -> ^( GROUP ( group_element )* ) ;
    public final RuleMLPresentationSyntaxParser.group_return group() throws RecognitionException {
        RuleMLPresentationSyntaxParser.group_return retval = new RuleMLPresentationSyntaxParser.group_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token GROUP23=null;
        Token LPAR24=null;
        Token RPAR26=null;
        RuleMLPresentationSyntaxParser.group_element_return group_element25 =null;


        CommonTree GROUP23_tree=null;
        CommonTree LPAR24_tree=null;
        CommonTree RPAR26_tree=null;
        RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleSubtreeStream stream_group_element=new RewriteRuleSubtreeStream(adaptor,"rule group_element");
        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:88:5: ( GROUP LPAR ( group_element )* RPAR -> ^( GROUP ( group_element )* ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:88:9: GROUP LPAR ( group_element )* RPAR
            {
            GROUP23=(Token)match(input,GROUP,FOLLOW_GROUP_in_group376);  
            stream_GROUP.add(GROUP23);


            LPAR24=(Token)match(input,LPAR,FOLLOW_LPAR_in_group378);  
            stream_LPAR.add(LPAR24);


            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:88:20: ( group_element )*
            loop8:
            do {
                int alt8=2;
                switch ( input.LA(1) ) {
                case AND:
                case CURIE:
                case EXISTS:
                case EXTERNAL:
                case FORALL:
                case GROUP:
                case ID:
                case IRI_REF:
                case NUMBER:
                case OR:
                case STRING:
                case VAR_ID:
                    {
                    alt8=1;
                    }
                    break;

                }

                switch (alt8) {
            	case 1 :
            	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:88:20: group_element
            	    {
            	    pushFollow(FOLLOW_group_element_in_group380);
            	    group_element25=group_element();

            	    state._fsp--;

            	    stream_group_element.add(group_element25.getTree());

            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);


            RPAR26=(Token)match(input,RPAR,FOLLOW_RPAR_in_group383);  
            stream_RPAR.add(RPAR26);


            // AST REWRITE
            // elements: group_element, GROUP
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 88:40: -> ^( GROUP ( group_element )* )
            {
                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:88:43: ^( GROUP ( group_element )* )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_GROUP.nextNode()
                , root_1);

                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:88:51: ( group_element )*
                while ( stream_group_element.hasNext() ) {
                    adaptor.addChild(root_1, stream_group_element.nextTree());

                }
                stream_group_element.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "group"


    public static class group_element_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "group_element"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:91:1: group_element : ( rule | group );
    public final RuleMLPresentationSyntaxParser.group_element_return group_element() throws RecognitionException {
        RuleMLPresentationSyntaxParser.group_element_return retval = new RuleMLPresentationSyntaxParser.group_element_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        RuleMLPresentationSyntaxParser.rule_return rule27 =null;

        RuleMLPresentationSyntaxParser.group_return group28 =null;



        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:92:5: ( rule | group )
            int alt9=2;
            switch ( input.LA(1) ) {
            case AND:
            case CURIE:
            case EXISTS:
            case EXTERNAL:
            case FORALL:
            case ID:
            case IRI_REF:
            case NUMBER:
            case OR:
            case STRING:
            case VAR_ID:
                {
                alt9=1;
                }
                break;
            case GROUP:
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
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:92:9: rule
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_rule_in_group_element411);
                    rule27=rule();

                    state._fsp--;

                    adaptor.addChild(root_0, rule27.getTree());

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:93:9: group
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_group_in_group_element421);
                    group28=group();

                    state._fsp--;

                    adaptor.addChild(root_0, group28.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "group_element"


    public static class rule_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "rule"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:96:1: rule : ( FORALL ( VAR_ID )+ LPAR clause RPAR -> ^( FORALL ^( VAR_LIST ( VAR_ID )+ ) clause ) | clause );
    public final RuleMLPresentationSyntaxParser.rule_return rule() throws RecognitionException {
        RuleMLPresentationSyntaxParser.rule_return retval = new RuleMLPresentationSyntaxParser.rule_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token FORALL29=null;
        Token VAR_ID30=null;
        Token LPAR31=null;
        Token RPAR33=null;
        RuleMLPresentationSyntaxParser.clause_return clause32 =null;

        RuleMLPresentationSyntaxParser.clause_return clause34 =null;


        CommonTree FORALL29_tree=null;
        CommonTree VAR_ID30_tree=null;
        CommonTree LPAR31_tree=null;
        CommonTree RPAR33_tree=null;
        RewriteRuleTokenStream stream_FORALL=new RewriteRuleTokenStream(adaptor,"token FORALL");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_VAR_ID=new RewriteRuleTokenStream(adaptor,"token VAR_ID");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleSubtreeStream stream_clause=new RewriteRuleSubtreeStream(adaptor,"rule clause");
        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:97:5: ( FORALL ( VAR_ID )+ LPAR clause RPAR -> ^( FORALL ^( VAR_LIST ( VAR_ID )+ ) clause ) | clause )
            int alt11=2;
            switch ( input.LA(1) ) {
            case FORALL:
                {
                alt11=1;
                }
                break;
            case AND:
            case CURIE:
            case EXISTS:
            case EXTERNAL:
            case ID:
            case IRI_REF:
            case NUMBER:
            case OR:
            case STRING:
            case VAR_ID:
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
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:97:9: FORALL ( VAR_ID )+ LPAR clause RPAR
                    {
                    FORALL29=(Token)match(input,FORALL,FOLLOW_FORALL_in_rule440);  
                    stream_FORALL.add(FORALL29);


                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:97:16: ( VAR_ID )+
                    int cnt10=0;
                    loop10:
                    do {
                        int alt10=2;
                        switch ( input.LA(1) ) {
                        case VAR_ID:
                            {
                            alt10=1;
                            }
                            break;

                        }

                        switch (alt10) {
                    	case 1 :
                    	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:97:16: VAR_ID
                    	    {
                    	    VAR_ID30=(Token)match(input,VAR_ID,FOLLOW_VAR_ID_in_rule442);  
                    	    stream_VAR_ID.add(VAR_ID30);


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt10 >= 1 ) break loop10;
                                EarlyExitException eee =
                                    new EarlyExitException(10, input);
                                throw eee;
                        }
                        cnt10++;
                    } while (true);


                    LPAR31=(Token)match(input,LPAR,FOLLOW_LPAR_in_rule445);  
                    stream_LPAR.add(LPAR31);


                    pushFollow(FOLLOW_clause_in_rule447);
                    clause32=clause();

                    state._fsp--;

                    stream_clause.add(clause32.getTree());

                    RPAR33=(Token)match(input,RPAR,FOLLOW_RPAR_in_rule449);  
                    stream_RPAR.add(RPAR33);


                    // AST REWRITE
                    // elements: clause, FORALL, VAR_ID
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 98:9: -> ^( FORALL ^( VAR_LIST ( VAR_ID )+ ) clause )
                    {
                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:98:12: ^( FORALL ^( VAR_LIST ( VAR_ID )+ ) clause )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        stream_FORALL.nextNode()
                        , root_1);

                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:98:21: ^( VAR_LIST ( VAR_ID )+ )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(VAR_LIST, "VAR_LIST")
                        , root_2);

                        if ( !(stream_VAR_ID.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_VAR_ID.hasNext() ) {
                            adaptor.addChild(root_2, 
                            stream_VAR_ID.nextNode()
                            );

                        }
                        stream_VAR_ID.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_1, stream_clause.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:99:9: clause
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_clause_in_rule482);
                    clause34=clause();

                    state._fsp--;

                    adaptor.addChild(root_0, clause34.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "rule"


    public static class clause_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "clause"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:102:1: clause : f1= formula ( IMPLICATION f2= formula )? -> {isRule}? ^( IMPLICATION $f1 $f2) -> $f1;
    public final RuleMLPresentationSyntaxParser.clause_return clause() throws RecognitionException {
        RuleMLPresentationSyntaxParser.clause_return retval = new RuleMLPresentationSyntaxParser.clause_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token IMPLICATION35=null;
        RuleMLPresentationSyntaxParser.formula_return f1 =null;

        RuleMLPresentationSyntaxParser.formula_return f2 =null;


        CommonTree IMPLICATION35_tree=null;
        RewriteRuleTokenStream stream_IMPLICATION=new RewriteRuleTokenStream(adaptor,"token IMPLICATION");
        RewriteRuleSubtreeStream stream_formula=new RewriteRuleSubtreeStream(adaptor,"rule formula");
         boolean isRule = false; 
        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:116:5: (f1= formula ( IMPLICATION f2= formula )? -> {isRule}? ^( IMPLICATION $f1 $f2) -> $f1)
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:116:9: f1= formula ( IMPLICATION f2= formula )?
            {
            pushFollow(FOLLOW_formula_in_clause517);
            f1=formula();

            state._fsp--;

            stream_formula.add(f1.getTree());

            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:116:20: ( IMPLICATION f2= formula )?
            int alt12=2;
            switch ( input.LA(1) ) {
                case IMPLICATION:
                    {
                    alt12=1;
                    }
                    break;
            }

            switch (alt12) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:116:22: IMPLICATION f2= formula
                    {
                    IMPLICATION35=(Token)match(input,IMPLICATION,FOLLOW_IMPLICATION_in_clause521);  
                    stream_IMPLICATION.add(IMPLICATION35);


                    pushFollow(FOLLOW_formula_in_clause525);
                    f2=formula();

                    state._fsp--;

                    stream_formula.add(f2.getTree());

                     isRule = true; 

                    }
                    break;

            }


            // AST REWRITE
            // elements: f1, IMPLICATION, f2, f1
            // token labels: 
            // rule labels: retval, f1, f2
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_f1=new RewriteRuleSubtreeStream(adaptor,"rule f1",f1!=null?f1.tree:null);
            RewriteRuleSubtreeStream stream_f2=new RewriteRuleSubtreeStream(adaptor,"rule f2",f2!=null?f2.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 117:5: -> {isRule}? ^( IMPLICATION $f1 $f2)
            if (isRule) {
                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:117:18: ^( IMPLICATION $f1 $f2)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_IMPLICATION.nextNode()
                , root_1);

                adaptor.addChild(root_1, stream_f1.nextTree());

                adaptor.addChild(root_1, stream_f2.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            else // 118:5: -> $f1
            {
                adaptor.addChild(root_0, stream_f1.nextTree());

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);


                    if (isRule)
                    {
                        if (!(f1!=null?f1.isValidHead:false))
                            throw new RuntimeException("Unacceptable head formula:" + (f1!=null?input.toString(f1.start,f1.stop):null));
                    }
                    else if (!(f1!=null?f1.isAtomic:false))
                    {
                        throw new RuntimeException("Unacceptable clause:" + input.toString(retval.start,input.LT(-1)));
                    }
                
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "clause"


    public static class formula_return extends ParserRuleReturnScope {
        public boolean isValidHead;
        public boolean isAtomic;
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "formula"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:121:1: formula returns [boolean isValidHead, boolean isAtomic] : ( AND LPAR (f= formula )+ RPAR -> ^( AND ( formula )* ) | OR LPAR ( formula )+ RPAR -> ^( OR ( formula )* ) | EXISTS ( VAR_ID )+ LPAR f= formula RPAR -> ^( EXISTS ^( VAR_LIST ( VAR_ID )+ ) $f) | atomic -> atomic | ( external_term -> external_term ) ( psoa_rest -> ^( PSOA $formula psoa_rest ) )? );
    public final RuleMLPresentationSyntaxParser.formula_return formula() throws RecognitionException {
        RuleMLPresentationSyntaxParser.formula_return retval = new RuleMLPresentationSyntaxParser.formula_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token AND36=null;
        Token LPAR37=null;
        Token RPAR38=null;
        Token OR39=null;
        Token LPAR40=null;
        Token RPAR42=null;
        Token EXISTS43=null;
        Token VAR_ID44=null;
        Token LPAR45=null;
        Token RPAR46=null;
        RuleMLPresentationSyntaxParser.formula_return f =null;

        RuleMLPresentationSyntaxParser.formula_return formula41 =null;

        RuleMLPresentationSyntaxParser.atomic_return atomic47 =null;

        RuleMLPresentationSyntaxParser.external_term_return external_term48 =null;

        RuleMLPresentationSyntaxParser.psoa_rest_return psoa_rest49 =null;


        CommonTree AND36_tree=null;
        CommonTree LPAR37_tree=null;
        CommonTree RPAR38_tree=null;
        CommonTree OR39_tree=null;
        CommonTree LPAR40_tree=null;
        CommonTree RPAR42_tree=null;
        CommonTree EXISTS43_tree=null;
        CommonTree VAR_ID44_tree=null;
        CommonTree LPAR45_tree=null;
        CommonTree RPAR46_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_VAR_ID=new RewriteRuleTokenStream(adaptor,"token VAR_ID");
        RewriteRuleTokenStream stream_EXISTS=new RewriteRuleTokenStream(adaptor,"token EXISTS");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleTokenStream stream_OR=new RewriteRuleTokenStream(adaptor,"token OR");
        RewriteRuleSubtreeStream stream_external_term=new RewriteRuleSubtreeStream(adaptor,"rule external_term");
        RewriteRuleSubtreeStream stream_psoa_rest=new RewriteRuleSubtreeStream(adaptor,"rule psoa_rest");
        RewriteRuleSubtreeStream stream_atomic=new RewriteRuleSubtreeStream(adaptor,"rule atomic");
        RewriteRuleSubtreeStream stream_formula=new RewriteRuleSubtreeStream(adaptor,"rule formula");
         retval.isValidHead = true; retval.isAtomic = false; 
        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:123:5: ( AND LPAR (f= formula )+ RPAR -> ^( AND ( formula )* ) | OR LPAR ( formula )+ RPAR -> ^( OR ( formula )* ) | EXISTS ( VAR_ID )+ LPAR f= formula RPAR -> ^( EXISTS ^( VAR_LIST ( VAR_ID )+ ) $f) | atomic -> atomic | ( external_term -> external_term ) ( psoa_rest -> ^( PSOA $formula psoa_rest ) )? )
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
            case CURIE:
            case ID:
            case IRI_REF:
            case NUMBER:
            case STRING:
            case VAR_ID:
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
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:123:9: AND LPAR (f= formula )+ RPAR
                    {
                    AND36=(Token)match(input,AND,FOLLOW_AND_in_formula595);  
                    stream_AND.add(AND36);


                    LPAR37=(Token)match(input,LPAR,FOLLOW_LPAR_in_formula597);  
                    stream_LPAR.add(LPAR37);


                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:123:18: (f= formula )+
                    int cnt13=0;
                    loop13:
                    do {
                        int alt13=2;
                        switch ( input.LA(1) ) {
                        case AND:
                        case CURIE:
                        case EXISTS:
                        case EXTERNAL:
                        case ID:
                        case IRI_REF:
                        case NUMBER:
                        case OR:
                        case STRING:
                        case VAR_ID:
                            {
                            alt13=1;
                            }
                            break;

                        }

                        switch (alt13) {
                    	case 1 :
                    	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:123:19: f= formula
                    	    {
                    	    pushFollow(FOLLOW_formula_in_formula602);
                    	    f=formula();

                    	    state._fsp--;

                    	    stream_formula.add(f.getTree());

                    	     if(!(f!=null?f.isValidHead:false)) retval.isValidHead = false; 

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


                    RPAR38=(Token)match(input,RPAR,FOLLOW_RPAR_in_formula609);  
                    stream_RPAR.add(RPAR38);


                    // AST REWRITE
                    // elements: AND, formula
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 123:83: -> ^( AND ( formula )* )
                    {
                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:123:86: ^( AND ( formula )* )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        stream_AND.nextNode()
                        , root_1);

                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:123:92: ( formula )*
                        while ( stream_formula.hasNext() ) {
                            adaptor.addChild(root_1, stream_formula.nextTree());

                        }
                        stream_formula.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:124:9: OR LPAR ( formula )+ RPAR
                    {
                    OR39=(Token)match(input,OR,FOLLOW_OR_in_formula628);  
                    stream_OR.add(OR39);


                    LPAR40=(Token)match(input,LPAR,FOLLOW_LPAR_in_formula630);  
                    stream_LPAR.add(LPAR40);


                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:124:17: ( formula )+
                    int cnt14=0;
                    loop14:
                    do {
                        int alt14=2;
                        switch ( input.LA(1) ) {
                        case AND:
                        case CURIE:
                        case EXISTS:
                        case EXTERNAL:
                        case ID:
                        case IRI_REF:
                        case NUMBER:
                        case OR:
                        case STRING:
                        case VAR_ID:
                            {
                            alt14=1;
                            }
                            break;

                        }

                        switch (alt14) {
                    	case 1 :
                    	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:124:17: formula
                    	    {
                    	    pushFollow(FOLLOW_formula_in_formula632);
                    	    formula41=formula();

                    	    state._fsp--;

                    	    stream_formula.add(formula41.getTree());

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


                    RPAR42=(Token)match(input,RPAR,FOLLOW_RPAR_in_formula635);  
                    stream_RPAR.add(RPAR42);


                     retval.isValidHead = false; 

                    // AST REWRITE
                    // elements: formula, OR
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 124:57: -> ^( OR ( formula )* )
                    {
                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:124:60: ^( OR ( formula )* )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        stream_OR.nextNode()
                        , root_1);

                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:124:65: ( formula )*
                        while ( stream_formula.hasNext() ) {
                            adaptor.addChild(root_1, stream_formula.nextTree());

                        }
                        stream_formula.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 3 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:125:9: EXISTS ( VAR_ID )+ LPAR f= formula RPAR
                    {
                    EXISTS43=(Token)match(input,EXISTS,FOLLOW_EXISTS_in_formula656);  
                    stream_EXISTS.add(EXISTS43);


                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:125:16: ( VAR_ID )+
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
                    	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:125:16: VAR_ID
                    	    {
                    	    VAR_ID44=(Token)match(input,VAR_ID,FOLLOW_VAR_ID_in_formula658);  
                    	    stream_VAR_ID.add(VAR_ID44);


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


                    LPAR45=(Token)match(input,LPAR,FOLLOW_LPAR_in_formula661);  
                    stream_LPAR.add(LPAR45);


                    pushFollow(FOLLOW_formula_in_formula665);
                    f=formula();

                    state._fsp--;

                    stream_formula.add(f.getTree());

                    RPAR46=(Token)match(input,RPAR,FOLLOW_RPAR_in_formula667);  
                    stream_RPAR.add(RPAR46);


                     retval.isValidHead = (f!=null?f.isAtomic:false); 

                    // AST REWRITE
                    // elements: f, EXISTS, VAR_ID
                    // token labels: 
                    // rule labels: f, retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_f=new RewriteRuleSubtreeStream(adaptor,"rule f",f!=null?f.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 126:9: -> ^( EXISTS ^( VAR_LIST ( VAR_ID )+ ) $f)
                    {
                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:126:12: ^( EXISTS ^( VAR_LIST ( VAR_ID )+ ) $f)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        stream_EXISTS.nextNode()
                        , root_1);

                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:126:21: ^( VAR_LIST ( VAR_ID )+ )
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(VAR_LIST, "VAR_LIST")
                        , root_2);

                        if ( !(stream_VAR_ID.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_VAR_ID.hasNext() ) {
                            adaptor.addChild(root_2, 
                            stream_VAR_ID.nextNode()
                            );

                        }
                        stream_VAR_ID.reset();

                        adaptor.addChild(root_1, root_2);
                        }

                        adaptor.addChild(root_1, stream_f.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 4 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:127:9: atomic
                    {
                    pushFollow(FOLLOW_atomic_in_formula703);
                    atomic47=atomic();

                    state._fsp--;

                    stream_atomic.add(atomic47.getTree());

                     retval.isAtomic = true; 

                    // AST REWRITE
                    // elements: atomic
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 127:38: -> atomic
                    {
                        adaptor.addChild(root_0, stream_atomic.nextTree());

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 5 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:128:9: ( external_term -> external_term ) ( psoa_rest -> ^( PSOA $formula psoa_rest ) )?
                    {
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:128:9: ( external_term -> external_term )
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:128:10: external_term
                    {
                    pushFollow(FOLLOW_external_term_in_formula720);
                    external_term48=external_term();

                    state._fsp--;

                    stream_external_term.add(external_term48.getTree());

                     retval.isValidHead = false; 

                    // AST REWRITE
                    // elements: external_term
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 128:50: -> external_term
                    {
                        adaptor.addChild(root_0, stream_external_term.nextTree());

                    }


                    retval.tree = root_0;

                    }


                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:129:9: ( psoa_rest -> ^( PSOA $formula psoa_rest ) )?
                    int alt16=2;
                    switch ( input.LA(1) ) {
                        case INSTANCE:
                            {
                            alt16=1;
                            }
                            break;
                    }

                    switch (alt16) {
                        case 1 :
                            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:129:10: psoa_rest
                            {
                            pushFollow(FOLLOW_psoa_rest_in_formula738);
                            psoa_rest49=psoa_rest();

                            state._fsp--;

                            stream_psoa_rest.add(psoa_rest49.getTree());

                             retval.isAtomic = true; 

                            // AST REWRITE
                            // elements: formula, psoa_rest
                            // token labels: 
                            // rule labels: retval
                            // token list labels: 
                            // rule list labels: 
                            // wildcard labels: 
                            retval.tree = root_0;
                            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                            root_0 = (CommonTree)adaptor.nil();
                            // 129:42: -> ^( PSOA $formula psoa_rest )
                            {
                                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:129:45: ^( PSOA $formula psoa_rest )
                                {
                                CommonTree root_1 = (CommonTree)adaptor.nil();
                                root_1 = (CommonTree)adaptor.becomeRoot(
                                (CommonTree)adaptor.create(PSOA, "PSOA")
                                , root_1);

                                adaptor.addChild(root_1, stream_retval.nextTree());

                                adaptor.addChild(root_1, stream_psoa_rest.nextTree());

                                adaptor.addChild(root_0, root_1);
                                }

                            }


                            retval.tree = root_0;

                            }
                            break;

                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "formula"


    public static class atomic_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "atomic"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:132:1: atomic : non_ex_term= internal_term ( ( EQUAL | SUBCLASS ) ^ term )? ;
    public final RuleMLPresentationSyntaxParser.atomic_return atomic() throws RecognitionException {
        RuleMLPresentationSyntaxParser.atomic_return retval = new RuleMLPresentationSyntaxParser.atomic_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token set50=null;
        RuleMLPresentationSyntaxParser.internal_term_return non_ex_term =null;

        RuleMLPresentationSyntaxParser.term_return term51 =null;


        CommonTree set50_tree=null;

        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:138:5: (non_ex_term= internal_term ( ( EQUAL | SUBCLASS ) ^ term )? )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:138:9: non_ex_term= internal_term ( ( EQUAL | SUBCLASS ) ^ term )?
            {
            root_0 = (CommonTree)adaptor.nil();


            pushFollow(FOLLOW_internal_term_in_atomic783);
            non_ex_term=internal_term();

            state._fsp--;

            adaptor.addChild(root_0, non_ex_term.getTree());

            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:138:35: ( ( EQUAL | SUBCLASS ) ^ term )?
            int alt18=2;
            switch ( input.LA(1) ) {
                case EQUAL:
                case SUBCLASS:
                    {
                    alt18=1;
                    }
                    break;
            }

            switch (alt18) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:138:36: ( EQUAL | SUBCLASS ) ^ term
                    {
                    set50=(Token)input.LT(1);

                    set50=(Token)input.LT(1);

                    if ( input.LA(1)==EQUAL||input.LA(1)==SUBCLASS ) {
                        input.consume();
                        root_0 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(set50)
                        , root_0);
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    pushFollow(FOLLOW_term_in_atomic795);
                    term51=term();

                    state._fsp--;

                    adaptor.addChild(root_0, term51.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);


                if (((CommonTree)retval.tree).getChildCount() == 1 && (non_ex_term!=null?non_ex_term.isSimple:false))
                    throw new RuntimeException("Simple term cannot be an atomic formula:" + (non_ex_term!=null?input.toString(non_ex_term.start,non_ex_term.stop):null));

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "atomic"


    public static class term_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "term"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:141:1: term : ( internal_term -> internal_term | external_term -> external_term );
    public final RuleMLPresentationSyntaxParser.term_return term() throws RecognitionException {
        RuleMLPresentationSyntaxParser.term_return retval = new RuleMLPresentationSyntaxParser.term_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        RuleMLPresentationSyntaxParser.internal_term_return internal_term52 =null;

        RuleMLPresentationSyntaxParser.external_term_return external_term53 =null;


        RewriteRuleSubtreeStream stream_internal_term=new RewriteRuleSubtreeStream(adaptor,"rule internal_term");
        RewriteRuleSubtreeStream stream_external_term=new RewriteRuleSubtreeStream(adaptor,"rule external_term");
        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:142:5: ( internal_term -> internal_term | external_term -> external_term )
            int alt19=2;
            switch ( input.LA(1) ) {
            case CURIE:
            case ID:
            case IRI_REF:
            case NUMBER:
            case STRING:
            case VAR_ID:
                {
                alt19=1;
                }
                break;
            case EXTERNAL:
                {
                alt19=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;

            }

            switch (alt19) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:142:9: internal_term
                    {
                    pushFollow(FOLLOW_internal_term_in_term816);
                    internal_term52=internal_term();

                    state._fsp--;

                    stream_internal_term.add(internal_term52.getTree());

                    // AST REWRITE
                    // elements: internal_term
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 142:23: -> internal_term
                    {
                        adaptor.addChild(root_0, stream_internal_term.nextTree());

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:143:9: external_term
                    {
                    pushFollow(FOLLOW_external_term_in_term830);
                    external_term53=external_term();

                    state._fsp--;

                    stream_external_term.add(external_term53.getTree());

                    // AST REWRITE
                    // elements: external_term
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 143:23: -> external_term
                    {
                        adaptor.addChild(root_0, stream_external_term.nextTree());

                    }


                    retval.tree = root_0;

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "term"


    public static class simple_term_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "simple_term"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:146:1: simple_term : ( constant | VAR_ID );
    public final RuleMLPresentationSyntaxParser.simple_term_return simple_term() throws RecognitionException {
        RuleMLPresentationSyntaxParser.simple_term_return retval = new RuleMLPresentationSyntaxParser.simple_term_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token VAR_ID55=null;
        RuleMLPresentationSyntaxParser.constant_return constant54 =null;


        CommonTree VAR_ID55_tree=null;

        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:147:5: ( constant | VAR_ID )
            int alt20=2;
            switch ( input.LA(1) ) {
            case CURIE:
            case ID:
            case IRI_REF:
            case NUMBER:
            case STRING:
                {
                alt20=1;
                }
                break;
            case VAR_ID:
                {
                alt20=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;

            }

            switch (alt20) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:147:9: constant
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    pushFollow(FOLLOW_constant_in_simple_term853);
                    constant54=constant();

                    state._fsp--;

                    adaptor.addChild(root_0, constant54.getTree());

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:148:9: VAR_ID
                    {
                    root_0 = (CommonTree)adaptor.nil();


                    VAR_ID55=(Token)match(input,VAR_ID,FOLLOW_VAR_ID_in_simple_term863); 
                    VAR_ID55_tree = 
                    (CommonTree)adaptor.create(VAR_ID55)
                    ;
                    adaptor.addChild(root_0, VAR_ID55_tree);


                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "simple_term"


    public static class external_term_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "external_term"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:151:1: external_term : EXTERNAL LPAR simple_term LPAR ( term )* RPAR RPAR -> ^( EXTERNAL ^( PSOA ^( INSTANCE simple_term ) ^( TUPLE ( term )* ) ) ) ;
    public final RuleMLPresentationSyntaxParser.external_term_return external_term() throws RecognitionException {
        RuleMLPresentationSyntaxParser.external_term_return retval = new RuleMLPresentationSyntaxParser.external_term_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token EXTERNAL56=null;
        Token LPAR57=null;
        Token LPAR59=null;
        Token RPAR61=null;
        Token RPAR62=null;
        RuleMLPresentationSyntaxParser.simple_term_return simple_term58 =null;

        RuleMLPresentationSyntaxParser.term_return term60 =null;


        CommonTree EXTERNAL56_tree=null;
        CommonTree LPAR57_tree=null;
        CommonTree LPAR59_tree=null;
        CommonTree RPAR61_tree=null;
        CommonTree RPAR62_tree=null;
        RewriteRuleTokenStream stream_EXTERNAL=new RewriteRuleTokenStream(adaptor,"token EXTERNAL");
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        RewriteRuleSubtreeStream stream_simple_term=new RewriteRuleSubtreeStream(adaptor,"rule simple_term");
        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:152:5: ( EXTERNAL LPAR simple_term LPAR ( term )* RPAR RPAR -> ^( EXTERNAL ^( PSOA ^( INSTANCE simple_term ) ^( TUPLE ( term )* ) ) ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:152:9: EXTERNAL LPAR simple_term LPAR ( term )* RPAR RPAR
            {
            EXTERNAL56=(Token)match(input,EXTERNAL,FOLLOW_EXTERNAL_in_external_term882);  
            stream_EXTERNAL.add(EXTERNAL56);


            LPAR57=(Token)match(input,LPAR,FOLLOW_LPAR_in_external_term884);  
            stream_LPAR.add(LPAR57);


            pushFollow(FOLLOW_simple_term_in_external_term886);
            simple_term58=simple_term();

            state._fsp--;

            stream_simple_term.add(simple_term58.getTree());

            LPAR59=(Token)match(input,LPAR,FOLLOW_LPAR_in_external_term888);  
            stream_LPAR.add(LPAR59);


            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:152:40: ( term )*
            loop21:
            do {
                int alt21=2;
                switch ( input.LA(1) ) {
                case CURIE:
                case EXTERNAL:
                case ID:
                case IRI_REF:
                case NUMBER:
                case STRING:
                case VAR_ID:
                    {
                    alt21=1;
                    }
                    break;

                }

                switch (alt21) {
            	case 1 :
            	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:152:40: term
            	    {
            	    pushFollow(FOLLOW_term_in_external_term890);
            	    term60=term();

            	    state._fsp--;

            	    stream_term.add(term60.getTree());

            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);


            RPAR61=(Token)match(input,RPAR,FOLLOW_RPAR_in_external_term893);  
            stream_RPAR.add(RPAR61);


            RPAR62=(Token)match(input,RPAR,FOLLOW_RPAR_in_external_term895);  
            stream_RPAR.add(RPAR62);


            // AST REWRITE
            // elements: term, simple_term, EXTERNAL
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 153:5: -> ^( EXTERNAL ^( PSOA ^( INSTANCE simple_term ) ^( TUPLE ( term )* ) ) )
            {
                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:153:8: ^( EXTERNAL ^( PSOA ^( INSTANCE simple_term ) ^( TUPLE ( term )* ) ) )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_EXTERNAL.nextNode()
                , root_1);

                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:153:19: ^( PSOA ^( INSTANCE simple_term ) ^( TUPLE ( term )* ) )
                {
                CommonTree root_2 = (CommonTree)adaptor.nil();
                root_2 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(PSOA, "PSOA")
                , root_2);

                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:153:26: ^( INSTANCE simple_term )
                {
                CommonTree root_3 = (CommonTree)adaptor.nil();
                root_3 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(INSTANCE, "INSTANCE")
                , root_3);

                adaptor.addChild(root_3, stream_simple_term.nextTree());

                adaptor.addChild(root_2, root_3);
                }

                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:153:50: ^( TUPLE ( term )* )
                {
                CommonTree root_3 = (CommonTree)adaptor.nil();
                root_3 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TUPLE, "TUPLE")
                , root_3);

                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:153:58: ( term )*
                while ( stream_term.hasNext() ) {
                    adaptor.addChild(root_3, stream_term.nextTree());

                }
                stream_term.reset();

                adaptor.addChild(root_2, root_3);
                }

                adaptor.addChild(root_1, root_2);
                }

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "external_term"


    public static class internal_term_return extends ParserRuleReturnScope {
        public boolean isSimple;
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "internal_term"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:156:1: internal_term returns [boolean isSimple] : ( simple_term -> simple_term ) ( LPAR ( tuples_and_slots )? RPAR -> ^( PSOA ^( INSTANCE $internal_term) ( tuples_and_slots )? ) )? ( psoa_rest -> ^( PSOA $internal_term psoa_rest ) )* ;
    public final RuleMLPresentationSyntaxParser.internal_term_return internal_term() throws RecognitionException {
        RuleMLPresentationSyntaxParser.internal_term_return retval = new RuleMLPresentationSyntaxParser.internal_term_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token LPAR64=null;
        Token RPAR66=null;
        RuleMLPresentationSyntaxParser.simple_term_return simple_term63 =null;

        RuleMLPresentationSyntaxParser.tuples_and_slots_return tuples_and_slots65 =null;

        RuleMLPresentationSyntaxParser.psoa_rest_return psoa_rest67 =null;


        CommonTree LPAR64_tree=null;
        CommonTree RPAR66_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleSubtreeStream stream_tuples_and_slots=new RewriteRuleSubtreeStream(adaptor,"rule tuples_and_slots");
        RewriteRuleSubtreeStream stream_simple_term=new RewriteRuleSubtreeStream(adaptor,"rule simple_term");
        RewriteRuleSubtreeStream stream_psoa_rest=new RewriteRuleSubtreeStream(adaptor,"rule psoa_rest");
         retval.isSimple = true; 
        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:158:5: ( ( simple_term -> simple_term ) ( LPAR ( tuples_and_slots )? RPAR -> ^( PSOA ^( INSTANCE $internal_term) ( tuples_and_slots )? ) )? ( psoa_rest -> ^( PSOA $internal_term psoa_rest ) )* )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:158:9: ( simple_term -> simple_term ) ( LPAR ( tuples_and_slots )? RPAR -> ^( PSOA ^( INSTANCE $internal_term) ( tuples_and_slots )? ) )? ( psoa_rest -> ^( PSOA $internal_term psoa_rest ) )*
            {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:158:9: ( simple_term -> simple_term )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:158:10: simple_term
            {
            pushFollow(FOLLOW_simple_term_in_internal_term951);
            simple_term63=simple_term();

            state._fsp--;

            stream_simple_term.add(simple_term63.getTree());

            // AST REWRITE
            // elements: simple_term
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 158:22: -> simple_term
            {
                adaptor.addChild(root_0, stream_simple_term.nextTree());

            }


            retval.tree = root_0;

            }


            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:159:9: ( LPAR ( tuples_and_slots )? RPAR -> ^( PSOA ^( INSTANCE $internal_term) ( tuples_and_slots )? ) )?
            int alt23=2;
            switch ( input.LA(1) ) {
                case LPAR:
                    {
                    alt23=1;
                    }
                    break;
            }

            switch (alt23) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:159:10: LPAR ( tuples_and_slots )? RPAR
                    {
                    LPAR64=(Token)match(input,LPAR,FOLLOW_LPAR_in_internal_term967);  
                    stream_LPAR.add(LPAR64);


                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:159:15: ( tuples_and_slots )?
                    int alt22=2;
                    switch ( input.LA(1) ) {
                        case CURIE:
                        case EXTERNAL:
                        case ID:
                        case IRI_REF:
                        case LSQBR:
                        case NUMBER:
                        case STRING:
                        case VAR_ID:
                            {
                            alt22=1;
                            }
                            break;
                    }

                    switch (alt22) {
                        case 1 :
                            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:159:15: tuples_and_slots
                            {
                            pushFollow(FOLLOW_tuples_and_slots_in_internal_term969);
                            tuples_and_slots65=tuples_and_slots();

                            state._fsp--;

                            stream_tuples_and_slots.add(tuples_and_slots65.getTree());

                            }
                            break;

                    }


                    RPAR66=(Token)match(input,RPAR,FOLLOW_RPAR_in_internal_term972);  
                    stream_RPAR.add(RPAR66);


                     retval.isSimple = false; 

                    // AST REWRITE
                    // elements: internal_term, tuples_and_slots
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 160:10: -> ^( PSOA ^( INSTANCE $internal_term) ( tuples_and_slots )? )
                    {
                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:160:13: ^( PSOA ^( INSTANCE $internal_term) ( tuples_and_slots )? )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(PSOA, "PSOA")
                        , root_1);

                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:160:20: ^( INSTANCE $internal_term)
                        {
                        CommonTree root_2 = (CommonTree)adaptor.nil();
                        root_2 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(INSTANCE, "INSTANCE")
                        , root_2);

                        adaptor.addChild(root_2, stream_retval.nextTree());

                        adaptor.addChild(root_1, root_2);
                        }

                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:160:47: ( tuples_and_slots )?
                        if ( stream_tuples_and_slots.hasNext() ) {
                            adaptor.addChild(root_1, stream_tuples_and_slots.nextTree());

                        }
                        stream_tuples_and_slots.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;

            }


            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:161:9: ( psoa_rest -> ^( PSOA $internal_term psoa_rest ) )*
            loop24:
            do {
                int alt24=2;
                switch ( input.LA(1) ) {
                case INSTANCE:
                    {
                    alt24=1;
                    }
                    break;

                }

                switch (alt24) {
            	case 1 :
            	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:161:10: psoa_rest
            	    {
            	    pushFollow(FOLLOW_psoa_rest_in_internal_term1012);
            	    psoa_rest67=psoa_rest();

            	    state._fsp--;

            	    stream_psoa_rest.add(psoa_rest67.getTree());

            	     retval.isSimple = false; 

            	    // AST REWRITE
            	    // elements: psoa_rest, internal_term
            	    // token labels: 
            	    // rule labels: retval
            	    // token list labels: 
            	    // rule list labels: 
            	    // wildcard labels: 
            	    retval.tree = root_0;
            	    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            	    root_0 = (CommonTree)adaptor.nil();
            	    // 161:43: -> ^( PSOA $internal_term psoa_rest )
            	    {
            	        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:161:46: ^( PSOA $internal_term psoa_rest )
            	        {
            	        CommonTree root_1 = (CommonTree)adaptor.nil();
            	        root_1 = (CommonTree)adaptor.becomeRoot(
            	        (CommonTree)adaptor.create(PSOA, "PSOA")
            	        , root_1);

            	        adaptor.addChild(root_1, stream_retval.nextTree());

            	        adaptor.addChild(root_1, stream_psoa_rest.nextTree());

            	        adaptor.addChild(root_0, root_1);
            	        }

            	    }


            	    retval.tree = root_0;

            	    }
            	    break;

            	default :
            	    break loop24;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "internal_term"


    public static class psoa_rest_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "psoa_rest"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:164:1: psoa_rest : INSTANCE simple_term ( LPAR ( tuples_and_slots )? RPAR )? -> ^( INSTANCE simple_term ) ( tuples_and_slots )? ;
    public final RuleMLPresentationSyntaxParser.psoa_rest_return psoa_rest() throws RecognitionException {
        RuleMLPresentationSyntaxParser.psoa_rest_return retval = new RuleMLPresentationSyntaxParser.psoa_rest_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token INSTANCE68=null;
        Token LPAR70=null;
        Token RPAR72=null;
        RuleMLPresentationSyntaxParser.simple_term_return simple_term69 =null;

        RuleMLPresentationSyntaxParser.tuples_and_slots_return tuples_and_slots71 =null;


        CommonTree INSTANCE68_tree=null;
        CommonTree LPAR70_tree=null;
        CommonTree RPAR72_tree=null;
        RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
        RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
        RewriteRuleTokenStream stream_INSTANCE=new RewriteRuleTokenStream(adaptor,"token INSTANCE");
        RewriteRuleSubtreeStream stream_tuples_and_slots=new RewriteRuleSubtreeStream(adaptor,"rule tuples_and_slots");
        RewriteRuleSubtreeStream stream_simple_term=new RewriteRuleSubtreeStream(adaptor,"rule simple_term");
        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:165:5: ( INSTANCE simple_term ( LPAR ( tuples_and_slots )? RPAR )? -> ^( INSTANCE simple_term ) ( tuples_and_slots )? )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:165:9: INSTANCE simple_term ( LPAR ( tuples_and_slots )? RPAR )?
            {
            INSTANCE68=(Token)match(input,INSTANCE,FOLLOW_INSTANCE_in_psoa_rest1046);  
            stream_INSTANCE.add(INSTANCE68);


            pushFollow(FOLLOW_simple_term_in_psoa_rest1048);
            simple_term69=simple_term();

            state._fsp--;

            stream_simple_term.add(simple_term69.getTree());

            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:165:30: ( LPAR ( tuples_and_slots )? RPAR )?
            int alt26=2;
            switch ( input.LA(1) ) {
                case LPAR:
                    {
                    alt26=1;
                    }
                    break;
            }

            switch (alt26) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:165:31: LPAR ( tuples_and_slots )? RPAR
                    {
                    LPAR70=(Token)match(input,LPAR,FOLLOW_LPAR_in_psoa_rest1051);  
                    stream_LPAR.add(LPAR70);


                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:165:36: ( tuples_and_slots )?
                    int alt25=2;
                    switch ( input.LA(1) ) {
                        case CURIE:
                        case EXTERNAL:
                        case ID:
                        case IRI_REF:
                        case LSQBR:
                        case NUMBER:
                        case STRING:
                        case VAR_ID:
                            {
                            alt25=1;
                            }
                            break;
                    }

                    switch (alt25) {
                        case 1 :
                            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:165:36: tuples_and_slots
                            {
                            pushFollow(FOLLOW_tuples_and_slots_in_psoa_rest1053);
                            tuples_and_slots71=tuples_and_slots();

                            state._fsp--;

                            stream_tuples_and_slots.add(tuples_and_slots71.getTree());

                            }
                            break;

                    }


                    RPAR72=(Token)match(input,RPAR,FOLLOW_RPAR_in_psoa_rest1056);  
                    stream_RPAR.add(RPAR72);


                    }
                    break;

            }


            // AST REWRITE
            // elements: tuples_and_slots, INSTANCE, simple_term
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 166:5: -> ^( INSTANCE simple_term ) ( tuples_and_slots )?
            {
                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:166:8: ^( INSTANCE simple_term )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                stream_INSTANCE.nextNode()
                , root_1);

                adaptor.addChild(root_1, stream_simple_term.nextTree());

                adaptor.addChild(root_0, root_1);
                }

                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:166:32: ( tuples_and_slots )?
                if ( stream_tuples_and_slots.hasNext() ) {
                    adaptor.addChild(root_0, stream_tuples_and_slots.nextTree());

                }
                stream_tuples_and_slots.reset();

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "psoa_rest"


    public static class tuples_and_slots_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "tuples_and_slots"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:169:1: tuples_and_slots : ( ( tuple )+ ( slot )* -> ( tuple )+ ( slot )* | (terms+= term )+ ( SLOT_ARROW first_slot_value= term ( slot )* )? -> {!hasSlot}? ^( TUPLE ) -> {$terms.size() == 1}? ^( SLOT $first_slot_value) ( slot )* -> ^( TUPLE ) ^( SLOT $first_slot_value) ( slot )* );
    public final RuleMLPresentationSyntaxParser.tuples_and_slots_return tuples_and_slots() throws RecognitionException {
        RuleMLPresentationSyntaxParser.tuples_and_slots_return retval = new RuleMLPresentationSyntaxParser.tuples_and_slots_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token SLOT_ARROW75=null;
        List list_terms=null;
        RuleMLPresentationSyntaxParser.term_return first_slot_value =null;

        RuleMLPresentationSyntaxParser.tuple_return tuple73 =null;

        RuleMLPresentationSyntaxParser.slot_return slot74 =null;

        RuleMLPresentationSyntaxParser.slot_return slot76 =null;

        RuleReturnScope terms = null;
        CommonTree SLOT_ARROW75_tree=null;
        RewriteRuleTokenStream stream_SLOT_ARROW=new RewriteRuleTokenStream(adaptor,"token SLOT_ARROW");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        RewriteRuleSubtreeStream stream_tuple=new RewriteRuleSubtreeStream(adaptor,"rule tuple");
        RewriteRuleSubtreeStream stream_slot=new RewriteRuleSubtreeStream(adaptor,"rule slot");
        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:170:5: ( ( tuple )+ ( slot )* -> ( tuple )+ ( slot )* | (terms+= term )+ ( SLOT_ARROW first_slot_value= term ( slot )* )? -> {!hasSlot}? ^( TUPLE ) -> {$terms.size() == 1}? ^( SLOT $first_slot_value) ( slot )* -> ^( TUPLE ) ^( SLOT $first_slot_value) ( slot )* )
            int alt32=2;
            switch ( input.LA(1) ) {
            case LSQBR:
                {
                alt32=1;
                }
                break;
            case CURIE:
            case EXTERNAL:
            case ID:
            case IRI_REF:
            case NUMBER:
            case STRING:
            case VAR_ID:
                {
                alt32=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;

            }

            switch (alt32) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:170:9: ( tuple )+ ( slot )*
                    {
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:170:9: ( tuple )+
                    int cnt27=0;
                    loop27:
                    do {
                        int alt27=2;
                        switch ( input.LA(1) ) {
                        case LSQBR:
                            {
                            alt27=1;
                            }
                            break;

                        }

                        switch (alt27) {
                    	case 1 :
                    	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:170:9: tuple
                    	    {
                    	    pushFollow(FOLLOW_tuple_in_tuples_and_slots1092);
                    	    tuple73=tuple();

                    	    state._fsp--;

                    	    stream_tuple.add(tuple73.getTree());

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt27 >= 1 ) break loop27;
                                EarlyExitException eee =
                                    new EarlyExitException(27, input);
                                throw eee;
                        }
                        cnt27++;
                    } while (true);


                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:170:16: ( slot )*
                    loop28:
                    do {
                        int alt28=2;
                        switch ( input.LA(1) ) {
                        case CURIE:
                        case EXTERNAL:
                        case ID:
                        case IRI_REF:
                        case NUMBER:
                        case STRING:
                        case VAR_ID:
                            {
                            alt28=1;
                            }
                            break;

                        }

                        switch (alt28) {
                    	case 1 :
                    	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:170:16: slot
                    	    {
                    	    pushFollow(FOLLOW_slot_in_tuples_and_slots1095);
                    	    slot74=slot();

                    	    state._fsp--;

                    	    stream_slot.add(slot74.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop28;
                        }
                    } while (true);


                    // AST REWRITE
                    // elements: slot, tuple
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 170:22: -> ( tuple )+ ( slot )*
                    {
                        if ( !(stream_tuple.hasNext()) ) {
                            throw new RewriteEarlyExitException();
                        }
                        while ( stream_tuple.hasNext() ) {
                            adaptor.addChild(root_0, stream_tuple.nextTree());

                        }
                        stream_tuple.reset();

                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:170:32: ( slot )*
                        while ( stream_slot.hasNext() ) {
                            adaptor.addChild(root_0, stream_slot.nextTree());

                        }
                        stream_slot.reset();

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:171:9: (terms+= term )+ ( SLOT_ARROW first_slot_value= term ( slot )* )?
                    {
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:171:14: (terms+= term )+
                    int cnt29=0;
                    loop29:
                    do {
                        int alt29=2;
                        switch ( input.LA(1) ) {
                        case CURIE:
                        case EXTERNAL:
                        case ID:
                        case IRI_REF:
                        case NUMBER:
                        case STRING:
                        case VAR_ID:
                            {
                            alt29=1;
                            }
                            break;

                        }

                        switch (alt29) {
                    	case 1 :
                    	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:171:14: terms+= term
                    	    {
                    	    pushFollow(FOLLOW_term_in_tuples_and_slots1116);
                    	    terms=term();

                    	    state._fsp--;

                    	    stream_term.add(terms.getTree());
                    	    if (list_terms==null) list_terms=new ArrayList();
                    	    list_terms.add(terms.getTree());


                    	    }
                    	    break;

                    	default :
                    	    if ( cnt29 >= 1 ) break loop29;
                                EarlyExitException eee =
                                    new EarlyExitException(29, input);
                                throw eee;
                        }
                        cnt29++;
                    } while (true);


                     boolean hasSlot = false; 

                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:172:9: ( SLOT_ARROW first_slot_value= term ( slot )* )?
                    int alt31=2;
                    switch ( input.LA(1) ) {
                        case SLOT_ARROW:
                            {
                            alt31=1;
                            }
                            break;
                    }

                    switch (alt31) {
                        case 1 :
                            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:172:10: SLOT_ARROW first_slot_value= term ( slot )*
                            {
                            SLOT_ARROW75=(Token)match(input,SLOT_ARROW,FOLLOW_SLOT_ARROW_in_tuples_and_slots1130);  
                            stream_SLOT_ARROW.add(SLOT_ARROW75);


                            pushFollow(FOLLOW_term_in_tuples_and_slots1134);
                            first_slot_value=term();

                            state._fsp--;

                            stream_term.add(first_slot_value.getTree());

                             hasSlot = true; 

                            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:172:63: ( slot )*
                            loop30:
                            do {
                                int alt30=2;
                                switch ( input.LA(1) ) {
                                case CURIE:
                                case EXTERNAL:
                                case ID:
                                case IRI_REF:
                                case NUMBER:
                                case STRING:
                                case VAR_ID:
                                    {
                                    alt30=1;
                                    }
                                    break;

                                }

                                switch (alt30) {
                            	case 1 :
                            	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:172:63: slot
                            	    {
                            	    pushFollow(FOLLOW_slot_in_tuples_and_slots1138);
                            	    slot76=slot();

                            	    state._fsp--;

                            	    stream_slot.add(slot76.getTree());

                            	    }
                            	    break;

                            	default :
                            	    break loop30;
                                }
                            } while (true);


                            }
                            break;

                    }


                    // AST REWRITE
                    // elements: first_slot_value, slot, slot, first_slot_value
                    // token labels: 
                    // rule labels: retval, first_slot_value
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_first_slot_value=new RewriteRuleSubtreeStream(adaptor,"rule first_slot_value",first_slot_value!=null?first_slot_value.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 173:5: -> {!hasSlot}? ^( TUPLE )
                    if (!hasSlot) {
                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:173:20: ^( TUPLE )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TUPLE, "TUPLE")
                        , root_1);

                        adaptor.addChild(root_1, getTupleTree(list_terms, list_terms.size()) );

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    else // 174:5: -> {$terms.size() == 1}? ^( SLOT $first_slot_value) ( slot )*
                    if (list_terms.size() == 1) {
                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:175:9: ^( SLOT $first_slot_value)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(SLOT, "SLOT")
                        , root_1);

                        adaptor.addChild(root_1, list_terms.get(0));

                        adaptor.addChild(root_1, stream_first_slot_value.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:175:51: ( slot )*
                        while ( stream_slot.hasNext() ) {
                            adaptor.addChild(root_0, stream_slot.nextTree());

                        }
                        stream_slot.reset();

                    }

                    else // 176:5: -> ^( TUPLE ) ^( SLOT $first_slot_value) ( slot )*
                    {
                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:176:9: ^( TUPLE )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(TUPLE, "TUPLE")
                        , root_1);

                        adaptor.addChild(root_1, getTupleTree(list_terms, list_terms.size() - 1));

                        adaptor.addChild(root_0, root_1);
                        }

                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:176:60: ^( SLOT $first_slot_value)
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(SLOT, "SLOT")
                        , root_1);

                        adaptor.addChild(root_1, list_terms.get(list_terms.size() - 1));

                        adaptor.addChild(root_1, stream_first_slot_value.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:176:118: ( slot )*
                        while ( stream_slot.hasNext() ) {
                            adaptor.addChild(root_0, stream_slot.nextTree());

                        }
                        stream_slot.reset();

                    }


                    retval.tree = root_0;

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "tuples_and_slots"


    public static class tuple_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "tuple"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:179:1: tuple : LSQBR ( term )+ RSQBR -> ^( TUPLE ( term )+ ) ;
    public final RuleMLPresentationSyntaxParser.tuple_return tuple() throws RecognitionException {
        RuleMLPresentationSyntaxParser.tuple_return retval = new RuleMLPresentationSyntaxParser.tuple_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token LSQBR77=null;
        Token RSQBR79=null;
        RuleMLPresentationSyntaxParser.term_return term78 =null;


        CommonTree LSQBR77_tree=null;
        CommonTree RSQBR79_tree=null;
        RewriteRuleTokenStream stream_LSQBR=new RewriteRuleTokenStream(adaptor,"token LSQBR");
        RewriteRuleTokenStream stream_RSQBR=new RewriteRuleTokenStream(adaptor,"token RSQBR");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:180:5: ( LSQBR ( term )+ RSQBR -> ^( TUPLE ( term )+ ) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:180:9: LSQBR ( term )+ RSQBR
            {
            LSQBR77=(Token)match(input,LSQBR,FOLLOW_LSQBR_in_tuple1233);  
            stream_LSQBR.add(LSQBR77);


            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:180:15: ( term )+
            int cnt33=0;
            loop33:
            do {
                int alt33=2;
                switch ( input.LA(1) ) {
                case CURIE:
                case EXTERNAL:
                case ID:
                case IRI_REF:
                case NUMBER:
                case STRING:
                case VAR_ID:
                    {
                    alt33=1;
                    }
                    break;

                }

                switch (alt33) {
            	case 1 :
            	    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:180:15: term
            	    {
            	    pushFollow(FOLLOW_term_in_tuple1235);
            	    term78=term();

            	    state._fsp--;

            	    stream_term.add(term78.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt33 >= 1 ) break loop33;
                        EarlyExitException eee =
                            new EarlyExitException(33, input);
                        throw eee;
                }
                cnt33++;
            } while (true);


            RSQBR79=(Token)match(input,RSQBR,FOLLOW_RSQBR_in_tuple1238);  
            stream_RSQBR.add(RSQBR79);


            // AST REWRITE
            // elements: term
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 180:27: -> ^( TUPLE ( term )+ )
            {
                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:180:30: ^( TUPLE ( term )+ )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(TUPLE, "TUPLE")
                , root_1);

                if ( !(stream_term.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_term.hasNext() ) {
                    adaptor.addChild(root_1, stream_term.nextTree());

                }
                stream_term.reset();

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "tuple"


    public static class slot_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "slot"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:183:1: slot : name= term SLOT_ARROW value= term -> ^( SLOT $name $value) ;
    public final RuleMLPresentationSyntaxParser.slot_return slot() throws RecognitionException {
        RuleMLPresentationSyntaxParser.slot_return retval = new RuleMLPresentationSyntaxParser.slot_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token SLOT_ARROW80=null;
        RuleMLPresentationSyntaxParser.term_return name =null;

        RuleMLPresentationSyntaxParser.term_return value =null;


        CommonTree SLOT_ARROW80_tree=null;
        RewriteRuleTokenStream stream_SLOT_ARROW=new RewriteRuleTokenStream(adaptor,"token SLOT_ARROW");
        RewriteRuleSubtreeStream stream_term=new RewriteRuleSubtreeStream(adaptor,"rule term");
        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:184:5: (name= term SLOT_ARROW value= term -> ^( SLOT $name $value) )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:185:9: name= term SLOT_ARROW value= term
            {
            pushFollow(FOLLOW_term_in_slot1274);
            name=term();

            state._fsp--;

            stream_term.add(name.getTree());

            SLOT_ARROW80=(Token)match(input,SLOT_ARROW,FOLLOW_SLOT_ARROW_in_slot1276);  
            stream_SLOT_ARROW.add(SLOT_ARROW80);


            pushFollow(FOLLOW_term_in_slot1280);
            value=term();

            state._fsp--;

            stream_term.add(value.getTree());

            // AST REWRITE
            // elements: name, value
            // token labels: 
            // rule labels: retval, name, value
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_name=new RewriteRuleSubtreeStream(adaptor,"rule name",name!=null?name.tree:null);
            RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value",value!=null?value.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 185:41: -> ^( SLOT $name $value)
            {
                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:185:44: ^( SLOT $name $value)
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(SLOT, "SLOT")
                , root_1);

                adaptor.addChild(root_1, stream_name.nextTree());

                adaptor.addChild(root_1, stream_value.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "slot"


    public static class constant_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "constant"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:194:1: constant : ( const_string -> const_string | CURIE -> ^( SHORTCONST IRI[$CURIE.text] ) | NUMBER -> ^( SHORTCONST NUMBER[$NUMBER.text] ) | ID -> ^( SHORTCONST LOCAL[$ID.text.substring(1)] ) | IRI_REF -> ^( SHORTCONST IRI[$IRI_REF.text] ) );
    public final RuleMLPresentationSyntaxParser.constant_return constant() throws RecognitionException {
        RuleMLPresentationSyntaxParser.constant_return retval = new RuleMLPresentationSyntaxParser.constant_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token CURIE82=null;
        Token NUMBER83=null;
        Token ID84=null;
        Token IRI_REF85=null;
        RuleMLPresentationSyntaxParser.const_string_return const_string81 =null;


        CommonTree CURIE82_tree=null;
        CommonTree NUMBER83_tree=null;
        CommonTree ID84_tree=null;
        CommonTree IRI_REF85_tree=null;
        RewriteRuleTokenStream stream_IRI_REF=new RewriteRuleTokenStream(adaptor,"token IRI_REF");
        RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
        RewriteRuleTokenStream stream_CURIE=new RewriteRuleTokenStream(adaptor,"token CURIE");
        RewriteRuleTokenStream stream_NUMBER=new RewriteRuleTokenStream(adaptor,"token NUMBER");
        RewriteRuleSubtreeStream stream_const_string=new RewriteRuleSubtreeStream(adaptor,"rule const_string");
        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:195:5: ( const_string -> const_string | CURIE -> ^( SHORTCONST IRI[$CURIE.text] ) | NUMBER -> ^( SHORTCONST NUMBER[$NUMBER.text] ) | ID -> ^( SHORTCONST LOCAL[$ID.text.substring(1)] ) | IRI_REF -> ^( SHORTCONST IRI[$IRI_REF.text] ) )
            int alt34=5;
            switch ( input.LA(1) ) {
            case STRING:
                {
                alt34=1;
                }
                break;
            case CURIE:
                {
                alt34=2;
                }
                break;
            case NUMBER:
                {
                alt34=3;
                }
                break;
            case ID:
                {
                alt34=4;
                }
                break;
            case IRI_REF:
                {
                alt34=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 34, 0, input);

                throw nvae;

            }

            switch (alt34) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:195:9: const_string
                    {
                    pushFollow(FOLLOW_const_string_in_constant1314);
                    const_string81=const_string();

                    state._fsp--;

                    stream_const_string.add(const_string81.getTree());

                    // AST REWRITE
                    // elements: const_string
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 195:22: -> const_string
                    {
                        adaptor.addChild(root_0, stream_const_string.nextTree());

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:196:9: CURIE
                    {
                    CURIE82=(Token)match(input,CURIE,FOLLOW_CURIE_in_constant1328);  
                    stream_CURIE.add(CURIE82);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 196:17: -> ^( SHORTCONST IRI[$CURIE.text] )
                    {
                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:196:20: ^( SHORTCONST IRI[$CURIE.text] )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(SHORTCONST, "SHORTCONST")
                        , root_1);

                        adaptor.addChild(root_1, 
                        (CommonTree)adaptor.create(IRI, (CURIE82!=null?CURIE82.getText():null))
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 3 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:197:9: NUMBER
                    {
                    NUMBER83=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_constant1350);  
                    stream_NUMBER.add(NUMBER83);


                    // AST REWRITE
                    // elements: NUMBER
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 197:17: -> ^( SHORTCONST NUMBER[$NUMBER.text] )
                    {
                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:197:20: ^( SHORTCONST NUMBER[$NUMBER.text] )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(SHORTCONST, "SHORTCONST")
                        , root_1);

                        adaptor.addChild(root_1, 
                        (CommonTree)adaptor.create(NUMBER, (NUMBER83!=null?NUMBER83.getText():null))
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 4 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:198:9: ID
                    {
                    ID84=(Token)match(input,ID,FOLLOW_ID_in_constant1370);  
                    stream_ID.add(ID84);



                                if (!(ID84!=null?ID84.getText():null).startsWith("_"))
                                    throw new RuntimeException("Incorrect constant format:" + (ID84!=null?ID84.getText():null));
                            

                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 202:9: -> ^( SHORTCONST LOCAL[$ID.text.substring(1)] )
                    {
                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:202:12: ^( SHORTCONST LOCAL[$ID.text.substring(1)] )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(SHORTCONST, "SHORTCONST")
                        , root_1);

                        adaptor.addChild(root_1, 
                        (CommonTree)adaptor.create(LOCAL, (ID84!=null?ID84.getText():null).substring(1))
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;
                case 5 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:203:9: IRI_REF
                    {
                    IRI_REF85=(Token)match(input,IRI_REF,FOLLOW_IRI_REF_in_constant1401);  
                    stream_IRI_REF.add(IRI_REF85);


                    // AST REWRITE
                    // elements: 
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (CommonTree)adaptor.nil();
                    // 203:17: -> ^( SHORTCONST IRI[$IRI_REF.text] )
                    {
                        // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:203:20: ^( SHORTCONST IRI[$IRI_REF.text] )
                        {
                        CommonTree root_1 = (CommonTree)adaptor.nil();
                        root_1 = (CommonTree)adaptor.becomeRoot(
                        (CommonTree)adaptor.create(SHORTCONST, "SHORTCONST")
                        , root_1);

                        adaptor.addChild(root_1, 
                        (CommonTree)adaptor.create(IRI, (IRI_REF85!=null?IRI_REF85.getText():null))
                        );

                        adaptor.addChild(root_0, root_1);
                        }

                    }


                    retval.tree = root_0;

                    }
                    break;

            }
            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "constant"


    public static class const_string_return extends ParserRuleReturnScope {
        CommonTree tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "const_string"
    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:208:1: const_string : STRING ( ( SYMSPACE_OPER symspace= ( IRI_REF | CURIE ) ) | '@' )? -> {isAbbrivated}? ^( SHORTCONST LITERAL[getStrValue($STRING.text)] ) -> LITERAL[getStrValue($STRING.text)] IRI[$symspace.text] ;
    public final RuleMLPresentationSyntaxParser.const_string_return const_string() throws RecognitionException {
        RuleMLPresentationSyntaxParser.const_string_return retval = new RuleMLPresentationSyntaxParser.const_string_return();
        retval.start = input.LT(1);


        CommonTree root_0 = null;

        Token symspace=null;
        Token STRING86=null;
        Token SYMSPACE_OPER87=null;
        Token IRI_REF88=null;
        Token CURIE89=null;
        Token char_literal90=null;

        CommonTree symspace_tree=null;
        CommonTree STRING86_tree=null;
        CommonTree SYMSPACE_OPER87_tree=null;
        CommonTree IRI_REF88_tree=null;
        CommonTree CURIE89_tree=null;
        CommonTree char_literal90_tree=null;
        RewriteRuleTokenStream stream_IRI_REF=new RewriteRuleTokenStream(adaptor,"token IRI_REF");
        RewriteRuleTokenStream stream_CURIE=new RewriteRuleTokenStream(adaptor,"token CURIE");
        RewriteRuleTokenStream stream_SYMSPACE_OPER=new RewriteRuleTokenStream(adaptor,"token SYMSPACE_OPER");
        RewriteRuleTokenStream stream_STRING=new RewriteRuleTokenStream(adaptor,"token STRING");
        RewriteRuleTokenStream stream_50=new RewriteRuleTokenStream(adaptor,"token 50");

         boolean isAbbrivated = true; 
        try {
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:210:5: ( STRING ( ( SYMSPACE_OPER symspace= ( IRI_REF | CURIE ) ) | '@' )? -> {isAbbrivated}? ^( SHORTCONST LITERAL[getStrValue($STRING.text)] ) -> LITERAL[getStrValue($STRING.text)] IRI[$symspace.text] )
            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:210:7: STRING ( ( SYMSPACE_OPER symspace= ( IRI_REF | CURIE ) ) | '@' )?
            {
            STRING86=(Token)match(input,STRING,FOLLOW_STRING_in_const_string1435);  
            stream_STRING.add(STRING86);


            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:210:14: ( ( SYMSPACE_OPER symspace= ( IRI_REF | CURIE ) ) | '@' )?
            int alt36=3;
            switch ( input.LA(1) ) {
                case SYMSPACE_OPER:
                    {
                    alt36=1;
                    }
                    break;
                case 50:
                    {
                    alt36=2;
                    }
                    break;
            }

            switch (alt36) {
                case 1 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:210:15: ( SYMSPACE_OPER symspace= ( IRI_REF | CURIE ) )
                    {
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:210:15: ( SYMSPACE_OPER symspace= ( IRI_REF | CURIE ) )
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:210:16: SYMSPACE_OPER symspace= ( IRI_REF | CURIE )
                    {
                    SYMSPACE_OPER87=(Token)match(input,SYMSPACE_OPER,FOLLOW_SYMSPACE_OPER_in_const_string1439);  
                    stream_SYMSPACE_OPER.add(SYMSPACE_OPER87);


                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:210:39: ( IRI_REF | CURIE )
                    int alt35=2;
                    switch ( input.LA(1) ) {
                    case IRI_REF:
                        {
                        alt35=1;
                        }
                        break;
                    case CURIE:
                        {
                        alt35=2;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 35, 0, input);

                        throw nvae;

                    }

                    switch (alt35) {
                        case 1 :
                            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:210:40: IRI_REF
                            {
                            IRI_REF88=(Token)match(input,IRI_REF,FOLLOW_IRI_REF_in_const_string1444);  
                            stream_IRI_REF.add(IRI_REF88);


                            }
                            break;
                        case 2 :
                            // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:210:50: CURIE
                            {
                            CURIE89=(Token)match(input,CURIE,FOLLOW_CURIE_in_const_string1448);  
                            stream_CURIE.add(CURIE89);


                            }
                            break;

                    }


                     isAbbrivated = false; 

                    }


                    }
                    break;
                case 2 :
                    // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:210:87: '@'
                    {
                    char_literal90=(Token)match(input,50,FOLLOW_50_in_const_string1457);  
                    stream_50.add(char_literal90);


                    }
                    break;

            }


            // AST REWRITE
            // elements: 
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (CommonTree)adaptor.nil();
            // 211:5: -> {isAbbrivated}? ^( SHORTCONST LITERAL[getStrValue($STRING.text)] )
            if (isAbbrivated) {
                // org/ruleml/api/presentation_syntax_parser/RuleMLPresentationSyntax.g:211:24: ^( SHORTCONST LITERAL[getStrValue($STRING.text)] )
                {
                CommonTree root_1 = (CommonTree)adaptor.nil();
                root_1 = (CommonTree)adaptor.becomeRoot(
                (CommonTree)adaptor.create(SHORTCONST, "SHORTCONST")
                , root_1);

                adaptor.addChild(root_1, 
                (CommonTree)adaptor.create(LITERAL, getStrValue((STRING86!=null?STRING86.getText():null)))
                );

                adaptor.addChild(root_0, root_1);
                }

            }

            else // 212:5: -> LITERAL[getStrValue($STRING.text)] IRI[$symspace.text]
            {
                adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(LITERAL, getStrValue((STRING86!=null?STRING86.getText():null)))
                );

                adaptor.addChild(root_0, 
                (CommonTree)adaptor.create(IRI, (symspace!=null?symspace.getText():null))
                );

            }


            retval.tree = root_0;

            }

            retval.stop = input.LT(-1);


            retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "const_string"

    // Delegated rules


 

    public static final BitSet FOLLOW_document_in_top_level_item166 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_top_level_item169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule_in_queries183 = new BitSet(new long[]{0x0000880C0404E0A2L});
    public static final BitSet FOLLOW_DOCUMENT_in_document198 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAR_in_document200 = new BitSet(new long[]{0x0000005000420040L});
    public static final BitSet FOLLOW_base_in_document202 = new BitSet(new long[]{0x0000005000420000L});
    public static final BitSet FOLLOW_prefix_in_document205 = new BitSet(new long[]{0x0000005000420000L});
    public static final BitSet FOLLOW_importDecl_in_document208 = new BitSet(new long[]{0x0000004000420000L});
    public static final BitSet FOLLOW_group_in_document211 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RPAR_in_document214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BASE_in_base259 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAR_in_base261 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_IRI_REF_in_base263 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RPAR_in_base265 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PREFIX_in_prefix292 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAR_in_prefix294 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_ID_in_prefix296 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_IRI_REF_in_prefix298 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RPAR_in_prefix300 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importDecl329 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAR_in_importDecl331 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_IRI_REF_in_importDecl335 = new BitSet(new long[]{0x0000004004000000L});
    public static final BitSet FOLLOW_IRI_REF_in_importDecl340 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RPAR_in_importDecl344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_group376 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAR_in_group378 = new BitSet(new long[]{0x0000884C0406E0A0L});
    public static final BitSet FOLLOW_group_element_in_group380 = new BitSet(new long[]{0x0000884C0406E0A0L});
    public static final BitSet FOLLOW_RPAR_in_group383 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rule_in_group_element411 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_group_in_group_element421 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FORALL_in_rule440 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_VAR_ID_in_rule442 = new BitSet(new long[]{0x0000800080000000L});
    public static final BitSet FOLLOW_LPAR_in_rule445 = new BitSet(new long[]{0x0000880C040460A0L});
    public static final BitSet FOLLOW_clause_in_rule447 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RPAR_in_rule449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_clause_in_rule482 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formula_in_clause517 = new BitSet(new long[]{0x0000000000200002L});
    public static final BitSet FOLLOW_IMPLICATION_in_clause521 = new BitSet(new long[]{0x0000880C040460A0L});
    public static final BitSet FOLLOW_formula_in_clause525 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AND_in_formula595 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAR_in_formula597 = new BitSet(new long[]{0x0000880C040460A0L});
    public static final BitSet FOLLOW_formula_in_formula602 = new BitSet(new long[]{0x0000884C040460A0L});
    public static final BitSet FOLLOW_RPAR_in_formula609 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OR_in_formula628 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAR_in_formula630 = new BitSet(new long[]{0x0000880C040460A0L});
    public static final BitSet FOLLOW_formula_in_formula632 = new BitSet(new long[]{0x0000884C040460A0L});
    public static final BitSet FOLLOW_RPAR_in_formula635 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXISTS_in_formula656 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_VAR_ID_in_formula658 = new BitSet(new long[]{0x0000800080000000L});
    public static final BitSet FOLLOW_LPAR_in_formula661 = new BitSet(new long[]{0x0000880C040460A0L});
    public static final BitSet FOLLOW_formula_in_formula665 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RPAR_in_formula667 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_atomic_in_formula703 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_external_term_in_formula720 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_psoa_rest_in_formula738 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_internal_term_in_atomic783 = new BitSet(new long[]{0x0000100000001002L});
    public static final BitSet FOLLOW_set_in_atomic786 = new BitSet(new long[]{0x0000880404044080L});
    public static final BitSet FOLLOW_term_in_atomic795 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_internal_term_in_term816 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_external_term_in_term830 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constant_in_simple_term853 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_ID_in_simple_term863 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXTERNAL_in_external_term882 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAR_in_external_term884 = new BitSet(new long[]{0x0000880404040080L});
    public static final BitSet FOLLOW_simple_term_in_external_term886 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LPAR_in_external_term888 = new BitSet(new long[]{0x0000884404044080L});
    public static final BitSet FOLLOW_term_in_external_term890 = new BitSet(new long[]{0x0000884404044080L});
    public static final BitSet FOLLOW_RPAR_in_external_term893 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RPAR_in_external_term895 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simple_term_in_internal_term951 = new BitSet(new long[]{0x0000000080800002L});
    public static final BitSet FOLLOW_LPAR_in_internal_term967 = new BitSet(new long[]{0x0000884504044080L});
    public static final BitSet FOLLOW_tuples_and_slots_in_internal_term969 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RPAR_in_internal_term972 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_psoa_rest_in_internal_term1012 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_INSTANCE_in_psoa_rest1046 = new BitSet(new long[]{0x0000880404040080L});
    public static final BitSet FOLLOW_simple_term_in_psoa_rest1048 = new BitSet(new long[]{0x0000000080000002L});
    public static final BitSet FOLLOW_LPAR_in_psoa_rest1051 = new BitSet(new long[]{0x0000884504044080L});
    public static final BitSet FOLLOW_tuples_and_slots_in_psoa_rest1053 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_RPAR_in_psoa_rest1056 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tuple_in_tuples_and_slots1092 = new BitSet(new long[]{0x0000880504044082L});
    public static final BitSet FOLLOW_slot_in_tuples_and_slots1095 = new BitSet(new long[]{0x0000880404044082L});
    public static final BitSet FOLLOW_term_in_tuples_and_slots1116 = new BitSet(new long[]{0x00008C0404044082L});
    public static final BitSet FOLLOW_SLOT_ARROW_in_tuples_and_slots1130 = new BitSet(new long[]{0x0000880404044080L});
    public static final BitSet FOLLOW_term_in_tuples_and_slots1134 = new BitSet(new long[]{0x0000880404044082L});
    public static final BitSet FOLLOW_slot_in_tuples_and_slots1138 = new BitSet(new long[]{0x0000880404044082L});
    public static final BitSet FOLLOW_LSQBR_in_tuple1233 = new BitSet(new long[]{0x0000880404044080L});
    public static final BitSet FOLLOW_term_in_tuple1235 = new BitSet(new long[]{0x0000888404044080L});
    public static final BitSet FOLLOW_RSQBR_in_tuple1238 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_term_in_slot1274 = new BitSet(new long[]{0x0000040000000000L});
    public static final BitSet FOLLOW_SLOT_ARROW_in_slot1276 = new BitSet(new long[]{0x0000880404044080L});
    public static final BitSet FOLLOW_term_in_slot1280 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_const_string_in_constant1314 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CURIE_in_constant1328 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_constant1350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_constant1370 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IRI_REF_in_constant1401 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_const_string1435 = new BitSet(new long[]{0x0004200000000002L});
    public static final BitSet FOLLOW_SYMSPACE_OPER_in_const_string1439 = new BitSet(new long[]{0x0000000004000080L});
    public static final BitSet FOLLOW_IRI_REF_in_const_string1444 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CURIE_in_const_string1448 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_50_in_const_string1457 = new BitSet(new long[]{0x0000000000000002L});

}