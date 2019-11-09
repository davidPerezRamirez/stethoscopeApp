import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	private static final String PULSACIONES_WAV = "c:/Users/David/Desktop/pulsacion.wav";
	
	public static void main(String[] args) {

		ContadorDePulsosMedianteCorrelacion contador;
		ConvertidorAudioAArray convertidorAudioAArray;
		int cantidadDeMuestrasEntreMaximos;
		
		try {
			/* Obtiene audio, remuestrea, lo convierte en un array y lo guarda en un archivo */
			
			convertidorAudioAArray = new ConvertidorAudioAArray(PULSACIONES_WAV);
			short[] audio = convertidorAudioAArray.convertir();
			guadarEnArchivos(audio);
			
			/* Calcular las ppm en base al array obtenido */
			
			contador = new ContadorDePulsosMedianteCorrelacion(audio, 14);
			cantidadDeMuestrasEntreMaximos = contador.obtenerCantidadDeMuestrasEntreMaximos();
			
			System.out.println("cantidad de muestras entre maximos: " + cantidadDeMuestrasEntreMaximos);
			System.out.println("ppm: " + contador.calcularPulsosPorMinuto());
			
		} catch (Exception e) {
			System.out.println("*********Error: " + e.getMessage());
		}
	}
	
	public static void guadarEnArchivos(short[] audio) throws IOException { 
		BufferedWriter writer = new BufferedWriter(new FileWriter("c:/Users/David/Desktop/pulsacion-test.txt"));
		
		for (int i = 0; i < audio.length; i++) {
			String value = String.valueOf(audio[i]);
			
			writer.write(value);
			writer.newLine();
		}
		writer.close();
	}
}
