package com.simonbaars.clonerefactor.metrics;

import com.simonbaars.clonerefactor.helper.Type1Test;
import com.simonbaars.clonerefactor.metrics.enums.CloneRefactorability.Refactorability;
import com.simonbaars.clonerefactor.model.DetectionResults;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Unit test for the node locations.
 */
public class CloneExtractTest extends Type1Test {

	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CloneExtractTest( String testName ) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( CloneExtractTest.class );
    }
    
    public void testFullMethod() {
        test("PartialBlock", Refactorability.PARTIALBLOCK);
    }
    
    public void testPartialMethod() {
        test("SimpleClone", Refactorability.CANBEEXTRACTED);
    }
    
    public void testSeveralMethods() {
        test("SeveralMethodsCloned", Refactorability.NOEXTRACTIONBYCONTENTTYPE);
    }

	private void test(String name, Refactorability loc) {
		DetectionResults r = testProject(name);
		System.out.println(r);
        Assert.assertEquals(loc, r.getMetrics().amountPerExtract.keySet().iterator().next());
	}
}
