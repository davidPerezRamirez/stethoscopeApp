import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ConvertidorAudioAArray {
	
	private static final float FRECUENCY_RESAMPLING = 300.0f;

	private static final String PULSACIONES_TEST_WAV = "c:/Users/David/Desktop/pulsacion-test.wav";
	
	private String fileName;
	
	public ConvertidorAudioAArray(String fileNameAudio) {
		this.fileName = fileNameAudio;
	}

	public double[] convertir() throws UnsupportedAudioFileException, IOException {
		File inputFile = new File(this.fileName);
		File outputFile = new File(PULSACIONES_TEST_WAV);
		AudioInputStream stream = null;

		try {
			
            stream = AudioSystem.getAudioInputStream(inputFile);
            stream = convertSampleRate(FRECUENCY_RESAMPLING, stream);
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, outputFile);
            stream = AudioSystem.getAudioInputStream(outputFile);
            
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

		return this.obtenerAudioComoArray(stream);
	}
	
	private double[] obtenerAudioComoArray(AudioInputStream inputStream) throws IOException {
		byte[] samples;

		DataInputStream dis = new DataInputStream(inputStream);
		try {
		    AudioFormat format = inputStream.getFormat();
		    samples = new byte[(int)(inputStream.getFrameLength() * format.getFrameSize())];
		    dis.readFully(samples);
		}
		finally {
		    dis.close();
		}
		
		return ConvertidorByteArray.toDoubleArray(samples);
	}
	
	private AudioInputStream convertSampleRate(float fSampleRate,AudioInputStream sourceStream) {
        AudioFormat sourceFormat = sourceStream.getFormat();
        AudioFormat targetFormat = new AudioFormat(sourceFormat.getEncoding(),
        		fSampleRate,
        		sourceFormat.getSampleSizeInBits(),
        		sourceFormat.getChannels(),
        		sourceFormat.getFrameSize(),
        		fSampleRate,
        		sourceFormat.isBigEndian()
        );
        
        return AudioSystem.getAudioInputStream(targetFormat,sourceStream);
    }
}
