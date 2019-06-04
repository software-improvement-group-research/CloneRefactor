package com.simonbaars.clonerefactor;

import com.simonbaars.clonerefactor.model.DetectionResults;
import com.simonbaars.clonerefactor.settings.CloneType;
import com.simonbaars.clonerefactor.settings.Settings;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for the clone detector.
 */
public class Type3Test extends TestCase {    

	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public Type3Test( String testName ) {
        super( testName );
    }
    
    @Override
    public void setUp() {
    	Settings.get().setCloneType(CloneType.TYPE3);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(Type3Test.class);
    }
    
    public void testStatementAddedLeft() {
    	System.out.println("testStatementAddedLeft");
    	System.out.println(testProject("StatementAddedLeft"));
    }
    
    public void testStatementAddedRight() {
    	System.out.println("testStatementAddedRight");
    	System.out.println(testProject("StatementAddedRight"));
    }
    
    public void testStatementAddedBothSides() {
    	System.out.println("testStatementAddedBothSides");
    	System.out.println(testProject("StatementAddedBothSides"));
    }

	private DetectionResults testProject(String project) {
		return Main.cloneDetection(Type3Test.class.getClassLoader().getResource("Type3"+project).getFile()).sorted();
	}
}
