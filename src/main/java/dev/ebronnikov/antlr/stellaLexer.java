// Generated from stellaLexer.g4 by ANTLR 4.7.2
package dev.ebronnikov.antlr;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class stellaLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Surrogate_id_SYMB_0=1, Surrogate_id_SYMB_1=2, Surrogate_id_SYMB_2=3, Surrogate_id_SYMB_3=4, 
		Surrogate_id_SYMB_4=5, Surrogate_id_SYMB_5=6, Surrogate_id_SYMB_6=7, Surrogate_id_SYMB_7=8, 
		Surrogate_id_SYMB_8=9, Surrogate_id_SYMB_9=10, Surrogate_id_SYMB_10=11, 
		Surrogate_id_SYMB_11=12, Surrogate_id_SYMB_12=13, Surrogate_id_SYMB_13=14, 
		Surrogate_id_SYMB_14=15, Surrogate_id_SYMB_15=16, Surrogate_id_SYMB_16=17, 
		Surrogate_id_SYMB_17=18, Surrogate_id_SYMB_18=19, Surrogate_id_SYMB_19=20, 
		Surrogate_id_SYMB_20=21, Surrogate_id_SYMB_21=22, Surrogate_id_SYMB_22=23, 
		Surrogate_id_SYMB_23=24, Surrogate_id_SYMB_24=25, Surrogate_id_SYMB_25=26, 
		Surrogate_id_SYMB_26=27, Surrogate_id_SYMB_27=28, Surrogate_id_SYMB_28=29, 
		Surrogate_id_SYMB_29=30, Surrogate_id_SYMB_30=31, Surrogate_id_SYMB_31=32, 
		Surrogate_id_SYMB_32=33, Surrogate_id_SYMB_33=34, Surrogate_id_SYMB_34=35, 
		Surrogate_id_SYMB_35=36, Surrogate_id_SYMB_36=37, Surrogate_id_SYMB_37=38, 
		Surrogate_id_SYMB_38=39, Surrogate_id_SYMB_39=40, Surrogate_id_SYMB_40=41, 
		Surrogate_id_SYMB_41=42, Surrogate_id_SYMB_42=43, Surrogate_id_SYMB_43=44, 
		Surrogate_id_SYMB_44=45, Surrogate_id_SYMB_45=46, Surrogate_id_SYMB_46=47, 
		Surrogate_id_SYMB_47=48, Surrogate_id_SYMB_48=49, Surrogate_id_SYMB_49=50, 
		Surrogate_id_SYMB_50=51, Surrogate_id_SYMB_51=52, Surrogate_id_SYMB_52=53, 
		Surrogate_id_SYMB_53=54, Surrogate_id_SYMB_54=55, Surrogate_id_SYMB_55=56, 
		Surrogate_id_SYMB_56=57, Surrogate_id_SYMB_57=58, Surrogate_id_SYMB_58=59, 
		Surrogate_id_SYMB_59=60, Surrogate_id_SYMB_60=61, Surrogate_id_SYMB_61=62, 
		Surrogate_id_SYMB_62=63, Surrogate_id_SYMB_63=64, Surrogate_id_SYMB_64=65, 
		Surrogate_id_SYMB_65=66, EXCEPTION=67, VARIANT=68, CAST=69, ASSIGN=70, 
		REF_TYPE=71, REFERENCE=72, PANIC=73, THROW=74, TRY=75, CATCH=76, TOP_TYPE=77, 
		BOTTOM_TYPE=78, AUTO_TYPE=79, GENERIC=80, FORALL=81, COMMENT_antlr_builtin=82, 
		MULTICOMMENT_antlr_builtin=83, StellaIdent=84, ExtensionName=85, MemoryAddress=86, 
		INTEGER=87, WS=88, ErrorToken=89;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"LETTER", "CAPITAL", "SMALL", "DIGIT", "Surrogate_id_SYMB_0", "Surrogate_id_SYMB_1", 
			"Surrogate_id_SYMB_2", "Surrogate_id_SYMB_3", "Surrogate_id_SYMB_4", 
			"Surrogate_id_SYMB_5", "Surrogate_id_SYMB_6", "Surrogate_id_SYMB_7", 
			"Surrogate_id_SYMB_8", "Surrogate_id_SYMB_9", "Surrogate_id_SYMB_10", 
			"Surrogate_id_SYMB_11", "Surrogate_id_SYMB_12", "Surrogate_id_SYMB_13", 
			"Surrogate_id_SYMB_14", "Surrogate_id_SYMB_15", "Surrogate_id_SYMB_16", 
			"Surrogate_id_SYMB_17", "Surrogate_id_SYMB_18", "Surrogate_id_SYMB_19", 
			"Surrogate_id_SYMB_20", "Surrogate_id_SYMB_21", "Surrogate_id_SYMB_22", 
			"Surrogate_id_SYMB_23", "Surrogate_id_SYMB_24", "Surrogate_id_SYMB_25", 
			"Surrogate_id_SYMB_26", "Surrogate_id_SYMB_27", "Surrogate_id_SYMB_28", 
			"Surrogate_id_SYMB_29", "Surrogate_id_SYMB_30", "Surrogate_id_SYMB_31", 
			"Surrogate_id_SYMB_32", "Surrogate_id_SYMB_33", "Surrogate_id_SYMB_34", 
			"Surrogate_id_SYMB_35", "Surrogate_id_SYMB_36", "Surrogate_id_SYMB_37", 
			"Surrogate_id_SYMB_38", "Surrogate_id_SYMB_39", "Surrogate_id_SYMB_40", 
			"Surrogate_id_SYMB_41", "Surrogate_id_SYMB_42", "Surrogate_id_SYMB_43", 
			"Surrogate_id_SYMB_44", "Surrogate_id_SYMB_45", "Surrogate_id_SYMB_46", 
			"Surrogate_id_SYMB_47", "Surrogate_id_SYMB_48", "Surrogate_id_SYMB_49", 
			"Surrogate_id_SYMB_50", "Surrogate_id_SYMB_51", "Surrogate_id_SYMB_52", 
			"Surrogate_id_SYMB_53", "Surrogate_id_SYMB_54", "Surrogate_id_SYMB_55", 
			"Surrogate_id_SYMB_56", "Surrogate_id_SYMB_57", "Surrogate_id_SYMB_58", 
			"Surrogate_id_SYMB_59", "Surrogate_id_SYMB_60", "Surrogate_id_SYMB_61", 
			"Surrogate_id_SYMB_62", "Surrogate_id_SYMB_63", "Surrogate_id_SYMB_64", 
			"Surrogate_id_SYMB_65", "EXCEPTION", "VARIANT", "CAST", "ASSIGN", "REF_TYPE", 
			"REFERENCE", "PANIC", "THROW", "TRY", "CATCH", "TOP_TYPE", "BOTTOM_TYPE", 
			"AUTO_TYPE", "GENERIC", "FORALL", "COMMENT_antlr_builtin", "MULTICOMMENT_antlr_builtin", 
			"StellaIdent", "ExtensionName", "MemoryAddress", "INTEGER", "WS", "Escapable", 
			"ErrorToken"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "','", "';'", "'('", "')'", "'{'", "'}'", "'='", "':'", "'->'", 
			"'=>'", "'|'", "'<|'", "'|>'", "'['", "']'", "'<'", "'<='", "'>'", "'>='", 
			"'=='", "'!='", "'+'", "'-'", "'*'", "'/'", "'.'", "'List::head'", "'List::isempty'", 
			"'List::tail'", "'Nat::pred'", "'Nat::iszero'", "'Nat::rec'", "'Bool'", 
			"'Nat'", "'Unit'", "'and'", "'as'", "'cons'", "'core'", "'else'", "'extend'", 
			"'false'", "'fix'", "'fn'", "'fold'", "'if'", "'in'", "'inl'", "'inline'", 
			"'inr'", "'language'", "'let'", "'letrec'", "'match'", "'not'", "'or'", 
			"'return'", "'succ'", "'then'", "'throws'", "'true'", "'type'", "'unfold'", 
			"'unit'", "'with'", "'\u00B5'", "'exception'", "'variant'", "'cast'", 
			"':='", "'&'", "'new'", "'panic!'", "'throw'", "'try'", "'catch'", "'Top'", 
			"'Bot'", "'auto'", "'generic'", "'forall'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "Surrogate_id_SYMB_0", "Surrogate_id_SYMB_1", "Surrogate_id_SYMB_2", 
			"Surrogate_id_SYMB_3", "Surrogate_id_SYMB_4", "Surrogate_id_SYMB_5", 
			"Surrogate_id_SYMB_6", "Surrogate_id_SYMB_7", "Surrogate_id_SYMB_8", 
			"Surrogate_id_SYMB_9", "Surrogate_id_SYMB_10", "Surrogate_id_SYMB_11", 
			"Surrogate_id_SYMB_12", "Surrogate_id_SYMB_13", "Surrogate_id_SYMB_14", 
			"Surrogate_id_SYMB_15", "Surrogate_id_SYMB_16", "Surrogate_id_SYMB_17", 
			"Surrogate_id_SYMB_18", "Surrogate_id_SYMB_19", "Surrogate_id_SYMB_20", 
			"Surrogate_id_SYMB_21", "Surrogate_id_SYMB_22", "Surrogate_id_SYMB_23", 
			"Surrogate_id_SYMB_24", "Surrogate_id_SYMB_25", "Surrogate_id_SYMB_26", 
			"Surrogate_id_SYMB_27", "Surrogate_id_SYMB_28", "Surrogate_id_SYMB_29", 
			"Surrogate_id_SYMB_30", "Surrogate_id_SYMB_31", "Surrogate_id_SYMB_32", 
			"Surrogate_id_SYMB_33", "Surrogate_id_SYMB_34", "Surrogate_id_SYMB_35", 
			"Surrogate_id_SYMB_36", "Surrogate_id_SYMB_37", "Surrogate_id_SYMB_38", 
			"Surrogate_id_SYMB_39", "Surrogate_id_SYMB_40", "Surrogate_id_SYMB_41", 
			"Surrogate_id_SYMB_42", "Surrogate_id_SYMB_43", "Surrogate_id_SYMB_44", 
			"Surrogate_id_SYMB_45", "Surrogate_id_SYMB_46", "Surrogate_id_SYMB_47", 
			"Surrogate_id_SYMB_48", "Surrogate_id_SYMB_49", "Surrogate_id_SYMB_50", 
			"Surrogate_id_SYMB_51", "Surrogate_id_SYMB_52", "Surrogate_id_SYMB_53", 
			"Surrogate_id_SYMB_54", "Surrogate_id_SYMB_55", "Surrogate_id_SYMB_56", 
			"Surrogate_id_SYMB_57", "Surrogate_id_SYMB_58", "Surrogate_id_SYMB_59", 
			"Surrogate_id_SYMB_60", "Surrogate_id_SYMB_61", "Surrogate_id_SYMB_62", 
			"Surrogate_id_SYMB_63", "Surrogate_id_SYMB_64", "Surrogate_id_SYMB_65", 
			"EXCEPTION", "VARIANT", "CAST", "ASSIGN", "REF_TYPE", "REFERENCE", "PANIC", 
			"THROW", "TRY", "CATCH", "TOP_TYPE", "BOTTOM_TYPE", "AUTO_TYPE", "GENERIC", 
			"FORALL", "COMMENT_antlr_builtin", "MULTICOMMENT_antlr_builtin", "StellaIdent", 
			"ExtensionName", "MemoryAddress", "INTEGER", "WS", "ErrorToken"
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


	public stellaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "stellaLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2[\u0297\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\3"+
		"\2\3\2\5\2\u00c2\n\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3"+
		"\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17"+
		"\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25"+
		"\3\26\3\26\3\26\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32"+
		"\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3 \3 \3 \3 \3"+
		" \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3\"\3\"\3\"\3"+
		"\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3$\3$\3$"+
		"\3$\3$\3$\3$\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&"+
		"\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3)\3)\3)\3)\3*\3*\3*\3+\3+\3+\3+\3+\3"+
		",\3,\3,\3,\3,\3-\3-\3-\3-\3-\3.\3.\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/\3/\3"+
		"\60\3\60\3\60\3\60\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\62\3\63\3\63\3"+
		"\63\3\64\3\64\3\64\3\65\3\65\3\65\3\65\3\66\3\66\3\66\3\66\3\66\3\66\3"+
		"\66\3\67\3\67\3\67\3\67\38\38\38\38\38\38\38\38\38\39\39\39\39\3:\3:\3"+
		":\3:\3:\3:\3:\3;\3;\3;\3;\3;\3;\3<\3<\3<\3<\3=\3=\3=\3>\3>\3>\3>\3>\3"+
		">\3>\3?\3?\3?\3?\3?\3@\3@\3@\3@\3@\3A\3A\3A\3A\3A\3A\3A\3B\3B\3B\3B\3"+
		"B\3C\3C\3C\3C\3C\3D\3D\3D\3D\3D\3D\3D\3E\3E\3E\3E\3E\3F\3F\3F\3F\3F\3"+
		"G\3G\3H\3H\3H\3H\3H\3H\3H\3H\3H\3H\3I\3I\3I\3I\3I\3I\3I\3I\3J\3J\3J\3"+
		"J\3J\3K\3K\3K\3L\3L\3M\3M\3M\3M\3N\3N\3N\3N\3N\3N\3N\3O\3O\3O\3O\3O\3"+
		"O\3P\3P\3P\3P\3Q\3Q\3Q\3Q\3Q\3Q\3R\3R\3R\3R\3S\3S\3S\3S\3T\3T\3T\3T\3"+
		"T\3U\3U\3U\3U\3U\3U\3U\3U\3V\3V\3V\3V\3V\3V\3V\3W\3W\3W\3W\7W\u0248\n"+
		"W\fW\16W\u024b\13W\3W\5W\u024e\nW\3W\3W\5W\u0252\nW\3W\3W\3X\3X\3X\3X"+
		"\7X\u025a\nX\fX\16X\u025d\13X\3X\3X\3X\3X\3X\3Y\3Y\5Y\u0266\nY\3Y\3Y\3"+
		"Y\5Y\u026b\nY\7Y\u026d\nY\fY\16Y\u0270\13Y\3Z\3Z\3Z\3Z\5Z\u0276\nZ\6Z"+
		"\u0278\nZ\rZ\16Z\u0279\3[\3[\3[\3[\3[\3[\6[\u0282\n[\r[\16[\u0283\3[\3"+
		"[\3\\\6\\\u0289\n\\\r\\\16\\\u028a\3]\6]\u028e\n]\r]\16]\u028f\3]\3]\3"+
		"^\3^\3_\3_\3\u025b\2`\3\2\5\2\7\2\t\2\13\3\r\4\17\5\21\6\23\7\25\b\27"+
		"\t\31\n\33\13\35\f\37\r!\16#\17%\20\'\21)\22+\23-\24/\25\61\26\63\27\65"+
		"\30\67\319\32;\33=\34?\35A\36C\37E G!I\"K#M$O%Q&S\'U(W)Y*[+],_-a.c/e\60"+
		"g\61i\62k\63m\64o\65q\66s\67u8w9y:{;}<\177=\u0081>\u0083?\u0085@\u0087"+
		"A\u0089B\u008bC\u008dD\u008fE\u0091F\u0093G\u0095H\u0097I\u0099J\u009b"+
		"K\u009dL\u009fM\u00a1N\u00a3O\u00a5P\u00a7Q\u00a9R\u00abS\u00adT\u00af"+
		"U\u00b1V\u00b3W\u00b5X\u00b7Y\u00b9Z\u00bb\2\u00bd[\3\2\13\5\2C\\\u00c2"+
		"\u00d8\u00da\u00e0\5\2c|\u00e1\u00f8\u00fa\u0101\3\2\62;\4\2\f\f\17\17"+
		"\7\2##//<<AAaa\4\2//aa\4\2CHch\5\2\13\f\16\17\"\"\b\2$$^^hhppttvv\2\u02a1"+
		"\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"+
		"\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2"+
		"\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2"+
		"\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3"+
		"\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2"+
		"\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2"+
		"Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3"+
		"\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2"+
		"\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2"+
		"w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2"+
		"\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2\2\u0087\3\2\2\2\2\u0089\3\2\2\2\2\u008b"+
		"\3\2\2\2\2\u008d\3\2\2\2\2\u008f\3\2\2\2\2\u0091\3\2\2\2\2\u0093\3\2\2"+
		"\2\2\u0095\3\2\2\2\2\u0097\3\2\2\2\2\u0099\3\2\2\2\2\u009b\3\2\2\2\2\u009d"+
		"\3\2\2\2\2\u009f\3\2\2\2\2\u00a1\3\2\2\2\2\u00a3\3\2\2\2\2\u00a5\3\2\2"+
		"\2\2\u00a7\3\2\2\2\2\u00a9\3\2\2\2\2\u00ab\3\2\2\2\2\u00ad\3\2\2\2\2\u00af"+
		"\3\2\2\2\2\u00b1\3\2\2\2\2\u00b3\3\2\2\2\2\u00b5\3\2\2\2\2\u00b7\3\2\2"+
		"\2\2\u00b9\3\2\2\2\2\u00bd\3\2\2\2\3\u00c1\3\2\2\2\5\u00c3\3\2\2\2\7\u00c5"+
		"\3\2\2\2\t\u00c7\3\2\2\2\13\u00c9\3\2\2\2\r\u00cb\3\2\2\2\17\u00cd\3\2"+
		"\2\2\21\u00cf\3\2\2\2\23\u00d1\3\2\2\2\25\u00d3\3\2\2\2\27\u00d5\3\2\2"+
		"\2\31\u00d7\3\2\2\2\33\u00d9\3\2\2\2\35\u00dc\3\2\2\2\37\u00df\3\2\2\2"+
		"!\u00e1\3\2\2\2#\u00e4\3\2\2\2%\u00e7\3\2\2\2\'\u00e9\3\2\2\2)\u00eb\3"+
		"\2\2\2+\u00ed\3\2\2\2-\u00f0\3\2\2\2/\u00f2\3\2\2\2\61\u00f5\3\2\2\2\63"+
		"\u00f8\3\2\2\2\65\u00fb\3\2\2\2\67\u00fd\3\2\2\29\u00ff\3\2\2\2;\u0101"+
		"\3\2\2\2=\u0103\3\2\2\2?\u0105\3\2\2\2A\u0110\3\2\2\2C\u011e\3\2\2\2E"+
		"\u0129\3\2\2\2G\u0133\3\2\2\2I\u013f\3\2\2\2K\u0148\3\2\2\2M\u014d\3\2"+
		"\2\2O\u0151\3\2\2\2Q\u0156\3\2\2\2S\u015a\3\2\2\2U\u015d\3\2\2\2W\u0162"+
		"\3\2\2\2Y\u0167\3\2\2\2[\u016c\3\2\2\2]\u0173\3\2\2\2_\u0179\3\2\2\2a"+
		"\u017d\3\2\2\2c\u0180\3\2\2\2e\u0185\3\2\2\2g\u0188\3\2\2\2i\u018b\3\2"+
		"\2\2k\u018f\3\2\2\2m\u0196\3\2\2\2o\u019a\3\2\2\2q\u01a3\3\2\2\2s\u01a7"+
		"\3\2\2\2u\u01ae\3\2\2\2w\u01b4\3\2\2\2y\u01b8\3\2\2\2{\u01bb\3\2\2\2}"+
		"\u01c2\3\2\2\2\177\u01c7\3\2\2\2\u0081\u01cc\3\2\2\2\u0083\u01d3\3\2\2"+
		"\2\u0085\u01d8\3\2\2\2\u0087\u01dd\3\2\2\2\u0089\u01e4\3\2\2\2\u008b\u01e9"+
		"\3\2\2\2\u008d\u01ee\3\2\2\2\u008f\u01f0\3\2\2\2\u0091\u01fa\3\2\2\2\u0093"+
		"\u0202\3\2\2\2\u0095\u0207\3\2\2\2\u0097\u020a\3\2\2\2\u0099\u020c\3\2"+
		"\2\2\u009b\u0210\3\2\2\2\u009d\u0217\3\2\2\2\u009f\u021d\3\2\2\2\u00a1"+
		"\u0221\3\2\2\2\u00a3\u0227\3\2\2\2\u00a5\u022b\3\2\2\2\u00a7\u022f\3\2"+
		"\2\2\u00a9\u0234\3\2\2\2\u00ab\u023c\3\2\2\2\u00ad\u0243\3\2\2\2\u00af"+
		"\u0255\3\2\2\2\u00b1\u0265\3\2\2\2\u00b3\u0271\3\2\2\2\u00b5\u027b\3\2"+
		"\2\2\u00b7\u0288\3\2\2\2\u00b9\u028d\3\2\2\2\u00bb\u0293\3\2\2\2\u00bd"+
		"\u0295\3\2\2\2\u00bf\u00c2\5\5\3\2\u00c0\u00c2\5\7\4\2\u00c1\u00bf\3\2"+
		"\2\2\u00c1\u00c0\3\2\2\2\u00c2\4\3\2\2\2\u00c3\u00c4\t\2\2\2\u00c4\6\3"+
		"\2\2\2\u00c5\u00c6\t\3\2\2\u00c6\b\3\2\2\2\u00c7\u00c8\t\4\2\2\u00c8\n"+
		"\3\2\2\2\u00c9\u00ca\7.\2\2\u00ca\f\3\2\2\2\u00cb\u00cc\7=\2\2\u00cc\16"+
		"\3\2\2\2\u00cd\u00ce\7*\2\2\u00ce\20\3\2\2\2\u00cf\u00d0\7+\2\2\u00d0"+
		"\22\3\2\2\2\u00d1\u00d2\7}\2\2\u00d2\24\3\2\2\2\u00d3\u00d4\7\177\2\2"+
		"\u00d4\26\3\2\2\2\u00d5\u00d6\7?\2\2\u00d6\30\3\2\2\2\u00d7\u00d8\7<\2"+
		"\2\u00d8\32\3\2\2\2\u00d9\u00da\7/\2\2\u00da\u00db\7@\2\2\u00db\34\3\2"+
		"\2\2\u00dc\u00dd\7?\2\2\u00dd\u00de\7@\2\2\u00de\36\3\2\2\2\u00df\u00e0"+
		"\7~\2\2\u00e0 \3\2\2\2\u00e1\u00e2\7>\2\2\u00e2\u00e3\7~\2\2\u00e3\"\3"+
		"\2\2\2\u00e4\u00e5\7~\2\2\u00e5\u00e6\7@\2\2\u00e6$\3\2\2\2\u00e7\u00e8"+
		"\7]\2\2\u00e8&\3\2\2\2\u00e9\u00ea\7_\2\2\u00ea(\3\2\2\2\u00eb\u00ec\7"+
		">\2\2\u00ec*\3\2\2\2\u00ed\u00ee\7>\2\2\u00ee\u00ef\7?\2\2\u00ef,\3\2"+
		"\2\2\u00f0\u00f1\7@\2\2\u00f1.\3\2\2\2\u00f2\u00f3\7@\2\2\u00f3\u00f4"+
		"\7?\2\2\u00f4\60\3\2\2\2\u00f5\u00f6\7?\2\2\u00f6\u00f7\7?\2\2\u00f7\62"+
		"\3\2\2\2\u00f8\u00f9\7#\2\2\u00f9\u00fa\7?\2\2\u00fa\64\3\2\2\2\u00fb"+
		"\u00fc\7-\2\2\u00fc\66\3\2\2\2\u00fd\u00fe\7/\2\2\u00fe8\3\2\2\2\u00ff"+
		"\u0100\7,\2\2\u0100:\3\2\2\2\u0101\u0102\7\61\2\2\u0102<\3\2\2\2\u0103"+
		"\u0104\7\60\2\2\u0104>\3\2\2\2\u0105\u0106\7N\2\2\u0106\u0107\7k\2\2\u0107"+
		"\u0108\7u\2\2\u0108\u0109\7v\2\2\u0109\u010a\7<\2\2\u010a\u010b\7<\2\2"+
		"\u010b\u010c\7j\2\2\u010c\u010d\7g\2\2\u010d\u010e\7c\2\2\u010e\u010f"+
		"\7f\2\2\u010f@\3\2\2\2\u0110\u0111\7N\2\2\u0111\u0112\7k\2\2\u0112\u0113"+
		"\7u\2\2\u0113\u0114\7v\2\2\u0114\u0115\7<\2\2\u0115\u0116\7<\2\2\u0116"+
		"\u0117\7k\2\2\u0117\u0118\7u\2\2\u0118\u0119\7g\2\2\u0119\u011a\7o\2\2"+
		"\u011a\u011b\7r\2\2\u011b\u011c\7v\2\2\u011c\u011d\7{\2\2\u011dB\3\2\2"+
		"\2\u011e\u011f\7N\2\2\u011f\u0120\7k\2\2\u0120\u0121\7u\2\2\u0121\u0122"+
		"\7v\2\2\u0122\u0123\7<\2\2\u0123\u0124\7<\2\2\u0124\u0125\7v\2\2\u0125"+
		"\u0126\7c\2\2\u0126\u0127\7k\2\2\u0127\u0128\7n\2\2\u0128D\3\2\2\2\u0129"+
		"\u012a\7P\2\2\u012a\u012b\7c\2\2\u012b\u012c\7v\2\2\u012c\u012d\7<\2\2"+
		"\u012d\u012e\7<\2\2\u012e\u012f\7r\2\2\u012f\u0130\7t\2\2\u0130\u0131"+
		"\7g\2\2\u0131\u0132\7f\2\2\u0132F\3\2\2\2\u0133\u0134\7P\2\2\u0134\u0135"+
		"\7c\2\2\u0135\u0136\7v\2\2\u0136\u0137\7<\2\2\u0137\u0138\7<\2\2\u0138"+
		"\u0139\7k\2\2\u0139\u013a\7u\2\2\u013a\u013b\7|\2\2\u013b\u013c\7g\2\2"+
		"\u013c\u013d\7t\2\2\u013d\u013e\7q\2\2\u013eH\3\2\2\2\u013f\u0140\7P\2"+
		"\2\u0140\u0141\7c\2\2\u0141\u0142\7v\2\2\u0142\u0143\7<\2\2\u0143\u0144"+
		"\7<\2\2\u0144\u0145\7t\2\2\u0145\u0146\7g\2\2\u0146\u0147\7e\2\2\u0147"+
		"J\3\2\2\2\u0148\u0149\7D\2\2\u0149\u014a\7q\2\2\u014a\u014b\7q\2\2\u014b"+
		"\u014c\7n\2\2\u014cL\3\2\2\2\u014d\u014e\7P\2\2\u014e\u014f\7c\2\2\u014f"+
		"\u0150\7v\2\2\u0150N\3\2\2\2\u0151\u0152\7W\2\2\u0152\u0153\7p\2\2\u0153"+
		"\u0154\7k\2\2\u0154\u0155\7v\2\2\u0155P\3\2\2\2\u0156\u0157\7c\2\2\u0157"+
		"\u0158\7p\2\2\u0158\u0159\7f\2\2\u0159R\3\2\2\2\u015a\u015b\7c\2\2\u015b"+
		"\u015c\7u\2\2\u015cT\3\2\2\2\u015d\u015e\7e\2\2\u015e\u015f\7q\2\2\u015f"+
		"\u0160\7p\2\2\u0160\u0161\7u\2\2\u0161V\3\2\2\2\u0162\u0163\7e\2\2\u0163"+
		"\u0164\7q\2\2\u0164\u0165\7t\2\2\u0165\u0166\7g\2\2\u0166X\3\2\2\2\u0167"+
		"\u0168\7g\2\2\u0168\u0169\7n\2\2\u0169\u016a\7u\2\2\u016a\u016b\7g\2\2"+
		"\u016bZ\3\2\2\2\u016c\u016d\7g\2\2\u016d\u016e\7z\2\2\u016e\u016f\7v\2"+
		"\2\u016f\u0170\7g\2\2\u0170\u0171\7p\2\2\u0171\u0172\7f\2\2\u0172\\\3"+
		"\2\2\2\u0173\u0174\7h\2\2\u0174\u0175\7c\2\2\u0175\u0176\7n\2\2\u0176"+
		"\u0177\7u\2\2\u0177\u0178\7g\2\2\u0178^\3\2\2\2\u0179\u017a\7h\2\2\u017a"+
		"\u017b\7k\2\2\u017b\u017c\7z\2\2\u017c`\3\2\2\2\u017d\u017e\7h\2\2\u017e"+
		"\u017f\7p\2\2\u017fb\3\2\2\2\u0180\u0181\7h\2\2\u0181\u0182\7q\2\2\u0182"+
		"\u0183\7n\2\2\u0183\u0184\7f\2\2\u0184d\3\2\2\2\u0185\u0186\7k\2\2\u0186"+
		"\u0187\7h\2\2\u0187f\3\2\2\2\u0188\u0189\7k\2\2\u0189\u018a\7p\2\2\u018a"+
		"h\3\2\2\2\u018b\u018c\7k\2\2\u018c\u018d\7p\2\2\u018d\u018e\7n\2\2\u018e"+
		"j\3\2\2\2\u018f\u0190\7k\2\2\u0190\u0191\7p\2\2\u0191\u0192\7n\2\2\u0192"+
		"\u0193\7k\2\2\u0193\u0194\7p\2\2\u0194\u0195\7g\2\2\u0195l\3\2\2\2\u0196"+
		"\u0197\7k\2\2\u0197\u0198\7p\2\2\u0198\u0199\7t\2\2\u0199n\3\2\2\2\u019a"+
		"\u019b\7n\2\2\u019b\u019c\7c\2\2\u019c\u019d\7p\2\2\u019d\u019e\7i\2\2"+
		"\u019e\u019f\7w\2\2\u019f\u01a0\7c\2\2\u01a0\u01a1\7i\2\2\u01a1\u01a2"+
		"\7g\2\2\u01a2p\3\2\2\2\u01a3\u01a4\7n\2\2\u01a4\u01a5\7g\2\2\u01a5\u01a6"+
		"\7v\2\2\u01a6r\3\2\2\2\u01a7\u01a8\7n\2\2\u01a8\u01a9\7g\2\2\u01a9\u01aa"+
		"\7v\2\2\u01aa\u01ab\7t\2\2\u01ab\u01ac\7g\2\2\u01ac\u01ad\7e\2\2\u01ad"+
		"t\3\2\2\2\u01ae\u01af\7o\2\2\u01af\u01b0\7c\2\2\u01b0\u01b1\7v\2\2\u01b1"+
		"\u01b2\7e\2\2\u01b2\u01b3\7j\2\2\u01b3v\3\2\2\2\u01b4\u01b5\7p\2\2\u01b5"+
		"\u01b6\7q\2\2\u01b6\u01b7\7v\2\2\u01b7x\3\2\2\2\u01b8\u01b9\7q\2\2\u01b9"+
		"\u01ba\7t\2\2\u01baz\3\2\2\2\u01bb\u01bc\7t\2\2\u01bc\u01bd\7g\2\2\u01bd"+
		"\u01be\7v\2\2\u01be\u01bf\7w\2\2\u01bf\u01c0\7t\2\2\u01c0\u01c1\7p\2\2"+
		"\u01c1|\3\2\2\2\u01c2\u01c3\7u\2\2\u01c3\u01c4\7w\2\2\u01c4\u01c5\7e\2"+
		"\2\u01c5\u01c6\7e\2\2\u01c6~\3\2\2\2\u01c7\u01c8\7v\2\2\u01c8\u01c9\7"+
		"j\2\2\u01c9\u01ca\7g\2\2\u01ca\u01cb\7p\2\2\u01cb\u0080\3\2\2\2\u01cc"+
		"\u01cd\7v\2\2\u01cd\u01ce\7j\2\2\u01ce\u01cf\7t\2\2\u01cf\u01d0\7q\2\2"+
		"\u01d0\u01d1\7y\2\2\u01d1\u01d2\7u\2\2\u01d2\u0082\3\2\2\2\u01d3\u01d4"+
		"\7v\2\2\u01d4\u01d5\7t\2\2\u01d5\u01d6\7w\2\2\u01d6\u01d7\7g\2\2\u01d7"+
		"\u0084\3\2\2\2\u01d8\u01d9\7v\2\2\u01d9\u01da\7{\2\2\u01da\u01db\7r\2"+
		"\2\u01db\u01dc\7g\2\2\u01dc\u0086\3\2\2\2\u01dd\u01de\7w\2\2\u01de\u01df"+
		"\7p\2\2\u01df\u01e0\7h\2\2\u01e0\u01e1\7q\2\2\u01e1\u01e2\7n\2\2\u01e2"+
		"\u01e3\7f\2\2\u01e3\u0088\3\2\2\2\u01e4\u01e5\7w\2\2\u01e5\u01e6\7p\2"+
		"\2\u01e6\u01e7\7k\2\2\u01e7\u01e8\7v\2\2\u01e8\u008a\3\2\2\2\u01e9\u01ea"+
		"\7y\2\2\u01ea\u01eb\7k\2\2\u01eb\u01ec\7v\2\2\u01ec\u01ed\7j\2\2\u01ed"+
		"\u008c\3\2\2\2\u01ee\u01ef\7\u00b7\2\2\u01ef\u008e\3\2\2\2\u01f0\u01f1"+
		"\7g\2\2\u01f1\u01f2\7z\2\2\u01f2\u01f3\7e\2\2\u01f3\u01f4\7g\2\2\u01f4"+
		"\u01f5\7r\2\2\u01f5\u01f6\7v\2\2\u01f6\u01f7\7k\2\2\u01f7\u01f8\7q\2\2"+
		"\u01f8\u01f9\7p\2\2\u01f9\u0090\3\2\2\2\u01fa\u01fb\7x\2\2\u01fb\u01fc"+
		"\7c\2\2\u01fc\u01fd\7t\2\2\u01fd\u01fe\7k\2\2\u01fe\u01ff\7c\2\2\u01ff"+
		"\u0200\7p\2\2\u0200\u0201\7v\2\2\u0201\u0092\3\2\2\2\u0202\u0203\7e\2"+
		"\2\u0203\u0204\7c\2\2\u0204\u0205\7u\2\2\u0205\u0206\7v\2\2\u0206\u0094"+
		"\3\2\2\2\u0207\u0208\7<\2\2\u0208\u0209\7?\2\2\u0209\u0096\3\2\2\2\u020a"+
		"\u020b\7(\2\2\u020b\u0098\3\2\2\2\u020c\u020d\7p\2\2\u020d\u020e\7g\2"+
		"\2\u020e\u020f\7y\2\2\u020f\u009a\3\2\2\2\u0210\u0211\7r\2\2\u0211\u0212"+
		"\7c\2\2\u0212\u0213\7p\2\2\u0213\u0214\7k\2\2\u0214\u0215\7e\2\2\u0215"+
		"\u0216\7#\2\2\u0216\u009c\3\2\2\2\u0217\u0218\7v\2\2\u0218\u0219\7j\2"+
		"\2\u0219\u021a\7t\2\2\u021a\u021b\7q\2\2\u021b\u021c\7y\2\2\u021c\u009e"+
		"\3\2\2\2\u021d\u021e\7v\2\2\u021e\u021f\7t\2\2\u021f\u0220\7{\2\2\u0220"+
		"\u00a0\3\2\2\2\u0221\u0222\7e\2\2\u0222\u0223\7c\2\2\u0223\u0224\7v\2"+
		"\2\u0224\u0225\7e\2\2\u0225\u0226\7j\2\2\u0226\u00a2\3\2\2\2\u0227\u0228"+
		"\7V\2\2\u0228\u0229\7q\2\2\u0229\u022a\7r\2\2\u022a\u00a4\3\2\2\2\u022b"+
		"\u022c\7D\2\2\u022c\u022d\7q\2\2\u022d\u022e\7v\2\2\u022e\u00a6\3\2\2"+
		"\2\u022f\u0230\7c\2\2\u0230\u0231\7w\2\2\u0231\u0232\7v\2\2\u0232\u0233"+
		"\7q\2\2\u0233\u00a8\3\2\2\2\u0234\u0235\7i\2\2\u0235\u0236\7g\2\2\u0236"+
		"\u0237\7p\2\2\u0237\u0238\7g\2\2\u0238\u0239\7t\2\2\u0239\u023a\7k\2\2"+
		"\u023a\u023b\7e\2\2\u023b\u00aa\3\2\2\2\u023c\u023d\7h\2\2\u023d\u023e"+
		"\7q\2\2\u023e\u023f\7t\2\2\u023f\u0240\7c\2\2\u0240\u0241\7n\2\2\u0241"+
		"\u0242\7n\2\2\u0242\u00ac\3\2\2\2\u0243\u0244\7\61\2\2\u0244\u0245\7\61"+
		"\2\2\u0245\u0249\3\2\2\2\u0246\u0248\n\5\2\2\u0247\u0246\3\2\2\2\u0248"+
		"\u024b\3\2\2\2\u0249\u0247\3\2\2\2\u0249\u024a\3\2\2\2\u024a\u0251\3\2"+
		"\2\2\u024b\u0249\3\2\2\2\u024c\u024e\7\17\2\2\u024d\u024c\3\2\2\2\u024d"+
		"\u024e\3\2\2\2\u024e\u024f\3\2\2\2\u024f\u0252\7\f\2\2\u0250\u0252\7\2"+
		"\2\3\u0251\u024d\3\2\2\2\u0251\u0250\3\2\2\2\u0252\u0253\3\2\2\2\u0253"+
		"\u0254\bW\2\2\u0254\u00ae\3\2\2\2\u0255\u0256\7\61\2\2\u0256\u0257\7,"+
		"\2\2\u0257\u025b\3\2\2\2\u0258\u025a\13\2\2\2\u0259\u0258\3\2\2\2\u025a"+
		"\u025d\3\2\2\2\u025b\u025c\3\2\2\2\u025b\u0259\3\2\2\2\u025c\u025e\3\2"+
		"\2\2\u025d\u025b\3\2\2\2\u025e\u025f\7,\2\2\u025f\u0260\7\61\2\2\u0260"+
		"\u0261\3\2\2\2\u0261\u0262\bX\2\2\u0262\u00b0\3\2\2\2\u0263\u0266\7a\2"+
		"\2\u0264\u0266\5\3\2\2\u0265\u0263\3\2\2\2\u0265\u0264\3\2\2\2\u0266\u026e"+
		"\3\2\2\2\u0267\u026d\t\6\2\2\u0268\u026b\5\t\5\2\u0269\u026b\5\3\2\2\u026a"+
		"\u0268\3\2\2\2\u026a\u0269\3\2\2\2\u026b\u026d\3\2\2\2\u026c\u0267\3\2"+
		"\2\2\u026c\u026a\3\2\2\2\u026d\u0270\3\2\2\2\u026e\u026c\3\2\2\2\u026e"+
		"\u026f\3\2\2\2\u026f\u00b2\3\2\2\2\u0270\u026e\3\2\2\2\u0271\u0277\7%"+
		"\2\2\u0272\u0278\t\7\2\2\u0273\u0276\5\t\5\2\u0274\u0276\5\3\2\2\u0275"+
		"\u0273\3\2\2\2\u0275\u0274\3\2\2\2\u0276\u0278\3\2\2\2\u0277\u0272\3\2"+
		"\2\2\u0277\u0275\3\2\2\2\u0278\u0279\3\2\2\2\u0279\u0277\3\2\2\2\u0279"+
		"\u027a\3\2\2\2\u027a\u00b4\3\2\2\2\u027b\u027c\7>\2\2\u027c\u027d\7\62"+
		"\2\2\u027d\u027e\7z\2\2\u027e\u0281\3\2\2\2\u027f\u0282\5\t\5\2\u0280"+
		"\u0282\t\b\2\2\u0281\u027f\3\2\2\2\u0281\u0280\3\2\2\2\u0282\u0283\3\2"+
		"\2\2\u0283\u0281\3\2\2\2\u0283\u0284\3\2\2\2\u0284\u0285\3\2\2\2\u0285"+
		"\u0286\7@\2\2\u0286\u00b6\3\2\2\2\u0287\u0289\5\t\5\2\u0288\u0287\3\2"+
		"\2\2\u0289\u028a\3\2\2\2\u028a\u0288\3\2\2\2\u028a\u028b\3\2\2\2\u028b"+
		"\u00b8\3\2\2\2\u028c\u028e\t\t\2\2\u028d\u028c\3\2\2\2\u028e\u028f\3\2"+
		"\2\2\u028f\u028d\3\2\2\2\u028f\u0290\3\2\2\2\u0290\u0291\3\2\2\2\u0291"+
		"\u0292\b]\2\2\u0292\u00ba\3\2\2\2\u0293\u0294\t\n\2\2\u0294\u00bc\3\2"+
		"\2\2\u0295\u0296\13\2\2\2\u0296\u00be\3\2\2\2\23\2\u00c1\u0249\u024d\u0251"+
		"\u025b\u0265\u026a\u026c\u026e\u0275\u0277\u0279\u0281\u0283\u028a\u028f"+
		"\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}