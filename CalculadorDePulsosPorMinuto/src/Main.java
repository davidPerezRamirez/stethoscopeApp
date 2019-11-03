public class Main {

	public static void main(String[] args) {

		ContadorDePulsosMedianteCorrelacion contador = new ContadorDePulsosMedianteCorrelacion();
		int cantidadDeMuestrasEntreMaximos = contador.obtenerCantidadDeMuestrasEntreMaximos();
		
		System.out.println("cantidad de muestras entre maximos: " + cantidadDeMuestrasEntreMaximos);
		System.out.println("ppm: " + contador.calcularPulsosPorMinuto());

	}

}
