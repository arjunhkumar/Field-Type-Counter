/**
 * 
 */
package in.ac.iitmandi.compl.ftc;

import java.util.HashSet;
import java.util.Set;

import soot.SootClass;
import soot.SootField;

/**
 * @author arjun
 *
 */
public class ContainerMetadata {

	private SootClass containerKlass;
	private Set<SootField> fieldsOfInterest;
	private int boxed_integer_count = 0;
	private int boxed_float_count = 0;
	private int boxed_byte_count = 0;
	private int boxed_short_count = 0;
	private int boxed_long_count = 0;
	private int boxed_double_count = 0;
	private int boxed_boolean_count = 0;
	private int boxed_character_count = 0;
	private int java_lang_number_count = 0;
	
	/**
	 * @param containerKlass
	 */
	public ContainerMetadata(SootClass containerKlass) {
		this.containerKlass = containerKlass;
	}
	
	public void addFieldOfInterest(SootField fieldToBeAdded) {
		if(null == this.fieldsOfInterest) {
			this.fieldsOfInterest = new HashSet<>();
		}
		this.fieldsOfInterest.add(fieldToBeAdded);
	}

	/**
	 * @return the containerKlass
	 */
	public SootClass getContainerKlass() {
		return containerKlass;
	}

	/**
	 * @return the fieldsOfInterest
	 */
	public Set<SootField> getFieldsOfInterest() {
		return fieldsOfInterest;
	}

	/**
	 * @param boxed_integer_count the boxed_integer_count to set
	 */
	public void incBoxed_integer_count() {
		this.boxed_integer_count++;
	}

	/**
	 * @param boxed_float_count the boxed_float_count to set
	 */
	public void incBoxed_float_count() {
		this.boxed_float_count++;
	}

	/**
	 * @param boxed_byte_count the boxed_byte_count to set
	 */
	public void incBoxed_byte_count() {
		this.boxed_byte_count++;
	}

	/**
	 * @param boxed_short_count the boxed_short_count to set
	 */
	public void incBoxed_short_count() {
		this.boxed_short_count++;
	}

	/**
	 * @param boxed_long_count the boxed_long_count to set
	 */
	public void incBoxed_long_count() {
		this.boxed_long_count++;
	}

	/**
	 * @param boxed_double_count the boxed_double_count to set
	 */
	public void incBoxed_double_count() {
		this.boxed_double_count++;
	}

	/**
	 * @param boxed_boolean_count the boxed_boolean_count to set
	 */
	public void incBoxed_boolean_count() {
		this.boxed_boolean_count++;
	}

	/**
	 * @param boxed_character_count the boxed_character_count to set
	 */
	public void incBoxed_character_count() {
		this.boxed_character_count++;
	}
	
	public void incJava_lang_number_count() {
		this.java_lang_number_count++;
	}

	/**
	 * @return the boxed_integer_count
	 */
	public int getBoxed_integer_count() {
		return boxed_integer_count;
	}

	/**
	 * @return the boxed_float_count
	 */
	public int getBoxed_float_count() {
		return boxed_float_count;
	}

	/**
	 * @return the boxed_byte_count
	 */
	public int getBoxed_byte_count() {
		return boxed_byte_count;
	}

	/**
	 * @return the boxed_short_count
	 */
	public int getBoxed_short_count() {
		return boxed_short_count;
	}

	/**
	 * @return the boxed_long_count
	 */
	public int getBoxed_long_count() {
		return boxed_long_count;
	}

	/**
	 * @return the boxed_double_count
	 */
	public int getBoxed_double_count() {
		return boxed_double_count;
	}

	/**
	 * @return the boxed_boolean_count
	 */
	public int getBoxed_boolean_count() {
		return boxed_boolean_count;
	}

	/**
	 * @return the boxed_character_count
	 */
	public int getBoxed_character_count() {
		return boxed_character_count;
	}

	/**
	 * @return the java_lang_number_count
	 */
	public int getJava_lang_number_count() {
		return java_lang_number_count;
	}
	
}
