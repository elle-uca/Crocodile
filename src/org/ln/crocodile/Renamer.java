package org.ln.crocodile;

import java.io.File;

public class Renamer {
	
	
	
	
	
	
	public Renamer() {
	}






	public static String renameFile(File old, File rep) {
		System.out.println(old);
		System.out.println(old.getParent());
		File[] files = old.listFiles();
		
		for (File file : files) {
			System.out.println("file   "+file);
//			if(old.compareTo(file) != 0) {
//				return "Ko";
//			}
		}
		old.renameTo(rep); 
		return "Ok";

	}
	
	public static boolean existsFile(File file) {
		return file.exists();
	}
	
	public static String renameDir(File old, File rep) {
		
		if(existsFile(rep)) {
			return "Ko";
		}
	

		old.renameTo(rep); 
		return "Ok";
		

	}
	
	  public static void main(String[] args) {
		  File f = new File("/home/luke/ren/Comune/pavia");
		  System.out.println(existsFile(f));
	    }
	
	
	

	/*
	 * public static void main(String[] args) { String s =
	 * "C:\\SicraScanDir\\Output"; File f = new File(s); new Renamer(f); }
	 */

}