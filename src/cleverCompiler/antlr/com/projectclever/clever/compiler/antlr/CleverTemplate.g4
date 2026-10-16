grammar CleverTemplate;

@header {
package com.projectclever.clever.compiler.antlr;
}

templateFile
	: templateDeclaration REQUIRED_ARGS_CONSTRUCTOR? codeBlock templateBlock EOF
	;

templateDeclaration
	: PAGE IDENTIFIER
	| COMPONENT IDENTIFIER
	| LAYOUT IDENTIFIER
	;

codeBlock
	: CODE block
	;

block
	: LBRACE blockContent* RBRACE
	;

blockContent
	: nestedBlock
	| QUOTED
	| TEXT_BLOCK
	| LINE_COMMENT
	| BLOCK_COMMENT
	| HTML_COMMENT
	| ~(LBRACE | RBRACE)
	;

nestedBlock
	: LBRACE blockContent* RBRACE
	;

templateBlock
	: TEMPLATE templateBody
	;

templateBody
	: LBRACE templateContent* RBRACE
	;

templateContent
	: nestedTemplateBlock
	| interpolation
	| QUOTED
	| TEXT_BLOCK
	| LINE_COMMENT
	| BLOCK_COMMENT
	| HTML_COMMENT
	| ~(LBRACE | RBRACE)
	;

nestedTemplateBlock
	: LBRACE templateContent* RBRACE
	;

interpolation
	: INTERPOLATION_START expressionContent* RPAREN
	;

expressionContent
	: parenGroup
	| QUOTED
	| TEXT_BLOCK
	| LINE_COMMENT
	| BLOCK_COMMENT
	| HTML_COMMENT
	| ~(LPAREN | RPAREN)
	;

parenGroup
	: LPAREN expressionContent* RPAREN
	;

PAGE: '@Page';
COMPONENT: '@Component';
LAYOUT: '@Layout';
CODE: '@Code';
TEMPLATE: '@Template';
REQUIRED_ARGS_CONSTRUCTOR: '@RequiredArgsConstructor';
INTERPOLATION_START: '@(';
LBRACE: '{';
RBRACE: '}';
LPAREN: '(';
RPAREN: ')';
IMPORT: 'import';
SEMICOLON: ';';
TEXT_BLOCK: '"""' .*? '"""';
QUOTED: '"' ('\\' . | ~["\\])* '"'
	| '\'' ('\\' . | ~['\\])* '\''
	;
HTML_COMMENT: '<!--' .*? '-->';
BLOCK_COMMENT: '/*' .*? '*/';
LINE_COMMENT: '//' ~[\r\n]*;
IDENTIFIER: [a-zA-Z_$] [a-zA-Z0-9_$]*;
WHITESPACE: [ \t\r\n]+ -> channel(HIDDEN);
OTHER: .;
