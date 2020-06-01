package dNN.transformers;

import java.io.File;
import java.nio.file.Files;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.immutable.ImmutableClassDef;
import org.jf.dexlib2.immutable.reference.ImmutableMethodReference;
import org.jf.dexlib2.rewriter.DexRewriter;
import org.jf.dexlib2.rewriter.Rewriter;
import org.jf.dexlib2.rewriter.RewriterModule;
import org.jf.dexlib2.rewriter.Rewriters;

/**
 * @author Gorav Gupta
 *
 */
public class ClassName {

	static void deobfuscateClass(String inDex, String outDex, int API, String mapPath) throws Exception {

		DexFile dexFile = DexFileFactory.loadDexFile(inDex, Opcodes.forApi(API));

		List<String> classList = new ArrayList<>();
		List<ClassDef> classes = new ArrayList<>();

		List<String> normalClassList = new ArrayList<>();

		for (ClassDef classDef : dexFile.getClasses()) {
			classList.add(classDef.getType());
		}

		for (ClassDef classDef : dexFile.getClasses()) {

			String Type = classDef.getType().replaceFirst(";", ""); // Lpkg/class or Lpkg/class$a

			String Type2;

			if (Type.lastIndexOf("/") != -1) {
				Type2 = Type.substring(0, Type.lastIndexOf("/")); // Lpkg
			} else {
				Type2 = Type; // Lpkg
			}

			String Type3 = Type.substring(Type.lastIndexOf("/") + 1); // class or class$a

			String Type4;

			try {
				Type4 = Type3.substring(Type3.indexOf("$")) + ";"; // $a;
			} catch (Exception ex) {
				Type4 = ";";
			}

			String sourceFile = classDef.getSourceFile();

			if (sourceFile != null) {
				if (sourceFile.endsWith(".java") || sourceFile.endsWith(".kt")) {

					String TypeFinal;

					try {
						TypeFinal = Type2 + "/" + sourceFile.substring(0, sourceFile.lastIndexOf(".")) + Type4;
					} catch (Exception ex) {
						TypeFinal = classDef.getType();
					}

					boolean added = false;
					for (String clazz : classList) {
						if (TypeFinal.equals(clazz)) {
							added = true;
						}
					}

					boolean added2 = false;
					for (ClassDef cD : classes) {
						if (TypeFinal.equals(cD.getType())) {
							added2 = true;
						}
					}

					if (!added && !added2) {
						normalClassList.add(TypeFinal);

						classes.add(new ImmutableClassDef(TypeFinal, classDef.getAccessFlags(),
								classDef.getSuperclass(), classDef.getInterfaces(), classDef.getSourceFile(),
								classDef.getAnnotations(), classDef.getFields(), classDef.getMethods()));
					} else {
						normalClassList.add(classDef.getType());
						classes.add(classDef);
					}
				} else {
					normalClassList.add(classDef.getType());
					classes.add(classDef);
				}
			} else {
				normalClassList.add(classDef.getType());
				classes.add(classDef);
			}
		}

		DexRewriter dexRewriter = new DexRewriter(new RewriterModule() {
			@Override
			public Rewriter<MethodReference> getMethodReferenceRewriter(Rewriters rewriters) {
				return new Rewriter<MethodReference>() {

					@Override
					public MethodReference rewrite(MethodReference value) {
						// TODO Auto-generated method stub
						return new ImmutableMethodReference(
								replaceRefClass(value.getDefiningClass(), classList, normalClassList), value.getName(),
								value.getParameterTypes(), value.getReturnType());
					}

				};
			}
		});
		
		DexFile dex = dexRewriter.getDexFileRewriter().rewrite(getDexFile(classes, API));
		
		DexFileFactory.writeDexFile(outDex, dex);

		// Write mapping
		writeMapping(mapPath, classList, normalClassList);

		System.out.println("https://github.com/Modify24x7");
	}

	private static void writeMapping(String mapPath, List<String> oldRefList, List<String> newRefList)
			throws Exception {

		List<String> mapping = new ArrayList<>();

		for (int i = 0; i < oldRefList.size(); i++) {
			mapping.add(oldRefList.get(i) + " -> " + newRefList.get(i));
		}

		Collections.sort(mapping);

		StringBuilder builder = new StringBuilder();
		for (String str : mapping) {
			builder.append(str + "\n");
		}

		File file = new File(mapPath);

		Files.write(file.toPath(), builder.toString().getBytes("UTF-8"));
	}

	private static String replaceRefClass(String refClass, List<String> oldRefList, List<String> newRefList) {
		for (int i = 0; i < oldRefList.size(); i++) {
			if (refClass.equals(oldRefList.get(i))) {
				return newRefList.get(i);
			}
		}

		return refClass;
	}

	private static DexFile getDexFile(List<ClassDef> classes, int API) {

		Collections.sort(classes);

		return new DexFile() {

			@Nonnull
			@Override
			public Set<? extends ClassDef> getClasses() {
				// TODO Auto-generated method stub
				return new AbstractSet<ClassDef>() {

					@Override
					public Iterator<ClassDef> iterator() {
						// TODO Auto-generated method stub
						return classes.iterator();
					}

					@Override
					public int size() {
						// TODO Auto-generated method stub
						return classes.size();
					}

				};
			}

			@Override
			public Opcodes getOpcodes() {
				// TODO Auto-generated method stub
				return Opcodes.forApi(API);
			}
		};
	}

}
