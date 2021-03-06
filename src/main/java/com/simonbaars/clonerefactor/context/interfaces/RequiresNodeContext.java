package com.simonbaars.clonerefactor.context.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;
import com.simonbaars.clonerefactor.datatype.SimpleEqualsSet;

public interface RequiresNodeContext {
	public default Optional<MethodDeclaration> getMethod(Node n1) {
		return getNode(MethodDeclaration.class, n1);
	}
	
	public default Optional<ConstructorDeclaration> getConstructor(Node n1) {
		return getNode(ConstructorDeclaration.class, n1);
	}
	
	public default Optional<ClassOrInterfaceDeclaration> getClass(Node n1) {
		return getNode(ClassOrInterfaceDeclaration.class, n1);
	}
	
	public default Optional<CompilationUnit> getCompilationUnit(Node n1) {
		return getNode(CompilationUnit.class, n1);
	}
	
	public default Optional<EnumDeclaration> getEnum(Node n1) {
		return getNode(EnumDeclaration.class, n1);
	}
	
	public default Optional<TryStmt> getTryStatement(Node n1) {
		return getNode(TryStmt.class, n1);
	}
	
	public default Optional<Statement> getParentStatement(Node n1) {
		return getNode(Statement.class, n1);
	}
	
	@SuppressWarnings("unchecked")
	public default<T extends Node> Optional<T> getNode(Class<T> type, Node n1) {
		while (!n1.getClass().isAssignableFrom(type)) {
			if(n1.getParentNode().isPresent()) {
				n1 = n1.getParentNode().get();
			} else return Optional.empty();
		}
		return Optional.of((T)n1);
	}
	
	public default Collection<CompilationUnit> getUniqueCompilationUnits(List<Node> nodes) {
		return nodes.stream().map(this::getCompilationUnit).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toCollection(SimpleEqualsSet::new));
	}
}
