/**
 * 
 */
package in.ac.iitmandi.compl.ftc;

/**
 * @author arjun
 *
 */
public class IntegerTest {

	public void foo(boolean flag) {
		Integer x = -7;
		if(flag) {
			x = -9;
		}
		double val = x.doubleValue();
		System.out.println(val);
	}
	
}
