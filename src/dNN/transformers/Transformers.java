package dNN.transformers;

/**
 * @author Gorav Gupta
 *
 */
public class Transformers {

	public static void normalizeClassName(String inDex, String outDex, int api, String outMappingPath)
			throws Exception {
		ClassName.deobfuscateClass(inDex, outDex, api, outMappingPath);
	}

}
