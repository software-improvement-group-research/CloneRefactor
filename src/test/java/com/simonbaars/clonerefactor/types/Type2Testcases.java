package com.simonbaars.clonerefactor.types;

import java.nio.file.Paths;

import com.simonbaars.clonerefactor.Main;
import com.simonbaars.clonerefactor.helper.Type2Test;
import com.simonbaars.clonerefactor.settings.CloneType;
import com.simonbaars.clonerefactor.settings.Settings;
import com.simonbaars.clonerefactor.thread.CalculatesTimeIntervals;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Unit test for the clone detector.
 */
public class Type2Testcases extends Type2Test implements CalculatesTimeIntervals {    

	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public Type2Testcases( String testName ) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(Type2Testcases.class);
    }
    
    public void testSolrMQ() {
    	System.out.println("custom");
        String path = "/Users/sbaars/clone/git/SolrMQ/";
		System.out.println(Main.cloneDetection(Paths.get(path), Paths.get(path+"src/main/java/")).sorted());
    }
    
    public void testCaronas() {
    	Settings.get().setCloneType(CloneType.TYPE2);
    	System.out.println("caronas");
    	String path = "/Users/sbaars/clone/git/Caronas/";
    	System.out.println(Settings.get());
		System.out.println(Main.cloneDetection(Paths.get(path), Paths.get(path+"src/main/java/")).sorted());
		Settings.get().setCloneType(CloneType.TYPE2R);
    }
    
    public void testSimpleHTTPServer() {
    	System.out.println("SimpleHTTPServer");
        String path = "/Users/sbaars/clone/git/SimpleHTTPServer/";
		System.out.println(Main.cloneDetection(Paths.get(path), Paths.get(path+"src/main/java/")).sorted());
    }
    
    public void testWykopplJavaSDK() {
    	System.out.println("Wykop.pl-Java-SDK");
        String path = "/Users/sbaars/clone/git/Wykop.pl-Java-SDK/";
		System.out.println(Main.cloneDetection(Paths.get(path), Paths.get(path+"src/main/java/")).sorted());
    }
    
    public void testCotopaxiCore() {
    	System.out.println("cotopaxi-core");
    	String path = "/Users/sbaars/clone/git/cotopaxi-core/";
		System.out.println(Main.cloneDetection(Paths.get(path), Paths.get(path+"src/main/java/")).sorted());
    }
    
    public void testAbmash() {
    	System.out.println("abmash");
    	String path = "/Users/sbaars/clone/git/abmash/";
    	long t = System.currentTimeMillis();
		System.out.println(Main.cloneDetection(Paths.get(path), Paths.get(path+"src/main/java/")).sorted());
		System.out.println("Time 2R: "+interval(t));
    }
    
    public void testAbmashLiterature() {
    	Settings.get().setCloneType(CloneType.TYPE2);
    	System.out.println("abmash");
    	String path = "/Users/sbaars/clone/git/abmash/";
    	long t = System.nanoTime();
		System.out.println(Main.cloneDetection(Paths.get(path), Paths.get(path+"src/main/java/")).sorted());
		System.out.println("Time T2: "+(System.nanoTime()-t));
		Settings.get().setCloneType(CloneType.TYPE2R);
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
    
    public void testThresholds() {
    	Settings.get().setType2VariabilityPercentage(100);
    	System.out.println("testHighVariabilityInstance");
    	System.out.println(testProject("HighVariabilityInstance"));
    	Settings.get().setType2VariabilityPercentage(5);
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
}
