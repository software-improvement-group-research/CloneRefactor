
public class Clone2 {

	public static void main2(String[] args) {
		System.out.println("I'm a clone1");
		System.out.println("I'm a clone2");
		System.out.println("I'm a clone3");
		System.out.println("I'm a clone4");
		System.out.println("I'm a clone5");
		myCustomPrintln("I'm a clone6");
		System.out.println("I'm a clone7");
		System.out.println("I'm a clone8");
		System.out.println("I'm a clone9");
		System.out.println("I'm a clone10");
		System.out.println("I'm a clone11");
		System.out.println("I'm a clone12");
	}
	
	public void myCustomPrintln(String x) throws IllegalStateException{
		System.out.println("== "+x+" ==");
		throw new IllegalStateException("Don't do this");
	}

}
