package dNN;

import java.io.File;
import java.util.HashMap;

import dNN.transformers.Transformers;

/**
 * @author Gorav Gupta
 *
 */
public class Main {

	private static final String ARG_IN = "-inDex";
	private static final String ARG_OUT = "-outDex";
	private static final String ARG_API = "-api";
	private static final String ARG_MAP_PATH = "-outMappingPath";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// TODO Auto-generated method stub
		// test();
		if (args.length < 8) {
			printUsage();
		} else {
			try {
				executeProcess(readArgs(args));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void test() {
		try {
			Transformers.normalizeClassName("test/classes.dex", "test/classes-nor.dex", Integer.valueOf("15"),
					"test/classes-Mapping.txt");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/***
	 * @param argsList
	 * @throws Exception
	 */
	private static void executeProcess(HashMap<String, String> argsList) throws Exception {
		Transformers.normalizeClassName(argsList.get(ARG_IN), argsList.get(ARG_OUT),
				Integer.valueOf(argsList.get(ARG_API)), argsList.get(ARG_MAP_PATH));
	}

	private static void printUsage() {
		String command = "DexNameNormalizer.jar";
		System.out.println("DexNameNormalizer Version: 0.2");
		System.out.println();
		System.out.println("Usage: java -jar " + command + " " + ARG_IN + " claases.dex " + ARG_OUT
				+ " claases-Normal.dex " + ARG_API + " 15 " + ARG_MAP_PATH + " classes-Mapping.txt");

		System.out.println();
		System.out.println("https://github.com/Modify24x7");
	}

	/***
	 * @param args
	 * @return
	 */
	private static HashMap<String, String> readArgs(String[] args) {
		HashMap<String, String> argsList = new HashMap<>();

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals(ARG_IN)) {

				argsList.put(ARG_IN, args[++i]);

			} else if (args[i].equals(ARG_OUT)) {

				argsList.put(ARG_OUT, args[++i]);

				File parent = new File(args[i]).getParentFile();
				if (parent != null && (!parent.exists()))
					parent.mkdirs();

			} else if (args[i].equals(ARG_API)) {

				argsList.put(ARG_API, args[++i]);

			} else if (args[i].equals(ARG_MAP_PATH)) {

				argsList.put(ARG_MAP_PATH, args[++i]);

				File parent = new File(args[i]).getParentFile();
				if (parent != null && (!parent.exists()))
					parent.mkdirs();

			}
		}
		return argsList;
	}

}
