// Generated from c:/Users/User/Desktop/projects/clever/src/cleverCompiler/antlr/com/projectclever/clever/compiler/antlr/CleverTemplate.g4 by ANTLR 4.13.1

package com.projectclever.clever.compiler.antlr;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class CleverTemplateParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PAGE=1, COMPONENT=2, LAYOUT=3, CODE=4, TEMPLATE=5, REQUIRED_ARGS_CONSTRUCTOR=6, 
		INTERPOLATION_START=7, LBRACE=8, RBRACE=9, LPAREN=10, RPAREN=11, IMPORT=12, 
		SEMICOLON=13, TEXT_BLOCK=14, QUOTED=15, HTML_COMMENT=16, BLOCK_COMMENT=17, 
		LINE_COMMENT=18, IDENTIFIER=19, WHITESPACE=20, OTHER=21;
	public static final int
		RULE_templateFile = 0, RULE_templateDeclaration = 1, RULE_codeBlock = 2, 
		RULE_block = 3, RULE_blockContent = 4, RULE_nestedBlock = 5, RULE_templateBlock = 6, 
		RULE_templateBody = 7, RULE_templateContent = 8, RULE_nestedTemplateBlock = 9, 
		RULE_interpolation = 10, RULE_expressionContent = 11, RULE_parenGroup = 12;
	private static String[] makeRuleNames() {
		return new String[] {
			"templateFile", "templateDeclaration", "codeBlock", "block", "blockContent", 
			"nestedBlock", "templateBlock", "templateBody", "templateContent", "nestedTemplateBlock", 
			"interpolation", "expressionContent", "parenGroup"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'@Page'", "'@Component'", "'@Layout'", "'@Code'", "'@Template'", 
			"'@RequiredArgsConstructor'", "'@('", "'{'", "'}'", "'('", "')'", "'import'", 
			"';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PAGE", "COMPONENT", "LAYOUT", "CODE", "TEMPLATE", "REQUIRED_ARGS_CONSTRUCTOR", 
			"INTERPOLATION_START", "LBRACE", "RBRACE", "LPAREN", "RPAREN", "IMPORT", 
			"SEMICOLON", "TEXT_BLOCK", "QUOTED", "HTML_COMMENT", "BLOCK_COMMENT", 
			"LINE_COMMENT", "IDENTIFIER", "WHITESPACE", "OTHER"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "CleverTemplate.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CleverTemplateParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TemplateFileContext extends ParserRuleContext {
		public TemplateDeclarationContext templateDeclaration() {
			return getRuleContext(TemplateDeclarationContext.class,0);
		}
		public CodeBlockContext codeBlock() {
			return getRuleContext(CodeBlockContext.class,0);
		}
		public TemplateBlockContext templateBlock() {
			return getRuleContext(TemplateBlockContext.class,0);
		}
		public TerminalNode EOF() { return getToken(CleverTemplateParser.EOF, 0); }
		public TerminalNode REQUIRED_ARGS_CONSTRUCTOR() { return getToken(CleverTemplateParser.REQUIRED_ARGS_CONSTRUCTOR, 0); }
		public TemplateFileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateFile; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).enterTemplateFile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).exitTemplateFile(this);
		}
	}

	public final TemplateFileContext templateFile() throws RecognitionException {
		TemplateFileContext _localctx = new TemplateFileContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_templateFile);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26);
			templateDeclaration();
			setState(28);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==REQUIRED_ARGS_CONSTRUCTOR) {
				{
				setState(27);
				match(REQUIRED_ARGS_CONSTRUCTOR);
				}
			}

			setState(30);
			codeBlock();
			setState(31);
			templateBlock();
			setState(32);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TemplateDeclarationContext extends ParserRuleContext {
		public TerminalNode PAGE() { return getToken(CleverTemplateParser.PAGE, 0); }
		public TerminalNode IDENTIFIER() { return getToken(CleverTemplateParser.IDENTIFIER, 0); }
		public TerminalNode COMPONENT() { return getToken(CleverTemplateParser.COMPONENT, 0); }
		public TerminalNode LAYOUT() { return getToken(CleverTemplateParser.LAYOUT, 0); }
		public TemplateDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).enterTemplateDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).exitTemplateDeclaration(this);
		}
	}

	public final TemplateDeclarationContext templateDeclaration() throws RecognitionException {
		TemplateDeclarationContext _localctx = new TemplateDeclarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_templateDeclaration);
		try {
			setState(40);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PAGE:
				enterOuterAlt(_localctx, 1);
				{
				setState(34);
				match(PAGE);
				setState(35);
				match(IDENTIFIER);
				}
				break;
			case COMPONENT:
				enterOuterAlt(_localctx, 2);
				{
				setState(36);
				match(COMPONENT);
				setState(37);
				match(IDENTIFIER);
				}
				break;
			case LAYOUT:
				enterOuterAlt(_localctx, 3);
				{
				setState(38);
				match(LAYOUT);
				setState(39);
				match(IDENTIFIER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CodeBlockContext extends ParserRuleContext {
		public TerminalNode CODE() { return getToken(CleverTemplateParser.CODE, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public CodeBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_codeBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).enterCodeBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).exitCodeBlock(this);
		}
	}

	public final CodeBlockContext codeBlock() throws RecognitionException {
		CodeBlockContext _localctx = new CodeBlockContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_codeBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			match(CODE);
			setState(43);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(CleverTemplateParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(CleverTemplateParser.RBRACE, 0); }
		public List<BlockContentContext> blockContent() {
			return getRuleContexts(BlockContentContext.class);
		}
		public BlockContentContext blockContent(int i) {
			return getRuleContext(BlockContentContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).exitBlock(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45);
			match(LBRACE);
			setState(49);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4193790L) != 0)) {
				{
				{
				setState(46);
				blockContent();
				}
				}
				setState(51);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(52);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContentContext extends ParserRuleContext {
		public NestedBlockContext nestedBlock() {
			return getRuleContext(NestedBlockContext.class,0);
		}
		public TerminalNode QUOTED() { return getToken(CleverTemplateParser.QUOTED, 0); }
		public TerminalNode TEXT_BLOCK() { return getToken(CleverTemplateParser.TEXT_BLOCK, 0); }
		public TerminalNode LINE_COMMENT() { return getToken(CleverTemplateParser.LINE_COMMENT, 0); }
		public TerminalNode BLOCK_COMMENT() { return getToken(CleverTemplateParser.BLOCK_COMMENT, 0); }
		public TerminalNode HTML_COMMENT() { return getToken(CleverTemplateParser.HTML_COMMENT, 0); }
		public TerminalNode LBRACE() { return getToken(CleverTemplateParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(CleverTemplateParser.RBRACE, 0); }
		public BlockContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).enterBlockContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).exitBlockContent(this);
		}
	}

	public final BlockContentContext blockContent() throws RecognitionException {
		BlockContentContext _localctx = new BlockContentContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_blockContent);
		int _la;
		try {
			setState(61);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(54);
				nestedBlock();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(55);
				match(QUOTED);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(56);
				match(TEXT_BLOCK);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(57);
				match(LINE_COMMENT);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(58);
				match(BLOCK_COMMENT);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(59);
				match(HTML_COMMENT);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(60);
				_la = _input.LA(1);
				if ( _la <= 0 || (_la==LBRACE || _la==RBRACE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NestedBlockContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(CleverTemplateParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(CleverTemplateParser.RBRACE, 0); }
		public List<BlockContentContext> blockContent() {
			return getRuleContexts(BlockContentContext.class);
		}
		public BlockContentContext blockContent(int i) {
			return getRuleContext(BlockContentContext.class,i);
		}
		public NestedBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nestedBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).enterNestedBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).exitNestedBlock(this);
		}
	}

	public final NestedBlockContext nestedBlock() throws RecognitionException {
		NestedBlockContext _localctx = new NestedBlockContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_nestedBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
			match(LBRACE);
			setState(67);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4193790L) != 0)) {
				{
				{
				setState(64);
				blockContent();
				}
				}
				setState(69);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(70);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TemplateBlockContext extends ParserRuleContext {
		public TerminalNode TEMPLATE() { return getToken(CleverTemplateParser.TEMPLATE, 0); }
		public TemplateBodyContext templateBody() {
			return getRuleContext(TemplateBodyContext.class,0);
		}
		public TemplateBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).enterTemplateBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).exitTemplateBlock(this);
		}
	}

	public final TemplateBlockContext templateBlock() throws RecognitionException {
		TemplateBlockContext _localctx = new TemplateBlockContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_templateBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			match(TEMPLATE);
			setState(73);
			templateBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TemplateBodyContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(CleverTemplateParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(CleverTemplateParser.RBRACE, 0); }
		public List<TemplateContentContext> templateContent() {
			return getRuleContexts(TemplateContentContext.class);
		}
		public TemplateContentContext templateContent(int i) {
			return getRuleContext(TemplateContentContext.class,i);
		}
		public TemplateBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).enterTemplateBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).exitTemplateBody(this);
		}
	}

	public final TemplateBodyContext templateBody() throws RecognitionException {
		TemplateBodyContext _localctx = new TemplateBodyContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_templateBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			match(LBRACE);
			setState(79);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4193790L) != 0)) {
				{
				{
				setState(76);
				templateContent();
				}
				}
				setState(81);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(82);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TemplateContentContext extends ParserRuleContext {
		public NestedTemplateBlockContext nestedTemplateBlock() {
			return getRuleContext(NestedTemplateBlockContext.class,0);
		}
		public InterpolationContext interpolation() {
			return getRuleContext(InterpolationContext.class,0);
		}
		public TerminalNode QUOTED() { return getToken(CleverTemplateParser.QUOTED, 0); }
		public TerminalNode TEXT_BLOCK() { return getToken(CleverTemplateParser.TEXT_BLOCK, 0); }
		public TerminalNode LINE_COMMENT() { return getToken(CleverTemplateParser.LINE_COMMENT, 0); }
		public TerminalNode BLOCK_COMMENT() { return getToken(CleverTemplateParser.BLOCK_COMMENT, 0); }
		public TerminalNode HTML_COMMENT() { return getToken(CleverTemplateParser.HTML_COMMENT, 0); }
		public TerminalNode LBRACE() { return getToken(CleverTemplateParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(CleverTemplateParser.RBRACE, 0); }
		public TemplateContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).enterTemplateContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).exitTemplateContent(this);
		}
	}

	public final TemplateContentContext templateContent() throws RecognitionException {
		TemplateContentContext _localctx = new TemplateContentContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_templateContent);
		int _la;
		try {
			setState(92);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(84);
				nestedTemplateBlock();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(85);
				interpolation();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(86);
				match(QUOTED);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(87);
				match(TEXT_BLOCK);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(88);
				match(LINE_COMMENT);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(89);
				match(BLOCK_COMMENT);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(90);
				match(HTML_COMMENT);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(91);
				_la = _input.LA(1);
				if ( _la <= 0 || (_la==LBRACE || _la==RBRACE) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NestedTemplateBlockContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(CleverTemplateParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(CleverTemplateParser.RBRACE, 0); }
		public List<TemplateContentContext> templateContent() {
			return getRuleContexts(TemplateContentContext.class);
		}
		public TemplateContentContext templateContent(int i) {
			return getRuleContext(TemplateContentContext.class,i);
		}
		public NestedTemplateBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nestedTemplateBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).enterNestedTemplateBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).exitNestedTemplateBlock(this);
		}
	}

	public final NestedTemplateBlockContext nestedTemplateBlock() throws RecognitionException {
		NestedTemplateBlockContext _localctx = new NestedTemplateBlockContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_nestedTemplateBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			match(LBRACE);
			setState(98);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4193790L) != 0)) {
				{
				{
				setState(95);
				templateContent();
				}
				}
				setState(100);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(101);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InterpolationContext extends ParserRuleContext {
		public TerminalNode INTERPOLATION_START() { return getToken(CleverTemplateParser.INTERPOLATION_START, 0); }
		public TerminalNode RPAREN() { return getToken(CleverTemplateParser.RPAREN, 0); }
		public List<ExpressionContentContext> expressionContent() {
			return getRuleContexts(ExpressionContentContext.class);
		}
		public ExpressionContentContext expressionContent(int i) {
			return getRuleContext(ExpressionContentContext.class,i);
		}
		public InterpolationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interpolation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).enterInterpolation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).exitInterpolation(this);
		}
	}

	public final InterpolationContext interpolation() throws RecognitionException {
		InterpolationContext _localctx = new InterpolationContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_interpolation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			match(INTERPOLATION_START);
			setState(107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4192254L) != 0)) {
				{
				{
				setState(104);
				expressionContent();
				}
				}
				setState(109);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(110);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContentContext extends ParserRuleContext {
		public ParenGroupContext parenGroup() {
			return getRuleContext(ParenGroupContext.class,0);
		}
		public TerminalNode QUOTED() { return getToken(CleverTemplateParser.QUOTED, 0); }
		public TerminalNode TEXT_BLOCK() { return getToken(CleverTemplateParser.TEXT_BLOCK, 0); }
		public TerminalNode LINE_COMMENT() { return getToken(CleverTemplateParser.LINE_COMMENT, 0); }
		public TerminalNode BLOCK_COMMENT() { return getToken(CleverTemplateParser.BLOCK_COMMENT, 0); }
		public TerminalNode HTML_COMMENT() { return getToken(CleverTemplateParser.HTML_COMMENT, 0); }
		public TerminalNode LPAREN() { return getToken(CleverTemplateParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(CleverTemplateParser.RPAREN, 0); }
		public ExpressionContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).enterExpressionContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).exitExpressionContent(this);
		}
	}

	public final ExpressionContentContext expressionContent() throws RecognitionException {
		ExpressionContentContext _localctx = new ExpressionContentContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_expressionContent);
		int _la;
		try {
			setState(119);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(112);
				parenGroup();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(113);
				match(QUOTED);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(114);
				match(TEXT_BLOCK);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(115);
				match(LINE_COMMENT);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(116);
				match(BLOCK_COMMENT);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(117);
				match(HTML_COMMENT);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(118);
				_la = _input.LA(1);
				if ( _la <= 0 || (_la==LPAREN || _la==RPAREN) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParenGroupContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(CleverTemplateParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(CleverTemplateParser.RPAREN, 0); }
		public List<ExpressionContentContext> expressionContent() {
			return getRuleContexts(ExpressionContentContext.class);
		}
		public ExpressionContentContext expressionContent(int i) {
			return getRuleContext(ExpressionContentContext.class,i);
		}
		public ParenGroupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parenGroup; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).enterParenGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CleverTemplateListener ) ((CleverTemplateListener)listener).exitParenGroup(this);
		}
	}

	public final ParenGroupContext parenGroup() throws RecognitionException {
		ParenGroupContext _localctx = new ParenGroupContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_parenGroup);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			match(LPAREN);
			setState(125);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4192254L) != 0)) {
				{
				{
				setState(122);
				expressionContent();
				}
				}
				setState(127);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(128);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0015\u0083\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0001\u0000\u0001\u0000\u0003\u0000\u001d\b\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001)\b\u0001\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0005\u00030\b"+
		"\u0003\n\u0003\f\u00033\t\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003"+
		"\u0004>\b\u0004\u0001\u0005\u0001\u0005\u0005\u0005B\b\u0005\n\u0005\f"+
		"\u0005E\t\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0001\u0007\u0005\u0007N\b\u0007\n\u0007\f\u0007Q\t"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b"+
		"\u0001\b\u0001\b\u0001\b\u0003\b]\b\b\u0001\t\u0001\t\u0005\ta\b\t\n\t"+
		"\f\td\t\t\u0001\t\u0001\t\u0001\n\u0001\n\u0005\nj\b\n\n\n\f\nm\t\n\u0001"+
		"\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0003\u000bx\b\u000b\u0001\f\u0001\f\u0005\f"+
		"|\b\f\n\f\f\f\u007f\t\f\u0001\f\u0001\f\u0001\f\u0000\u0000\r\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u0000\u0002\u0001"+
		"\u0000\b\t\u0001\u0000\n\u000b\u0091\u0000\u001a\u0001\u0000\u0000\u0000"+
		"\u0002(\u0001\u0000\u0000\u0000\u0004*\u0001\u0000\u0000\u0000\u0006-"+
		"\u0001\u0000\u0000\u0000\b=\u0001\u0000\u0000\u0000\n?\u0001\u0000\u0000"+
		"\u0000\fH\u0001\u0000\u0000\u0000\u000eK\u0001\u0000\u0000\u0000\u0010"+
		"\\\u0001\u0000\u0000\u0000\u0012^\u0001\u0000\u0000\u0000\u0014g\u0001"+
		"\u0000\u0000\u0000\u0016w\u0001\u0000\u0000\u0000\u0018y\u0001\u0000\u0000"+
		"\u0000\u001a\u001c\u0003\u0002\u0001\u0000\u001b\u001d\u0005\u0006\u0000"+
		"\u0000\u001c\u001b\u0001\u0000\u0000\u0000\u001c\u001d\u0001\u0000\u0000"+
		"\u0000\u001d\u001e\u0001\u0000\u0000\u0000\u001e\u001f\u0003\u0004\u0002"+
		"\u0000\u001f \u0003\f\u0006\u0000 !\u0005\u0000\u0000\u0001!\u0001\u0001"+
		"\u0000\u0000\u0000\"#\u0005\u0001\u0000\u0000#)\u0005\u0013\u0000\u0000"+
		"$%\u0005\u0002\u0000\u0000%)\u0005\u0013\u0000\u0000&\'\u0005\u0003\u0000"+
		"\u0000\')\u0005\u0013\u0000\u0000(\"\u0001\u0000\u0000\u0000($\u0001\u0000"+
		"\u0000\u0000(&\u0001\u0000\u0000\u0000)\u0003\u0001\u0000\u0000\u0000"+
		"*+\u0005\u0004\u0000\u0000+,\u0003\u0006\u0003\u0000,\u0005\u0001\u0000"+
		"\u0000\u0000-1\u0005\b\u0000\u0000.0\u0003\b\u0004\u0000/.\u0001\u0000"+
		"\u0000\u000003\u0001\u0000\u0000\u00001/\u0001\u0000\u0000\u000012\u0001"+
		"\u0000\u0000\u000024\u0001\u0000\u0000\u000031\u0001\u0000\u0000\u0000"+
		"45\u0005\t\u0000\u00005\u0007\u0001\u0000\u0000\u00006>\u0003\n\u0005"+
		"\u00007>\u0005\u000f\u0000\u00008>\u0005\u000e\u0000\u00009>\u0005\u0012"+
		"\u0000\u0000:>\u0005\u0011\u0000\u0000;>\u0005\u0010\u0000\u0000<>\b\u0000"+
		"\u0000\u0000=6\u0001\u0000\u0000\u0000=7\u0001\u0000\u0000\u0000=8\u0001"+
		"\u0000\u0000\u0000=9\u0001\u0000\u0000\u0000=:\u0001\u0000\u0000\u0000"+
		"=;\u0001\u0000\u0000\u0000=<\u0001\u0000\u0000\u0000>\t\u0001\u0000\u0000"+
		"\u0000?C\u0005\b\u0000\u0000@B\u0003\b\u0004\u0000A@\u0001\u0000\u0000"+
		"\u0000BE\u0001\u0000\u0000\u0000CA\u0001\u0000\u0000\u0000CD\u0001\u0000"+
		"\u0000\u0000DF\u0001\u0000\u0000\u0000EC\u0001\u0000\u0000\u0000FG\u0005"+
		"\t\u0000\u0000G\u000b\u0001\u0000\u0000\u0000HI\u0005\u0005\u0000\u0000"+
		"IJ\u0003\u000e\u0007\u0000J\r\u0001\u0000\u0000\u0000KO\u0005\b\u0000"+
		"\u0000LN\u0003\u0010\b\u0000ML\u0001\u0000\u0000\u0000NQ\u0001\u0000\u0000"+
		"\u0000OM\u0001\u0000\u0000\u0000OP\u0001\u0000\u0000\u0000PR\u0001\u0000"+
		"\u0000\u0000QO\u0001\u0000\u0000\u0000RS\u0005\t\u0000\u0000S\u000f\u0001"+
		"\u0000\u0000\u0000T]\u0003\u0012\t\u0000U]\u0003\u0014\n\u0000V]\u0005"+
		"\u000f\u0000\u0000W]\u0005\u000e\u0000\u0000X]\u0005\u0012\u0000\u0000"+
		"Y]\u0005\u0011\u0000\u0000Z]\u0005\u0010\u0000\u0000[]\b\u0000\u0000\u0000"+
		"\\T\u0001\u0000\u0000\u0000\\U\u0001\u0000\u0000\u0000\\V\u0001\u0000"+
		"\u0000\u0000\\W\u0001\u0000\u0000\u0000\\X\u0001\u0000\u0000\u0000\\Y"+
		"\u0001\u0000\u0000\u0000\\Z\u0001\u0000\u0000\u0000\\[\u0001\u0000\u0000"+
		"\u0000]\u0011\u0001\u0000\u0000\u0000^b\u0005\b\u0000\u0000_a\u0003\u0010"+
		"\b\u0000`_\u0001\u0000\u0000\u0000ad\u0001\u0000\u0000\u0000b`\u0001\u0000"+
		"\u0000\u0000bc\u0001\u0000\u0000\u0000ce\u0001\u0000\u0000\u0000db\u0001"+
		"\u0000\u0000\u0000ef\u0005\t\u0000\u0000f\u0013\u0001\u0000\u0000\u0000"+
		"gk\u0005\u0007\u0000\u0000hj\u0003\u0016\u000b\u0000ih\u0001\u0000\u0000"+
		"\u0000jm\u0001\u0000\u0000\u0000ki\u0001\u0000\u0000\u0000kl\u0001\u0000"+
		"\u0000\u0000ln\u0001\u0000\u0000\u0000mk\u0001\u0000\u0000\u0000no\u0005"+
		"\u000b\u0000\u0000o\u0015\u0001\u0000\u0000\u0000px\u0003\u0018\f\u0000"+
		"qx\u0005\u000f\u0000\u0000rx\u0005\u000e\u0000\u0000sx\u0005\u0012\u0000"+
		"\u0000tx\u0005\u0011\u0000\u0000ux\u0005\u0010\u0000\u0000vx\b\u0001\u0000"+
		"\u0000wp\u0001\u0000\u0000\u0000wq\u0001\u0000\u0000\u0000wr\u0001\u0000"+
		"\u0000\u0000ws\u0001\u0000\u0000\u0000wt\u0001\u0000\u0000\u0000wu\u0001"+
		"\u0000\u0000\u0000wv\u0001\u0000\u0000\u0000x\u0017\u0001\u0000\u0000"+
		"\u0000y}\u0005\n\u0000\u0000z|\u0003\u0016\u000b\u0000{z\u0001\u0000\u0000"+
		"\u0000|\u007f\u0001\u0000\u0000\u0000}{\u0001\u0000\u0000\u0000}~\u0001"+
		"\u0000\u0000\u0000~\u0080\u0001\u0000\u0000\u0000\u007f}\u0001\u0000\u0000"+
		"\u0000\u0080\u0081\u0005\u000b\u0000\u0000\u0081\u0019\u0001\u0000\u0000"+
		"\u0000\u000b\u001c(1=CO\\bkw}";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}