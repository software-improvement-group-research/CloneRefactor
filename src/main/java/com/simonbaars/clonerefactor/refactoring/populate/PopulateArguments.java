package com.simonbaars.clonerefactor.refactoring.populate;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.resolution.types.ResolvedType;
import com.simonbaars.clonerefactor.refactoring.visitor.DeclaresVariableVisitor;
import com.simonbaars.clonerefactor.refactoring.visitor.VariableVisitor;

public class PopulateArguments implements PopulatesExtractedMethod {
	public PopulateArguments() {}
	
	@Override
	public void prePopulate(MethodDeclaration extractedMethod, List<Node> topLevel) {
		HashMap<NameExpr, ResolvedType> usedVariables = new HashMap<>();
		topLevel.forEach(n -> n.accept(new VariableVisitor(), usedVariables));
		for(Entry<NameExpr, ResolvedType> var : usedVariables.entrySet()) {
			if(!declaresVariable(extractedMethod, var.getKey())) {
				extractedMethod.addParameter(var.getValue().describe(), var.getKey().getNameAsString());
			}
		}
	}

	@Override
	public void modifyMethodCall(MethodCallExpr expr) {
		// Does not modify method call
	}

	@Override
	public void postPopulate(MethodDeclaration extractedMethod) {
		// Does not post populate
	}

	private boolean declaresVariable(MethodDeclaration extractedMethod, NameExpr varName) {
		Boolean b = extractedMethod.accept(new DeclaresVariableVisitor(), varName);
		if(b == null)
			return false;
		return b;
	}

}
