package es.upm.grise.file;

public class FileUtils {

	private static long result;
	
	/*
	 * Useful for testing
	 */
	public static void defineCRC(long result) {
		
		FileUtils.result = result;
		
	}
	
	/*
	 * Implementation not needed
	 */
	public static long calculateCRC32(byte[] bytes) {
		
	    return result;
	    
	}
	
}
