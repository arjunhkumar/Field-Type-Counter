/**
 * 
 */
package in.ac.iitmandi.compl.ftc;

import soot.SootClass;
import soot.SootField;

/**
 * 
 */
public class FieldMetadata {

	private SootClass containerKlass;
	private SootField field;
	/**
	 * @param containerKlass
	 * @param field
	 */
	public FieldMetadata(SootClass containerKlass, SootField field) {
		this.containerKlass = containerKlass;
		this.field = field;
	}
	/**
	 * @return the containerKlass
	 */
	public SootClass getContainerKlass() {
		return containerKlass;
	}
	/**
	 * @return the field
	 */
	public SootField getField() {
		return field;
	}
	
}
