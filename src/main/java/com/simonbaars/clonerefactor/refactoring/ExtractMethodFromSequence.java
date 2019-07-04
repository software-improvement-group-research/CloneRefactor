package com.simonbaars.clonerefactor.refactoring;

import java.util.List;
import java.util.Random;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.VoidType;
import com.simonbaars.clonerefactor.ast.interfaces.RequiresNodeOperations;
import com.simonbaars.clonerefactor.metrics.enums.CloneRefactorability.Refactorability;
import com.simonbaars.clonerefactor.metrics.enums.CloneRelation.RelationType;
import com.simonbaars.clonerefactor.metrics.enums.RequiresNodeContext;
import com.simonbaars.clonerefactor.model.Sequence;
import com.simonbaars.clonerefactor.model.location.Location;

public class ExtractMethodFromSequence implements RequiresNodeContext, RequiresNodeOperations {
	private final Random rand = new Random();
	
	public void tryToExtractMethod(Sequence s) {
		Refactorability ref = s.getRefactorability();
		if(ref == Refactorability.CANBEEXTRACTED) {
			RelationType relation = s.getRelationType();
			MethodDeclaration decl = new MethodDeclaration(Modifier.createModifierList(Keyword.PRIVATE), getReturnType(s.getAny()), "cloneRefactor"+rand.nextInt());
			s.getAny().getContents().getNodes().forEach(node -> decl.getBody().get().addStatement((Statement)node));
			if(relation == RelationType.SAMECLASS || relation == RelationType.SAMEMETHOD) {
				ClassOrInterfaceDeclaration cd = getClass(s.getAny().getContents().getNodes().get(0));
				cd.getMembers().add(decl);
			}
			List<Node> lowestNodes = lowestNodes(s.getAny().getContents().getNodes());
			lowestNodes.forEach(n -> lowestNodes.get(0).getParentNode().get().remove(n));
			// TODO: Add the new method call!
		}
	}

	private Type getReturnType(Location any) {
		Node lastNode = any.getContents().getNodes().get(any.getContents().getNodes().size()-1);
		if(lastNode instanceof ReturnStmt) {
			MethodDeclaration d = getMethod(lastNode);
			if(d != null)
				return d.getType();
		}
		return new VoidType();
	}
}
