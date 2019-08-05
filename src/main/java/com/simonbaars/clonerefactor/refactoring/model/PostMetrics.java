package com.simonbaars.clonerefactor.refactoring.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.Statement;
import com.simonbaars.clonerefactor.ast.interfaces.CalculatesLineSize;
import com.simonbaars.clonerefactor.metrics.calculators.CalculatesCyclomaticComplexity;
import com.simonbaars.clonerefactor.metrics.calculators.CyclomaticComplexityCalculator;
import com.simonbaars.clonerefactor.metrics.calculators.UnitLineSizeCalculator;
import com.simonbaars.clonerefactor.metrics.context.interfaces.RequiresNodeContext;

public class PostMetrics implements RequiresNodeContext, CalculatesLineSize, CalculatesCyclomaticComplexity {
	private final Map<MethodDeclaration, Integer> methodCC = new HashMap<>();
	private final Map<MethodDeclaration, Integer> methodLineSize = new HashMap<>();
	private final Map<MethodDeclaration, Integer> methodTokenSize = new HashMap<>();
	private final Map<MethodDeclaration, Integer> methodUnitInterfaceSize = new HashMap<>();
	
	private final int addedTokenVolume;
	private final int addedLineVolume;
	private final int addedNodeVolume;
	
	private final int unitInterfaceSize;
	private final int cc;
	
	public PostMetrics(MethodDeclaration newMethod, Optional<ClassOrInterfaceDeclaration> classOrInterface, List<Statement> methodcalls) {
		methodcalls.stream().map(m -> getMethod(m)).filter(e -> e.isPresent()).map(o -> o.get()).collect(Collectors.toSet()).forEach(m -> {
				methodCC.put(m, new CyclomaticComplexityCalculator().calculate(m));
				methodLineSize.put(m, new UnitLineSizeCalculator().calculate(m));
				methodLineSize.put(m, new CyclomaticComplexityCalculator().calculate(m));
		});
		
		addedTokenVolume = calculateAddedVolume(this::countTokens, classOrInterface, newMethod, methodcalls);
		addedLineVolume = calculateAddedVolume(this::lineSize, classOrInterface, newMethod, methodcalls);
		addedNodeVolume = calculateAddedVolume(this::amountOfNodes, classOrInterface, newMethod, methodcalls);
		unitInterfaceSize = newMethod.getParameters().size();
		cc = calculateCC(newMethod);
	}
	
	public int amountOfNodes(Node n) {
		return 1;
	}
	
	private int calculateAddedVolume(Function<Node, Integer> calculateMetric, Optional<ClassOrInterfaceDeclaration> classOrInterface,
			MethodDeclaration newMethod, List<Statement> methodcalls) {
		int total = 0;
		if(classOrInterface.isPresent()) {
			total += calculateMetric.apply(classOrInterface.get());
		} else {
			total += calculateMetric.apply(newMethod);
		}
		total += methodcalls.stream().mapToInt(calculateMetric::apply).sum();
		return total;
	}

	public int getAddedTokenVolume() {
		return addedTokenVolume;
	}

	public int getAddedLineVolume() {
		return addedLineVolume;
	}

	public int getAddedNodeVolume() {
		return addedNodeVolume;
	}

	public int getUnitInterfaceSize() {
		return unitInterfaceSize;
	}

	public int getCc() {
		return cc;
	}
	
	public CombinedMetrics combine(PreMetrics metrics) {
		return new CombinedMetrics(getCc()-metrics.getCc(), getAddedLineVolume()-metrics.getLines(), getAddedTokenVolume()-metrics.getTokens(), getAddedNodeVolume()-metrics.getNodes(), getUnitInterfaceSize(), metrics.getNodes(), metrics.getTokens(), metrics.getLines());
	}
}
