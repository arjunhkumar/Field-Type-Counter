/**
 * 
 */
package in.ac.iitmandi.compl.ftc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import in.ac.iitmandi.compl.utils.CommonUtils;
import soot.Body;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.CallGraphBuilder;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.pointer.DumbPointerAnalysis;

/**
 * @author arjun
 *
 */
public class FieldCountAnalysis extends SceneTransformer {

	private static CallGraph sparkCG;
	private static CallGraph chaCG;
	private Set<SootClass> analyzedClasses = new HashSet<>();
	private static Map<SootClass,ContainerMetadata> classMap;
	public static final String OUT_FILE_PATH = "./res.out";
	
	@Override
	protected void internalTransform(String phaseName, Map<String, String> options) {
		for(SootClass sootClass: Scene.v().getClasses()) {
			analyzeClass(sootClass);
		}
		printResults();
	}
	
	private void analyzeCallGraph(SootMethod methodToBeAnalyzed) {
		if(null != methodToBeAnalyzed && methodToBeAnalyzed.hasActiveBody()) {
			Set<Stmt> invokeStmts = analyzeMethod(methodToBeAnalyzed);
			if(CommonUtils.isNotNull(invokeStmts)) {
				for(Stmt stmt : invokeStmts) {
					handleNextMethodCall(stmt);
				}
			}
		}
	}

	public Set<Stmt> analyzeMethod(SootMethod methodToBeAnalyzed) {
		Set<Stmt> invokeStmts = new HashSet<>();
		if(null != methodToBeAnalyzed && null != methodToBeAnalyzed.getDeclaringClass() 
				&& methodToBeAnalyzed.hasActiveBody()) {
			SootClass classOfIntrest = methodToBeAnalyzed.getDeclaringClass();
			if(!isAlreadyAnalyzed(classOfIntrest)) {
				analyzeClass(classOfIntrest);
				Body activeBody = methodToBeAnalyzed.getActiveBody();
				for(Unit unit : activeBody.getUnits()) {
					if(unit instanceof Stmt) {
						Stmt stmt = (Stmt)unit;
						if(stmt.containsInvokeExpr()) {
							invokeStmts.add(stmt);
						}
					}
				}
			}
		}
		return invokeStmts;
	}
	
	private void analyzeClass(SootClass classOfIntrest) {
		if(null != classOfIntrest.getFields()) {
//			System.out.println(classOfIntrest);
			for(SootField field: classOfIntrest.getFields()) {
				analyzeField(field);
			}
			this.analyzedClasses.add(classOfIntrest);
		}
	}

	private void analyzeField(SootField field) {
		if(null != field && CommonUtils.isNotNull(field.getType().toQuotedString())) {
			matchFieldType(field);
		}
	}

