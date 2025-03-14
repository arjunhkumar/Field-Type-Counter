/**
 * 
 */
package in.ac.iitmandi.compl.ftc;

import java.util.HashSet;
import java.util.Set;

import soot.Scene;
import soot.SootClass;

/**
 * @author arjun
 *
 */
public class CommonConstants {

	public static final String BOXED_BOOLEAN = "java.lang.Boolean";
	public static final String BOXED_INTEGER = "java.lang.Integer";
	public static final String BOXED_LONG = "java.lang.Long";
	public static final String BOXED_FLOAT = "java.lang.Float";
	public static final String BOXED_DOUBLE = "java.lang.Double";
	public static final String BOXED_BYTE = "java.lang.Byte";
	public static final String BOXED_SHORT = "java.lang.Short";
	public static final String BOXED_CHARACTER = "java.lang.Character";
	public static final String BOXED_OPTIONAL = "java.util.Optional";
	public static final String NUMBER = "java.lang.Number";
	public static final Set<SootClass> classSet = new HashSet<>();
	
	public static Set<SootClass> getClassesOfInterest(){
		if(classSet.isEmpty()) {
			classSet.add(Scene.v().getSootClass(BOXED_BOOLEAN));
			classSet.add(Scene.v().getSootClass(BOXED_INTEGER));
			classSet.add(Scene.v().getSootClass(BOXED_LONG));
			classSet.add(Scene.v().getSootClass(BOXED_FLOAT));
			classSet.add(Scene.v().getSootClass(BOXED_DOUBLE));
			classSet.add(Scene.v().getSootClass(BOXED_BYTE));
			classSet.add(Scene.v().getSootClass(BOXED_SHORT));
			classSet.add(Scene.v().getSootClass(BOXED_CHARACTER));
			classSet.add(Scene.v().getSootClass(BOXED_OPTIONAL));
			classSet.add(Scene.v().getSootClass(NUMBER));
		}
		return classSet;
	}
	
}
