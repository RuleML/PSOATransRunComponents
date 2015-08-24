package org.ruleml.psoa.psoatransrun.test;

public class PSOATransRunTest {
	
	public static void main(String[] args) {
		TestSuite ts = new TestSuite("test", args[0]);
		ts.run();
		ts.outputSummary();
		
//		ts = new TestSuite("test", "prolog");
//		System.out.println("Prolog");
//		ts.run();
//		ts.outputSummary();
	}
}
