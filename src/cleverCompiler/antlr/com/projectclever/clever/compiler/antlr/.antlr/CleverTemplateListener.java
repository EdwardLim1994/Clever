// Generated from c:/Users/User/Desktop/projects/clever/src/cleverCompiler/antlr/com/projectclever/clever/compiler/antlr/CleverTemplate.g4 by ANTLR 4.13.1

package com.projectclever.clever.compiler.antlr;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CleverTemplateParser}.
 */
public interface CleverTemplateListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CleverTemplateParser#templateFile}.
	 * @param ctx the parse tree
	 */
	void enterTemplateFile(CleverTemplateParser.TemplateFileContext ctx);
	/**
	 * Exit a parse tree produced by {@link CleverTemplateParser#templateFile}.
	 * @param ctx the parse tree
	 */
	void exitTemplateFile(CleverTemplateParser.TemplateFileContext ctx);
	/**
	 * Enter a parse tree produced by {@link CleverTemplateParser#templateDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTemplateDeclaration(CleverTemplateParser.TemplateDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CleverTemplateParser#templateDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTemplateDeclaration(CleverTemplateParser.TemplateDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CleverTemplateParser#codeBlock}.
	 * @param ctx the parse tree
	 */
	void enterCodeBlock(CleverTemplateParser.CodeBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link CleverTemplateParser#codeBlock}.
	 * @param ctx the parse tree
	 */
	void exitCodeBlock(CleverTemplateParser.CodeBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link CleverTemplateParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(CleverTemplateParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link CleverTemplateParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(CleverTemplateParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link CleverTemplateParser#blockContent}.
	 * @param ctx the parse tree
	 */
	void enterBlockContent(CleverTemplateParser.BlockContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link CleverTemplateParser#blockContent}.
	 * @param ctx the parse tree
	 */
	void exitBlockContent(CleverTemplateParser.BlockContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link CleverTemplateParser#nestedBlock}.
	 * @param ctx the parse tree
	 */
	void enterNestedBlock(CleverTemplateParser.NestedBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link CleverTemplateParser#nestedBlock}.
	 * @param ctx the parse tree
	 */
	void exitNestedBlock(CleverTemplateParser.NestedBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link CleverTemplateParser#templateBlock}.
	 * @param ctx the parse tree
	 */
	void enterTemplateBlock(CleverTemplateParser.TemplateBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link CleverTemplateParser#templateBlock}.
	 * @param ctx the parse tree
	 */
	void exitTemplateBlock(CleverTemplateParser.TemplateBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link CleverTemplateParser#templateBody}.
	 * @param ctx the parse tree
	 */
	void enterTemplateBody(CleverTemplateParser.TemplateBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link CleverTemplateParser#templateBody}.
	 * @param ctx the parse tree
	 */
	void exitTemplateBody(CleverTemplateParser.TemplateBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link CleverTemplateParser#templateContent}.
	 * @param ctx the parse tree
	 */
	void enterTemplateContent(CleverTemplateParser.TemplateContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link CleverTemplateParser#templateContent}.
	 * @param ctx the parse tree
	 */
	void exitTemplateContent(CleverTemplateParser.TemplateContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link CleverTemplateParser#nestedTemplateBlock}.
	 * @param ctx the parse tree
	 */
	void enterNestedTemplateBlock(CleverTemplateParser.NestedTemplateBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link CleverTemplateParser#nestedTemplateBlock}.
	 * @param ctx the parse tree
	 */
	void exitNestedTemplateBlock(CleverTemplateParser.NestedTemplateBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link CleverTemplateParser#interpolation}.
	 * @param ctx the parse tree
	 */
	void enterInterpolation(CleverTemplateParser.InterpolationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CleverTemplateParser#interpolation}.
	 * @param ctx the parse tree
	 */
	void exitInterpolation(CleverTemplateParser.InterpolationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CleverTemplateParser#expressionContent}.
	 * @param ctx the parse tree
	 */
	void enterExpressionContent(CleverTemplateParser.ExpressionContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link CleverTemplateParser#expressionContent}.
	 * @param ctx the parse tree
	 */
	void exitExpressionContent(CleverTemplateParser.ExpressionContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link CleverTemplateParser#parenGroup}.
	 * @param ctx the parse tree
	 */
	void enterParenGroup(CleverTemplateParser.ParenGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link CleverTemplateParser#parenGroup}.
	 * @param ctx the parse tree
	 */
	void exitParenGroup(CleverTemplateParser.ParenGroupContext ctx);
}