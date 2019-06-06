package com.simonbaars.clonerefactor.types;

import java.io.File;
import java.nio.file.Paths;

import com.simonbaars.clonerefactor.Main;
import com.simonbaars.clonerefactor.helper.Type1Test;
import com.simonbaars.clonerefactor.settings.Scope;
import com.simonbaars.clonerefactor.settings.Settings;
import com.simonbaars.clonerefactor.thread.CorpusThread;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Unit test for the clone detector.
 */
public class Type1Testcases extends Type1Test {
    private static final String SEVERAL_METHODS_PROJECT = "SeveralMethodsCloned";
	private static final String UNEQUAL_SIZE_CLONES_PROJECT = "UnequalSizeClones";
	private static final String SINGLE_FILE_PROJECT = "SingleFile";
	private static final String PARTIAL_CLONES_LEFT = "PartialClonesLeft";
	private static final String PARTIAL_CLONES_RIGHT = "PartialClonesRight";
	private static final String SIMPLE_PROJECT = "SimpleClone";
    private static final String EQUAL_LINES_PROJECT = "EqualLines";
    private static final String ENUM_PROJECT = "EnumClone";
    

	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public Type1Testcases( String testName ) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( Type1Testcases.class );
    }
    
    public void testCustom() {
    	System.out.println("kryo-serializers");
    	String path = "/Users/sbaars/clone/git/kryo-serializers/";
    	System.out.println(Settings.get());
		System.out.println(Main.cloneDetection(Paths.get(path), Paths.get(path+"src/main/java/")).sorted());
    }
    
    public void testAbmashMethodScope() {
    	Settings.get().setScope(Scope.METHODSONLY);
    	System.out.println("abmash");
    	String path = "/Users/sbaars/clone/git/abmash/";
		System.out.println(Main.cloneDetection(Paths.get(path), Paths.get(path+"src/main/java/")).sorted());
    }
    
    public void testThread() {
    	System.out.println("custom2");
    	CorpusThread t = new CorpusThread(new File("/Users/sbaars/clone/java_projects/gatein-forge-plugin/src/main/java/"));
    	while(t.isAlive())
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
    }
    
    /**
     * Test for clones that consist of lines that do not occur elsewhere.
     */
    public void testSimpleClones() {
    	System.out.println("testSimpleClones");
    	System.out.println(testProject(SIMPLE_PROJECT));
    }
    
    public void testNestedClone() {
    	System.out.println("testNestedClone");
    	System.out.println(testProject("NestedClone"));
    }
    
	/**
     * Test for clones that consist of all equal lines.
     */
    public void testEqualLines() {
    	System.out.println("testEqualLines");
    	System.out.println(testProject(EQUAL_LINES_PROJECT));
    }
    
    /**
     * Test for three clones, of which one starts a line later than the others.
     */
    public void testPartialClonesLeft() {
    	System.out.println("testPartialClonesLeft");
    	System.out.println(testProject(PARTIAL_CLONES_LEFT));
    }
    
    /**
     * Test for three clones, of which one ends a line later than the others.
     */
    public void testPartialLinesRight() {
    	System.out.println("testPartialLinesRight");
    	System.out.println(testProject(PARTIAL_CLONES_RIGHT));
    }

	/**
     * Test for clones in Java enumerations.
     */
    public void testEnumClone() {
    	System.out.println("testEnumClone");
    	System.out.println(testProject(ENUM_PROJECT));
    }
    
    /**
     * Test for clones in a single file, with just a single line to separate the clones.
     */
    public void testSingleFile() {
    	System.out.println("testSingleFile");
    	System.out.println(testProject(SINGLE_FILE_PROJECT));
    }
    
    
    /**
     * Test for clones that differ in length but consist of lines with equal tokens.
     */
    public void testUnequalSizeClones() {
    	System.out.println("testUnequalSizeClones");
    	System.out.println(testProject(UNEQUAL_SIZE_CLONES_PROJECT));
    }
    
    /**
     * Test for clones that span multiple methods.
     */
    public void testSeveralMethodsCloned() {
    	System.out.println("testSeveralMethodsCloned");
    	System.out.println(testProject(SEVERAL_METHODS_PROJECT));
    }
    
    /**
     * Test for clones in import statements.
     */
    public void testImportStatements() {
    	System.out.println("testImportStatements");
    	System.out.println(testProject("EqualImportStatements"));
    }
    
    public void testEqualLinesDifferentLength() {
    	System.out.println("testEqualLinesDifferentLength");
    	System.out.println(testProject("EqualLinesDifferentLength"));
    }
}