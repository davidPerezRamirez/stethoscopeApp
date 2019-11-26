import java.nio.ByteBuffer;

public class ConvertidorByteArray {

	private static final int VALOR_FILTRO_PASA_ALTOS = 4500;

	/**
	 * PRE: Se recibe un array de byte que representa un audio grabado en 2 canales (izq y derecho)
	 * POST: Se obtiene un array de double que representa las muestras del audio del canal izquierdo 
	 */
	public static double[] toDoubleArray(byte[] byteArray){
		double[] samples = new double[byteArray.length]; 
		
		for (int i =0; i < byteArray.length; i += 4) {
		    double left = (double)((byteArray [i] & 0xff) | (byteArray[i + 1] << 8));

		    if (left > VALOR_FILTRO_PASA_ALTOS) {
		    	samples[i] = left;
		    } else {
		    	samples[i] = 0.0;
		    }
		}
		
		return samples;
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
