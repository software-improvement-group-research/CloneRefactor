package com.simonbaars.clonerefactor.metrics.context.analyze;

import static com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType.FULLCLASS;
import static com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType.FULLCONSTRUCTOR;
import static com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType.FULLENUM;
import static com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType.FULLINTERFACE;
import static com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType.FULLMETHOD;
import static com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType.HASCLASSDECLARATION;
import static com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType.HASENUMDECLARATION;
import static com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType.HASENUMFIELDS;
import static com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType.HASINTERFACEDECLARATION;
import static com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType.INCLUDESCONSTRUCTOR;
import static com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType.INCLUDESFIELDS;
import static com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType.ONLYFIELDS;
import static com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType.OTHER;
import static com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType.PARTIALCONSTRUCTOR;
import static com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType.PARTIALMETHOD;
import static com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType.SEVERALMETHODS;

import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.simonbaars.clonerefactor.ast.interfaces.RequiresNodeOperations;
import com.simonbaars.clonerefactor.metrics.context.analyze.CloneContents.ContentsType;
import com.simonbaars.clonerefactor.metrics.context.interfaces.MetricEnum;
import com.simonbaars.clonerefactor.model.Sequence;
import com.simonbaars.clonerefactor.model.location.LocationContents;

public class CloneContents implements MetricEnum<ContentsType>, RequiresNodeOperations {
	public enum ContentsType{
		FULLMETHOD, 
		PARTIALMETHOD, 
		SEVERALMETHODS, 
		FULLCONSTRUCTOR,
		PARTIALCONSTRUCTOR,
		ONLYFIELDS, 
		FULLCLASS, 
		FULLINTERFACE,
		FULLENUM,
		HASCLASSDECLARATION, 
		HASINTERFACEDECLARATION, 
		HASENUMDECLARATION, 
		HASENUMFIELDS,
		INCLUDESFIELDS,
		INCLUDESCONSTRUCTOR,
		OTHER;
	}

	@Override
	public ContentsType get(Sequence sequence) {
		return get(sequence.getAny().getContents());
	}

	//TODO: This method must be refactored
	public ContentsType get(LocationContents c) {
		List<Node> nodes = c.getNodes();
		Node lastNode = nodes.get(nodes.size()-1);
		Node lastStatement = getLastStatement(nodes.get(0));
		if(nodes.get(0) instanceof MethodDeclaration && lastNode == lastStatement) {
			return FULLMETHOD;
		} 
		
		Optional<MethodDeclaration> method = getMethod(nodes.get(0));
		if(method.isPresent()) {
			Optional<MethodDeclaration> method2 = getMethod(lastNode);
			if(method2.isPresent() && method.get() == method2.get())
				return PARTIALMETHOD;
		}

		if(nodes.get(0) instanceof ConstructorDeclaration && lastNode == lastStatement) {
			return FULLCONSTRUCTOR;
		} 

		Optional<ConstructorDeclaration> constructor = getConstructor(nodes.get(0));
		if(constructor.isPresent()) {
			Optional<ConstructorDeclaration> constructor2 = getConstructor(lastNode);
			if(constructor2.isPresent() && constructor == constructor2) {
				return PARTIALCONSTRUCTOR;
			} 
		}
		if(nodes.stream().allMatch(e -> getMethod(e).isPresent())) {
			return SEVERALMETHODS;
		} else if(nodes.stream().allMatch(e -> e instanceof FieldDeclaration)) {
			return ONLYFIELDS;
		} else if(nodes.get(0) instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration)nodes.get(0)).isInterface() && nodes.get(nodes.size()-1) == getLastStatement(nodes.get(0))) {
			return FULLCLASS;
		} else if(nodes.get(0) instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration)nodes.get(0)).isInterface() && nodes.get(nodes.size()-1) == getLastStatement(nodes.get(0))) {
			return FULLINTERFACE;
		} else if(nodes.get(0) instanceof EnumDeclaration && nodes.get(nodes.size()-1) == getLastStatement(nodes.get(0))) {
			return FULLENUM;
		} else if(nodes.stream().anyMatch(e -> e instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration)e).isInterface())) {
			return HASCLASSDECLARATION;
		} else if(nodes.stream().anyMatch(e -> e instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration)e).isInterface())) {
			return HASINTERFACEDECLARATION;
		} else if(nodes.stream().anyMatch(e -> e instanceof EnumDeclaration)) {
			return HASENUMDECLARATION;
		} else if(nodes.stream().anyMatch(e -> e instanceof EnumConstantDeclaration)) {
			return HASENUMFIELDS;
		} else if(nodes.stream().anyMatch(e -> e instanceof FieldDeclaration)) {
			return INCLUDESFIELDS;
		} else if(nodes.stream().anyMatch(e -> getConstructor(e).isPresent())) {
			return INCLUDESCONSTRUCTOR;
		}
		return OTHER;
	}

	private Node getLastStatement(Node n) {
		List<Node> children = n.getChildNodes();
		Optional<Node> reduce = children.stream().filter(e -> !isExcluded(e)).reduce((first, second) -> second);
		if(reduce.isPresent()) {
			n = reduce.get();
			if(!n.getChildNodes().isEmpty())
				return getLastStatement(n);
		}
		return n;
	}
}