	private void matchFieldType(SootField field) {
		if(field.getType().toQuotedString().equals(CommonConstants.BOXED_BYTE)) {
			SootClass containingClass = field.getDeclaringClass();
			ContainerMetadata container = getClassInstance(containingClass);
			container.addFieldOfInterest(field);
			container.incBoxed_byte_count();
		}else if(field.getType().toQuotedString().equals(CommonConstants.BOXED_BOOLEAN)) {
			SootClass containingClass = field.getDeclaringClass();
			ContainerMetadata container = getClassInstance(containingClass);
			container.addFieldOfInterest(field);
			container.incBoxed_boolean_count();
		}else if(field.getType().toQuotedString().equals(CommonConstants.BOXED_CHARACTER)) {
			SootClass containingClass = field.getDeclaringClass();
			ContainerMetadata container = getClassInstance(containingClass);
			container.addFieldOfInterest(field);
			container.incBoxed_character_count();
		}else if(field.getType().toQuotedString().equals(CommonConstants.BOXED_DOUBLE)) {
			SootClass containingClass = field.getDeclaringClass();
			ContainerMetadata container = getClassInstance(containingClass);
			container.addFieldOfInterest(field);
			container.incBoxed_double_count();
		}else if(field.getType().toQuotedString().equals(CommonConstants.BOXED_FLOAT)) {
			SootClass containingClass = field.getDeclaringClass();
			ContainerMetadata container = getClassInstance(containingClass);
			container.addFieldOfInterest(field);
			container.incBoxed_float_count();
		}else if(field.getType().toQuotedString().equals(CommonConstants.BOXED_INTEGER)) {
			SootClass containingClass = field.getDeclaringClass();
			ContainerMetadata container = getClassInstance(containingClass);
			container.addFieldOfInterest(field);
			container.incBoxed_integer_count();
		}else if(field.getType().toQuotedString().equals(CommonConstants.BOXED_LONG)) {
			SootClass containingClass = field.getDeclaringClass();
			ContainerMetadata container = getClassInstance(containingClass);
			container.addFieldOfInterest(field);
			container.incBoxed_long_count();
		}else if(field.getType().toQuotedString().equals(CommonConstants.BOXED_SHORT)) {
			SootClass containingClass = field.getDeclaringClass();
			ContainerMetadata container = getClassInstance(containingClass);
			container.addFieldOfInterest(field);
			container.incBoxed_short_count();
		}
	}

	private boolean isAlreadyAnalyzed(SootClass classOfIntrest) {
		return CommonUtils.isNotNull(this.analyzedClasses) && analyzedClasses.contains(classOfIntrest);
	}

	private void handleNextMethodCall(Stmt jimpleStmt) {
		Iterator<Edge> sparkItr = sparkCG.edgesOutOf(jimpleStmt);
		if(sparkCG.edgesOutOf(jimpleStmt).hasNext()) {
			while(sparkItr.hasNext()) {
				Edge edge = sparkItr.next();
				MethodOrMethodContext method = edge.getTgt();
				if(null!=method) {
					SootMethod sootMethod = method.method();
					if(null!=sootMethod) {
						analyzeCallGraph(sootMethod);
					}
				}
			}
		}
		else {
			Iterator<Edge> chaIterator = chaCG.edgesOutOf(jimpleStmt);
			while(chaIterator.hasNext()) {
				Edge edge = chaIterator.next();
				MethodOrMethodContext method = edge.getTgt();
				if(null!=method) {
					SootMethod sootMethod = method.method();
					if(null!=sootMethod) {
						analyzeCallGraph(sootMethod);
					}
				}
			}
		}
	}
	
	private static ContainerMetadata getClassInstance(SootClass classOfInterest) {
		if(null == classMap) {
			classMap = new HashMap<>();
		}
		if(classMap.containsKey(classOfInterest)) {
			return classMap.get(classOfInterest);
		}else {
			ContainerMetadata containerMetadata = new ContainerMetadata(classOfInterest);
			classMap.put(classOfInterest, containerMetadata);
			return containerMetadata;
		}
	}
	
