import java.nio.ByteBuffer;

public class ConvertidorByteArray {

	public static double[] toDoubleArray(byte[] byteArray){
	    int times = Double.SIZE / Byte.SIZE;
	    double[] doubles = new double[byteArray.length / times];
	    
	    for(int i=0;i<doubles.length;i++){
	        doubles[i] = ByteBuffer.wrap(byteArray, i*times, times).getDouble();
	    }
	    
	    return doubles;
	}
	
	public static int[] toIntArray(byte[] byteArray){
	    int times = Integer.SIZE / Byte.SIZE;
	    int[] ints = new int[byteArray.length / times];
	    
	    for(int i=0;i<ints.length;i++){
	        ints[i] = ByteBuffer.wrap(byteArray, i*times, times).getInt();
	    }
	    
	    return ints;
	}
	
	public static short[] toShortArray(byte[] byteArray) {
		int size = byteArray.length;
		short[] shortArray = new short[size];

		for (int index = 0; index < size; index++) {
		    shortArray[index] = (short) byteArray[index];
		}
		
		return shortArray;
	}
}
