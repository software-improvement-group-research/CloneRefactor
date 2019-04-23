package com.simonbaars.clonerefactor.metrics.enums;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.simonbaars.clonerefactor.model.Sequence;

@SuppressWarnings("rawtypes")
public interface MetricEnum<MyEnum extends Enum> {
	public MyEnum get(Sequence sequence);
	
	public default MethodDeclaration getMethod(Node n1) {
		while (!(n1 instanceof MethodDeclaration)) {
			if(n1.getParentNode().isPresent()) {
				n1 = n1.getParentNode().get();
			} else return null;
		}
		return (MethodDeclaration)n1;
	}
	
	public default ClassOrInterfaceDeclaration getClass(Node n1) {
		while (!(n1 instanceof ClassOrInterfaceDeclaration)) {
			if(n1.getParentNode().isPresent()) {
				n1 = n1.getParentNode().get();
			} else return null;
		}
		return (ClassOrInterfaceDeclaration)n1;
	}
	
	public default CompilationUnit getCompilationUnit(Node n1) {
		while (!(n1 instanceof CompilationUnit)) {
			if(n1.getParentNode().isPresent()) {
				n1 = n1.getParentNode().get();
			} else return null;
		}
		return (CompilationUnit)n1;
	}
	
	public default EnumDeclaration getEnum(Node n1) {
		while (!(n1 instanceof EnumDeclaration)) {
			if(n1.getParentNode().isPresent()) {
				n1 = n1.getParentNode().get();
			} else return null;
		}
		return (EnumDeclaration)n1;
	}
}
