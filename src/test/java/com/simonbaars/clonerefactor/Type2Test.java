package com.simonbaars.clonerefactor;

import java.nio.file.Paths;

import com.simonbaars.clonerefactor.model.DetectionResults;
import com.simonbaars.clonerefactor.settings.CloneType;
import com.simonbaars.clonerefactor.settings.Settings;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for the clone detector.
 */
public class Type2Test extends TestCase {    

	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public Type2Test( String testName ) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(Type2Test.class);
    }
    
    @Override
    public void setUp() {
    	Settings.get().setCloneType(CloneType.TYPE2);
    	Settings.get().setType2VariabilityPercentage(5);
    }
    
    public void testCustom() {
    	System.out.println("custom");
        String path = "/Users/sbaars/clone/git/SolrMQ/";
		System.out.println(Main.cloneDetection(Paths.get(path), Paths.get(path+"src/main/java/")).sorted());
    }
    
    public void testDifferentLiterals() {
    	System.out.println("testDifferentLiterals");
    	System.out.println(testProject("DifferentLiterals"));
    }
    
    public void testDifferentMethods() {
    	System.out.println("testDifferentMethods");
    	System.out.println(testProject("DifferentMethods"));
    }
    
    public void testHighVariability() {
    	System.out.println("testHighVariability");
    	System.out.println(testProject("HighVariability"));
    }
    
    public void testHighVariabilityInstance() {
    	System.out.println("testHighVariabilityInstance");
    	System.out.println(testProject("HighVariabilityInstance"));
    }
    
    public void testThreeDifferent() {
    	System.out.println("testThreeDifferent");
    	System.out.println(testProject("ThreeDifferent"));
    }
    
    public void testSingle() {
    	System.out.println("testSingle");
    	System.out.println(testProject("Single"));
    }
    
    public void testPartCloned() {
    	System.out.println("testPartCloned");
    	System.out.println(testProject("PartCloned"));
    }

	private DetectionResults testProject(String project) {
		return Main.cloneDetection(Type2Test.class.getClassLoader().getResource("Type2"+project).getFile()).sorted();
	}
}
