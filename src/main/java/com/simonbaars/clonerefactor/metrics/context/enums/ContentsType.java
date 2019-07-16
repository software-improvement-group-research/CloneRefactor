package com.simonbaars.clonerefactor.metrics.context.enums;

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