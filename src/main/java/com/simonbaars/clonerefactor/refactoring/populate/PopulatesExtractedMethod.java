package com.simonbaars.clonerefactor.refactoring.populate;

import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.simonbaars.clonerefactor.detection.model.Sequence;

public interface PopulatesExtractedMethod {
	/**
	 * Ran before the extractedMethod is populated. This method is called only once per Sequence.
	 * @param extractedMethod The extracted method that will be populated right after.
	 * @param topLevel The nodes of the clone right before they are being moved to the extractedMethod.
	 */
	public void prePopulate(MethodDeclaration extractedMethod, List<Node> topLevel);
	/**
	 * Gives the opportunity to modify the method call while it is being added to each location. This method is called for each location in each Sequence.
	 * @param expr The method call to the newly refactored method that has just been added to the location.
	 */
	public Optional<Statement> modifyMethodCall(Sequence s, MethodCallExpr expr);
	/**
	 * Ran after the extractedMethod is populated and placed at the appropriate location. This method is called only once per Sequence.
	 * @param extractedMethod The extracted method that is currently located at its appropriate location in the code.
	 */
	public void postPopulate(Sequence s, MethodDeclaration extractedMethod);
}
