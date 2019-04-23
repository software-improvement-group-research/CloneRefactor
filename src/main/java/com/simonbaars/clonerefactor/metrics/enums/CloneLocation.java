package com.simonbaars.clonerefactor.metrics.enums;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.simonbaars.clonerefactor.metrics.enums.CloneLocation.LocationType;
import static com.simonbaars.clonerefactor.metrics.enums.CloneLocation.LocationType.*;
import com.simonbaars.clonerefactor.model.Sequence;

public class CloneLocation implements MetricEnum<LocationType> {
	public enum LocationType{
		METHODLEVEL,
		CLASSLEVEL,
		INTERFACELEVEL,
		ENUMLEVEL,
		OUTSIDE;
	}

	@Override
	public LocationType get(Sequence sequence) {
		List<LocationType> locations = new ArrayList<>();
		for(int i = 0; i<sequence.getSequence().get(0).getContents().getNodes().size(); i++) {
			locations.add(getLocation(sequence.getSequence().get(0).getContents().getNodes().get(i), i));
		}
		return locations.stream().sorted().reduce((first, second) -> second).get();
	}

	private LocationType getLocation(Node node, int i) {
		if(getMethod(node)!=null && (!(node instanceof MethodDeclaration) || i == 0))
			return METHODLEVEL;
		ClassOrInterfaceDeclaration class1 = getClass(node);
		if(class1 != null) {
			if(class1.isInterface())
				return INTERFACELEVEL;
			else return CLASSLEVEL;
		}
		if(getEnum(node)!=null)
			return ENUMLEVEL;
		return OUTSIDE;
	}
	
}
