grammar CleverTemplate;

@header {
package com.projectclever.clever.compiler.antlr;
}

templateFile
	: templateDeclaration inputsDirective? REQUIRED_ARGS_CONSTRUCTOR? codeBlock templateBlock EOF
	;

templateDeclaration
	: PAGE IDENTIFIER
	| COMPONENT IDENTIFIER
	| LAYOUT IDENTIFIER
	;

inputsDirective
	: INPUTS LPAREN inputParameter (COMMA inputParameter)* RPAREN
	;

inputParameter
	: javaType IDENTIFIER
	;

javaType
	: IDENTIFIER (DOT IDENTIFIER)* (LT javaType (COMMA javaType)* GT)? (LBRACK RBRACK)*
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
	: componentInvocation
	| layoutInvocation
	| slotDeclaration
	| nestedTemplateBlock
	| interpolation
	| QUOTED
	| TEXT_BLOCK
	| LINE_COMMENT
	| BLOCK_COMMENT
	| HTML_COMMENT
	| ~(LBRACE | RBRACE)
	;

componentInvocation
	: COMPONENT LPAREN qualifiedName DOT CLASS (COMMA javaArgumentList)? RPAREN
	;

layoutInvocation
	: LAYOUT LPAREN qualifiedName DOT CLASS (COMMA javaArgumentList)? RPAREN
	  LBRACE slotAssignment* RBRACE
	;

slotAssignment
	: SLOT LPAREN QUOTED RPAREN LBRACE templateContent* RBRACE
	;

slotDeclaration
	: SLOT LPAREN QUOTED RPAREN
	;

qualifiedName
	: IDENTIFIER (DOT IDENTIFIER)*
	;

javaArgumentList
	: javaExpression (COMMA javaExpression)*
	;

javaExpression
	: expressionContent+
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
	| ~(LPAREN | RPAREN | COMMA)
	;

parenGroup
	: LPAREN expressionContent* RPAREN
	;

PAGE: '@Page';
COMPONENT: '@Component';
LAYOUT: '@Layout';
SLOT: '@Slot';
INPUTS: '@Inputs';
CODE: '@Code';
TEMPLATE: '@Template';
REQUIRED_ARGS_CONSTRUCTOR: '@RequiredArgsConstructor';
INTERPOLATION_START: '@(';
LBRACE: '{';
RBRACE: '}';
LPAREN: '(';
RPAREN: ')';
IMPORT: 'import';
CLASS: 'class';
DOT: '.';
COMMA: ',';
LT: '<';
GT: '>';
LBRACK: '[';
RBRACK: ']';
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
