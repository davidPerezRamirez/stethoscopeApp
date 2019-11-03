import java.util.ArrayList;
import java.util.List;

public class ContadorDePulsosMedianteCorrelacion {

	private static final Integer UN_SEGUNDO_EN_MILISEGUNDOS = 60000;
	public double[] samples;
	public int tiempoDeMuestras;

	public ContadorDePulsosMedianteCorrelacion(double[] pulsos, int tiempoDeAudio) {
		this.samples = pulsos;
		this.tiempoDeMuestras = tiempoDeAudio;
	}

	public ContadorDePulsosMedianteCorrelacion() {
		//this.samples = InformacionPulsos.pulsos;
		this.tiempoDeMuestras = 1000;
		double[] examples = InformacionPulsos.pulsos;
		this.samples = new double[examples.length];
		
		for (int i = 0; i < examples.length; i++) {
			if (examples[i] < 0.25) {
				samples[i] = 0;
			} else {
				samples[i] = examples[i];
			}
		}

	}
	
	public int calcularPulsosPorMinuto() {
		double cantMuestrasPorMsec = (double)(this.tiempoDeMuestras / samples.length);
		int cantidadDeMuestrasEnterMaximos = this.obtenerCantidadDeMuestrasEntreMaximos();
		double tiempoEntrePicosMaximos = cantidadDeMuestrasEnterMaximos * cantMuestrasPorMsec; 
		
		return (int) (UN_SEGUNDO_EN_MILISEGUNDOS/tiempoEntrePicosMaximos);		
	}

	public int obtenerCantidadDeMuestrasEntreMaximos() {
		double[] correlations = this.calcularCorrelaciones();
		List<Integer> muestrasEntreMaximos = getCantidadDeMuestrasEntreMaximos(correlations);
		int maximaCantidadDeMuestras = 0;
		
		for (int muestras: muestrasEntreMaximos) {
			if (muestras > maximaCantidadDeMuestras) {
				maximaCantidadDeMuestras = muestras;
			}
		}
		
		return maximaCantidadDeMuestras;
	}
	
	private double[] calcularCorrelaciones() {
		double[] correlations = new double[samples.length];
		double[] offsetSamples;

		for (int i = 0; i < samples.length; i++) {
			offsetSamples = this.getOffsetSamplesArray(i, samples);
			correlations[i] = this.calculateCorrelation(samples, offsetSamples);
		}

		return correlations;
	}

	public List<Integer> getCantidadDeMuestrasEntreMaximos(double[] correlations) {
		List<Double> maximums = new ArrayList<>();
		List<Integer> offsets = new ArrayList<>();
		int offset = 0;
		int indice = 0;

		while (indice < correlations.length - 1) {
			while ((indice + 1 < correlations.length) && (correlations[indice + 1] >= correlations[indice])) {
				offset++;
				indice++;
			}
			if ((indice < correlations.length - 1) && offset > 0) {
				maximums.add(correlations[indice]);
				offsets.add(offset);
				offset = 0;
			}
			while ((indice + 1 < correlations.length) && (correlations[indice + 1] <= correlations[indice])) {
				offset++;
				indice++;
			}
		}

		return offsets;
	}

	private double calculateCorrelation(double[] samples, double[] offsetSamples) {
		double sumatoria = 0;

		for (int i = 0; i < samples.length; i++) {
			sumatoria += (samples[i] * offsetSamples[i]);
		}

		return sumatoria;
	}

	private double[] getOffsetSamplesArray(int offset, double[] samples) {
		double[] offsetSamples = new double[samples.length];

		for (int i = 0; i < offset; i++) {
			offsetSamples[i] = 0;
		}
		for (int j = 0; j < samples.length - offset; j++) {
			offsetSamples[j + offset] = samples[j];
		}

		return offsetSamples;
	}

}
