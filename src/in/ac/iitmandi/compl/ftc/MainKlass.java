/**
 * 
 */
package in.ac.iitmandi.compl.ftc;

import soot.PackManager;
import soot.Transform;

/**
 * @author arjun
 *
 */
public class MainKlass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PackManager.v().getPack("wjtp").add(new Transform("wjtp.pea", new FieldCountAnalysis()));
		String[] sootArgs = generateSootArgs(args);
		soot.Main.main(sootArgs);
		
	}
	
	private static String[] generateSootArgs(String[] args) {
		if(null == args || args.length < 3) {
			System.out.println("Provide args in the format. [Reflection-Mode] [Process Dir] [Main Class]");
			return null;
		}else {
			String processDir = args[1];
			String mainClass = args[2];
			String reflectionMode = args[0];
			if(reflectionMode.equalsIgnoreCase("TRUE")) {
				String reflLogPath="reflection-log:"+processDir+"/refl.log";
				return new String[] {
						"-cp", ".", "-pp",
						"-w",
						"-f","c",
						"-no-bodies-for-excluded",
						"-include", "org.apache", "-include", "org.w3c",
						"-p", "cg", reflLogPath,
						"-p","cg.spark","on",
						"-process-dir" , processDir,
						"-main-class", mainClass
				};
			}else {
				return new String[] {
						"-cp", ".", "-pp",
		                "-w",
		                "-f","c",
		                "-no-bodies-for-excluded",
		                "-p","cg.spark","on",
		                "-process-dir" , processDir,
		                "-main-class", mainClass
				};
			}
		}
	}
	
}