	private void printResults() {
		if(CommonUtils.isNotNull(classMap)) {
			createOutFile();
			int boxedIntegerFieldCount = 0;
			int boxedBooleanFieldCount = 0;
			int boxedByteFieldCount = 0;
			int boxedShortFieldCount = 0;
			int boxedCharFieldCount = 0;
			int boxedFloatFieldCount = 0;
			int boxedDoubleFieldCount = 0;
			int boxedLongFieldCount = 0;
			
			Iterator<Entry<SootClass, ContainerMetadata>> itr = classMap.entrySet().iterator();
			while(itr.hasNext()) {
				Entry<SootClass, ContainerMetadata> entry = itr.next();
				ContainerMetadata container = entry.getValue();
				if(isNonLibaryClass(container.getContainerKlass())){
					writeToOutFile(container.getContainerKlass());
					if(CommonUtils.isNotNull(container.getFieldsOfInterest())) {
						boxedIntegerFieldCount+=container.getBoxed_integer_count();
						boxedBooleanFieldCount+=container.getBoxed_boolean_count();
						boxedByteFieldCount+=container.getBoxed_byte_count();
						boxedShortFieldCount+=container.getBoxed_short_count();
						boxedCharFieldCount+=container.getBoxed_character_count();
						boxedFloatFieldCount+=container.getBoxed_float_count();
						boxedDoubleFieldCount+=container.getBoxed_double_count();
						boxedLongFieldCount+=container.getBoxed_long_count();
						for(SootField field: container.getFieldsOfInterest()) {
							writeToOutFile(field);
						}
					}
					writeToOutFile("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
				}
			}
			writeToOutFile("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
			writeToOutFile(CommonConstants.BOXED_INTEGER+" count: "+boxedIntegerFieldCount);
			writeToOutFile(CommonConstants.BOXED_BOOLEAN+" count: "+boxedBooleanFieldCount);
			writeToOutFile(CommonConstants.BOXED_BYTE+" count: "+boxedByteFieldCount);
			writeToOutFile(CommonConstants.BOXED_CHARACTER+" count: "+boxedCharFieldCount);
			writeToOutFile(CommonConstants.BOXED_DOUBLE+" count: "+boxedDoubleFieldCount);
			writeToOutFile(CommonConstants.BOXED_FLOAT+" count: "+boxedFloatFieldCount);
			writeToOutFile(CommonConstants.BOXED_LONG+" count: "+boxedLongFieldCount);
			writeToOutFile(CommonConstants.BOXED_SHORT+" count: "+boxedShortFieldCount);
			writeToOutFile("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
		}
		
	}
	
	private boolean isNonLibaryClass(SootClass containerKlass) {
		if(!containerKlass.isJavaLibraryClass()) {
			String packageName = containerKlass.getJavaPackageName();
			if(packageName.startsWith("java.") || packageName.startsWith("jdk.")) {
				return false;
			}
			return true;
		}
		return false;
	}

	public static void createOutFile() {
		Path path = Paths.get(OUT_FILE_PATH);
		File file = new File(OUT_FILE_PATH);
        if (file.exists()) {
        	try {
        		Files.delete(file.toPath());
			} catch (IOException e) {
				System.out.println("File: "+OUT_FILE_PATH+" could not be deleted.");
			}
        }
        try {
        	Files.createDirectories(path.getParent()); 
			Files.createFile(path);
		} catch (IOException e) {
			System.out.println("File: "+OUT_FILE_PATH+" could not be created.");
        }
	}
	
	public static void writeToOutFile(SootField field) {
		File file = new File(OUT_FILE_PATH);
		try(BufferedWriter output = new BufferedWriter(new FileWriter(file,true))){
			try(PrintWriter writer = new PrintWriter(output, true)){
				writer.write(field.getName()+"\t"+field.getType().toQuotedString()+"\t"+field.getSignature()+"\n");
			}
		} catch (IOException e) {
			System.out.println("File: "+OUT_FILE_PATH+" could not be opened.");
		}
	}
	
	public static void writeToOutFile(SootClass classMetadata) {
		File file = new File(OUT_FILE_PATH);
		try(BufferedWriter output = new BufferedWriter(new FileWriter(file,true))){
			try(PrintWriter writer = new PrintWriter(output, true)){
				writer.write("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*\n");
				writer.write("Class: "+classMetadata.getName()+"\n");
			}
		} catch (IOException e) {
			System.out.println("File: "+OUT_FILE_PATH+" could not be opened.");
		}
	}
	
	public static void writeToOutFile(String str) {
		File file = new File(OUT_FILE_PATH);
		try(BufferedWriter output = new BufferedWriter(new FileWriter(file,true))){
			try(PrintWriter writer = new PrintWriter(output, true)){
				writer.write(str+"\n");
			}
		} catch (IOException e) {
			System.out.println("File: "+OUT_FILE_PATH+" could not be opened.");
		}
	}
}
